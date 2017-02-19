package org.bts_mdsd.javafoundations.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bts_mdsd.javafoundations.OnlineOrderOps;
import org.bts_mdsd.javafoundations.model.BTSRestaurantDish;
import org.bts_mdsd.javafoundations.model.BTSRestaurantDish.OrderType;
import org.bts_mdsd.javafoundations.model.FileExtract;
import org.bts_mdsd.javafoundations.model.Order;
import org.eclipse.collections.impl.list.mutable.ListAdapter;


public class OrderManager implements OnlineOrderOps<Order, BTSRestaurantDish> {
	private static final Logger log = LogManager.getRootLogger();

	@Override
	public int getNumberOrders(List<Order> orderList) {
		return orderList.size();
	}

	@Override
	public Order getOrder(List<Order> orderList, int orderIndex) {
		return orderList.get(orderIndex);
	}

	@Override
	public String getAllOrdersToString(List<Order> orderList) {
		return orderList.toString();
	}

	@Override
	public BTSRestaurantDish getDish(List<BTSRestaurantDish> dishList, int dishIndex) {
		return dishList.get(dishIndex);
	}

	@Override
	public String getAllDishToString(List<BTSRestaurantDish> dishList) {
		return dishList.toString();
	}

	@Override
	public List<BTSRestaurantDish> getDishesByType(List<BTSRestaurantDish> dishList, String dishType) {
		return ListAdapter.adapt(dishList).stream()
								.filter(dish -> dish.getOrderType().getText().equalsIgnoreCase(dishType))
								.collect(Collectors.toList());
	}

	@Override
	public List<BTSRestaurantDish> getDishesByFeature(List<BTSRestaurantDish> dishList, String feature) {
		List<BTSRestaurantDish> dishesFromType = new ArrayList<BTSRestaurantDish>();

		switch (feature) {
		case "gfd":
			dishesFromType = ListAdapter.adapt(dishList).stream()
								.filter(dish -> dish.isGlutenFree()).collect(Collectors.toList());
			break;
		case "vgd":
			dishesFromType = ListAdapter.adapt(dishList).stream()
								.filter(dish -> dish.isVeggie()).collect(Collectors.toList());
			break;
		case "hmd":
			dishesFromType = ListAdapter.adapt(dishList).stream()
								.filter(dish -> dish.isHalal()).collect(Collectors.toList());
			break;
		case "sfd":
			dishesFromType = ListAdapter.adapt(dishList).stream()
								.filter(dish -> dish.isSeaFoodFree()).collect(Collectors.toList());
			break;
		default:
			throw new IllegalArgumentException("Non expected Feature: " + feature);
		}

		return dishesFromType;
	}

	/*
	 * Method to check the stats by Dish Type and return it into a formatted String
	 * @see org.bts_mdsd.javafoundations.OnlineOrderOps#getStatsByDishType(java.util.List, java.lang.String)
	 */
	@Override
	public String getStatsByDishType(List<BTSRestaurantDish> dishList, String dishType) {
		int total = dishList.size();
		double percentage = 10;
		if(total>0) {
			int nbOfDishesOfDishType = ListAdapter.adapt(dishList)
					.count(dish -> dish.getOrderType().getText().equalsIgnoreCase(dishType));
			percentage = ((float)nbOfDishesOfDishType / (float)total) * 100F;
		}
		return String.format("%s represents %.2f%% of the available dishes" , BTSRestaurantDish.OrderType.fromString(dishType), percentage);
	}
	
	public String getTypeStatsFromOrders(List<Order> orderList){
		Map<OrderType, List<BTSRestaurantDish>> orderListByType = orderList.stream().map(order -> order.getDish())
				.collect(Collectors.groupingBy(BTSRestaurantDish::getOrderType));
		StringBuilder stats = new StringBuilder();
		int totalOrders = orderList.size(); 
		for(OrderType oType : orderListByType.keySet()){
			stats.append(oType).append(" - ").append(orderListByType.get(oType).size()).append(" of ").append(totalOrders).append(" orders.\n");
		}
		return stats.toString();
	}
	
	public String getFeatureStatsFromOrders(List<Order> orderList){
		StringBuilder stats = new StringBuilder();
		float orderQty = orderList.size();
		List<BTSRestaurantDish> dishList = orderList.stream().map(order -> order.getDish()).collect(Collectors.toList());
		stats.append("Stats from all orders based on their features:\n")
			.append(String.format("%6.2f",(float)getDishesByFeature(dishList, "vgd").size()*100f/orderQty)).append("% of the ordered dishes are Veggie.\n")
			.append(String.format("%6.2f",(float)getDishesByFeature(dishList, "gfd").size()*100f/orderQty)).append("% of the ordered dishes are Gluten Free.\n")
			.append(String.format("%6.2f",(float)getDishesByFeature(dishList, "hmd").size()*100f/orderQty)).append("% of the ordered dishes are Halal Meat.\n")
			.append(String.format("%6.2f",(float)getDishesByFeature(dishList, "sfd").size()*100f/orderQty)).append("% of the ordered dishes are Sea Food Free.\n");

		return stats.toString();
	}
	
