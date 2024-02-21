package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.stream.Collectors;

import beans.Cart;
import beans.Customer;
import beans.Manager;
import beans.Order;
import beans.Order.Status;
import beans.Vehicle;

public class OrderDao {
	private ArrayList<Order> ordersAfterAction = new ArrayList<>();
	private ArrayList<Order> orders = new ArrayList<>();
	private String contextPath = "C:\\Users\\Luka\\Desktop\\rent-a-car\\WebContent\\WEB-INF\\orders.txt";

	private CartDao cartDao;
	private OrdersAndVehiclesDao ordersAndVehiclesDao;
	private UserDao userDao;
	private RentACarObjectDao rentACarDao;

	public OrderDao() {
		loadOrders();
	}

	private void loadOrders() {

		BufferedReader in = null;
		try {
			File file = new File(contextPath);
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;

			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, "|");
				while (st.hasMoreTokens()) {
					String id = st.nextToken().trim();
					int rentACarObjectId = Integer.parseInt(st.nextToken().trim());
					String dateTimeString = st.nextToken().trim();
					LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
					int duration = Integer.parseInt(st.nextToken().trim());
					double price = Double.parseDouble(st.nextToken().trim());
					int customerId = Integer.parseInt(st.nextToken().trim());
					Status status = Status.valueOf(st.nextToken().trim());

					Order order = new Order();
					order.setId(id);
					order.setRentACarObjectId(rentACarObjectId);
					order.setBeginDate(dateTime);
					order.setRentDuration(duration);
					order.setEndDate();
					order.setPrice(price);
					order.setStatus(status);
					order.setCustomerId(customerId);
					orders.add(order);
					
					ordersAfterAction = orders;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}

	}

	private void saveOrders() {

		try {
			FileWriter file = new FileWriter(contextPath);

			BufferedWriter output = new BufferedWriter(file);

			for (Order p : orders) {
				output.write(p.returnSerializableFormat());
			}

			output.close();

		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
		}
	}

	public ArrayList<Order> getAll() {
		return orders;
	}

