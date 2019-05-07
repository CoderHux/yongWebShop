package com.yong.springboot.service;

import com.yong.springboot.entities.User;

/*
    用户Service层
    功能: 查询用户(登录)
          负责插入用户(注册)
          更新用户(修改用户信息)
          删除用户(管理员功能)
     JpaRepository已经实现CURD功能
 */
public interface UserServiceInterface {
    public User  login(User user) ;
    public  User regist(User user) ;
    public  User update(User user) ;
    public  User getUser(Integer id) ;


}
