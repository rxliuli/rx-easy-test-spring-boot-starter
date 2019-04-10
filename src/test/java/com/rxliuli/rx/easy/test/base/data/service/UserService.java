package com.rxliuli.rx.easy.test.base.data.service;

import com.rxliuli.rx.easy.test.base.data.entity.User;

import java.util.List;

/**
 * @author rxliuli
 */
public interface UserService {
    boolean insert(User user);

    List<User> list();

    boolean insertBatch(List<User> userList);
}
