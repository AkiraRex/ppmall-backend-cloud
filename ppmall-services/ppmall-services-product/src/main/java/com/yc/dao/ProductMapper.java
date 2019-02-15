package com.yc.dao;

import java.util.List;
import java.util.Map;

import com.yc.pojo.Product;

public interface ProductMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Product record);

	int insertSelective(Product record);

	Product selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Product record);

	int updateByPrimaryKey(Product record);

	List<Product> selectAll(Map<String, Object> paramMap);

	int updateBatchSelective(List<?> list);

	List<?> selectHotProduct(Integer limit);
}