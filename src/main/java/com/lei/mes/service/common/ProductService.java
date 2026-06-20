package com.lei.mes.service.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mes.entity.common.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mes.request.common.ProductSaveRequest;
import com.lei.mes.request.common.ProductUpdateRequest;
import jakarta.validation.Valid;

/**
* @author lei
* @description 针对表【product(产品表)】的数据库操作Service
* @createDate 2026-06-20 09:40:31
*/
public interface ProductService extends IService<Product> {

    IPage<Product> getProductPage(int pageNum, int pageSize, String productNameOrCode);

    /**
     * 生成产品编号
     */
    String generateProductCode(String category);


    void saveProduct(@Valid ProductSaveRequest request);

    void updateProduct(@Valid ProductUpdateRequest request);

    void deleteProduct(Long id);

    void changeStatus(Long id, Integer status);
}
