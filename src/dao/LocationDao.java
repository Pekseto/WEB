package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import beans.Location;

public class LocationDao {

	private ArrayList<Location> locations = new ArrayList<>();
	private String contextPath = "C:\\Users\\Luka\\Desktop\\rent-a-car\\WebContent\\WEB-INF\\locations.txt";

	public LocationDao() {
		loadLocations();
	}

	private void loadLocations() {

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
					double longitude = Double.parseDouble(st.nextToken().trim());
					double latitude = Double.parseDouble(st.nextToken().trim());
					String street = st.nextToken().trim();
					String city = st.nextToken().trim();
					int postalCode = Integer.parseInt(st.nextToken().trim());
					Location location = new Location(longitude, latitude, street, city, postalCode);
					location.setId(id);
					locations.add(location);
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

	private void saveLocations() {
		try {
			FileWriter file = new FileWriter(contextPath);

			BufferedWriter output = new BufferedWriter(file);

			for (Location location : locations) {
				output.write(location.getSerializableFormat());
			}

			output.close();

		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
		}
	}

	public int addObject(Location location) {
		int maxId = 0;
		for (Location l : locations) {
			if (l.getId() > maxId)
				maxId = l.getId();
		}
		location.setId(maxId + 1);
		locations.add(location);
		saveLocations();

		return maxId + 1;
	}

	public ArrayList<Location> getAll() {
		return locations;
	}

	public Location getOne(int id) {
		for (Location location : locations)
			if (location.getId() == id)
				return location;

		return null;
	}

	public Location getById(int locationId) {
		Location retVal = null;
		for (Location location : locations) {
			if (location.getId() == locationId) {
				retVal = location;
			}
		}
		return retVal;
	}

}
