package services;

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

import beans.RentACarObject;
import dao.RentACarObjectDao;
import dto.FilterParamsDto;
import dto.SearchParamsDto;

@Path("/rentACarObjects")
public class RentACarObjectService {

	@Context
	ServletContext ctx;

	public RentACarObjectService() {

	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("rentACarObjectDao") == null) {
			ctx.setAttribute("rentACarObjectDao", new RentACarObjectDao());
		}
	}

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public int createRentACarObject(RentACarObject rentACarObject) {
		System.out.println("Create rent a car object begin...");
		RentACarObjectDao dao = (RentACarObjectDao) ctx.getAttribute("rentACarObjectDao");
		return dao.addObject(rentACarObject);

	}

	@GET
	@Path("/names")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<String> getAllObjectNames() {
		System.out.println("Get all object names begin...");
		RentACarObjectDao dao = (RentACarObjectDao) ctx.getAttribute("rentACarObjectDao");
		return dao.getAllNames();
	}

	@GET
	@Path("/getById/{objectId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RentACarObject getObjectById(@PathParam("objectId") int objectId) {
		System.out.println("Get object by id begin...");
		RentACarObjectDao dao = (RentACarObjectDao) ctx.getAttribute("rentACarObjectDao");
		return dao.getObjectById(objectId);
	}

	@GET
	@Path("/getAllForHomePage")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<RentACarObject> getAllForHomePage() {
		System.out.println("Get all objects for home page begin...");
		RentACarObjectDao dao = (RentACarObjectDao) ctx.getAttribute("rentACarObjectDao");
		return dao.getAllForHomePage();
	}

	@POST
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<RentACarObject> getAllByFilters(FilterParamsDto filterParams) {
		System.out.println("Get all objects by filters...");
		RentACarObjectDao dao = (RentACarObjectDao) ctx.getAttribute("rentACarObjectDao");
		return dao.getAllByFilters(filterParams);
	}

	@POST
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<RentACarObject> getAllBySearch(SearchParamsDto searchParams) {
		System.out.println("Search begin..." + searchParams);
		RentACarObjectDao dao = (RentACarObjectDao) ctx.getAttribute("rentACarObjectDao");
		return dao.getAllBySearch(searchParams);
	}

	@GET
	@Path("/sort/{sortColumn}/{sortFilter}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<RentACarObject> sort(@PathParam("sortColumn") String sortColumn,
			@PathParam("sortFilter") String sortFilter) {
		System.out.println("Sort begin...");
		RentACarObjectDao dao = (RentACarObjectDao) ctx.getAttribute("rentACarObjectDao");
		return dao.sortList(sortColumn, sortFilter);
	}
	
	@PUT
	@Path("/updateRatingForObject/{objectId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRatingForObject(@PathParam("objectId") int objectId) {
		System.out.println("Update rating begin...");
		RentACarObjectDao dao = (RentACarObjectDao) ctx.getAttribute("rentACarObjectDao");
		
		if(!dao.updateRatingForObject(objectId)) {
			System.out.println("Error while updating rating");
			return Response.status(400).build();
		}
		
		return Response.status(200).build();
		
	}
}
