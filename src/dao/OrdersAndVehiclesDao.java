package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import dto.OrderAndVehicleDto;

public class OrdersAndVehiclesDao {

	private ArrayList<OrderAndVehicleDto> ordersAndVehicles = new ArrayList<>();
	private String contextPath = "C:\\Users\\Luka\\Desktop\\rent-a-car\\WebContent\\WEB-INF\\ordersAndVehicles.txt";

	public OrdersAndVehiclesDao() {
		loadOrdersAndVehicles();
	}

	private void loadOrdersAndVehicles() {

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
					String orderId = st.nextToken().trim();
					int vehicleId = Integer.parseInt(st.nextToken().trim());
					OrderAndVehicleDto orderAndVehiclesDto = new OrderAndVehicleDto();
					orderAndVehiclesDto.setOrderId(orderId);
					orderAndVehiclesDto.setVehicleId(vehicleId);
					ordersAndVehicles.add(orderAndVehiclesDto);
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

	private void saveOrdersAndVehicles() {
		try {
			FileWriter file = new FileWriter(contextPath);

			BufferedWriter output = new BufferedWriter(file);

			for (OrderAndVehicleDto orderAndVehicleDto : ordersAndVehicles) {
				output.write(orderAndVehicleDto.returnSerializableFormat());
			}

			output.close();

		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
		}
	}

	public ArrayList<OrderAndVehicleDto> getAll() {
		return ordersAndVehicles;
	}

	public boolean containsVehicleId(int vehicleId) {
		for (OrderAndVehicleDto orderAndVehicleDto : ordersAndVehicles) {
			if (orderAndVehicleDto.getVehicleId() == vehicleId) {
				return true;
			}
		}
		return false;
	}

	public void setOrderAndVehicle(String orderId, int vehicleId) {
		OrderAndVehicleDto newOrderAndVehicleDto = new OrderAndVehicleDto();
		newOrderAndVehicleDto.setOrderId(orderId);
		newOrderAndVehicleDto.setVehicleId(vehicleId);
		ordersAndVehicles.add(newOrderAndVehicleDto);
		saveOrdersAndVehicles();
	}

}
