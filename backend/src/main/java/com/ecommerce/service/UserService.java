package com.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommerce.entity.User;

/**
 * 用户业务逻辑接口
 */
public interface UserService extends IService<User> {
    // 继承 IService<User> 后自带通用业务方法（save、removeById、getById 等），这里只补充自定义方法

    /**
     * 分页查询用户列表
     *
     * @param page    当前页码
     * @param size    每页条数
     * @param keyword 搜索关键字（可为空，匹配用户名或手机号）
     * @return 分页结果
     */
    IPage<User> pageList(Integer page, Integer size, String keyword);

    /**
     * 用户登录：校验用户名和密码
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户信息（密码已置空）
     */
    User login(String username, String password);
}
