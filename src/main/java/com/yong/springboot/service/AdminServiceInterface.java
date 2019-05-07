package com.yong.springboot.service;

import com.yong.springboot.entities.Admin;
import com.yong.springboot.entities.Product;
import com.yong.springboot.entities.Type;
import com.yong.springboot.entities.User;

import java.util.List;

public interface AdminServiceInterface {
    public Admin getAdminById(Integer Id);
    public Admin getAdmin(Admin admin) ;
    public List<User> findUsers() ;
    public void deleteUser(Integer userId) ;
    public Product addProduct(Product product) ;
    public Type addType(Type type) ;
    public Type getType(Type type) ;
    public void deleteType(Type type) ;
}
