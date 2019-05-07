package com.yong.springboot.controller;


import com.yong.springboot.entities.*;
import com.yong.springboot.service.Imp.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    AdminService adminService ;
    //前往管理员登录页面
    @GetMapping("/admin")
    public  String getAdminPage(){
        return "admin/login" ;
    }
    @GetMapping("adminLogin")
    public String adminLogin(Admin admin, HttpSession session, Model model){
        Admin admin1 = adminService.getAdmin(admin);
        if (admin != null) {
            //清空用户信息
            session.setAttribute("user",null);
            session.setAttribute("buycar",null);
            //保存管理员信息
            session.setAttribute("admin",admin1);
            //保存所有用户信息
            List<User> users = adminService.findUsers();
            model.addAttribute("users",users);
            return "admin/admin";
        }
        else{
            model.addAttribute("msg","管理员登录失败") ;
            return "admin/error" ;
        }
    }
    @GetMapping("/deleteUser")
    public String deleteUser(Integer userId,Model model){
        adminService.deleteUser(userId);
        List<User> users = adminService.findUsers();
        model.addAttribute("users",users);
        return "admin/admin" ;
    }
    @GetMapping("/searchUser")
    public String seachUser(String userName ,Model model){
        List<User> userByName = adminService.findUserByName(userName);
        model.addAttribute("user_search",userByName) ;
        return "admin/search" ;
    }
    @GetMapping("/manageOrder")
    public String manageOrder(Model model){
        List<Order> orders = adminService.getOrders();
        model.addAttribute("orders",orders) ;
        return "admin/manageOrder" ;
    }
    @GetMapping("/deleteOrder")
    public String deleteOrder(Integer orderId,Model model){
        adminService.deleteOrderById(orderId);
        //更新订单数据
        List<Order> orders = adminService.getOrders() ;
        model.addAttribute("orders",orders) ;

        return "admin/manageOrder" ;
    }
    @GetMapping("/adminIndex")
    public String admin(Model model) {
        List<User> users = adminService.findUsers();
        model.addAttribute("users",users);
        return  "admin/admin" ;
    }
    @GetMapping("/manageProduct")
    public String manageProduct(){
        return "admin/manageProduct" ;
    }
    @GetMapping("/inserType")
    public String inserType(String name){
        Type type = new Type();
        type.setName(name);
        adminService.addType(type) ;
        return  "admin/manageProduct" ;
    }
    @GetMapping("/inserProduct")
    public String inserProduct(Product product,String productType){
        Type type = new Type();
        type.setName(productType);
        Type type1 = adminService.getType(type);

        product.setType(type1);

        adminService.addProduct(product) ;

        return "admin/manageProduct" ;

    }
}
