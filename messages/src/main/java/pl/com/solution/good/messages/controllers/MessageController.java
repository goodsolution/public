package pl.com.solution.good.messages.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.com.solution.good.messages.controllers.requests.SendMessageRequest;
import pl.com.solution.good.messages.services.MessageService;

/**
 * 
 * @author Good Solution
 */
@Component
@Path("/messages")
public class MessageController {
	@Autowired
	private MessageService messageService;
	
	/**
	 *     {
	 *	    "address": "pkbiker@wp.pl",
	 *	    "title": "Welcome to java world",  
	 *	    "body": "Java is powerful"  
	 *		"type": "E" 
	 *		}
	 */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/sendMessage")
    public void sendMessage(SendMessageRequest req) {
    	messageService.saveMessage(req);  //TODO response
    }
}
