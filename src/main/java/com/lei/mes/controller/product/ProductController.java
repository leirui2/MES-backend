package com.lei.mes.controller.product;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lei.mes.common.Result;
import com.lei.mes.entity.product.Product;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.request.product.ProductSaveRequest;
import com.lei.mes.request.product.ProductUpdateRequest;
import com.lei.mes.service.product.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 产品控制器
 * @author lei
 */
@RestController
@RequestMapping("/api/sys/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     *  分页查询产品列表 (根据产品名称或产品编号查询)
    */
    @GetMapping("/list")
    public Result<IPage<Product>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String productNameOrCode) {

        IPage<Product> page = productService.getProductPage(pageNum, pageSize, productNameOrCode);
        IPage<Product> voPage = page.convert(product -> {
            Product vo = new Product();
            BeanUtils.copyProperties(product, vo);
            return vo;
        });
        return Result.success(voPage);
    }

    /**
     * 根据产品 ID查询产品详情
    */
    @GetMapping("/{id}")
    public Result<Product> detail(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error(404, "产品不存在或已删除");
        }
        return Result.success(product);
    }

    /**
     * 新增产品
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Valid ProductSaveRequest request) {
        try {
            productService.saveProduct(request);
            return Result.success("新增成功",null);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }


    /**
     * 编辑产品
     */
    @PutMapping("/update")
    public Result<Void> update(@Valid @RequestBody ProductUpdateRequest request) {
        try {
            productService.updateProduct(request);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }


    /**
     * 切换产品状态（启用/禁用）
     */
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            productService.changeStatus(id, status);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }
}
