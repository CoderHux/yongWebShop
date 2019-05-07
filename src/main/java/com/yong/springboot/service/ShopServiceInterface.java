package com.yong.springboot.service;

import com.yong.springboot.entities.Product;

import java.util.List;

public interface ShopServiceInterface {
    public List<Product> getProductsByIndex(Integer index) ;
    public  Product getProduct(Product product) ;
    public  Product updateProduct(Product product) ;
    public  boolean deleteProduct(Product product) ;


}
