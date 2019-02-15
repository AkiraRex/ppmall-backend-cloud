package com.yc.elasticsearch.entity;

import org.springframework.data.elasticsearch.annotations.Document;

import com.yc.entity.BaseEsEntity;

@Document(indexName = "ppmall-product", type = "product")
public class ProductEntity extends BaseEsEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4099861816781863630L;

}
