package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

import beans.Comment;
import beans.Comment.Status;
import beans.Location;
import dto.FilterParamsDto;
import dto.SearchParamsDto;
import beans.RentACarObject;
import beans.Vehicle;

public class RentACarObjectDao {
	private LocationDao locationDao;

	private ArrayList<RentACarObject> objects = new ArrayList<>();
	private ArrayList<RentACarObject> searchObjects = new ArrayList<>();

	private String contextPath = "C:\\Users\\Luka\\Desktop\\rent-a-car\\WebContent\\WEB-INF\\rentACarObjects.txt";
	
	private CommentDao commentDao;

	public RentACarObjectDao() {
		locationDao = new LocationDao();
		loadObjects();
	}

	private void loadObjects() {

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
					String name = st.nextToken().trim();
					String workingHours = st.nextToken().trim();
					boolean isOpen = Boolean.valueOf(st.nextToken().trim());
					int locationId = Integer.parseInt(st.nextToken().trim());
					int rating = Integer.parseInt(st.nextToken().trim());
					String logo = st.nextToken().trim();
					RentACarObject object = new RentACarObject();
					object.setId(id);
					object.setName(name);
					object.setLocationId(locationId);
					object.setWorkingHours(workingHours);
					object.setLogo(logo);
					object.setOpen(isOpen);
					object.setRating(rating);
					objects.add(object);
				}
			}
			searchObjects = objects;
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

	public void saveObjects() {
		try {
			FileWriter file = new FileWriter(contextPath);
			BufferedWriter output = new BufferedWriter(file);

			for (RentACarObject object : objects) {
				output.write(object.getSerializableFormat());
			}

			output.close();

		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
		}

	}

	public int addObject(RentACarObject object) {
		object.setOpen(false);
		object.setRating(0);
		int maxId = 0;
		for (RentACarObject c : objects) {
			if (c.getId() > maxId)
				maxId = c.getId();
		}
		object.setId(maxId + 1);
		objects.add(object);
		saveObjects();
		return maxId + 1;
	}

	public ArrayList<String> getAllNames() {
		ArrayList<String> retVal = new ArrayList<>();
		for (RentACarObject object : objects) {
			retVal.add(object.getName());
		}
		return retVal;
	}

	public ArrayList<RentACarObject> getAll() {
		return objects;
	}

	public ArrayList<RentACarObject> getAllForHomePage() {
		ArrayList<RentACarObject> shops = new ArrayList<>();
		
		for(RentACarObject ro: objects) {
			if(isWithinTimeInterval(ro.getWorkingHours())) ro.setOpen(true);
			else ro.setOpen(false);		
			ro.setLocation(locationDao.getById(ro.getLocationId()));
		}
		searchObjects = objects;
		saveObjects();
		
		for (RentACarObject shop : objects) {
			if (shop.isOpen())
				shops.add(shop);
		}

		for (RentACarObject shop : objects) {
			if (!shop.isOpen())
				shops.add(shop);
		}

		searchObjects = shops;
		return shops;
	}
	
	private boolean isWithinTimeInterval(String timeInterval) {
        LocalTime currentTime = LocalTime.now();
        
        String[] parts = timeInterval.split("-");
        LocalTime startTime = LocalTime.parse(parts[0]);
        LocalTime endTime = LocalTime.parse(parts[1]);
        
        return currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(endTime) <= 0;
    }

	public ArrayList<RentACarObject> getAllByFilters(FilterParamsDto filterParams) {
			VehicleDao vehicleDao = new VehicleDao();
			ArrayList<RentACarObject> shopsAfterFilters = new ArrayList<RentACarObject>();
			ArrayList<Vehicle> vehicles = vehicleDao.getAll();

			
		for(RentACarObject object: searchObjects) {
			boolean checkGearshift = false;
			boolean checkFueltype = false;
			boolean checkAvgRating = false;
			boolean checkOpen = false;
			
			if(!filterParams.getGearshiftFilter().isEmpty()) {
				for(Vehicle vehicle : vehicles) {
					if(vehicle.getRentACarObjectId() == object.getId() && vehicle.getKind().toString().equals(filterParams.getGearshiftFilter())) {
						checkGearshift = true;
					}	
				}
			}
			else checkGearshift = true;
			
			if(!filterParams.getFuelFilter().isEmpty()) {
				for(Vehicle vehicle : vehicles) {
					if(vehicle.getRentACarObjectId() == object.getId() && vehicle.getFuelType().toString().equals(filterParams.getFuelFilter())) {
						checkFueltype = true;
					}	
				}
			}
			else checkFueltype = true;
			
			if(!filterParams.getAvgRating().isEmpty() && object.getRating() == Integer.parseInt(filterParams.getAvgRating())) {
				checkAvgRating = true;
			}
			else if(filterParams.getAvgRating().isBlank()) checkAvgRating = true;
			
			if(filterParams.isOnlyOpen() && object.isOpen()) {
				checkOpen = true;
			}
			else if(!filterParams.isOnlyOpen()) checkOpen = true;
			
			if(checkAvgRating && checkFueltype && checkGearshift && checkOpen) shopsAfterFilters.add(object);

		}
		return shopsAfterFilters;
	}

	public ArrayList<RentACarObject> getAllBySearch(SearchParamsDto searchParams) {
		VehicleDao vehicleDao = new VehicleDao();
		LocationDao locationDao = new LocationDao();
		ArrayList<RentACarObject> shopsAfterSearch = new ArrayList<>();
		ArrayList<Vehicle> vehicles = vehicleDao.getAll();

		if (searchParams.getLocation().isEmpty() && searchParams.getRating().isEmpty() && searchParams.getShop().isEmpty() && searchParams.getVehicleType().isEmpty())
			return objects;
		
		for (RentACarObject object : objects) {
			boolean checkLocation = false;
			boolean checkRating = false;
			boolean checkShop = false;
			boolean checkVehicleType = false;
			
			Location location = locationDao.getById(object.getLocationId());
			if(compare(searchParams.getLocation().toLowerCase(), location.getCity().toLowerCase())
					|| compare(searchParams.getLocation().toLowerCase(), location.getStreet().toLowerCase())) {
				checkLocation = true;
			}
				
			if(!searchParams.getRating().isEmpty() && object.getRating() == Integer.parseInt(searchParams.getRating())) {
				checkRating = true;
			}
			else if(searchParams.getRating().isEmpty()) checkRating = true;
				
			if(compare(searchParams.getShop().toLowerCase(), object.getName().toLowerCase())) {
				checkShop = true;
			}
			
			for (Vehicle vehicle : vehicles)
				if (vehicle.getRentACarObjectId() == object.getId() &&
						compare(searchParams.getVehicleType().toLowerCase(), vehicle.getType().toString().toLowerCase())) {
					checkVehicleType = true;
					break;
			}
			
			if(checkLocation && checkRating && checkShop && checkVehicleType) shopsAfterSearch.add(object);
			
		}

		searchObjects = shopsAfterSearch;
		return shopsAfterSearch;
	}
	
	private boolean compare(String searchParam, String userField) {
		if(!searchParam.isEmpty()) return userField.contains(searchParam);
		return true;
	}

	public ArrayList<RentACarObject> sortList(String sortColumn, String sortFilter) {
		switch (sortColumn) {
		case "Name":
			return sortByName(sortFilter);
		case "Location":
			return sortByLocation(sortFilter);
		default:
			return sortByAvgRating(sortFilter);
		}
	}

	private ArrayList<RentACarObject> sortByName(String sortFilter) {
		Comparator<RentACarObject> comparator = new Comparator<RentACarObject>() {
			@Override
			public int compare(RentACarObject shop1, RentACarObject shop2) {
				return shop1.getName().compareTo(shop2.getName());
			}
		};

		if (sortFilter.equals("Desc")) {
			comparator = comparator.reversed();
		}

		Collections.sort(searchObjects, comparator);

		return searchObjects;
	}

	private ArrayList<RentACarObject> sortByLocation(String sortFilter) {
		Comparator<RentACarObject> comparator = new Comparator<RentACarObject>() {
			@Override
			public int compare(RentACarObject shop1, RentACarObject shop2) {
				return locationDao.getById(shop1.getLocationId()).getCity()
						.compareTo(locationDao.getById(shop2.getLocationId()).getCity());
			}
		};

		if (sortFilter.equals("Desc")) {
			comparator = comparator.reversed();
		}

		Collections.sort(searchObjects, comparator);

		return searchObjects;
	}

	private ArrayList<RentACarObject> sortByAvgRating(String sortFilter) {
		Comparator<RentACarObject> comparator = new Comparator<RentACarObject>() {
			@Override
			public int compare(RentACarObject shop1, RentACarObject shop2) {
				return Double.compare(shop2.getRating(), shop1.getRating());
			}
		};

		if (sortFilter.equals("Desc")) {
			comparator = comparator.reversed();
		}

		Collections.sort(searchObjects, comparator);

		return searchObjects;
	}

	public RentACarObject getObjectById(int objectId) {
		RentACarObject retVal = null;
		for (RentACarObject object : objects) {
			if (object.getId() == objectId) {
				retVal = object;
				break;
			}
		}
		return retVal;
	}

	public boolean updateRatingForObject(int objectId) {
		try {			
			commentDao = new CommentDao();			
			int ratingSum = 0;
			int count = 0;
			
			for (Comment comment : commentDao.getAll()) {
				if(comment.getRentACarObjectId() == objectId && comment.getStatus() == Status.Approved) {
					ratingSum += comment.getRating();
					count++;
				}
			}
			
			for (RentACarObject rentACarObject : objects) {
				if(rentACarObject.getId() == objectId) {					
					rentACarObject.setRating(ratingSum/count);
					break;
				}
			}
			
			saveObjects();
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}

}
