package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommerce.entity.Product;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商品业务逻辑实现类
 */
@Service   // 注册为 Spring 的 Service Bean，供 Controller 注入使用
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    // 继承 ServiceImpl 后可以直接使用 this.page()、this.list()、this.remove() 等方法操作 product 表

    /**
     * 分页查询商品列表，支持按名称/分类模糊搜索
     */
    @Override
    public IPage<Product> pageList(Integer page, Integer size, String keyword) {
        // LambdaQueryWrapper：用方法引用的方式构造查询条件，避免手写字段名字符串
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        // 关键字不为空时，条件为：name LIKE '%keyword%' OR category LIKE '%keyword%'
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Product::getName, keyword)
                    .or()
                    .like(Product::getCategory, keyword);
        }
        // 按创建时间倒序排列，最新添加的商品排在前面
        wrapper.orderByDesc(Product::getCreatedAt);
        // 执行分页查询：new Page<>(当前页, 每页条数)
        return this.page(new Page<>(page, size), wrapper);
    }

    /**
     * 按名称模糊查询商品列表，可能返回多条
     */
    @Override
    public List<Product> getByName(String name) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        // 条件：name LIKE '%name%'，并按创建时间倒序
        wrapper.like(Product::getName, name)
                .orderByDesc(Product::getCreatedAt);
        return this.list(wrapper);
    }

    /**
     * 按名称精确删除商品，名称完全相同的商品会全部删除
     */
    @Override
    public boolean removeByName(String name) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        // 条件：name = ?（精确匹配，避免模糊删除误伤其他商品）
        wrapper.eq(Product::getName, name);
        // 返回是否删除了至少一条记录
        return this.remove(wrapper);
    }
}