	public boolean makeOrder(int userId, Collection<Vehicle> vehicles) {
		try {
			List<Integer> distinctRentACarObjectIds = vehicles.stream().map(Vehicle::getRentACarObjectId).distinct().collect(Collectors.toList());
			cartDao = new CartDao();
			ordersAndVehiclesDao = new OrdersAndVehiclesDao();
			double customerPoints = new UserDao().getById(userId).getPoints();

			for (Integer objectId : distinctRentACarObjectIds) {
				Order newOrder = new Order();
				newOrder.setRentACarObjectId(objectId);
				newOrder.setCustomerId(userId);
				newOrder.setStatus(Status.Processing);
				Cart cart = cartDao.getById(userId);
				newOrder.setBeginDate(cart.getFromDate().atStartOfDay());
				newOrder.setRentDuration((int) ChronoUnit.DAYS.between(cart.getFromDate(), cart.getUntilDate()));			
				newOrder.setId(generateId());
				
				int priceForObject = 0;
				for (Vehicle vehicle : vehicles) {
					if (vehicle.getRentACarObjectId() == objectId) {
						priceForObject += vehicle.getPrice();
						ordersAndVehiclesDao.setOrderAndVehicle(newOrder.getId(), vehicle.getId());
					}
				}

				if(customerPoints < 3000) {
					newOrder.setPrice(priceForObject * 0.99);
				}
				else if(customerPoints < 6000) {
					newOrder.setPrice(priceForObject * 0.97);
				}
				else {
					newOrder.setPrice(priceForObject * 0.95);
				}
				
				orders.add(newOrder);
			}

			cartDao.deleteCart(userId);
			saveOrders();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	private String generateId() {
		UUID uuid = UUID.randomUUID();

		// Remove dashes and convert to uppercase
		String uuidString = uuid.toString().replace("-", "").toUpperCase();

		// Take the first 10 characters of the UUID
		String uniqueId = uuidString.substring(0, 10);
		return uniqueId;
	}

	public boolean isCustomerEligibleToAddComment(int customerId) {
		for (Order order : orders) {
			if(order.getCustomerId() == customerId && order.getStatus() == Status.Returned) return true;
		}
		return false;
	}
	
	public ArrayList<Order> getAllOrders(String username){
		userDao = new UserDao();
		if(userDao.getRole(username).toLowerCase().equals("customer")) {
			return getAllOrdersForCustomers(username);
		}
		
		
		return getAllOrdersForManager(username);
	}

	private ArrayList<Order> getAllOrdersForManager(String username) {
		userDao = new UserDao();
		rentACarDao = new RentACarObjectDao();
		
		ArrayList<Order> allOrders = new ArrayList<Order>();
		Manager manager = userDao.getManager(username);
		for(Order order : orders) {
			if(order.getRentACarObjectId() == manager.getRentACarObjectId()) {
				order.setUser(userDao.getCustomer(order.getCustomerId()));
				order.setRentACarObject(rentACarDao.getObjectById(order.getRentACarObjectId()));
				allOrders.add(order);				
			}
		}
		
		ordersAfterAction = allOrders;
		return allOrders;
	}
	
	private ArrayList<Order> getAllOrdersForCustomers(String username) {
		userDao = new UserDao();
		rentACarDao = new RentACarObjectDao();
		
		ArrayList<Order> allOrders = new ArrayList<Order>();
		Customer customer = userDao.getCustomer(username);
		for(Order order : orders) {
			if(order.getCustomerId() == customer.getId()) {
				order.setUser(userDao.getCustomer(order.getCustomerId()));
				order.setRentACarObject(rentACarDao.getObjectById(order.getRentACarObjectId()));				
				allOrders.add(order);
			}
		}

		ordersAfterAction = allOrders;
		return allOrders;
	}

	public boolean cancelOrder(String orderId) {
		try {
			for (Order order : orders) {
				if(order.getId().contains(orderId)) {
					order.setStatus(Status.Canceled);
				}
			}
			saveOrders();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean approveOrder(String orderId) {
		try {
			for (Order order : orders) {
				if(order.getId().contains(orderId)) {
					order.setStatus(Status.Approved);
				}
			}
			saveOrders();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean rejectOrder(String orderId) {
		try {
			for (Order order : orders) {
				if(order.getId().contains(orderId)) {
					order.setStatus(Status.Rejected);
				}
			}
			saveOrders();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean retrieveOrder(String orderId) {
		try {
			for (Order order : orders) {
				if(order.getId().contains(orderId)) {
					LocalDateTime currentDateTime = LocalDateTime.now();
					LocalDateTime beginDateTime = order.getBeginDate();
					LocalDateTime endDateTime = order.getBeginDate().plusDays(order.getRentDuration());
					if(currentDateTime.compareTo(beginDateTime) >= 0 && currentDateTime.compareTo(endDateTime) <= 0) {
						order.setStatus(Status.Retrieved);
						saveOrders();
						break;
					}
					else {
						System.out.println("It is not the appropriate date to change this order!");
						return false;
					}
					
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean returnOrder(String orderId) {
		try {
			for (Order order : orders) {
				if(order.getId().contains(orderId)) {
					order.setStatus(Status.Returned);
				}
			}
			saveOrders();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public ArrayList<Order> getAllBySearchForCustomer(String username, int priceFrom,
			int priceTo, LocalDateTime dateFrom, LocalDateTime dateTo, String shopName) {
		ArrayList<Order> ordersAfterSearch = new ArrayList<Order>();
		 for(Order order: getAllOrdersForCustomers(username)) {
		    	boolean comparePrice = comparePrice(order.getPrice(), priceFrom, priceTo);
		    	boolean compareDate = compareDate(order.getBeginDate(), order.getEndDate(), dateFrom, dateTo);
		    	boolean compareName = compareName(order.getRentACarObject().getName(), shopName);
		    	
		    	
		    	if(compareName && comparePrice && compareDate) 
		    		ordersAfterSearch.add(order);
		 }
		 
		 ordersAfterAction = ordersAfterSearch;
		 
		 return ordersAfterSearch;
	}

	public ArrayList<Order> getAllBySearchForManager(String username, int priceFrom,
			int priceTo, LocalDateTime dateFrom, LocalDateTime dateTo) {
		ArrayList<Order> ordersAfterSearch = new ArrayList<Order>();
		 for(Order order: getAllOrdersForCustomers(username)) {
		    	boolean comparePrice = comparePrice(order.getPrice(), priceFrom, priceTo);
		    	boolean compareDate = compareDate(order.getBeginDate(), order.getEndDate(), dateFrom, dateTo);
		    	
		    	if(comparePrice && compareDate) 
		    		ordersAfterSearch.add(order);
		 }
		 
		 ordersAfterAction = ordersAfterSearch;
		 
		 return ordersAfterSearch;
	}

	private boolean compareDate(LocalDateTime orderBegin, LocalDateTime orderEnd, LocalDateTime dateFrom, LocalDateTime dateTo) {
		if(orderBegin.isAfter(dateFrom) && orderEnd.isBefore(dateTo))
			return true;
		
		return false;
	}
	
	private boolean compareName(String order, String searchParam) {
		if(!searchParam.isEmpty())
			return order.toLowerCase().equals(searchParam.toLowerCase());
		
		return true;
	}
	
	private boolean comparePrice(double orderPrice, double priceFrom, double priceTo) {
		if(priceFrom == 0 && priceTo == 0)
			return true;
		else if(priceFrom == 0 && priceTo != 0) {
			return priceTo >= orderPrice;	
		}
		else if(priceFrom != 0 && priceTo == 0)
			return priceFrom <= orderPrice;
		else 
			return priceTo >= orderPrice && priceFrom <= orderPrice;
	}
	
	public ArrayList<Order> sortList(String sortColumn, String sortType){
		switch (sortColumn) {
		case "price":
			return sortByPrice(sortType);
		case "date":
			return sortByDate(sortType);
		default:
			return sortByShopName(sortType);
		}
	}
	
	private ArrayList<Order> sortByPrice(String sortType){
		Comparator<Order> comparator = new Comparator<Order>() {
			@Override
			public int compare(Order order1, Order order2) {
				return Double.compare(order2.getPrice(), order1.getPrice());
			}
		};

		if (sortType.equals("asc")) {
			comparator = comparator.reversed();
		}

		Collections.sort(ordersAfterAction, comparator);

		return ordersAfterAction;
	}
	
	private ArrayList<Order> sortByShopName(String sortType) {
		Comparator<Order> comparator = new Comparator<Order>() {
			@Override
			public int compare(Order order1, Order order2) {
				return order2.getRentACarObject().getName().compareTo(order1.getRentACarObject().getName());
			}
		};

		if (sortType.equals("asc")) {
			comparator = comparator.reversed();
		}

		Collections.sort(ordersAfterAction, comparator);

		return ordersAfterAction;
	}
	
	private ArrayList<Order> sortByDate(String sortType){
		Comparator<Order> comparator = new Comparator<Order>() {
			@Override
			public int compare(Order order1, Order order2) {
				return order2.getBeginDate().compareTo(order1.getBeginDate());
			}
		};


		if (sortType.equals("asc")) {

			comparator = comparator.reversed();
		}

		Collections.sort(ordersAfterAction, comparator);

		return ordersAfterAction;
	}


}
