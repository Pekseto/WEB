package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import dto.CartAndVehicleDto;

public class CartsAndVehiclesDao {

	private ArrayList<CartAndVehicleDto> cartsAndVehicles = new ArrayList<>();
	private String contextPath = "C:\\Users\\Luka\\Desktop\\rent-a-car\\WebContent\\WEB-INF\\cartsAndVehicles.txt";

	public CartsAndVehiclesDao() {
		loadCartsAndVehicles();
	}

	private void loadCartsAndVehicles() {
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
					int cartId = Integer.parseInt(st.nextToken().trim());
					int vehicleId = Integer.parseInt(st.nextToken().trim());
					CartAndVehicleDto cartAndVehiclesDto = new CartAndVehicleDto();
					cartAndVehiclesDto.setCartId(cartId);
					cartAndVehiclesDto.setVehicleId(vehicleId);
					cartsAndVehicles.add(cartAndVehiclesDto);
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

	private void saveCartsAndVehicles() {
		try {
			FileWriter file = new FileWriter(contextPath);

			BufferedWriter output = new BufferedWriter(file);

			for (CartAndVehicleDto cartAndVehicleDto : cartsAndVehicles) {
				output.write(cartAndVehicleDto.returnSerializableFormat());
			}

			output.close();

		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
		}
	}

	public CartAndVehicleDto getById(int customerId, int vehicleId) {
		for (CartAndVehicleDto cartAndVehicleDto : cartsAndVehicles) {
			if (cartAndVehicleDto.getCartId() == customerId && cartAndVehicleDto.getVehicleId() == vehicleId) {
				return cartAndVehicleDto;
			}
		}
		return null;
	}

	public void addCartAndVehicle(int customerId, int vehicleId) {
		CartAndVehicleDto newCartAndVehicle = new CartAndVehicleDto();
		newCartAndVehicle.setCartId(customerId);
		newCartAndVehicle.setVehicleId(vehicleId);
		cartsAndVehicles.add(newCartAndVehicle);
		saveCartsAndVehicles();

	}

	public ArrayList<Integer> getAllForCustomer(int customerId) {
		ArrayList<Integer> vehicleIds = new ArrayList<>();
		for (CartAndVehicleDto cartAndVehicleDto : cartsAndVehicles) {
			if (cartAndVehicleDto.getCartId() == customerId) {
				vehicleIds.add(cartAndVehicleDto.getVehicleId());
			}
		}
		return vehicleIds;
	}

	public void deleteVehicle(int vehicleId, int customerId) {
		for (CartAndVehicleDto cartAndVehicleDto : cartsAndVehicles) {
			if (cartAndVehicleDto.getCartId() == customerId && cartAndVehicleDto.getVehicleId() == vehicleId) {
				cartsAndVehicles.remove(cartAndVehicleDto);
				break;
			}
		}
		saveCartsAndVehicles();
	}

	public void deleteCartAndVehicles(int userId) {
		Iterator<CartAndVehicleDto> iterator = cartsAndVehicles.iterator();
		while (iterator.hasNext()) {
			CartAndVehicleDto cartAndVehicleDto = iterator.next();
			if (cartAndVehicleDto.getCartId() == userId) {
				iterator.remove(); // Safely remove the item
			}
		}

		saveCartsAndVehicles();

	}

}
