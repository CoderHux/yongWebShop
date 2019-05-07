package com.yong.springboot.repository;


import com.yong.springboot.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type,Integer> {
}
