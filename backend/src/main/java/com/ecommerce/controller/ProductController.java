package com.ecommerce.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ecommerce.common.Result;
import com.ecommerce.common.ResultCode;
import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品管理控制器：提供商品增删改查的 REST 接口
 */
@RestController                  // 标识为 REST 控制器，方法返回值自动序列化为 JSON
@RequestMapping("/api/product")  // 本控制器下所有接口的统一路径前缀
public class ProductController {

    @Autowired                   // 依赖注入：把商品业务层对象注入进来
    private ProductService productService;

    /**
     * 分页查询商品列表
     * GET /api/product/list?page=1&size=10&keyword=xxx
     *
     * @param page    页码，默认 1
     * @param size    每页条数，默认 10
     * @param keyword 搜索关键字（匹配名称/分类），可不传
     */
    @GetMapping("/list")
    public Result<IPage<Product>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.success(productService.pageList(page, size, keyword));
    }

    /**
     * 根据 id 查询商品
     * GET /api/product/{id}
     */
    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        // 查不到该商品时返回"商品不存在"错误
        if (product == null) {
            return Result.error(ResultCode.PRODUCT_NOT_FOUND);
        }
        return Result.success(product);
    }

    /**
     * 按名称查询商品（模糊匹配，同名商品全部返回）
     * GET /api/product/name/{name}
     */
    @GetMapping("/name/{name}")
    public Result<List<Product>> getByName(@PathVariable String name) {
        return Result.success(productService.getByName(name));
    }

    /**
     * 新增商品
     * POST /api/product（请求体为 JSON 格式的 Product 对象）
     */
    @PostMapping
    public Result<Void> add(@RequestBody Product product) {
        // 商品名称必填，其余字段不传时由数据库默认值填充（price 默认0、status 默认1上架）
        if (product == null || !StringUtils.hasText(product.getName())) {
            return Result.error(ResultCode.PARAM_ERROR);
        }
        // 强制把 id 置空，保证 id 由数据库自增生成，不信任前端传入
        product.setId(null);
        productService.save(product);
        return Result.success();
    }

    /**
     * 修改商品
     * PUT /api/product（请求体为 JSON 格式的 Product 对象，必须携带 id）
     */
    @PutMapping
    public Result<Void> update(@RequestBody Product product) {
        // 没有传 id，无法确定要修改哪条记录
        if (product.getId() == null) {
            return Result.error(ResultCode.PARAM_ERROR);
        }
        // 校验要修改的商品是否存在
        Product exist = productService.getById(product.getId());
        if (exist == null) {
            return Result.error(ResultCode.PRODUCT_NOT_FOUND);
        }
        // 创建/更新时间由数据库自动维护，不接收前端修改
        product.setCreatedAt(null);
        product.setUpdatedAt(null);
        productService.updateById(product);
        return Result.success();
    }

    /**
     * 根据 id 删除商品
     * DELETE /api/product/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        productService.removeById(id);
        return Result.success();
    }

    /**
     * 按名称删除商品（精确匹配，名称完全相同的商品会全部删除）
     * DELETE /api/product/name/{name}
     */
    @DeleteMapping("/name/{name}")
    public Result<Void> deleteByName(@PathVariable String name) {
        productService.removeByName(name);
        return Result.success();
    }
}
