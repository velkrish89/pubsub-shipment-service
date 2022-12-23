package com.pubsub.demo.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pubsub.demo.exception.NoOrderFoundException;
import com.pubsub.demo.model.Shipment;
import com.pubsub.demo.service.ShipmentService;
import com.pubsub.demo.utils.ShippingStatus;
import com.pubsub.demo.utils.StatusChangeRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/shipmentservice")
public class ShipmentController {
	
	@Autowired
	private ShipmentService shipmentService;

	@Operation(summary = "To fetch all the shipments by shipping status from DB")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = "Fetched all the shipments by shipping status"),
			@ApiResponse(responseCode = "404", 
			description = "No records found with given shipping status"),
			@ApiResponse(responseCode = "500", 
			description = "Internal Server Error")
	})
	@GetMapping("/shipment/{status}")
	public ResponseEntity<List<Shipment>> getOrdersByShipmentStatus(@PathVariable String status) {
		
		List<Shipment> orders = shipmentService.getOrdersByShipmentStatus(status);
		
		if(orders.isEmpty()) {
			throw new NoOrderFoundException();
		}
		
		return new ResponseEntity<List<Shipment>>(orders, HttpStatus.OK);
	}
	
	
	@Operation(summary = "To fetch shipment by order id from DB")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = "Fetched shipment by order id"),
			@ApiResponse(responseCode = "404", 
			description = "No records found with given order id"),
					@ApiResponse(responseCode = "500", 
					description = "Internal Server Error")
	})
	@GetMapping("/order/{orderId}")
	public ResponseEntity<?> getShipmentByOrderId(@PathVariable long orderId) {
		
		Shipment shipment = shipmentService
							.getShipmentByOrderId(orderId)
							.orElseThrow(() -> new NoOrderFoundException());
		
		return new ResponseEntity<Shipment>(shipment, HttpStatus.OK);
	}
	
	
	
	@Operation(summary = "To update shipment status by order id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = "Updated shipment status by order id"),
			@ApiResponse(responseCode = "404", 
			description = "No records found with given order id"),
			@ApiResponse(responseCode = "500", 
			description = "Internal Server Error")
	})
	@PutMapping("/order/{orderId}")
	public ResponseEntity<?> updateShippingStatusByOrderId(@PathVariable long orderId, @RequestBody StatusChangeRequest request) {
		
		Shipment shipment = shipmentService
							.updateShippingStatus(orderId, request.getShippingStatus());
		
		return new ResponseEntity<Shipment>(shipment, HttpStatus.OK);
	}
	
	
	
	
	


}
