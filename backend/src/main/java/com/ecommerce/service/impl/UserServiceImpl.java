package com.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommerce.common.BusinessException;
import com.ecommerce.common.ResultCode;
import com.ecommerce.entity.User;
import com.ecommerce.mapper.UserMapper;
import com.ecommerce.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户业务逻辑实现类
 */
@Service   // 注册为 Spring 的 Service Bean，供 Controller 注入使用
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    // 继承 ServiceImpl 后可以直接使用 this.page()、this.count() 等方法操作 user 表

    /**
     * 分页查询用户列表，支持按用户名/手机号模糊搜索
     */
    @Override
    public IPage<User> pageList(Integer page, Integer size, String keyword) {
        // LambdaQueryWrapper：用方法引用的方式构造查询条件，避免手写字段名字符串
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        // 关键字不为空时，条件为：username LIKE '%keyword%' OR phone LIKE '%keyword%'
        if (StringUtils.hasText(keyword)) {
            wrapper.like(User::getUsername, keyword)
                    .or()
                    .like(User::getPhone, keyword);
        }
        // 按创建时间倒序排列，最新注册的用户排在前面
        wrapper.orderByDesc(User::getCreateTime);
        // 执行分页查询：new Page<>(当前页, 每页条数)
        return this.page(new Page<>(page, size), wrapper);
    }

    /**
     * 用户登录：按用户名查出用户，再比对密码
     */
    @Override
    public User login(String username, String password) {
        // 按用户名查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);
        // 用户名不存在，抛出业务异常
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        // 密码不一致，抛出业务异常
        if (!user.getPassword().equals(password)) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        // 密码置空后再返回，避免泄露给前端
        user.setPassword(null);
        return user;
    }

    /**
     * 校验用户名是否已被占用
     *
     * @param username  要校验的用户名
     * @param excludeId 需要排除的用户 id（修改时排除自己，新增时传 null）
     */
    public void checkUsernameUnique(String username, Long excludeId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        // 条件：username = ?
        wrapper.eq(User::getUsername, username);
        // 如果传了 excludeId，则追加条件：id != excludeId（修改场景下排除自己）
        if (excludeId != null) {
            wrapper.ne(User::getId, excludeId);
        }
        // 能查到记录说明用户名已被占用，抛出业务异常（由全局异常处理器转成错误响应）
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ResultCode.USERNAME_EXIST);
        }
    }
}
