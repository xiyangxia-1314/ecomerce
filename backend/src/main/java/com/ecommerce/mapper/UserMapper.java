package com.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecommerce.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表数据访问层（DAO）
 */
@Mapper   // 声明为 MyBatis 的 Mapper 接口，Spring 启动时会自动生成代理实现并注册为 Bean
public interface UserMapper extends BaseMapper<User> {
    // 继承 BaseMapper<User> 后自带对 user 表的增删改查方法（insert、selectById、updateById、deleteById 等），无需手写 SQL
}
