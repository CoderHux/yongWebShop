package com.yong.springboot.controller;

import com.yong.springboot.entities.*;
import com.yong.springboot.service.Imp.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ShopController {
    @Autowired
    ShopService shopService ;
    //处理商品页数跳转
    @GetMapping("/page")
    public  String pageIndex( int index,  HttpSession session){

        //得到改页面的商品信息
        List<Product> products = shopService.getProductsByIndex(index);
        //得到商品页数
        int pageNum = shopService.getPageNum();
        List<Integer> pageList = new ArrayList<>() ;

        for (int j = 0; j < pageNum; j++) {
            pageList.add(j+1) ;
        }
        //保存数据
        session.setAttribute("products",products);
        session.setAttribute("pageNum",pageList);
        session.setAttribute("pageNow",index) ; //更新当前页

        return "index" ;
    }
    @GetMapping("/addCar")
    public  String addCar(Integer prodId ,HttpSession session){
        //得到加入购物车的商品实体
        Product product = shopService.getProductById(prodId);
        //得到购物车
         List<CarIterm> buycar= (List<CarIterm>)session.getAttribute("buycar");
         //把购买的物品加入购物车
        if(buycar.size() == 0 ){ //购物车为空 直接添加
            CarIterm iterm = new CarIterm();
            iterm.setProduct(product);
            iterm.setNum(1) ;
            buycar.add(iterm) ;
        }
        else{
            //不为空遍历购物车
            int size = buycar.size() ;
            int i ;
            for( i= 0 ; i < size ; i++) {
                if (buycar.get(i).getProduct().getId() == prodId) {//购物车中已经有该商品了
                    buycar.get(i).setNum(buycar.get(i).getNum() + 1);
                    break;
                }
            }
            //i>size 表明购物车里没有该商品 直接添加
            if(i == size){
                CarIterm iterm = new CarIterm();
                iterm.setProduct(product);
                iterm.setNum(1) ;
                buycar.add(iterm) ;
            }
            }
        //更新购物车
        session.setAttribute("buycar",buycar);

        return "index" ;
    }
    //跳转到购物车页面
    @GetMapping("/buyCar")
    public String  buyCar(){
        return "shop/buyCar" ;
    }

    @GetMapping("/deleteitem")
    public  String  deleteItem(int prodId,HttpSession session){
        //得到购物车
        List<CarIterm> buycar = (List<CarIterm>)session.getAttribute("buycar");
        //找到该物品
        for (CarIterm item: buycar) {
            if(item.getProduct().getId() == prodId){
                buycar.remove(item) ;
                break;
            }
        }
        return "shop/buyCar" ;
    }
    //直接购买一个物品
    @GetMapping("/buy")
    public  String  buy(int prodId, HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
        boolean order = shopService.order(prodId, user.getId());
        if (order){
            model.addAttribute("msg","下单成功") ;
            return "shop/error" ;
        }
        else{
            model.addAttribute("msg","下单失败") ;
            return "shop/error" ;
        }
    }

    //购物车的商品全部下单
    @GetMapping("/orderByCar")
    public String buyCar(HttpSession session,Model model){
        //得到购物车
        List<CarIterm> buycar =(List<CarIterm>) session.getAttribute("buycar");
        //得到用户ID
        User user = (User)session.getAttribute("user");
        Integer id = user.getId();
        //下单
        boolean order = shopService.order(buycar, id);
        if (order){
            model.addAttribute("msg","下单成功") ;
            //下单成功清空购物车
            buycar.clear();
            session.setAttribute("buycar",buycar);

            return "shop/error" ;
        }
        else {
            model.addAttribute("msg","下单失败") ;
            return "shop/error" ;
        }
    }
    //查看订单
    @GetMapping("/checkBill")
    public String checkBill(Integer userId ,Model model){
        List<Order> order = shopService.findOrder(userId);
        if (order == null){
            model.addAttribute("msg","您没有订单") ;
            return "shop/error" ;
        }
        else {
            model.addAttribute("order",order) ;
            return "shop/order" ;
        }
    }
    //查看订单详情
    @GetMapping("/orderDetail")
    public String orderDetail(Integer orderId,Model model){
        List<OrderDetail> orderDetail = shopService.findOrderDetail(orderId);
        if(orderDetail == null){
            model.addAttribute("msg","查询订单详情失败") ;
            return "shop/error" ;

        }
        else {
            List<Product> productList = new ArrayList<>() ;
            for (OrderDetail od: orderDetail
                 ) {
                productList.add(shopService.getProduct(od.getPid())) ;
            }
            model.addAttribute("productList",productList) ;
            model.addAttribute("orderDetail",orderDetail) ;
            return "shop/orderDetail" ;
        }
    }
    //Search Product By ProductName
    @GetMapping("/search")
    public String  search(String productName,Model model){
        List<Product> product = shopService.getProduct(productName);
        if (product != null ){
            model.addAttribute("searchList",product) ;
            return "shop/search" ;
        }
        else {
            model.addAttribute("msg","没有查询到该商品") ;
            return  "shop/error" ;
        }
    }
    //并发测试
    @ResponseBody
    @GetMapping("/buy/{productId}")
    public  boolean buy(@PathVariable(name = "productId") Integer productId){
        boolean order = shopService.order(productId, 2);
        return  order ;
    }
}
