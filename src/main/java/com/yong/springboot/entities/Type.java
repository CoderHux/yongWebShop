package com.yong.springboot.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.security.PrivateKey;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table
public class Type implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    @Column
    private  String name ;
    @OneToMany(targetEntity = Product.class,mappedBy ="type",cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    private  Set<Product> products = new HashSet<>() ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    public void addProduct(Product product){
        this.products.add(product) ;
        product.setType(this);
    }
}
