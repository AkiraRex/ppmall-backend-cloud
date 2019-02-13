package com.yc.elasticsearch.entry;

import java.util.HashMap;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "ppmall-product", type = "product")
public class ProductMap extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -429924571515235368L;

}
