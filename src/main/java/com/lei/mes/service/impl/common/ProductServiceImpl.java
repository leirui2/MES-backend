package com.lei.mes.service.impl.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mes.entity.common.Product;
import com.lei.mes.exception.BusinessException;
import com.lei.mes.request.common.ProductSaveRequest;
import com.lei.mes.request.common.ProductUpdateRequest;
import com.lei.mes.service.common.ProductService;
import com.lei.mes.mapper.common.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author lei
* @description 针对表【product(产品表)】的数据库操作Service实现
* @createDate 2026-06-20 09:40:31
*/
@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    /**
     * @param pageNum
     * @param pageSize
     * @param productNameOrCode
     * @return
     */
    @Override
    public IPage<Product> getProductPage(int pageNum, int pageSize, String productNameOrCode) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        //构建查询条件
        LambdaQueryWrapper<Product> query = new LambdaQueryWrapper<>();
        if(productNameOrCode != null){
            query.like(Product::getProductName, productNameOrCode)
                    .or()
                    .like(Product::getProductCode, productNameOrCode);
        }
        return this.page(page, query);
    }

    /**
     * @param request
     * @return
     */
    @Override
    public void saveProduct(ProductSaveRequest request) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_code",request.getProductCode());
        Product product = this.getOne(queryWrapper);
        if (product != null){
            throw new BusinessException(400,"该编号产品已存在。");
        }
        //新增
        Product pro = new Product();
        BeanUtils.copyProperties(request,pro);
        pro.setStatus(1);
        if (request.getProductCode() == null) {
            // 自动生成编号
            String productCode = this.generateProductCode(request.getCategory());
            pro.setProductCode(productCode);
        }

        this.save(pro);
        log.info("新增成功，产品编号{}，产品名{}",pro.getProductCode(),pro.getProductName());
    }

    /**
     * 更新产品信息
     */
    @Override
    public void updateProduct(ProductUpdateRequest request) {
        // 检查产品是否存在
        Product existing = this.getById(request.getId());
        if(existing == null){
            throw new BusinessException(404, "产品不存在或已删除");
        }
        // 检查产品编码是否已经在其他产品中存在
        long count = this.count(new LambdaQueryWrapper<Product>()
                .eq(Product::getProductCode, request.getProductCode())
                .ne(Product::getId, request.getId())); // 排除当前产品
        if(count > 0){
            throw new BusinessException(400, "产品编码已存在");
        }
        // 更新产品
        Product update = new Product();
        update.setId(request.getId());
        BeanUtils.copyProperties(request, update);
        this.updateById(update);
        log.info("更新产品成功: ID={} , 产品名称={}", request.getId(), request.getProductName());
    }

    /**
     * 删除产品
     */
    @Override
    public void deleteProduct(Long id) {
        // 检查产品是否存在
        Product existing = this.getById(id);
        if(existing == null){
            throw new BusinessException(404, "产品不存在或已删除");
        }
        // 逻辑删除（MyBatis-Plus @TableLogic 自动转 UPDATE SET is_delete=1）
        this.removeById(id);
        log.info("删除产品成功: ID={}", id);
    }

    /**
     * @param id
     * @param status
     */
    @Override
    public void changeStatus(Long id, Integer status) {
        // 检查产品是否存在
        Product existing = this.getById(id);
        if(existing == null){
            throw new BusinessException(404, "产品不存在或已删除");
        }
        // 检查状态是否有效
        if(status != 1 && status != 0){
            throw new BusinessException(400, "状态无效");
        }
        // 更新状态
        Product update = new Product();
        update.setId(id);
        update.setStatus(status);
        this.updateById(update);
        log.info("切换产品状态成功: ID={}, status={}", id, status);
    }

    /**
     * 生成产品编号
     */
    @Override
    public String generateProductCode(String category) {
        // 1. 根据分类获取缩写
        String categoryCode = getCategoryCode(category);  // 快充→FC

        // 2. 查询该类产品的最大序号
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(Product::getProductCode, "PWR-" + categoryCode + "-");
        wrapper.orderByDesc(Product::getProductCode);
        wrapper.last("LIMIT 1");

        Product lastProduct = this.getOne(wrapper);

        // 3. 计算新序号
        int nextSeq = 1;
        if (lastProduct != null) {
            // 从编号中提取序号：PWR-FC-002 → 2
            String code = lastProduct.getProductCode();
            String seqStr = code.substring(code.lastIndexOf("-") + 1);
            nextSeq = Integer.parseInt(seqStr) + 1;
        }

        // 4. 格式化返回：PWR-FC-003
        return String.format("PWR-%s-%03d", categoryCode, nextSeq);
    }

    private String getCategoryCode(String category) {
        return switch (category) {
            case "快充" -> "FC";
            case "普通" -> "NC";
            case "无线" -> "WL";
            default -> "OT";
        };
    }

}




