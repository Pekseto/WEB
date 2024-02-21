package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringTokenizer;

import beans.Cart;
import beans.Vehicle;

public class CartDao {

	private ArrayList<Cart> carts = new ArrayList<>();
	private String contextPath = "C:\\Users\\Luka\\Desktop\\rent-a-car\\WebContent\\WEB-INF\\carts.txt";
	// NAPRAVITI KADA SE NAPRAVI PORUDZBINA DA SE KORPA BRISE!!!!!!!!!!!!!

	private VehicleDao vehicleDao;
	private CartsAndVehiclesDao cartsAndVehiclesDao;

	public CartDao() {
		loadCarts();
	}

	private void loadCarts() {
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
					int customerId = Integer.parseInt(st.nextToken().trim());
					int price = Integer.parseInt(st.nextToken().trim());
					LocalDate fromDate = LocalDate.parse(st.nextToken().trim());
					LocalDate untilDate = LocalDate.parse(st.nextToken().trim());
					int deleted = Integer.parseInt(st.nextToken().trim());
					Cart cart = new Cart();
					cart.setCustomerId(customerId);
					cart.setPrice(price);
					cart.setFromDate(fromDate);
					cart.setUntilDate(untilDate);
					cart.setDeleted(deleted);
					carts.add(cart);
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

	private void saveCarts() {
		try {
			FileWriter file = new FileWriter(contextPath);

			BufferedWriter output = new BufferedWriter(file);

			for (Cart cart : carts) {
				output.write(cart.returnSerializableFormat());
			}

			output.close();

		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
		}
	}

	public boolean addVehicleToCart(int vehicleId, int customerId, LocalDate fromDate, LocalDate untilDate) {
		try {
			vehicleDao = new VehicleDao();
			cartsAndVehiclesDao = new CartsAndVehiclesDao();
			boolean foundCart = false;

			for (Cart cart : carts) {
				if (cart.getCustomerId() == customerId && cart.getDeleted() == 0) {
					foundCart = true;
					cartsAndVehiclesDao.addCartAndVehicle(customerId, vehicleId);
					cart.setPrice(cart.getPrice() + vehicleDao.getById(vehicleId).getPrice());
					saveCarts();
				}
			}

			if (!foundCart) {
				Cart newCart = new Cart();
				newCart.setCustomerId(customerId);
				newCart.setPrice(vehicleDao.getById(vehicleId).getPrice());
				newCart.setFromDate(fromDate);
				newCart.setUntilDate(untilDate);
				newCart.setDeleted(0);
				cartsAndVehiclesDao.addCartAndVehicle(customerId, vehicleId);
				carts.add(newCart);
				saveCarts();
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public ArrayList<Vehicle> getAllForCustomer(int customerId) {
		vehicleDao = new VehicleDao();
		cartsAndVehiclesDao = new CartsAndVehiclesDao();

		ArrayList<Integer> vehicleIds = new ArrayList<>();
		vehicleIds = cartsAndVehiclesDao.getAllForCustomer(customerId);

		return vehicleDao.getAllByIds(vehicleIds);
	}

	public boolean deleteVehicle(int vehicleId, int customerId) {
		try {
			cartsAndVehiclesDao = new CartsAndVehiclesDao();
			cartsAndVehiclesDao.deleteVehicle(vehicleId, customerId);

			vehicleDao = new VehicleDao();
			int newPrice = 0;
			for (Cart cart : carts) {
				if (cart.getCustomerId() == customerId && cart.getDeleted() == 0) {
					newPrice = cart.getPrice() - vehicleDao.getById(vehicleId).getPrice();
					if (newPrice != 0) {
						cart.setPrice(newPrice);
					} else {
						cart.setDeleted(1);
					}
				}
			}
			saveCarts();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Cart getById(int userId) {
		for (Cart cart : carts) {
			if (cart.getCustomerId() == userId && cart.getDeleted() == 0) {
				return cart;
			}
		}
		return null;
	}

	public void deleteCart(int userId) {
		for (Cart cart : carts) {
			if (cart.getCustomerId() == userId && cart.getDeleted() == 0) {
				cart.setDeleted(1);
				break;
			}
		}
		cartsAndVehiclesDao = new CartsAndVehiclesDao();
		cartsAndVehiclesDao.deleteCartAndVehicles(userId);
		saveCarts();
	}

}
