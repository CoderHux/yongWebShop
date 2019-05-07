package com.yong.springboot.service.Imp;

import com.yong.springboot.entities.User;
import com.yong.springboot.repository.UserRepository;
import com.yong.springboot.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService  implements UserServiceInterface {
    //注入UserDAO类
    @Autowired
    UserRepository userRepository ;

    @Override
    public User login(User user) {
        //this method search USER by userName and passdWord
        Example<User> example = Example.of(user) ;
        Optional<User> one = userRepository.findOne(example);
        if(one.isPresent()) //存在用户即返回
            return one.get() ;
        else
            return null;
    }

    @Override
    @CacheEvict(allEntries = true,cacheNames = "admin")
    public User regist(User user) {
        User save = userRepository.save(user);
        if(user != null)
            return user;
        else
            return  null ;
    }
    @CacheEvict(allEntries = true,cacheNames = "admin")
    @Override
    public User update(User user) {
        User user1 = userRepository.saveAndFlush(user);
        if(user != null)
            return user1;
        else
            return null ;

    }

    @Override
    public User getUser(Integer id) {
        Optional<User> byId = userRepository.findById(id);
        if ((byId.isPresent())) {
                return byId.get();
        }
        else
            return  null ;
    }
}
