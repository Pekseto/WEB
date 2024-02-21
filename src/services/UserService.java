package services;

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

import beans.Manager;
import beans.User;
import dao.UserDao;
import dto.UserDto;

@Path("/users")
public class UserService {

	@Context
	ServletContext ctx;

	public UserService() {

	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDao());
		}
	}
	
	@GET
	@Path("/getByUsername/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User getByUsername(@PathParam("username") String username) {
		System.out.println("Getting user by username...");
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		return dao.getByUsername(username);
	}
	
	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(UserDto customer) {
		System.out.println("Registration begin...");
		System.out.println(customer.toString());
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		if (!dao.addCustomer(customer)) {
			System.out.println("Server validation failed!");
			return Response.status(400).build();
		}
		return Response.status(200).build();

	}

	@GET
	@Path("/login/{username}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean loginUser(@PathParam("username") String username, @PathParam("password") String password) {
		System.out.println("Login begin...");
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		if (dao.loginUser(username, password)) {
			System.out.println("Success login!");
			return true;
		} else {
			System.out.println("Login failed!");
			return false;
		}
	}

	@GET
	@Path("/checkBlockStatus/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean checkBlockStatus(@PathParam("username") String username) {
		System.out.println("Checking block status...");
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");

		if (dao.checkBlockStatus(username))
			return false;

		return true;
	}

	@POST
	@Path("/registerManager")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerManager(UserDto manager) {
		System.out.println("Registration manager begin...");
		System.out.println(manager.toString());
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		if (!dao.addManager(manager)) {
			System.out.println("Server validation failed!");
			return Response.status(400).build();
		}
		return Response.status(200).build();

	}

	@GET
	@Path("/managers")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Manager> getAllAvailableManagers() {
		System.out.println("Get all available Managers begin...");
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		return dao.getAvailableManagers();
	}

	@POST
	@Path("/{managerId}/{rentACarObjectId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response connectManagerAndObject(@PathParam("managerId") int managerId,
			@PathParam("rentACarObjectId") int rentACarObjectId) {
		System.out.println("Connection between manager and object begin...");
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		if (!dao.connectManagerAndObject(managerId, rentACarObjectId)) {
			System.out.println("Error while connecting manager and rent a car object!");
			return Response.status(400).build();
		}
		return Response.status(200).build();

	}

	@PUT
	@Path("/generatePoints/{customerId}/{totalPrice}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response generatePointsForCustomer(@PathParam("customerId") int customerId,
			@PathParam("totalPrice") int totalPrice) {
		System.out.println("Generating points for user begin...");
		System.out.println(totalPrice);
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		if (!dao.generatePointsForUser(customerId, totalPrice)) {
			System.out.println("Error while generating points for user...");
			return Response.status(400).build();
		}
		return Response.status(200).build();
	}
	
	@POST
	@Path("/update/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("username") String username, UserDto user) {
		System.out.println("Edit user data begin..." + user);
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		if(!dao.updateUserData(username, user)) {
			System.out.println("Error while updating user...");
			return Response.status(400).build();
		}
		return Response.status(200).build();
	}
		
	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<User> getAll(){
		System.out.println("Get all users begin...");
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		return dao.getAll();
	}
	
	@POST
	@Path("/getBySearch")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<User> getBySearch(UserDto searchParams){
		System.out.println("Get all users by search begin...");
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		return dao.getAllBySearch(searchParams.getFirstName(), searchParams.getLastName(), searchParams.getUsername());
	}
	
	@GET
	@Path("/filterByRole/{role}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<User> filterByRole(@PathParam("role") String role){
		System.out.println("Filter by role begin...");
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		return dao.filterByRole(role);
	}
	
	@GET
	@Path("/filterByType/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<User> filterByType(@PathParam("type") String type){
		System.out.println("Filter by type begin...");
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		return dao.filterByType(type);
	}
	
	@GET
	@Path("/sort/{sortBy}/{sortType}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<User> sort(@PathParam("sortBy") String sortBy, @PathParam("sortType") String sortType){
		System.out.println("Sort begin...");
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		return dao.sort(sortBy, sortType);
	}
	
	@PUT
	@Path("/block")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response blockUser(User user){
		System.out.println("Block begin...");
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		if(!dao.blockUser(user)) {
			System.out.println("Error while blocking...");
			Response.status(400).build();
		}
		return Response.status(200).build();
	}
	
	@GET
	@Path("/suspiciousCustomers")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<User> getSuspiciousCustomers(){
		System.out.println("Get suspicious customers begin...");
		UserDao dao = (UserDao) ctx.getAttribute("userDAO");
		return dao.getSuspiciousCustomers();
	}
}
