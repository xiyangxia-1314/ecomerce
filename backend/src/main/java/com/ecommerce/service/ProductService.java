package com.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommerce.entity.Product;

import java.util.List;

/**
 * 商品业务逻辑接口
 */
public interface ProductService extends IService<Product> {
    // 继承 IService<Product> 后自带通用业务方法（save、removeById、getById 等），这里只补充自定义方法

    /**
     * 分页查询商品列表
     *
     * @param page    当前页码
     * @param size    每页条数
     * @param keyword 搜索关键字（可为空，匹配商品名称或分类）
     * @return 分页结果
     */
    IPage<Product> pageList(Integer page, Integer size, String keyword);

    /**
     * 按名称查询商品（模糊匹配，同名/含关键字的结果都会返回）
     *
     * @param name 商品名称关键字
     * @return 商品列表
     */
    List<Product> getByName(String name);

    /**
     * 按名称精确删除商品（名称完全相同的商品会全部删除）
     *
     * @param name 商品名称
     * @return 是否删除了记录
     */
    boolean removeByName(String name);
}
