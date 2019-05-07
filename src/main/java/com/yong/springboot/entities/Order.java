package com.yong.springboot.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "order_t") //order是mysql关键字,不可做表名
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    //用户ID FK TO USER

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userId")
    private  User user ;
    //与orderDetail进行连接
    @OneToMany(targetEntity = OrderDetail.class,mappedBy = "order",cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    private Set<OrderDetail> orderDetails = new HashSet<>() ;
    @Column
    private  String status ;
    @Column
    private Date orderTime ;
    @Column
    private  Double OrderPrice ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Double getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        OrderPrice = orderPrice;
    }
    public void addOrderDetail(OrderDetail orderDetail){
        this.orderDetails.add(orderDetail) ;
        orderDetail.setOrder(this) ;
    }
}
