# yongWebShop
一个简易的模拟电子商城，前端：bootstrap 模板引擎：thymeleaf 后端：spring-boot spring-data-jpa 数据库：mysql 缓存：redis
## 使用
该项目按照spring-boot正常使用即可（MAVEN），但是要注意配置文件的修改
运行前请前往src\main\resources\application.yml文件进行修改
~~~ yml
#请配置自己的数据库信息
    username: root
    password: 888888
redis:
    #此处配置Redis服务器IP地址，需自己手动配置
    host: 192.168.127.133
~~~
