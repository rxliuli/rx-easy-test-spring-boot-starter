package com.rxliuli.rx.easy.test.base.data.service.impl;

import com.rxliuli.rx.easy.test.base.data.dao.UserDao;
import com.rxliuli.rx.easy.test.base.data.entity.User;
import com.rxliuli.rx.easy.test.base.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author rxliuli
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public boolean insert(User user) {
        return userDao.insert(user) > 0;
    }

    @Override
    public List<User> list() {
        return userDao.list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertBatch(List<User> userList) {
        return userList.stream().allMatch(user -> userDao.insert(user) > 0);
    }
}
