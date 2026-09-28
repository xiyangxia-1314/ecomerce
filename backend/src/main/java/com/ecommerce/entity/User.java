package com.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类，对应数据库 gouwu1 库中的 user 表
 * 表字段：id、username、password、phone、user_type、create_time
 */
@Data                              // Lombok 注解：自动生成 getter/setter/toString/equals/hashCode
@TableName("user")                 // 指定该实体类映射数据库中的 user 表
public class User {

    @TableId(type = IdType.AUTO)   // 主键字段，AUTO 表示由数据库自增生成 id
    private Long id;

    private String username;       // 用户名（唯一，用于登录）

    private String password;       // 密码

    private String phone;          // 手机号

    private String userType;       // 用户类型，对应表的 user_type 列（驼峰自动映射下划线）

    private LocalDateTime createTime;  // 创建时间，对应表的 create_time 列
}
