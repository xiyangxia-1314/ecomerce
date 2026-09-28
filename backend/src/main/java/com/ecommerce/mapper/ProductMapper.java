package com.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecommerce.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品表数据访问层（DAO）
 */
@Mapper   // 声明为 MyBatis 的 Mapper 接口，Spring 启动时自动生成代理实现并注册为 Bean
public interface ProductMapper extends BaseMapper<Product> {
    // 继承 BaseMapper<Product> 后自带对 product 表的增删改查方法，无需手写 SQL
}