	public String getStatsForAllCustomers(List<Order> orderList) {
		
		StringBuilder stats = new StringBuilder();
		
		Map<String, List<Order>> orderListByCustomer = orderList.stream().collect(
				 Collectors.groupingBy(Order::getCustomerName));
		for(String customer : orderListByCustomer.keySet()){
			
			stats.append("------------------------------------------------------------------\n   ")
				.append(customer).append(" has ").append(orderListByCustomer.get(customer).size())
				.append(" orders.\n");
			
			List<BTSRestaurantDish> dishesFromCustomer = orderListByCustomer.get(customer).stream()
				.map(order -> order.getDish()).collect(Collectors.toList());
			float totalNbOfDishes = (float) dishesFromCustomer.size();
			stats.append("   ").append(customer).append(" has ordered ").append(dishesFromCustomer.size()).append(" different dishes:\n");
				for(BTSRestaurantDish d:dishesFromCustomer) {
					OrderType oType = d.getOrderType();
					stats.append("     ").append(d.getDishName().toUpperCase()).append(" - which is a ").append(oType).append(", ");
					String extraValue = d.getExtras();
					if (extraValue.isEmpty()) {
						stats.deleteCharAt(stats.length()-1);
					} else {
						switch (oType) {
						case STARTER:
							stats.append("needs ").append(d.getExtras());
							break;
						case MAIN_COURSE:
							String[] extras = d.getExtras().split("-");
							if(extras.length==2){
								stats.append("is made of ").append(extras[0]).append(" and goes well with ").append(extras[1]);
							} else {
								stats.deleteCharAt(stats.length()-1);
							}
							break;
						case DESSERT:
							stats.append("has ").append(d.getExtras()).append(" calories");
							break;
						default:
							log.error("Type not implemented: ["+oType+"]");
							break;
						}
					}
					stats.append(".\n");
				}
				stats.append("\n     Veggie Dishes: ").append((float)getDishesByFeature(dishesFromCustomer, "vgd").size()*100f/totalNbOfDishes)
				.append("%\n     Gluten Free Dishes: ").append((float)getDishesByFeature(dishesFromCustomer, "gfd").size()*100f/totalNbOfDishes)
				.append("%\n     Halal Meat Dishes: ").append((float)getDishesByFeature(dishesFromCustomer, "hmd").size()*100f/totalNbOfDishes)
				.append("%\n     Sea Food Free Dishes: ").append((float)getDishesByFeature(dishesFromCustomer, "sfd").size()*100f/totalNbOfDishes).append("%\n");
			
		}
		stats.append("------------------------------------------------------------------");
		return stats.toString();
	}

	/**
	 * The objective of this method is to read an Order File and load its data in memory
	 * @param filePath - The complete path of the file containing the orders to be read
	 * @return FileExtract - This method returns an instance of a FileExtract class.
	 * 
	 * This class will contain two lists - one with the orders and another with a summary 
	 *  of all the different dishes.
	 *  
	 * @throws FileNotFoundException 
	 */
	public FileExtract generateOrderFromFile(String filePath) throws FileNotFoundException {
		FileExtract fExtract = new FileExtract();
		
		log.debug("Working Directory = " + System.getProperty("user.dir"));
		
		FileInputStream fis = new FileInputStream(filePath);
		int numbLines = 0;
		List<Order> orderList = new ArrayList<Order>();
		List<BTSRestaurantDish> dishList = new ArrayList<BTSRestaurantDish>();
		
		/*
		 * The default order is taken from the sample file.
		 */
		int customerColPos = 0;
		int dishNameColPos = 1;
		int OrderTypeColPos = 2;
		int gfdColPos = 3;
		int vgdColPos = 4;
		int hmdColPos = 5;
		int sfdColPos = 6;
		int extraColPos = 7;

		try (Scanner scan = new Scanner(fis);) {
			while (scan.hasNextLine()) {
				numbLines++;
				String currentLine = scan.nextLine();
				
				String[] values = currentLine.split(",");
								
				log.debug("Line " + numbLines + " contains " + values.length + " fields\n");
				
				if (numbLines==1 && currentLine.contains("customerName")) {
					/*
					 * If the file contains a header, the program will check the column position from the header
					 * otherwise it will get it from default
					 */
					log.debug(" ** Your file contains a header! **");

					for(int i=0; i<values.length; i++) {
						log.trace("Line [" + numbLines + "], Column [" + i + "], Value [" + values[i] + "]");
					}

					/*
					 * The column order is then defined by checking the file header 
					 */
					for(int i=0; i<values.length; i++) {
						switch (values[i].toLowerCase()) {
						case "customername":
							customerColPos = i;
							break;
						case "dishname":
							dishNameColPos = i;
							break;
						case "type":
							OrderTypeColPos = i;
							break;
						case "gfd":
							gfdColPos = i;
							break;
						case "vgd":
							vgdColPos = i;
							break;
						case "hmd":
							hmdColPos = i;
							break;
						case "sfd":
							sfdColPos = i;
							break;
						case "extras":
							extraColPos = i;
							break;
						default:
							break;
						}
					}
				
				} else {
					
					for(int i=0; i<values.length; i++) {
						log.trace("Line [" + numbLines + "], Column [" + i + "], Value [" + values[i] + "]");
					}
					
					Order newOrder = new Order();
					newOrder.setCustomerName(values[customerColPos]);
					String dishName = values[dishNameColPos];
					
					BTSRestaurantDish dish = new BTSRestaurantDish(dishName, values[OrderTypeColPos],
							values[gfdColPos],values[vgdColPos],values[hmdColPos],values[sfdColPos]);
					
					boolean dishFound = false;
					for(BTSRestaurantDish d: dishList){
						if(d.equals(dish)){
							dish = d;
							dishFound = true;
							break;
						}
					}
					
					if(!dishFound){
						dishList.add(dish);
					}
					
					dish.getOrderList().add(newOrder);
					dish.setExtras(values[extraColPos]);
					
					newOrder.setDish(dish);
			
					orderList.add(newOrder);
				}
			}

			fExtract.setDishList(dishList);
			fExtract.setOrderList(orderList);

		}
		catch (Exception e) {
			throw e;
		}
		
		return fExtract;

	}

}
