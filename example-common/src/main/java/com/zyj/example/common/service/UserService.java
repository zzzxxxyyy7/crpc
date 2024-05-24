package com.zyj.example.common.service;

import com.zyj.example.common.model.User;

public interface UserService {
    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);

    default short getNumber() {
        return 1;
    }
}
