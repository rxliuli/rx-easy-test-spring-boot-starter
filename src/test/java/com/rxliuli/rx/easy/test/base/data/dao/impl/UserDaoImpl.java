package com.rxliuli.rx.easy.test.base.data.dao.impl;

import com.rxliuli.rx.easy.test.base.data.dao.UserDao;
import com.rxliuli.rx.easy.test.base.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rxliuli
 */
@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate template;

    @Override
    public int insert(User user) {
        return template.update("insert into user (username, password, lastLoginTime) values (?, ?, ?)", user.getUsername(), user.getPassword(), LocalDateTime.now());
    }

    @Override
    public List<User> list() {
        return template.query("select * from user", BeanPropertyRowMapper.newInstance(User.class));
    }
}
