package com.ecommerce.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ecommerce.common.Result;
import com.ecommerce.common.ResultCode;
import com.ecommerce.entity.User;
import com.ecommerce.service.UserService;
import com.ecommerce.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器：提供用户增删改查的 REST 接口
 */
@RestController                  // 标识为 REST 控制器，方法返回值自动序列化为 JSON
@RequestMapping("/api/user")     // 本控制器下所有接口的统一路径前缀
public class UserController {

    @Autowired                   // 依赖注入：把用户业务层对象注入进来
    private UserService userService;

    /**
     * 用户登录
     * POST /api/user/login（请求体为 JSON：{"username": "xxx", "password": "xxx"}）
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody User loginUser) {
        // 用户名或密码为空时直接返回参数错误
        if (loginUser.getUsername() == null || loginUser.getUsername().isEmpty()
                || loginUser.getPassword() == null || loginUser.getPassword().isEmpty()) {
            return Result.error(ResultCode.PARAM_ERROR);
        }
        // 调用业务层校验登录，用户名不存在或密码错误会抛业务异常，由全局异常处理器统一返回
        return Result.success(userService.login(loginUser.getUsername(), loginUser.getPassword()));
    }

    /**
     * 分页查询用户列表
     * GET /api/user/list?page=1&size=10&keyword=xxx
     *
     * @param page    页码，默认 1
     * @param size    每页条数，默认 10
     * @param keyword 搜索关键字，可不传
     */
    @GetMapping("/list")
    public Result<IPage<User>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.success(userService.pageList(page, size, keyword));
    }

    /**
     * 根据 id 查询单个用户
     * GET /api/user/{id}
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        // 查不到该用户时返回"用户不存在"错误
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_FOUND);
        }
        // 密码置空后再返回，避免泄露给前端
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 新增用户
     * POST /api/user（请求体为 JSON 格式的 User 对象）
     */
    @PostMapping
    public Result<Void> add(@RequestBody User user) {
        // 先校验用户名是否已被占用（新增时没有 id，排除参数传 null）
        ((UserServiceImpl) userService).checkUsernameUnique(user.getUsername(), null);
        // 强制把 id 置空，保证 id 由数据库自增生成，不信任前端传入
        user.setId(null);
        userService.save(user);
        return Result.success();
    }

    /**
     * 修改用户
     * PUT /api/user（请求体为 JSON 格式的 User 对象，必须携带 id）
     */
    @PutMapping
    public Result<Void> update(@RequestBody User user) {
        // 没有传 id，无法确定要修改哪条记录
        if (user.getId() == null) {
            return Result.error(ResultCode.PARAM_ERROR);
        }
        // 校验要修改的用户是否存在
        User exist = userService.getById(user.getId());
        if (exist == null) {
            return Result.error(ResultCode.USER_NOT_FOUND);
        }
        // 校验新用户名没有被其他用户占用（排除自己的 id）
        ((UserServiceImpl) userService).checkUsernameUnique(user.getUsername(), user.getId());
        // 密码为空表示本次不修改密码，沿用数据库里的旧密码
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(exist.getPassword());
        }
        userService.updateById(user);
        return Result.success();
    }

    /**
     * 删除用户
     * DELETE /api/user/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }
}
