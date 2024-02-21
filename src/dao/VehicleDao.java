package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import beans.Order;
import beans.Order.Status;
import beans.Vehicle;
import beans.Vehicle.FuelType;
import beans.Vehicle.Kind;
import beans.Vehicle.Type;
import dto.OrderAndVehicleDto;

public class VehicleDao {
	private ArrayList<Vehicle> vehicles = new ArrayList<>();
	private String contextPath = "C:\\Users\\Luka\\Desktop\\rent-a-car\\WebContent\\WEB-INF\\vehicles.txt";

	private OrderDao orderDao;
	private OrdersAndVehiclesDao ordersAndVehiclesDao;

	public VehicleDao() {
		loadVehicles();
	}

	private void loadVehicles() {

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
					int id = Integer.parseInt(st.nextToken().trim());
					String brand = st.nextToken().trim();
					String model = st.nextToken().trim();
					int price = Integer.parseInt(st.nextToken().trim());
					Type type = Type.valueOf(st.nextToken().trim());
					Kind kind = Kind.valueOf(st.nextToken().trim());
					FuelType fuelType = FuelType.valueOf(st.nextToken().trim());
					double consumption = Double.parseDouble(st.nextToken().trim());
					int numberOfDoors = Integer.parseInt(st.nextToken().trim());
					int personCapacity = Integer.parseInt(st.nextToken().trim());
					String description = st.nextToken().trim();
					String image = st.nextToken().trim();
					int rentACarObjectId = Integer.parseInt(st.nextToken().trim());
					boolean status = Boolean.valueOf(st.nextToken().trim());
					int isDeleted = Integer.parseInt(st.nextToken().trim());
					Vehicle vehicle = new Vehicle();
					vehicle.setId(id);
					vehicle.setBrand(brand);
					vehicle.setModel(model);
					vehicle.setPrice(price);
					vehicle.setType(type);
					vehicle.setKind(kind);
					vehicle.setFuelType(fuelType);
					vehicle.setConsumption(consumption);
					vehicle.setNumberOfDoors(numberOfDoors);
					vehicle.setPersonCapacity(personCapacity);
					vehicle.setImage(image);
					vehicle.setDescription(description);
					vehicle.setRentACarObjectId(rentACarObjectId);
					vehicle.setStatus(status);
					vehicle.setIsDeleted(isDeleted);
					vehicles.add(vehicle);
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

	private void saveVehicles() {
		try {
			FileWriter file = new FileWriter(contextPath);

			BufferedWriter output = new BufferedWriter(file);

			for (Vehicle v : vehicles) {
				output.write(v.returnSerializableFormat());
			}

			output.close();

		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
		}
	}

	public Vehicle addVehicle(Vehicle vehicle) {
		int maxId = 0;
		for (Vehicle v : vehicles) {
			if (v.getId() > maxId)
				maxId = v.getId();
		}
		vehicle.setId(maxId + 1);
		vehicle.setIsDeleted(0);
		vehicles.add(vehicle);
		saveVehicles();
		return vehicle;
	}

	public ArrayList<Vehicle> getAll(){
		return vehicles;
	}
	
	public ArrayList<Vehicle> getAllForObject(int objectId) {
		ArrayList<Vehicle> retVal = new ArrayList<>();
		for (Vehicle vehicle : vehicles) {
			if (vehicle.getRentACarObjectId() == objectId) {
				retVal.add(vehicle);
			}
		}
		return retVal;
	}

	public boolean changeVehicle(Vehicle vehicleForChange) {
		try {
			for (Vehicle vehicle : vehicles) {
				if (vehicle.getId() == vehicleForChange.getId()) {

					vehicle.setBrand(vehicleForChange.getBrand());
					vehicle.setConsumption(vehicleForChange.getConsumption());
					vehicle.setDescription(vehicleForChange.getDescription());
					vehicle.setFuelType(vehicleForChange.getFuelType());
					vehicle.setImage(vehicleForChange.getImage());
					vehicle.setIsDeleted(vehicleForChange.getIsDeleted());
					vehicle.setKind(vehicleForChange.getKind());
					vehicle.setModel(vehicleForChange.getModel());
					vehicle.setNumberOfDoors(vehicleForChange.getNumberOfDoors());
					vehicle.setPersonCapacity(vehicleForChange.getPersonCapacity());
					vehicle.setPrice(vehicleForChange.getPrice());
					vehicle.setStatus(vehicleForChange.getStatus());
					vehicle.setType(vehicleForChange.getType());
				}
			}

			saveVehicles();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public Collection<Vehicle> getByDateRange(LocalDateTime fromDate, LocalDateTime untilDate, int objectId) {
		orderDao = new OrderDao();
		ordersAndVehiclesDao = new OrdersAndVehiclesDao();

		ArrayList<Vehicle> retVal = new ArrayList<>();
		ArrayList<OrderAndVehicleDto> ordersAndVehicles = ordersAndVehiclesDao.getAll();
		ArrayList<Order> orders = orderDao.getAll();

		for (Vehicle vehicle : vehicles) {
			if (ordersAndVehiclesDao.containsVehicleId(vehicle.getId()) && vehicle.getStatus() && vehicle.getIsDeleted() == 0)
				for (OrderAndVehicleDto orderAndVehicleDto : ordersAndVehicles) {
					if (orderAndVehicleDto.getVehicleId() == vehicle.getId()) {
						for (Order order : orders) {
							if (order.getId().equals(orderAndVehicleDto.getOrderId())) {
								if (fromDate.isBefore(order.getBeginDate())
										&& untilDate.isBefore(order.getBeginDate())) {
									retVal.add(vehicle);
								}
								if (fromDate.isAfter(order.getEndDate()) && untilDate.isAfter(order.getEndDate())) {
									retVal.add(vehicle);
								}
								if (order.getStatus() == Status.Rejected || order.getStatus() == Status.Canceled) {
									retVal.add(vehicle);
								}
							}
						}
					}
				}
			else {
				retVal.add(vehicle);
			}
		}

		return retVal;
	}

	public Vehicle getById(int vehicleId) {
		for (Vehicle vehicle : vehicles) {
			if (vehicle.getId() == vehicleId) {
				return vehicle;
			}
		}
		return null;
	}

	public ArrayList<Vehicle> getAllByIds(ArrayList<Integer> vehicleIds) {
		ArrayList<Vehicle> retval = new ArrayList<>();

		for (Integer vehicleId : vehicleIds) {
			for (Vehicle vehicle : vehicles) {
				if (vehicle.getId() == vehicleId) {
					retval.add(vehicle);
					break;
				}
			}
		}

		return retval;
	}
	
	public ArrayList<Vehicle> getByOrder(String id){
		ArrayList<Vehicle> orderVehicles = new ArrayList<Vehicle>();
		
		ordersAndVehiclesDao = new OrdersAndVehiclesDao();
		
		for(OrderAndVehicleDto orderAndVehicle : ordersAndVehiclesDao.getAll())
			if(orderAndVehicle.getOrderId().equals(id)) {
				for(Vehicle vehicle : vehicles)
					if(vehicle.getId() == orderAndVehicle.getVehicleId())
						orderVehicles.add(vehicle);
			}
		
		return orderVehicles;
			
	}

	public void changeVehicleStatusToBusy(String orderId) {
		ordersAndVehiclesDao = new OrdersAndVehiclesDao();
		for(OrderAndVehicleDto orderAndVehicleDto : ordersAndVehiclesDao.getAll()) {
			if(orderAndVehicleDto.getOrderId().contains(orderId)) {
				for(Vehicle vehicle : vehicles) {
					if(vehicle.getId() == orderAndVehicleDto.getVehicleId()) {
						vehicle.setStatus(false);
					}
				}
			}
		}
		saveVehicles();
	}

	public void changeVehicleStatusToFree(String orderId) {
		ordersAndVehiclesDao = new OrdersAndVehiclesDao();
		for(OrderAndVehicleDto orderAndVehicleDto : ordersAndVehiclesDao.getAll()) {
			if(orderAndVehicleDto.getOrderId().contains(orderId)) {
				for(Vehicle vehicle : vehicles) {
					if(vehicle.getId() == orderAndVehicleDto.getVehicleId()) {
						vehicle.setStatus(true);
					}
				}
			}
		}
		saveVehicles();	
	}

}
