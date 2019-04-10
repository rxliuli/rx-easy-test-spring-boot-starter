package com.rxliuli.rx.easy.test.base.data.web;

import com.rxliuli.rx.easy.test.base.data.entity.Result;
import com.rxliuli.rx.easy.test.base.data.entity.User;
import com.rxliuli.rx.easy.test.base.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author rxliuli
 */
@RestController
@RequestMapping("/api/user")
public class UserApi {
    @Autowired
    private UserService userService;

    @PostMapping("/insert")
    public Result<Boolean> insert(@RequestBody User user) {
        return new Result<Boolean>().setData(userService.insert(user));
    }

    @GetMapping("/list")
    public Result<List<User>> list() {
        return new Result<List<User>>().setData(userService.list());
    }
}
