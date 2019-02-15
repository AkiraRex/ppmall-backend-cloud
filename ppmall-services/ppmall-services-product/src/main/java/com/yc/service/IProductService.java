package com.yc.service;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils.Null;

import com.github.pagehelper.PageInfo;
import com.yc.common.response.ServerResponse;
import com.yc.pojo.Product;
import com.yc.vo.ProductVo;

public interface IProductService {
	ServerResponse<Object> searchPageableEs(Integer page, Integer size, String q, Integer categoryId);
	
	ServerResponse<ProductVo> getDetailById(Integer productId);

	ServerResponse<PageInfo<Product>> getProductList(int pageNum, int pageSize, Map<String, Object> paramMap);
	
	ServerResponse<Null> saveProduct(Product product);
	
	ServerResponse<Null> setStatus(Product product);
}
