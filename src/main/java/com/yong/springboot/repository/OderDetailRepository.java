package com.yong.springboot.repository;

import com.yong.springboot.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OderDetailRepository extends JpaRepository<OrderDetail,Integer> {
}
