package services;

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

import beans.Comment;
import dao.CommentDao;

@Path("/comments")
public class CommentService {
	
	@Context ServletContext ctx;

	public CommentService() {
		
	}
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("commentDao") == null) {
			ctx.setAttribute("commentDao", new CommentDao());
		}
	}
	
	@GET
	@Path("/getFromObjectForCustomer/{objectId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Comment> getCommentsFromObjectForCustomer(@PathParam("objectId") int objectId){
		System.out.println("Get comments for customer begin...");
		CommentDao dao = (CommentDao) ctx.getAttribute("commentDao");
		return dao.getCommentsFromObjectForCustomer(objectId);
	}
	
	@GET
	@Path("/getAllForObject/{objectId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Comment> getAllForObject(@PathParam("objectId") int objectId){
		System.out.println("Get all comments for object begin...");
		CommentDao dao = (CommentDao) ctx.getAttribute("commentDao");
		return dao.getAllForObject(objectId);
	}
	
	@GET
	@Path("/getAllPendingForObject/{objectId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Comment> getAllPendingForObject(@PathParam("objectId") int objectId){
		System.out.println("Get pending for object begin...");
		CommentDao dao = (CommentDao) ctx.getAttribute("commentDao");
		return dao.getAllPendingForObject(objectId);
	}
	
	@POST
	@Path("/create/{objectId}/{customerId}/{text}/{rating}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean addComment(@PathParam("objectId") int objectId, @PathParam("customerId") int customerId,
			@PathParam("text") String text, @PathParam("rating") int rating){
		System.out.println("Add comment begin...");
		
		CommentDao dao = (CommentDao) ctx.getAttribute("commentDao");		
		if(!dao.addComment(objectId, customerId, text, rating)) {
			System.out.println("Error while adding comment");
			return false;
		}
		
		return true;
	}
	
	@POST
	@Path("/approveComment/{commentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean approveComment(@PathParam("commentId") int commentId){
		System.out.println("Approve comment begin...");		
		CommentDao dao = (CommentDao) ctx.getAttribute("commentDao");		
		if(!dao.approveComment(commentId)) {
			System.out.println("Error while approving comment");
			return false;
		}
		
		return true;
	}
	
	@POST
	@Path("/denyComment/{commentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean denyComment(@PathParam("commentId") int commentId){
		System.out.println("Approve comment begin...");		
		CommentDao dao = (CommentDao) ctx.getAttribute("commentDao");		
		if(!dao.denyComment(commentId)) {
			System.out.println("Error while denying comment");
			return false;
		}
		
		return true;
	}
}
