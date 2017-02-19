package org.bts_mdsd.javafoundations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bts_mdsd.javafoundations.model.BTSRestaurantDish;
import org.bts_mdsd.javafoundations.model.Dish;
import org.bts_mdsd.javafoundations.model.FileExtract;
import org.bts_mdsd.javafoundations.model.Order;
import org.bts_mdsd.javafoundations.service.OrderManager;

public class Main {
	
	private static final String DEFAULT_FILE = "online_order_sample.csv";

	public static void main(String[] args) throws IOException {
		System.out.println("Welcome to the BTS Restaurant!");
		Map<String,FileExtract> fileContent = new HashMap<String, FileExtract>();
		
		String currentFile = "";

		String userInput = "";
		boolean hasLoadedFile = false;
		while(!userInput.equalsIgnoreCase("x")) {
			System.out.println("------------------------------------------------------------------\nPlease chose an option from the menu below:");
			
			if (currentFile.isEmpty()){
				System.out.println("\n1. Load a file.");
			} else {
				hasLoadedFile=true;

				System.out.println("\n1. Load another file. This will unload the current one.");
				System.out.println("       Loaded file: ["+currentFile+"]");
				System.out.println("            2. Get the total number of orders");
				System.out.println("            3. Get a single order");
				System.out.println("            4. Get all orders");
				System.out.println("            5. Get extra stats");
			}
			System.out.println("X. Exit.");
	
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			userInput = br.readLine();
			OrderManager ordMan = new OrderManager();

	        switch (userInput) {
			case "1": //Load a file
				System.out.println("Please enter the file path, or press enter for the default file [" + DEFAULT_FILE + "].");
				System.out.println("After 3 wrong attempts the default file will be used for testing purposes [" + DEFAULT_FILE + "].");
				System.out.println("File path: ");
				
				String inputFile = br.readLine();
		        
		        //String inputFile = System.console().readLine();
				File varTmpDir = new File(inputFile);
				int attempts = 0;
				while(!varTmpDir.exists() && attempts<3 && !inputFile.isEmpty()) {
					attempts++;
					System.out.println("["+ attempts +"] Please select a file, or press enter for the default one [" + DEFAULT_FILE + "].");
					inputFile = br.readLine();
					varTmpDir = new File(inputFile);
				}
				String filePath;
				if(!varTmpDir.exists()){
					filePath = DEFAULT_FILE;
					System.out.println("Default file being used.");
				} else {
					filePath = inputFile;
				}
				
				FileExtract fileExtract = new FileExtract();
				
				try {
					fileExtract = ordMan.generateOrderFromFile(filePath);
					fileContent.put(filePath, fileExtract);
					currentFile = filePath;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				List<Order> orderList = fileExtract.getOrderList();
				List<BTSRestaurantDish> dishList = fileExtract.getDishList();
				
				int recordNb = ordMan.getNumberOrders(orderList);
				
				System.out.println("The file ["+ filePath +"] contains "+ recordNb +" records.\n");
				
				// Only display stats for files with more than 1 row
				if(recordNb>0) {
					
					if(recordNb==1){
						System.out.println("The only available order from the file is below:");
						System.out.println("  [1]: " + ordMan.getOrder(orderList, 0));
					} else if(recordNb==2){
						System.out.println("The two orders available from the file are below:");
						System.out.println("  [1]: " + ordMan.getOrder(orderList, 0));
						System.out.println("  [2]: " + ordMan.getOrder(orderList, 1));
					} else {
						System.out.println("The first 3 orders from the file are below:");
						System.out.println("  [1]: " + ordMan.getOrder(orderList, 0));
						System.out.println("  [2]: " + ordMan.getOrder(orderList, 1));
						System.out.println("  [3]: " + ordMan.getOrder(orderList, 2));
					}
					
					System.out.println("\n  All orders: " + ordMan.getAllOrdersToString(orderList));
					System.out.println("\nDishes by Type:\n  Starters: " + ordMan.getDishesByType(dishList, "st"));
					System.out.println("  Main Courses: " + ordMan.getDishesByType(dishList, "mc"));
					System.out.println("  Desserts: " + ordMan.getDishesByType(dishList, "ds"));
	
					System.out.println("\nDishes by Feature:\n  Veggie Dishes: " + ordMan.getDishesByFeature(dishList, "vgd"));
					System.out.println("  Gluten Free Dishes: " + ordMan.getDishesByFeature(dishList, "gfd"));
					System.out.println("  Halal Meat Dishes: " + ordMan.getDishesByFeature(dishList, "hmd"));
					System.out.println("  Sea Food Free Dishes: " + ordMan.getDishesByFeature(dishList, "sfd"));
					
					System.out.println("\nName of the "+dishList.size()+" different dishes available in the file:");
					for(Dish d:dishList){
						System.out.println("  - "+d.getDishName());
					}
					System.out.println("\nDish Type Stats:\n      " + ordMan.getStatsByDishType(dishList, "st"));
					System.out.println("  " + ordMan.getStatsByDishType(dishList, "mc"));
					System.out.println("      " + ordMan.getStatsByDishType(dishList, "ds"));
				}
				break;
			case "2":
				if(hasLoadedFile){
					System.out.println("The file [" + currentFile + "] contains " + ordMan.getNumberOrders(fileContent.get(currentFile).getOrderList()) + " records.");
				}
				break;
			case "3":
				if(hasLoadedFile){
					List<Order> listOrder = fileContent.get(currentFile).getOrderList();
					System.out.println("Enter a number from 1 to " + ordMan.getNumberOrders(listOrder) + " to retrieve a record.");
					String record = br.readLine();
					System.out.println("["+record+"]: " + ordMan.getOrder(listOrder, Integer.parseInt(record)-1));
				}
				break;
			case "4":
				if(hasLoadedFile){
					List<Order> listOrder = fileContent.get(currentFile).getOrderList();
					System.out.println("\n" + ordMan.getAllOrdersToString(listOrder));
				}
				break;
			case "5":
				if(hasLoadedFile){
					List<Order> listOrder = fileContent.get(currentFile).getOrderList();
					System.out.println("\n" + ordMan.getStatsForAllCustomers(listOrder));
					System.out.println("Stats from all orders:");
					System.out.println(ordMan.getTypeStatsFromOrders(listOrder));
					System.out.println(ordMan.getFeatureStatsFromOrders(listOrder));
				}
				break;
			case "x":
				System.out.println("Bye!");
				break;
			default:
				System.out.println("Invalid selection.");
				break;
			}
	        
		}
			

	}
	
}
