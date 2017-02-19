package org.bts_mdsd.javafoundations.model;

public class Order {
	
	private String customerName;
	private BTSRestaurantDish dish;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public BTSRestaurantDish getDish() {
		return dish;
	}
	public void setDish(BTSRestaurantDish dish) {
		this.dish = dish;
	}

	@Override
	public String toString() {
		return "Order [customerName=" + customerName + ", dish=" + dish.toString() + "]";
	}
	
}
