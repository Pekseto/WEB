package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Location;
import dao.LocationDao;

@Path("/locations")
public class LocationService {

	@Context
	ServletContext ctx;

	public LocationService() {

	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("locationDao") == null) {
			ctx.setAttribute("locationDao", new LocationDao());
		}
	}

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public int createLocation(Location location) {
		System.out.println("Create location begin...");
		LocationDao dao = (LocationDao) ctx.getAttribute("locationDao");
		return dao.addObject(location);
	}

	@GET
	@Path("/getById/{locationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Location getById(@PathParam("locationId") int locationId) {
		System.out.println("Get locationId begin...");
		LocationDao dao = (LocationDao) ctx.getAttribute("locationDao");
		return dao.getById(locationId);
	}

	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Location> getAll() {
		System.out.println("Get all location method started...");
		LocationDao dao = (LocationDao) ctx.getAttribute("locationDao");
		return dao.getAll();
	}

}
