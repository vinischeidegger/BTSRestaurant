package org.bts_mdsd.javafoundations.model;

import java.util.ArrayList;
import java.util.List;

public class BTSRestaurantDish extends Dish {

	public enum	OrderType {
		STARTER("st"),
		MAIN_COURSE("mc"),
		DESSERT("ds");
		
		private String text;
		
		OrderType(String text){
			this.text = text;
		}
		
		public String getText() {
			return this.text;
		}
		
		public static OrderType fromString(String text) {
			for (OrderType t : OrderType.values()) {
				if (t.text.equalsIgnoreCase(text)) {
					return t;
				}
			}
			return null;
		}
	}
	
	private OrderType orderType;
	private boolean isGlutenFree;
	private boolean isVeggie;
	private boolean isHalal;
	private boolean isSeaFoodFree;
	private List<Order> orderList = new ArrayList<Order>();

	public BTSRestaurantDish(String dishName, String orderType, String isGlutenFree, String isVeggie, String isHalal, String isSeaFoodFree) {
		super();
		this.dishName = dishName;
		this.orderType = OrderType.fromString(orderType);
		this.isGlutenFree = testStringValue(isGlutenFree);
		this.isVeggie = testStringValue(isVeggie);
		this.isHalal = testStringValue(isHalal);
		this.isSeaFoodFree = testStringValue(isSeaFoodFree);
	}
	
	public OrderType getOrderType() {
		return orderType;
	}
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	public boolean isGlutenFree() {
		return isGlutenFree;
	}
	public void setGlutenFree(boolean isGlutenFree) {
		this.isGlutenFree = isGlutenFree;
	}
	public boolean isVeggie() {
		return isVeggie;
	}
	public void setVeggie(boolean isVeggie) {
		this.isVeggie = isVeggie;
	}
	public boolean isHalal() {
		return isHalal;
	}
	public void setHalal(boolean isHalal) {
		this.isHalal = isHalal;
	}
	public boolean isSeaFoodFree() {
		return isSeaFoodFree;
	}
	public void setSeaFoodFree(boolean isSeaFoodFree) {
		this.isSeaFoodFree = isSeaFoodFree;
	}
	public List<Order> getOrderList() {
		return orderList;
	}

	public void setGlutenFree(String isGlutenFree) {
		this.isGlutenFree = testStringValue(isGlutenFree);
	}
	public void setVeggie(String isVeggie) {
		this.isVeggie = testStringValue(isVeggie);
	}
	public void setHalal(String isHalal) {
		this.isHalal = testStringValue(isHalal);
	}
	public void setSeaFoodFree(String isSeaFoodFree) {
		this.isSeaFoodFree = testStringValue(isSeaFoodFree);
	}
	
	private boolean testStringValue(String value) {
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
		    return Boolean.valueOf(value);
		} else {
		    throw new ClassCastException();
		}
	}
	
	@Override
	public String toString() {
		return "BTSRestaurantDish [dishName=" + dishName + ", orderType=" + orderType + ", isGlutenFree=" + isGlutenFree + ", isVeggie=" + isVeggie + ", isHalal=" + isHalal
				+ ", isSeaFoodFree=" + isSeaFoodFree + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dishName == null) ? 0 : dishName.hashCode());
		result = prime * result + (isGlutenFree ? 1231 : 1237);
		result = prime * result + (isHalal ? 1231 : 1237);
		result = prime * result + (isSeaFoodFree ? 1231 : 1237);
		result = prime * result + (isVeggie ? 1231 : 1237);
		result = prime * result + ((orderType == null) ? 0 : orderType.hashCode());
		return result;
	}

	/**
	 * Method used to compare two instances of BTSRestaurantDish.
	 * DishName (from superclass Dish) has alto to be equal to make two instances equal.
	 * OrderList is not taken into account, as they are a back reference (non thread-safe)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BTSRestaurantDish other = (BTSRestaurantDish) obj;
		if (dishName == null) {
			if (other.dishName != null)
				return false;
		} else if (!dishName.equals(other.dishName))
			return false;
		if (isGlutenFree != other.isGlutenFree)
			return false;
		if (isHalal != other.isHalal)
			return false;
		if (isSeaFoodFree != other.isSeaFoodFree)
			return false;
		if (isVeggie != other.isVeggie)
			return false;
		if (orderType != other.orderType)
			return false;
		return true;
	}


}
