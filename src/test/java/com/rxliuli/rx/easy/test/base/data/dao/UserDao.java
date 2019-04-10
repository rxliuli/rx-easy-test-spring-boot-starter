package com.rxliuli.rx.easy.test.base.data.dao;

import com.rxliuli.rx.easy.test.base.data.entity.User;

import java.util.List;

/**
 * @author rxliuli
 */
public interface UserDao {
    int insert(User user);

    List<User> list();
}
