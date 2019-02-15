package com.yc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yc.common.response.ServerResponse;
import com.yc.service.IProductService;
import com.yc.vo.ProductVo;

@RestController()
@RequestMapping("/portal/product")
public class PortalProductController {
	
	@Autowired
	private IProductService iProductService;


	@GetMapping("/query")
	public ServerResponse<Object> query(String q, Integer categoryId,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
			@RequestParam(value = "pageIndex", defaultValue = "1", required = false) Integer pageIndex)
			throws Exception {
		return iProductService.searchPageableEs(pageIndex, pageSize, q, categoryId);
	}
	
	@GetMapping("/detail")
	public ServerResponse<ProductVo> detail(Integer productId)
			throws Exception {
		return iProductService.getDetailById(productId);
	}
}
