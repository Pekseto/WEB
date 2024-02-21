package services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Vehicle;
import dao.VehicleDao;

@Path("/vehicles")
public class VehicleService {
	@Context
	ServletContext ctx;

	public VehicleService() {

	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("vehicleDao") == null) {
			ctx.setAttribute("vehicleDao", new VehicleDao());
		}
	}

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Vehicle createVehicle(Vehicle vehicle) {
		System.out.println("Create vehicle begin...");
		VehicleDao dao = (VehicleDao) ctx.getAttribute("vehicleDao");
		return dao.addVehicle(vehicle);
	}

	@GET
	@Path("/getAllForObject/{objectId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Vehicle> getAllForObject(@PathParam("objectId") int objectId) {
		System.out.println("Get all for object begin...");
		VehicleDao dao = (VehicleDao) ctx.getAttribute("vehicleDao");
		return dao.getAllForObject(objectId);
	}

	@PUT
	@Path("/change")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changeVehicle(Vehicle vehicle) {
		System.out.println("Change vehicle begin...");
		VehicleDao dao = (VehicleDao) ctx.getAttribute("vehicleDao");
		if (!dao.changeVehicle(vehicle)) {
			return Response.status(400).build();
		}
		return Response.status(200).build();
	}

	@GET
	@Path("/getByDateRange/{fromDate}/{untilDate}/{objectId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Vehicle> getByDateRange(@PathParam("fromDate") String fromDateString,
			@PathParam("untilDate") String untilDateString, @PathParam("objectId") int objectId) {
		System.out.println("Get by date range begin...");
		LocalDate fromDate = LocalDate.parse(fromDateString, DateTimeFormatter.ISO_LOCAL_DATE);
		LocalDate untilDate = LocalDate.parse(untilDateString, DateTimeFormatter.ISO_LOCAL_DATE);
		VehicleDao dao = (VehicleDao) ctx.getAttribute("vehicleDao");
		return dao.getByDateRange(fromDate.atStartOfDay(), untilDate.atStartOfDay(), objectId);
	}
	
	@GET
	@Path("/getAllForOrder/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Vehicle> getAllForOrder(@PathParam("id") String id){
		System.out.println("Get by order begin...");
		VehicleDao dao = (VehicleDao) ctx.getAttribute("vehicleDao");
		return dao.getByOrder(id);
	}
}
