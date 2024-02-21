package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

import beans.Administrator;
import beans.Customer;
import beans.Gender;
import beans.Manager;
import beans.Order;
import beans.RentACarObject;
import beans.User;
import beans.User.Role;
import dto.UserDto;

public class UserDao {
	private ArrayList<Customer> customers = new ArrayList<>();
	private ArrayList<Administrator> administrators = new ArrayList<>();
	private ArrayList<Manager> managers = new ArrayList<>();
	// private String contextPath =
	// "C:\\Users\\luka\\Desktop\\rent-a-car\\WebContent\\WEB-INF\\";
	private String contextPath = "C:\\Users\\Luka\\Desktop\\rent-a-car\\WebContent\\WEB-INF\\";

	public UserDao() {
		loadCustomers();
		loadManagers();
		loadAdministrators();
	}

	public UserDao(String contextPath) {
		loadCustomers();
		loadManagers();
		loadAdministrators();
	}

	private void loadManagers() {

		BufferedReader in = null;
		try {
			File file = new File(contextPath + "managers.txt");
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
					String username = st.nextToken().trim();
					String password = st.nextToken().trim();
					String firstName = st.nextToken().trim();
					String lastName = st.nextToken().trim();
					Gender gender = Gender.valueOf(st.nextToken().trim());
					String dateOfBirth = st.nextToken().trim();
					Role role = Role.valueOf(st.nextToken().trim());
					int rentACarObjectId = Integer.parseInt(st.nextToken().trim());
					boolean isBlocked = Boolean.parseBoolean(st.nextToken().trim());
					Manager manager = new Manager();
					manager.setId(id);
					manager.setUsername(username);
					manager.setPassword(password);
					manager.setFirstName(firstName);
					manager.setLastName(lastName);
					manager.setGender(gender);
					manager.setDateOfBirth(dateOfBirth);
					manager.setRole(role);
					manager.setRentACarObjectId(rentACarObjectId);
					manager.setBlocked(isBlocked);
					managers.add(manager);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	private void loadAdministrators() {

		BufferedReader in = null;
		try {
			File file = new File(contextPath + "administrators.txt");
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
					String username = st.nextToken().trim();
					String password = st.nextToken().trim();
					String firstName = st.nextToken().trim();
					String lastName = st.nextToken().trim();
					Gender gender = Gender.valueOf(st.nextToken().trim());
					String dateOfBirth = st.nextToken().trim();
					Role role = Role.valueOf(st.nextToken().trim());
					Administrator administrator = new Administrator();
					administrator.setId(id);
					administrator.setUsername(username);
					administrator.setPassword(password);
					administrator.setFirstName(firstName);
					administrator.setLastName(lastName);
					administrator.setGender(gender);
					administrator.setDateOfBirth(dateOfBirth);
					administrator.setRole(role);
					administrator.setBlocked(false);
					administrators.add(administrator);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public boolean addCustomer(UserDto userDto) {
		Customer newCustomer = new Customer();
		newCustomer.setUsername(userDto.getUsername());
		newCustomer.setFirstName(userDto.getFirstName());
		newCustomer.setLastName(userDto.getLastName());
		newCustomer.setPassword(userDto.getPassword());
		newCustomer.setDateOfBirth(userDto.getDateOfBirth());
		newCustomer.setGender(userDto.getGender());
		newCustomer.setRole(Role.Customer);
		newCustomer.setBlocked(false);

		int maxId = 0;
		for (Customer c : customers) {
			if (c.getId() > maxId)
				maxId = c.getId();
		}
		newCustomer.setId(maxId + 1);

		customers.add(newCustomer);
		saveCustomers();

		return true;
	}

	public boolean addManager(UserDto manager) {
		Manager newManager = new Manager();
		newManager.setUsername(manager.getUsername());
		newManager.setFirstName(manager.getFirstName());
		newManager.setLastName(manager.getLastName());
		newManager.setPassword(manager.getPassword());
		newManager.setDateOfBirth(manager.getDateOfBirth());
		newManager.setGender(manager.getGender());
		newManager.setRole(Role.Manager);
		newManager.setBlocked(false);

		int maxId = 0;
		for (Customer c : customers) {
			if (c.getId() > maxId)
				maxId = c.getId();
		}
		newManager.setId(maxId + 1);

		managers.add(newManager);
		saveManagers();
		return true;
	}

	private void loadCustomers() {

		BufferedReader in = null;
		try {
			File file = new File(contextPath + "customers.txt");
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
					String username = st.nextToken().trim();
					String password = st.nextToken().trim();
					String firstName = st.nextToken().trim();
					String lastName = st.nextToken().trim();
					Gender gender = Gender.valueOf(st.nextToken().trim());
					String dateOfBirth = st.nextToken().trim();
					Role role = Role.valueOf(st.nextToken().trim());
					Double points = Double.parseDouble(st.nextToken().trim());
					boolean isBlocked = Boolean.parseBoolean(st.nextToken().trim());
					boolean isSuspicious = Boolean.parseBoolean(st.nextToken().trim());
					Customer user = new Customer();
					user.setId(id);
					user.setUsername(username);
					user.setPassword(password);
					user.setFirstName(firstName);
					user.setLastName(lastName);
					user.setGender(gender);
					user.setDateOfBirth(dateOfBirth);
					user.setRole(role);
					user.setPoints(points);
					user.setBlocked(isBlocked);
					user.setSuspicious(isSuspicious);
					customers.add(user);
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

	private void saveCustomers() {
		try {
			FileWriter file = new FileWriter(contextPath + "customers.txt");
			BufferedWriter output = new BufferedWriter(file);

			for (Customer customer : customers) {
				output.write(customer.returnSerializableFormat());
			}

			output.close();

		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
		}

    }
	
	private void saveAdministrators() {
		try {
			FileWriter file = new FileWriter(contextPath + "administrators.txt");
			BufferedWriter output = new BufferedWriter(file);

			for (Administrator administrator : administrators) {
				output.write(administrator.returnSerializableFormat());
			}

			output.close();

		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
		}

    }
	
	public User getByUsername(String username) {
        for(Customer user : customers) {
            if(user.getUsername().equals(username))
                return user;
        }

        for(Administrator user : administrators) {
            if(user.getUsername().equals(username))
                return user;
        }

        for(Manager user : managers) {
            if(user.getUsername().equals(username))
                return user;
        }
        
        return null;
	}

	public String getRole(String username) {
        for(Customer user : customers) {
            if(user.getUsername().equals(username))
                return "customer";
        }

		for (Administrator user : administrators) {
			if (user.getUsername().equals(username))
				return "administrator";
		}

		for (Manager user : managers) {
			if (user.getUsername().equals(username))
				return "manager";
		}

		return ""; // User doesnt exist
	}

	public boolean checkBlockStatus(String username) {
		for (Customer user : customers)
			if (user.getUsername().equals(username) && user.isBlocked())
				return true;

		return false;
	}

	public boolean loginUser(String username, String password) {
		String role = getRole(username);

		if (role.equals("customer")) {
			for (Customer user : customers)
				if (user.getUsername().equals(username) && user.getPassword().equals(password))
					return true;
		}

		if (role.equals("manager")) {
			for (Manager user : managers)
				if (user.getUsername().equals(username) && user.getPassword().equals(password))
					return true;
		}

		if (role.equals("administrator")) {
			for (Administrator user : administrators)
				if (user.getUsername().equals(username) && user.getPassword().equals(password))
					return true;
		}

		return false;
	}

	private void saveManagers() {
		try {
			FileWriter file = new FileWriter(contextPath + "managers.txt");
			BufferedWriter output = new BufferedWriter(file);

			for (Manager manager : managers) {
				output.write(manager.returnSerializableFormat());
			}

			output.close();

		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
		}

	}

	public Collection<Manager> getAvailableManagers() {
		ArrayList<Manager> retVal = new ArrayList<>();
		for (Manager manager : managers) {
			if (manager.getRentACarObjectId() == 0 && !manager.isBlocked())
				retVal.add(manager);
		}
		return retVal;
	}

	public boolean connectManagerAndObject(int managerId, int rentACarObjectId) {
		try {
			for (Manager manager : managers) {
				if (manager.getId() == managerId)
					manager.setRentACarObjectId(rentACarObjectId);
			}
			saveManagers();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean generatePointsForUser(int customerId, double totalPrice) {
		try {
			System.out.println(totalPrice);
			for (Customer customer : customers) {
				if (customer.getId() == customerId) {
					customer.setPoints(customer.getPoints() + totalPrice / 1000 * 133);
				}
			}
			saveCustomers();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateUserData(String username, UserDto editedUser) {
		String role = getRole(username);
		if(role == "customer")
			return editCustomer(editedUser);
		else if(role == "manager")
			return editManager(editedUser);
		else
			return editAdministrator(editedUser);
	}
	
	private boolean editCustomer(UserDto editedUser) {
		Customer customer = new Customer();
		
		for (Customer user : customers) {
			if(user.getId() == editedUser.getId()) {
				customer.setId(editedUser.getId());
				customer.setPoints(user.getPoints());
				customer.setBlocked(user.isBlocked());
				customer.setSuspicious(user.isSuspicious());
				customer.setRole(user.getRole());
				if(editedUser.getUsername() != null)
					customer.setUsername(editedUser.getUsername());
				else
					customer.setUsername(user.getUsername());
				if(editedUser.getFirstName() != null)
					customer.setFirstName(editedUser.getFirstName());
				else
					customer.setFirstName(user.getFirstName());
				if(editedUser.getLastName() != null)
					customer.setLastName(editedUser.getLastName());
				else
					customer.setLastName(user.getLastName());
				if(editedUser.getPassword() != null)
					customer.setPassword(editedUser.getPassword());
				else
					customer.setPassword(editedUser.getPassword());
				if(editedUser.getGender() != null)
					customer.setGender(editedUser.getGender());
				else
					customer.setGender(user.getGender());
				if(editedUser.getDateOfBirth() != null)
					customer.setDateOfBirth(editedUser.getDateOfBirth());
				else
					customer.setDateOfBirth(user.getDateOfBirth());
			}
		}
		
    	customers.remove(customer.getId() - 1);
    	customers.add(customer.getId() - 1, customer);
    	saveCustomers();
    	return true;
	}

	private boolean editManager(UserDto editedUser) {
		Manager manager = new Manager();
		
		for (Manager user : managers) {
			if(user.getId() == editedUser.getId()) {
				manager.setId(editedUser.getId());
				manager.setBlocked(user.isBlocked());
				manager.setRole(user.getRole());
				manager.setRentACarObjectId(user.getRentACarObjectId());
				if(editedUser.getUsername() != null)
					manager.setUsername(editedUser.getUsername());
				else
					manager.setUsername(user.getUsername());
				if(editedUser.getFirstName() != null)
					manager.setFirstName(editedUser.getFirstName());
				else
					manager.setFirstName(user.getFirstName());
				if(editedUser.getLastName() != null)
					manager.setLastName(editedUser.getLastName());
				else
					manager.setLastName(user.getLastName());
				if(editedUser.getPassword() != null)
					manager.setPassword(editedUser.getPassword());
				else
					manager.setPassword(editedUser.getPassword());
				if(editedUser.getGender() != null)
					manager.setGender(editedUser.getGender());
				else
					manager.setGender(user.getGender());
				if(editedUser.getDateOfBirth() != null)
					manager.setDateOfBirth(editedUser.getDateOfBirth());
				else
					manager.setDateOfBirth(user.getDateOfBirth());
			}
		}
		
		managers.remove(manager.getId() - 1);
		managers.add(manager.getId() - 1, manager);
    	saveManagers();
    	return true;
	}
	
	private boolean editAdministrator(UserDto editedUser) {
		Administrator administrator = new Administrator();
		
		for (Administrator user : administrators) {
			if(user.getId() == editedUser.getId()) {
				administrator.setId(editedUser.getId());
				administrator.setRole(user.getRole());
				administrator.setBlocked(user.isBlocked());
				if(editedUser.getUsername() != null)
					administrator.setUsername(editedUser.getUsername());
				else
					administrator.setUsername(user.getUsername());
				if(editedUser.getFirstName() != null)
					administrator.setFirstName(editedUser.getFirstName());
				else
					administrator.setFirstName(user.getFirstName());
				if(editedUser.getLastName() != null)
					administrator.setLastName(editedUser.getLastName());
				else
					administrator.setLastName(user.getLastName());
				if(editedUser.getPassword() != null)
					administrator.setPassword(editedUser.getPassword());
				else
					administrator.setPassword(editedUser.getPassword());
				if(editedUser.getGender() != null)
					administrator.setGender(editedUser.getGender());
				else
					administrator.setGender(user.getGender());
				if(editedUser.getDateOfBirth() != null)
					administrator.setDateOfBirth(editedUser.getDateOfBirth());
				else
					administrator.setDateOfBirth(user.getDateOfBirth());
			}
		}
		
		administrators.remove(administrator.getId() - 1);
		administrators.add(administrator.getId() - 1, administrator);
		saveAdministrators();
    	return true;
	}

	public ArrayList<User> getAll() {
		ArrayList<User> retVal = new ArrayList<User>();
		
		for (User user : customers) {
			retVal.add(user);
		}
		for (User user : managers) {
			retVal.add(user);
		}
		for (User user : administrators) {
			retVal.add(user);
		}
		
		return retVal;
	}

	public Collection<User> getAllBySearch(String firstName, String lastName, String username) {
		ArrayList<User> retVal = new ArrayList<User>();
		
		for (User user : customers) {
			boolean checkFirstName = compare(firstName.toLowerCase(), user.getFirstName().toLowerCase());
			boolean checkLastName = compare(lastName.toLowerCase(), user.getLastName().toLowerCase());
			boolean checkUsername = compare(username.toLowerCase(), user.getUsername().toLowerCase());			
			if(checkFirstName && checkLastName && checkUsername) retVal.add(user);
			
		}
		for (User user : managers) {
			boolean checkFirstName = compare(firstName.toLowerCase(), user.getFirstName().toLowerCase());
			boolean checkLastName = compare(lastName.toLowerCase(), user.getLastName().toLowerCase());
			boolean checkUsername = compare(username.toLowerCase(), user.getUsername().toLowerCase());			
			if(checkFirstName && checkLastName && checkUsername) retVal.add(user);
		}
		for (User user : administrators) {
			boolean checkFirstName = compare(firstName.toLowerCase(), user.getFirstName().toLowerCase());
			boolean checkLastName = compare(lastName.toLowerCase(), user.getLastName().toLowerCase());
			boolean checkUsername = compare(username.toLowerCase(), user.getUsername().toLowerCase());			
			if(checkFirstName && checkLastName && checkUsername) retVal.add(user);
		}
		
		return retVal;
		
	}

	private boolean compare(String searchParam, String userField) {
		if(!searchParam.isEmpty()) return userField.contains(searchParam);
		return true;
	}

	public Collection<User> filterByRole(String role) {
		ArrayList<User> retVal = new ArrayList<User>();
		
		if(role.equals("Customer")) {
			for(User user: customers) {
				retVal.add(user);
			}
		}
		else if(role.equals("Manager")) {
			for(User user: managers) {
				retVal.add(user);
			}
		}
		else{
			for(User user: administrators) {
				retVal.add(user);
			}
		}
		
		return retVal;
	}
	
	public Collection<User> filterByType(String type){
		ArrayList<User> retVal = new ArrayList<User>();
		retVal.clear();
		
		if(type.equals("Bronze")) {
			for(Customer c: customers) {
				if(c.getPoints() < 3000) {
					retVal.add(c);
				}
			}
		}
		else if(type.equals("Silver")) {
			for(Customer c: customers) {
				if(c.getPoints() >= 3000 && c.getPoints() <= 6000) {
					retVal.add(c);
				}
			}
		}
		else {
			for(Customer c: customers) {
				if(c.getPoints() > 6000) {
					retVal.add(c);
				}
			}
		}
		
		return retVal;
	}
	
	public Collection<User> sort(String sortBy, String sortType) {
		
		if(sortBy.equals("points")) {			
			ArrayList<Customer> customersSort = new ArrayList<Customer>(customers);
			
			if(sortType.equals("Desc")) {
				Collections.sort(customersSort, new Comparator<Customer>() {
					@Override
					public int compare(Customer user1, Customer user2) {
						return Double.compare(user2.getPoints(), user1.getPoints());
					}
				});
			}
			else {
				Collections.sort(customersSort, new Comparator<Customer>() {
		            @Override
		            public int compare(Customer user1, Customer user2) {
		            	return Double.compare(user1.getPoints(), user2.getPoints());
		            }
				});
			}
			
			ArrayList<User> retVal = new ArrayList<User>();
			for(User user : customersSort) {
				retVal.add(user);
			}
		
			return retVal;
		}
		
		ArrayList<User> retVal = new ArrayList<User>(getAll());
		if(sortType.equals("Desc")) {
			Collections.sort(retVal, new Comparator<User>() {
				@Override
				public int compare(User user1, User user2) {
					if(sortBy.equals("firstName")) return user2.getFirstName().compareTo(user1.getFirstName());
					if(sortBy.equals("lastName")) return user2.getLastName().compareTo(user1.getLastName());
					return user2.getUsername().compareTo(user1.getUsername());
				}
			});
		}
		else {
			Collections.sort(retVal, new Comparator<User>() {
	            @Override
	            public int compare(User user1, User user2) {
	            	if(sortBy.equals("firstName")) return user1.getFirstName().compareTo(user2.getFirstName());
					if(sortBy.equals("lastName")) return user1.getLastName().compareTo(user2.getLastName());
					return user1.getUsername().compareTo(user2.getUsername());
	            }
			});
		}
	
		return retVal;
}

	public boolean blockUser(User user) {
		try {
			if(user.getRole() == Role.Customer) {
				for (Customer customer : customers) {
					if(customer.getUsername().equals(user.getUsername())) {
						customer.setBlocked(!customer.isBlocked());
						break;
					}
				}
				saveCustomers();
			}
			else {
				for (Manager manager : managers) {
					if(manager.getUsername().equals(user.getUsername())) {
						manager.setBlocked(!manager.isBlocked());
						break;
					}
				}
				saveManagers();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Customer getById(int customerId) {
		for(Customer customer: customers) {
			if(customer.getId() == customerId) {
				return customer;
			}
		}
		return null;
	}

	public User getCustomer(int id) {
		User retVal = null;
		for(Customer customer : customers)
			if(customer.getId() == id)
				retVal = customer;
		
		return retVal;
	}
	
	public Customer getCustomer(String username) {
		for(Customer customer : customers)
			if(customer.getUsername().equals(username))
				return customer;
		
		return null;
	}

	public Manager getManager(String username) {
		for(Manager manager : managers) {
			if(manager.getUsername().equals(username))
				return manager;
		}
		
		return null;
	}

	public boolean losePointsAndMarkSuspicious(int customerId, double price) {
		try {
			OrderCancellationDao orderCancellationDao = new OrderCancellationDao();
			for (Customer customer : customers) {
				if(customer.getId() == customerId) {
					customer.setPoints(customer.getPoints() - price/1000*133*4);
					if(orderCancellationDao.isSuspicious(customerId)) {
						customer.setSuspicious(true);
					}
					break;
				}
			}
			saveCustomers();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Collection<User> getSuspiciousCustomers() {
		ArrayList<User> retVal = new ArrayList<User>();
		for (Customer customer : customers) {
			if(customer.isSuspicious()) retVal.add(customer);
		}
		return retVal;
	}


}
