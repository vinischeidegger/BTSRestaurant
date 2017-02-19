package org.bts_mdsd.javafoundations;

import java.io.FileNotFoundException;
import java.util.List;

import org.bts_mdsd.javafoundations.model.BTSRestaurantDish;
import org.bts_mdsd.javafoundations.model.FileExtract;
import org.bts_mdsd.javafoundations.model.Order;
import org.bts_mdsd.javafoundations.service.OrderManager;

public class Main {

	public static void main(String[] args) {
		System.out.println("Please select a file, or press enter for the default one [online_order_sample.csv].");
		
		
		
		String filePath = "online_order_sample.csv";
		OrderManager ordMan = new OrderManager();
		FileExtract fileExtract = new FileExtract();
		
		try {
			fileExtract = ordMan.generateOrderFromFile(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Order> orderList = fileExtract.getOrderList();
		List<BTSRestaurantDish> dishList = fileExtract.getDishList();
		
		int numbOrders = ordMan.getNumberOrders(orderList);
		
		System.out.println("\n\nTotal of Orders= " + numbOrders);
		System.out.println("\nOrder number 1 is: " + ordMan.getOrder(orderList, 0));
		System.out.println("\nOrder number 2 is: " + ordMan.getOrder(orderList, 1));
		System.out.println("\nOrder number 3 is: " + ordMan.getOrder(orderList, 2));
		System.out.println("\n" + ordMan.getAllOrdersToString(orderList));
		System.out.println("Main Course Dishes: " + ordMan.getDishesByType(dishList, "mc"));
		System.out.println("Veggie Dishes: " + ordMan.getDishesByFeature(dishList, "vgd"));
		System.out.println("" + ordMan.getStatsByDishType(dishList, "mc"));
		
			

	}
	
}
