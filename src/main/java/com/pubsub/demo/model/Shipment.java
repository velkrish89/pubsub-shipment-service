package com.pubsub.demo.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.pubsub.demo.utils.OrderStatus;
import com.pubsub.demo.utils.ShippingStatus;

@Entity
public class Shipment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long shipmentId;
	private long orderId;
	private String orderDate;
	
	@OneToMany(targetEntity = Product.class, cascade = CascadeType.ALL)
	@JoinColumn(name="fk_ship_product", referencedColumnName = "shipmentId")
	private List<Product> products;
	private double price;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@Enumerated(EnumType.STRING)
	private ShippingStatus shippingStatus;
	private String lastUpdated;
	
	public long getShipmentId() {
		return shipmentId;
	}
	
	public void setShipmentId(long shipmentId) {
		this.shipmentId = shipmentId;
	}
	
	public long getOrderId() {
		return orderId;
	}
	
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
	public String getOrderDate() {
		return orderDate;
	}
	
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	public List<Product> getProducts() {
		return products;
	}
	
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public ShippingStatus getShippingStatus() {
		return shippingStatus;
	}
	
	public void setShippingStatus(ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}
	
	public String getLastUpdated() {
		return lastUpdated;
	}
	
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Shipment(long shipmentId, long orderId, String orderDate, List<Product> products, double price,
			OrderStatus orderStatus, ShippingStatus shippingStatus, String lastUpdated) {
		super();
		this.shipmentId = shipmentId;
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.products = products;
		this.price = price;
		this.orderStatus = orderStatus;
		this.shippingStatus = shippingStatus;
		this.lastUpdated = lastUpdated;
	}
	
	public Shipment() {
		
	}
	
	
	

}
