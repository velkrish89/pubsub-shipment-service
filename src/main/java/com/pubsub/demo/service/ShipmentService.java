package com.pubsub.demo.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.pubsub.demo.exception.NoOrderFoundException;
import com.pubsub.demo.model.Order;
import com.pubsub.demo.model.Shipment;
import com.pubsub.demo.repository.ShipmentRepository;
import com.pubsub.demo.utils.ShippingStatus;

@Service
public class ShipmentService {
	
	private static Logger logger = LoggerFactory.getLogger(ShipmentService.class);
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	public Optional<Shipment> getShipmentByOrderId(long orderId) {
		
		Optional<Shipment> shipment = shipmentRepository.findByOrderId(orderId);
		
		return shipment;
	}
	
	public List<Shipment> getOrdersByShipmentStatus(String status) {
		
		List<Shipment> orders = null;

		switch(status) {
		
		case "NOT_STARTED":
			orders = shipmentRepository.findByShippingStatus(ShippingStatus.NOT_STARTED);
			break;
			
		case "INPROGRESS":
			orders = shipmentRepository.findByShippingStatus(ShippingStatus.INPROGRESS);
			break;
			
		case "SHIPPED":
			orders = shipmentRepository.findByShippingStatus(ShippingStatus.SHIPPED);
			break;
			
		case "DELIVERED":
			orders = shipmentRepository.findByShippingStatus(ShippingStatus.DELIVERED);
			break;	
			
		default:
			orders = Collections.EMPTY_LIST;
			break;
		}
		return orders;
	}
	
	
	public Shipment updateShippingStatus(long orderId, String status) {
		
		Optional<Shipment> shipment = shipmentRepository.findByOrderId(orderId);

		ShippingStatus shipStatus = ShippingStatus.valueOf(status);

		Shipment result = null;
		Shipment current = shipment.orElseThrow(NoOrderFoundException::new);
		current.setShippingStatus(shipStatus);
		current.setLastUpdated(new Date().toString());
		result = shipmentRepository.save(shipment.get());
		
		return result;
	}
	
	
	
	// Adding all the orders from google pub/sub to db
	private String message;
	
	@Bean
	public MessageChannel inputChannel() {
		return new DirectChannel();
	}
	
	
	@Bean
	public PubSubInboundChannelAdapter messageAdapter(
			
			@Qualifier("inputChannel") MessageChannel channel,
			PubSubTemplate template
			) {
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(template, "order-topic-sub");
		adapter.setOutputChannel(channel);
		return adapter;
	}
	
	@ServiceActivator(inputChannel = "inputChannel")
	public void receiveMessage(String payload) throws JsonProcessingException {
		
		logger.info("payload from subscriber: " + payload.toString());
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(
		          DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
		          true);
		Order order = mapper.readValue(payload, Order.class);
		
		//Create new shipment
		Shipment shipment = new Shipment();
		shipment.setOrderId(order.getOrderId());
		shipment.setOrderDate(order.getOrderDate());
		shipment.setOrderStatus(order.getOrderStatus());
		shipment.setPrice(order.getPrice());
		shipment.setProducts(order.getProducts());
		shipment.setShippingStatus(ShippingStatus.NOT_STARTED);
		shipment.setLastUpdated(new Date().toString());
		
		//Add in Shipment db
		Shipment result = shipmentRepository.save(shipment);
		logger.info("Shipment added to db: "+ mapper.writeValueAsString(result).toString());
		
		this.message = mapper.writeValueAsString(result).toString();				
		
	}

}
