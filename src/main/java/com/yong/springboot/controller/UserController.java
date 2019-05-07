package com.yong.springboot.controller;

import com.yong.springboot.entities.CarIterm;
import com.yong.springboot.entities.Product;
import com.yong.springboot.entities.User;
import com.yong.springboot.service.Imp.ShopService;
import com.yong.springboot.service.Imp.UserService;
import com.yong.springboot.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class UserController {
    //注入UserService
    @Autowired
    UserService userService ;
    //注入ShopService
    @Autowired
    ShopService shopService ;

    @RequestMapping({"/index","/"})
    public String user(HttpSession session){
        //第一页把商品数据放到session里面
        //得到第一页的数据
        List<Product> products = shopService.getProductsByIndex(1);
        //得到商品页数
        int pageNum = shopService.getPageNum();
        List<Integer> pageList = new ArrayList<>() ;
        for (int i = 0; i < pageNum; i++) {
            pageList.add(i+1) ;
        }
        //保存数据
        session.setAttribute("products",products);
        session.setAttribute("pageNum",pageList);
        session.setAttribute("pageNow",1) ; //每次访问首页默认在商品的第一页

        return "index" ;
    }
    //指向注册页面
   @GetMapping("/regist")
    public String  registPaper(){
        return "user/regist" ;
   }
   //处理注册请求
    @PostMapping("/regist")
    public  String  regist(User user, HttpSession session,Model model){
        //得到注册时间
        Date regdate = new Date() ;
        user.setRegDate(regdate);
        //保存用户
        User regist = userService.regist(user);
        if(regist != null){
            //创建购物车
            List<CarIterm> carIterms = new ArrayList<>();
            session.setAttribute("buycar",carIterms);
            session.setAttribute("user",regist);  //保存用户
            return "index" ;
        }
        else {
            model.addAttribute("msg","注册失败") ; //保存错误提示信息
            return "user/error" ;
        }

    }
    //专向登录页面
    @GetMapping("/login")
    public  String loginPage(){
        return  "user/login" ;
    }
    @GetMapping("/loginAction")
    public String login(User user,Model model,HttpSession session){
        model.addAttribute("user",null) ;
        User login = userService.login(user);
        if(login != null){
            //创建购物车
            List<CarIterm> carIterms = new ArrayList<>();
            session.setAttribute("buycar",carIterms);

            //登录成功保存用户信息
            session.setAttribute("user" ,login) ;
            return "index" ; //返回首页
        }
        else {
            model.addAttribute("msg","登录失败,请检查是否注册") ;
            return "user/error";
        }
    }
    //个人信息页面
    @GetMapping("/user")
    public  String user(){
        return  "user/user_inf" ;
    }
    //
    //前往修改个人信息页面
    @GetMapping("/update")
    public  String updatePage(){
        return  "user/user_change" ;
    }
    //处理修改个人信息请求 采用方法PUT
    @PutMapping("/update")
    public  String update(User user,HttpSession session,Model model){
        //得到原来的USER
        User user1 = (User)session.getAttribute("user");
        user.setPassdWord(user1.getPassdWord());
        user.setQuestion(user1.getQuestion());
        user.setRegDate(user1.getRegDate());
        user.setAnswer(user1.getAnswer());
        user.setId(user1.getId()); //把原来的ID得到的USER不然不会执行UPDATE语句而是INSERT语句

        User update = userService.update(user);
        if(update != null){
            //更新在session中的USER
            session.setAttribute("user",update);
            return "user/user_inf" ; //返回到个人信息页面
        }
        else{
            model.addAttribute("msg","修改个人信息失败") ;
            return "user/error" ;
        }
    }
    @GetMapping("/exit")
    public  String  exit(HttpSession session){
        //退出把用户信息设置为NUll即可
        session.setAttribute("user",null);
        session.setAttribute("buycar",null);
        return "index" ;
    }
}
