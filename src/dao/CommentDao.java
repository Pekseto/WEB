package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import beans.Comment;
import beans.Comment.Status;

public class CommentDao {
	private ArrayList<Comment> comments = new ArrayList<Comment>();
	private String contextPath = "C:\\Users\\Luka\\Desktop\\rent-a-car\\WebContent\\WEB-INF\\comments.txt";
	
	private OrderDao orderDao;

	public CommentDao() {
		loadComments();
	}

	private void loadComments() {
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
                    int customerId = Integer.parseInt(st.nextToken().trim());
                    int racoId = Integer.parseInt(st.nextToken().trim());
                    String text = st.nextToken().trim();
                    int rating = Integer.parseInt(st.nextToken().trim());
                    Status status = Status.valueOf(st.nextToken().trim());
                    
                    Comment comment = new Comment();
                    comment.setId(id);
                    comment.setCustomerId(customerId);
                    comment.setRentACarObjectId(racoId);                   
                    comment.setText(text);                 
                    comment.setRating(rating);                  
                    comment.setStatus(status);                
                    comments.add(comment);
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
	
	public void saveComments() {
        try {
              FileWriter file = new FileWriter(contextPath);

              BufferedWriter output = new BufferedWriter(file);

              for(Comment c : comments)
              {
                  output.write(c.returnSerializableFormat());
              }
              
              output.close();
              
        }     
        catch (Exception e) {
        	  System.out.println(e);
              e.getStackTrace();
            }
    }

	public Collection<Comment> getCommentsFromObjectForCustomer(int objectId) {
		ArrayList<Comment> retVal = new ArrayList<Comment>();
		for (Comment comment : comments) {
			if(comment.getRentACarObjectId() == objectId && comment.getStatus() == Status.Approved) retVal.add(comment);
		}
		return retVal;
	}

	public Collection<Comment> getAllForObject(int objectId) {
		ArrayList<Comment> retVal = new ArrayList<Comment>();
		for (Comment comment : comments) {
			if(comment.getRentACarObjectId() == objectId) retVal.add(comment);
		}
		return retVal;
	}

	public boolean addComment(int objectId, int customerId, String text, int rating) {
		try {
			orderDao = new OrderDao();
			if(!orderDao.isCustomerEligibleToAddComment(customerId)) return false;
			
			Comment newComment = new Comment(objectId, customerId, text, rating);
			
			int maxId = 0;
			for (Comment comment : comments) {
				if(comment.getId() > maxId) maxId = comment.getId();
			}
			newComment.setId(maxId + 1);
			
			comments.add(newComment);
			saveComments();
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	public Collection<Comment> getAllPendingForObject(int objectId) {
		ArrayList<Comment> retVal = new ArrayList<Comment>();
		for (Comment comment : comments) {
			if(comment.getRentACarObjectId() == objectId && comment.getStatus() == Status.Pending) retVal.add(comment);
		}
		return retVal;
	}

	public boolean approveComment(int commentId) {
		for (Comment comment : comments) {
			if(comment.getId() == commentId) {
				comment.setStatus(Status.Approved);
				break;
			}
		}
		saveComments();
		return true;
	}
	
	public boolean denyComment(int commentId) {
		for (Comment comment : comments) {
			if(comment.getId() == commentId) {
				comment.setStatus(Status.Denied);
				break;
			}
		}
		saveComments();
		return true;
	}
	
	public Collection<Comment> getAll(){
		return comments;
	}

}
