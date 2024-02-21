package services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Vehicle;
import dao.CartDao;

@Path("/carts")
public class CartService {
	@Context
	ServletContext ctx;

	public CartService() {

	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("cartDao") == null) {
			ctx.setAttribute("cartDao", new CartDao());
		}
	}

	@POST
	@Path("/addVehicle/{vehicleId}/{customerId}/{fromDate}/{untilDate}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addVehicle(@PathParam("vehicleId") int vehicleId, @PathParam("customerId") int customerId,
			@PathParam("fromDate") String fromDateString, @PathParam("untilDate") String untilDateString) {
		System.out.println("Add vehicle to cart begin...");
		CartDao dao = (CartDao) ctx.getAttribute("cartDao");

		LocalDate fromDate = LocalDate.parse(fromDateString, DateTimeFormatter.ISO_LOCAL_DATE);
		LocalDate untilDate = LocalDate.parse(untilDateString, DateTimeFormatter.ISO_LOCAL_DATE);

		if (!dao.addVehicleToCart(vehicleId, customerId, fromDate, untilDate)) {
			System.out.println("Error while adding vehicle to cart!");
			return Response.status(400).build();
		}

		return Response.status(200).build();
	}

	@GET
	@Path("/getAllForCustomer/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Vehicle> getAllForCustomer(@PathParam("customerId") int customerId) {
		System.out.println("Get all from cart for user begin...");
		CartDao dao = (CartDao) ctx.getAttribute("cartDao");
		return dao.getAllForCustomer(customerId);
	}

	@DELETE
	@Path("/deleteVehicle/{vehicleId}/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteVehicle(@PathParam("vehicleId") int vehicleId, @PathParam("customerId") int customerId) {
		System.out.println("Delete vehicle from cart for user begin...");
		CartDao dao = (CartDao) ctx.getAttribute("cartDao");
		if (!dao.deleteVehicle(vehicleId, customerId)) {
			System.out.println("Error while delete vehicle from users cart!");
			return Response.status(400).build();
		}
		return Response.status(200).build();
	}

}
