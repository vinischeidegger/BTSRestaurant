package org.bts_mdsd.javafoundations.model;

public class Order {
	
	private String customerName;
	private Dish dish;
	private String extras;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Dish getDish() {
		return dish;
	}
	public void setDish(Dish dish) {
		this.dish = dish;
	}
	public String getExtras() {
		return extras;
	}
	public void setExtras(String extras) {
		this.extras = extras;
	}

	@Override
	public String toString() {
		return "Order [customerName=" + customerName + ", dish=" + dish.toString()
				+ ", extras=" + extras + "]";
	}
	
}
