package com.pubsub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pubsub.demo.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
