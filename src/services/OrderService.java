package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

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
import javax.ws.rs.core.Response;

import beans.Order;
import beans.Vehicle;
import dao.OrderCancellationDao;
import dao.OrderDao;
import dao.UserDao;
import dao.VehicleDao;

@Path("/orders")
public class OrderService {

	@Context
	ServletContext ctx;

	public OrderService() {

	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("orderDao") == null) {
			ctx.setAttribute("orderDao", new OrderDao());
		}
		if (ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDao());
		}
		if (ctx.getAttribute("vehicleDao") == null) {
			ctx.setAttribute("vehicleDao", new VehicleDao());
		}
	}

	@POST
	@Path("/makeOrder/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response makeOrder(@PathParam("userId") int userId, Collection<Vehicle> vehicles) {
		System.out.println("Making order begin...");
		OrderDao dao = (OrderDao) ctx.getAttribute("orderDao");

		if (!dao.makeOrder(userId, vehicles)) {
			System.out.println("Error while making order!");
			return Response.status(400).build();
		}

		return Response.status(200).build();
	}
	
	@GET
	@Path("/getAllOrders/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Order> getAllOrderss(@PathParam("username") String username){
		System.out.println("Getting all orders for manager...");
		OrderDao dao = (OrderDao) ctx.getAttribute("orderDao");
		
		return dao.getAllOrders(username);
	}
	
	@POST
	@Path("/cancelOrder/{orderId}/{customerId}/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelOrder(@PathParam("orderId") String orderId, @PathParam("customerId") int customerId, 
			@PathParam("price") Double price){
		System.out.println("Cancel order begin...");
		
		OrderDao dao = (OrderDao) ctx.getAttribute("orderDao");
		UserDao userDao = (UserDao) ctx.getAttribute("userDAO");	
		if(!dao.cancelOrder(orderId)) {
			System.out.println("Error while canceling order");
			return Response.status(400).build();
		}
		new OrderCancellationDao().addCancellation(customerId);
		userDao.losePointsAndMarkSuspicious(customerId, price);
		return Response.status(200).build();
	}
	
	@POST
	@Path("/approveOrder/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response approveOrder(@PathParam("orderId") String orderId){
		System.out.println("Approve order begin...");	
		OrderDao dao = (OrderDao) ctx.getAttribute("orderDao");
		
		if(!dao.approveOrder(orderId)) {
			System.out.println("Error while approve order");
			return Response.status(400).build();
		}
		return Response.status(200).build();
	}
	
	@POST
	@Path("/rejectOrder/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response rejectOrder(@PathParam("orderId") String orderId){
		System.out.println("Reject order begin...");	
		OrderDao dao = (OrderDao) ctx.getAttribute("orderDao");
		
		if(!dao.rejectOrder(orderId)) {
			System.out.println("Error while reject order");
			return Response.status(400).build();
		}
		return Response.status(200).build();
	}
	
	@POST
	@Path("/retrieveOrder/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean retrieveOrder(@PathParam("orderId") String orderId){
		System.out.println("Retrieve order begin...");	
		OrderDao dao = (OrderDao) ctx.getAttribute("orderDao");
		VehicleDao vehicleDao = (VehicleDao) ctx.getAttribute("vehicleDao");
		
		if(dao.retrieveOrder(orderId)) {
			vehicleDao.changeVehicleStatusToBusy(orderId);
			return true;
		}
		return false;
	}
	
	@POST
	@Path("/returnOrder/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean returnOrder(@PathParam("orderId") String orderId){
		System.out.println("Return order begin...");	
		OrderDao dao = (OrderDao) ctx.getAttribute("orderDao");
		VehicleDao vehicleDao = (VehicleDao) ctx.getAttribute("vehicleDao");
		
		if(dao.returnOrder(orderId)) {
			vehicleDao.changeVehicleStatusToFree(orderId);
			return true;
		}
		return false;
	}
	
	// @GET
	// @Path("/getAllBySearchForCustomer/{username}/{priceFrom}/{priceTo}/{dateFrom}/{dateTo}/{shopName}")
	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	// public ArrayList<Order> getAllBySearchForCustomer(@PathParam("username") String username, @PathParam("priceFrom") int priceFrom,
	// 		@PathParam("priceTo") int priceTo, @PathParam("dateFrom") LocalDateTime dateFrom, @PathParam("dateTo") LocalDateTime dateTo, @PathParam("shopName") String shopName) {
	// 	System.out.println("Getting all orders for customer by search...");
	// 	OrderDao dao = (OrderDao) ctx.getAttribute("orderDao");
		
	// 	return dao.getAllBySearchForCustomer(username, priceFrom, priceTo, dateFrom, dateTo, shopName);
	// }
	
	// @GET
	// @Path("/getAllBySearchForManager/{username}/{priceFrom}/{priceTo}/{dateFrom}/{dateTo}")
	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	// public ArrayList<Order> getAllBySearchForManager(@PathParam("username") String username, @PathParam("priceFrom") int priceFrom,
	// 		@PathParam("priceTo") int priceTo, @PathParam("dateFrom") LocalDateTime dateFrom, @PathParam("dateTo") LocalDateTime dateTo) {
	// 	System.out.println("Getting all orders for manager by search...");
	// 	OrderDao dao = (OrderDao) ctx.getAttribute("orderDao");
		
	// 	return dao.getAllBySearchForManager(username, priceFrom, priceTo, dateFrom, dateTo);
	// }
	
	@GET
	@Path("/sortForManager/{sortColumn}/{sortType}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Order> sortList(@PathParam("sortColumn") String sortColumn, @PathParam("sortType") String sortType){
		System.out.println("Sort started...");
		OrderDao dao = (OrderDao) ctx.getAttribute("orderDao");
		return dao.sortList(sortColumn, sortType);
	}

}