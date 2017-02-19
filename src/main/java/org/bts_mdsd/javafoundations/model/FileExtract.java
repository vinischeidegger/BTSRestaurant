package org.bts_mdsd.javafoundations.model;

import java.util.ArrayList;
import java.util.List;

public class FileExtract {
	
	private List<Order> orderList = new ArrayList<Order>();
	private List<BTSRestaurantDish> dishList = new ArrayList<BTSRestaurantDish>();
	
	public List<Order> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
	public List<BTSRestaurantDish> getDishList() {
		return dishList;
	}
	public void setDishList(List<BTSRestaurantDish> dishList) {
		this.dishList = dishList;
	}

}
