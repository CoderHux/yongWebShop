package com.yong.springboot.service.Imp;

import com.yong.springboot.entities.*;
import com.yong.springboot.repository.*;
import com.yong.springboot.service.AdminServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;


@Service
@CacheConfig(cacheNames = "admin")
public class AdminService implements AdminServiceInterface {
    @Autowired
    AdminRepository adminRepository ;
    @Autowired
    UserRepository userRepository ;
    @Autowired
    ProductRepository productRepository ;
    @Autowired
    TypeRepository typeRepository ;
    @Autowired
    OderDetailRepository oderDetailRepository ;
    @Autowired
    OrderRepository orderRepository ;
    @Override
    public Admin getAdminById(Integer Id) {

        return adminRepository.getOne(Id);
    }

    @Override
    public Admin getAdmin(Admin admin) {
        Optional<Admin> admin1 = adminRepository.findOne(Example.of(admin));
        if(admin1.isPresent())
            return admin1.get();
        else
            return null;
    }
    @Cacheable(key = "#root.methodName")
    @Override
    public List<User> findUsers() {
        userRepository.findAll();
        return  userRepository.findAll();
    }
    @Cacheable(key = "#userName")
    public List<User> findUserByName(String userName){
        User user = new User();
        user.setUserName(userName);
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("userName",match ->match.contains());
        Example<User> example = Example.of(user, matcher);
        List<User> all = userRepository.findAll(example);
        return all ;
    }
    @Transactional
    @CacheEvict(allEntries = true,cacheNames = "admin")//数据有变动删除缓存
    @Override
    public void deleteUser(Integer userId) {
        //得到该用户的订单ID
        userRepository.deleteById(userId);
    }

    @Override
    @CacheEvict(allEntries = true,cacheNames = "products")
    public Product addProduct(Product product) {
       return  productRepository.save(product);

    }

    @Override
    public Type addType(Type type) {

        return  typeRepository.save(type);
    }

    @Override
    public Type getType(Type type) {
        Optional<Type> one = typeRepository.findOne(Example.of(type));
        if ((one.isPresent()))
            return  one.get() ;
        else
            return null ;
    }

    @Override
    public void deleteType(Type type) {
        typeRepository.delete(type);

    }

    @Cacheable(key = "#root.methodName")
    public List<Order> getOrders(){
        return   orderRepository.findAll();
    }

    @CacheEvict(allEntries = true,cacheNames = "admin")
    public void deleteOrderById(Integer orderId){

        orderRepository.deleteById(orderId);
    }
}
