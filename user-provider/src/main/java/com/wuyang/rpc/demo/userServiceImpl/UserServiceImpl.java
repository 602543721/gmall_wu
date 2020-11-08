package com.wuyang.rpc.demo.userServiceImpl;


import com.wuyang.rpc.demo.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public String addUserName(String name) {
        return "添加姓名：WY"+name;
    }
}
