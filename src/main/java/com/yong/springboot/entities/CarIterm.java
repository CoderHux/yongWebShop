package com.yong.springboot.entities;

import org.springframework.stereotype.Component;


import java.io.Serializable;

@Component
public class CarIterm implements Serializable {
    private  Integer num ;
    private  Product product ;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
