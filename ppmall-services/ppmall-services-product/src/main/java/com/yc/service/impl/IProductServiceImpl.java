package com.yc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils.Null;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yc.common.constant.Const;
import com.yc.common.response.ServerResponse;
import com.yc.common.utils.StringUtil;
import com.yc.dao.ProductMapper;
import com.yc.elasticsearch.entity.ProductEntity;
import com.yc.pojo.Product;
import com.yc.service.IProductService;
import com.yc.vo.ProductVo;

@Service("iProductService")
public class IProductServiceImpl implements IProductService {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	private ProductMapper productMapper;

	@Value("${search.uploadFileLocation}")
	private String PATH;

	@Value("${search.preTag}")
	private String preTag;

	@Value("${search.postTag}")
	private String postTag;

	@Value("${ftp.server.http.prefix}")
	private String FtpServerhttpPrefix;

	@Override
	public ServerResponse<Object> searchPageableEs(Integer page, Integer size, String q, Integer categoryId) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page - 1, size);// ES 分页从0开始
		String hightlightFieldsStr[] = { "name", "subtitle", "detail" };
		String showFieldsStr[] = { "name", "subtitle", "detail", "stock", "price", "main_image", "sub_images" };
		String queryFieldsStr[] = { "name", "subtitle", "detail" };

		// 建立查询条件
		QueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(q, queryFieldsStr);
		QueryBuilder matchQuery = QueryBuilders.matchQuery("status", Const.ValidStatus.VALID);

		QueryBuilder booleanQuery = QueryBuilders.boolQuery().must(multiMatchQuery).must(matchQuery);

		if (categoryId != null) {
			QueryBuilder query = QueryBuilders.matchQuery("category_id", categoryId);
			booleanQuery = QueryBuilders.boolQuery().must(query);
		}

		// 高亮字段数组
		HighlightBuilder.Field fields[] = new HighlightBuilder.Field[hightlightFieldsStr.length];

		// 设置高亮字段数组
		for (int i = 0; i < hightlightFieldsStr.length; i++) {
			fields[i] = new HighlightBuilder.Field(hightlightFieldsStr[i]).preTags(preTag).postTags(postTag);
		}

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(booleanQuery)// 查询条件
				.withFields(showFieldsStr)// 需要查询的字段
				.withHighlightFields(fields)// 高亮
				.withPageable(pageable).build();

		Page<ProductEntity> searchPageResults = (Page<ProductEntity>) elasticsearchTemplate.queryForPage(searchQuery,
				ProductEntity.class,
				// 自定义ResultMapper将返回数据从新封装
				new SearchResultMapper() {

					@SuppressWarnings("unchecked")
					@Override
					public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz,
							Pageable pageable) {
						// TODO Auto-generated method stub

						List<T> chunk = new ArrayList<T>();

						long total = response.getHits().getTotalHits();

						if (response.getHits().getHits().length <= 0) {
							return new AggregatedPageImpl<>((List<T>) chunk, pageable, total);
						}

						for (SearchHit searchHit : response.getHits()) {

							Map<String, Object> entityMap = searchHit.getSource();

							Map<String, HighlightField> hightlightFieldsMap = searchHit.getHighlightFields();

							for (String highName : hightlightFieldsStr) {
								if (hightlightFieldsMap.containsKey(highName)) {
									String highValue = hightlightFieldsMap.get(highName).fragments()[0].toString();
									entityMap.put(highName, highValue);
								}
							}

							chunk.add((T) entityMap);
						}

						return new AggregatedPageImpl<>((List<T>) chunk, pageable, total);
					}
				});
		List<ProductEntity> contentList = new ArrayList<>();
		int totalPages = 0;
		Map<String, Object> returnMap = new HashMap<>();

		totalPages = searchPageResults.getTotalPages();
		long totalElemnets = searchPageResults.getTotalElements();

		if (searchPageResults != null && searchPageResults.getContent() != null) {
			contentList = searchPageResults.getContent();
		}

		returnMap.put("rows", contentList);
		returnMap.put("totalPages", totalPages);
		returnMap.put("totalElemnets", totalElemnets);

		return ServerResponse.createSuccess(returnMap);
	}

	@Override
	public ServerResponse<ProductVo> getDetailById(Integer productId) {
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product == null) {
			return ServerResponse.createErrorMessage("该商品已下架或被删除");
		}
		ProductVo productVo = new ProductVo(product);
		productVo.setImageHost(FtpServerhttpPrefix);

		return ServerResponse.createSuccess("获取成功", productVo);
	}

	@Override
	public ServerResponse<PageInfo<Product>> getProductList(int pageNum, int pageSize, Map<String, Object> paramMap) {
		PageHelper.startPage(pageNum, pageSize);
		List<Product> productList = productMapper.selectAll(paramMap);
		PageInfo<Product> pageResult = new PageInfo<>(productList);
		return ServerResponse.createSuccess("获取成功", pageResult);
	}

	@Override
	public ServerResponse<Null> saveProduct(Product product) {
		Integer productId = product.getId();

		if (StringUtil.isNotBlank(product.getSubImages())) {
			String subImg = product.getSubImages();
			product.setMainImage(subImg.split(",")[0]);
		}
		if (productId == null) {
			productMapper.insert(product);
			return ServerResponse.createSuccessMessage("添加成功");
		} else {
			productMapper.updateByPrimaryKey(product);
			return ServerResponse.createSuccessMessage("修改成功");
		}
	}

	@Override
	public ServerResponse<Null> setStatus(Product product) {
		productMapper.updateByPrimaryKeySelective(product);
		return ServerResponse.createSuccessMessage("修改成功");
	}

}
