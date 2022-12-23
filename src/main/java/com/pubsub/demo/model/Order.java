package com.pubsub.demo.model;

import java.util.Date;
import java.util.List;

import com.pubsub.demo.utils.OrderStatus;
import com.pubsub.demo.utils.ShippingStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Order {
	
	private long orderId;
	private String orderDate;
	private List<Product> products;
	private double price;
	private OrderStatus orderStatus;
	
	public Order() {

	}
	
	public Order(long orderId, List<Product> products, double price) {
		this.orderId = orderId;
		this.products = products;
		this.price = price;
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

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}


	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Order(long orderId, String orderDate, List<Product> products, double price, OrderStatus orderStatus) {
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.products = products;
		this.price = price;
		this.orderStatus = orderStatus;
	}
	
	
}
