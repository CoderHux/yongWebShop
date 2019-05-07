package com.yong.springboot.entities;



import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class User  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id ;
    @Column
    private  String userName ;
    @Column
    private  String  passdWord ;
    @Column
    private  String realName ;
    @Column
    private  String sex ;
    @Column
    private  String address ;
    @Column
    private  String question ;
    @Column
    private  String answer ;
    @Column
    private  String  email ;
    @Column
    private Date regDate ;
    @OneToMany(targetEntity = Order.class,mappedBy = "user" ,cascade = {CascadeType.REMOVE})
    private Set<Order> orders = new HashSet<>() ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassdWord() {
        return passdWord;
    }

    public void setPassdWord(String passdWord) {
        this.passdWord = passdWord;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
    public void addOrder(Order order){
        this.orders.add(order) ;
        order.setUser(this);
    }
}
