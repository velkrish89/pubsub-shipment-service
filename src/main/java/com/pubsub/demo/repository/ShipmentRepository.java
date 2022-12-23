package com.pubsub.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pubsub.demo.model.Shipment;
import com.pubsub.demo.utils.ShippingStatus;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long>{
	
	
	Optional<Shipment> findByOrderId(long orderId);
	
	@Query("SELECT s FROM Shipment s WHERE s.shippingStatus = :#{#status}")
	List<Shipment> findByShippingStatus(ShippingStatus status);
	

}
