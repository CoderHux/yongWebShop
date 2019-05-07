package com.yong.springboot.service.Imp;

import com.yong.springboot.entities.*;
import com.yong.springboot.repository.OderDetailRepository;
import com.yong.springboot.repository.OrderRepository;
import com.yong.springboot.repository.ProductRepository;
import com.yong.springboot.service.ShopServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "products") //该类的数据缓存名为products
public class ShopService implements ShopServiceInterface {
    @Autowired
    ProductRepository productRepository ;
    @Autowired
    OrderRepository orderRepository ;
    @Autowired
    OderDetailRepository oderDetailRepository ;


    @Override
    @Cacheable()
    public List<Product> getProductsByIndex(Integer index) {
        //添加查询条件 返回状态为待售的产品
        Product product = new Product();
        product.setStatus(1);
        Example<Product> example = Example.of(product);
        //四个一页
        PageRequest pageRequest = PageRequest.of(index - 1, 4);
        Page<Product> products = productRepository.findAll(example,pageRequest);

        return  products.getContent() ;
    }

    @Override
    public Product getProduct(Product product) {
        Optional<Product> one = productRepository.findOne(Example.of(product));
        if (one.isPresent())
            return  one.get();
        else
            return null ;
    }
    public  Product getProduct(Integer productId){
       return productRepository.findById(productId).get();
    }
    //以商品名进行模糊查询
    public List<Product> getProduct(String productName){
        Product product =new Product() ;
        product.setName(productName);
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", match -> match.contains());
        Example<Product> productExample = Example.of(product, matcher);
        List<Product> productList = productRepository.findAll(productExample);
        if(productList.size() != 0 ){
            return  productList ;
        }
        else
            return null ;
    }
    @Override
    //有数据更新即把全部缓存删除

    public Product updateProduct(Product product) {
         return  productRepository.save(product);


    }
    //有数据更新即把缓存全部删除

    public List<Product> updateProductByList(List<Product> productList){
        return productRepository.saveAll(productList);


    }
    @Override
    public boolean deleteProduct(Product product) {
        return false;
    }

    //为该方法添加缓存
    // 返回商品页数
    @Cacheable(key = "#root.methodName")
    public  int  getPageNum(){
        //添加查询条件 返回状态为待售的产品
        Product product = new Product();
        product.setStatus(1);
        Example<Product> example = Example.of(product);
        //四个一页
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<Product> products = productRepository.findAll(example,pageRequest);
        return  products.getTotalPages() ;
    }
    public Product getProductById(Integer id){
        Product product = productRepository.findById(id).get();
        return product ;
    }
    //以一个商品下单
    //需要清空缓存
    @Transactional//默认为Required
    @CacheEvict(allEntries = true ,cacheNames = {"products","admin"})
    public boolean order(Integer productId,Integer userId){
        Product product = productRepository.findById(productId).get();

        //创建订单实体
        Order order = new Order();
        User user = new User();
        user.setId(userId);
        order.setUser(user);
        order.setOrderTime(new Date());
        order.setStatus("已付款");
        order.setOrderPrice(product.getPrice());


        //创建订单详情表格
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setNum(1);
        orderDetail.setPid(product.getId());
        order.addOrderDetail(orderDetail);

        Order save = orderRepository.save(order);

        if(save != null ) {
            //购买成功,对应商品数量-1
            product.setNum(product.getNum()-1);
            this.updateProduct(product) ;
            return true;
        }
        else
            return false ;

    }
    //以购物车下单
    //需要清空缓存
    @Transactional
    @CacheEvict(allEntries = true ,cacheNames = {"products","admin"})
    public boolean  order(List<CarIterm> products , Integer userId){
        //创建实体订单
        Order order = new Order();
        User user = new User();
        user.setId(userId);
        order.setUser(user);
        order.setOrderTime(new Date());
        order.setStatus("已付款");
        double sum = 0.0 ;

        for(CarIterm p : products){
            sum=sum + p.getProduct().getPrice()*p.getNum() ;
        }
        order.setOrderPrice(sum);

         //创建订单详情表格

        for (CarIterm carIterm : products) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setNum(carIterm.getNum());
            orderDetail.setPid(carIterm.getProduct().getId());
            order.addOrderDetail(orderDetail);
        }
        //保存订单
       orderRepository.save(order);

        //创建订单成功更新商品
        List<Product> productList = new ArrayList<>() ;
        for(CarIterm carIterm: products){
            Product p = carIterm.getProduct() ;
            //数量更新
           p.setNum(p.getNum()-carIterm.getNum());
           productList.add(p) ;
        }
        //更新到数据库
        this.updateProductByList(productList) ;


        return true ;
    }
    //find Orders By UserId
    public List<Order> findOrder(Integer userId){
        Order order = new Order();
        User user = new User();
        user.setId(userId);
        order.setUser(user);
        //得到所有该用户的订单
        List<Order> orders = orderRepository.findAll(Example.of(order));

        if(orders.size() != 0)
            return orders ;
        else
            return null ;
    }
    //find OrderDetail BY OrderID
    public List<OrderDetail> findOrderDetail(Integer orderId){
        OrderDetail orderDetail = new OrderDetail();
        Order order = new Order();
        order.setId(orderId);
        orderDetail.setOrder(order);

        List<OrderDetail> details = oderDetailRepository.findAll(Example.of(orderDetail));

        if (details.size() != 0)
            return details ;
        else
            return null ;
    }

}
