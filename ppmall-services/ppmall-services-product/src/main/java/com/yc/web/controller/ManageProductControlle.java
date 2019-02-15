package com.yc.web.controller;

//import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.yc.common.response.ServerResponse;
import com.yc.pojo.Product;
import com.yc.service.IProductService;
import com.yc.vo.ProductVo;

@RequestMapping("/backend/product")
public class ManageProductControlle {

	@Autowired
	private IProductService iProductService;

	@GetMapping("/list")
	public ServerResponse<PageInfo<Product>> getProductList(Integer productId, String productName,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("productName", productName);
		paramMap.put("productId", productId);
		return iProductService.getProductList(pageNum, pageSize, paramMap);

	}

	@PostMapping("/save")
	public ServerResponse<Null> saveProduct(Product product) {
		return iProductService.saveProduct(product);
	}

	@GetMapping("/detail")
	public ServerResponse<ProductVo> getDetail(int productId) {
		return iProductService.getDetailById(productId);
	}

//	@PostMapping("/upload")
//	public ServerResponse<String> uploadFile(
//			@RequestParam(value = "upload_file", required = false) MultipartFile file) {
//		String path = session.getServletContext().getRealPath("upload");
//		try {
//			return iFileService.uploadFile(file, path);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return ServerResponse.createErrorMessage("上传失败");
//		}
//
//	}
	
	@PostMapping("/setSaleStatus")
	public ServerResponse<Null> setProductStatus(Product product) {
		return iProductService.setStatus(product);
	}
}
