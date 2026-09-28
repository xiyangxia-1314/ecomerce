package com.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类，对应数据库 product 表
 */
@Data                              // Lombok 注解：自动生成 getter/setter/toString/equals/hashCode
@TableName("product")              // 指定该实体类映射数据库中的 product 表
public class Product {

    @TableId(type = IdType.AUTO)   // 主键字段，AUTO 表示由数据库自增生成 id
    private Long id;

    private String name;           // 商品名称

    private String category;       // 分类名称

    private BigDecimal price;      // 售价，DECIMAL(10,2) 对应 BigDecimal，避免浮点精度丢失

    private Integer stock;         // 库存数量

    private String image;          // 商品图片地址

    private String description;    // 商品描述

    private Integer status;        // 上架状态：0下架 1上架

    private LocalDateTime createdAt;   // 创建时间，对应 created_at 列，数据库默认自动填充

    private LocalDateTime updatedAt;   // 更新时间，对应 updated_at 列，数据库在更新时自动维护
}
