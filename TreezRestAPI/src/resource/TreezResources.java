package resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import service.TreezService;
import model.CustomerOrder;
import model.Inventory;
import model.OrderItem;

@Path("/")
public class TreezResources {
		
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_XML)
	@Path("inventories")
	public Inventory createInventory(Inventory inventory)
	{
		try{
			inventory = new TreezService().createInventory(inventory);
			return inventory;
		}
		
		catch(Exception e){
			e.printStackTrace();			
		}		
		return inventory;
		
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("inventories/{itemID}")
	public Inventory readInventory(@PathParam("itemID") int itemID)
	{
		 Inventory inventory = null;
		 try{					
			 inventory = new TreezService().readInventory(itemID);		
			 return inventory;
		}
		
		catch(Exception e){
			e.printStackTrace();			
		}
		return inventory;
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("inventories")
	public List <Inventory> readInventories()
	{
		List <Inventory> inventories = null;
		 
		try{
			inventories = new TreezService().readInventories();
			return inventories;
		}
		
		catch(Exception e){
			e.printStackTrace();
			
		}
		
		return inventories;
		
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_XML)
	@Path("inventories/{itemID}")
	public Inventory updateInventory(@PathParam("itemID") int itemID, Inventory inventory)
	{
		try{
			inventory = new TreezService().updateInventory(itemID, inventory);
			return inventory;
		}
		
		catch(Exception e){
			e.printStackTrace();		
		}
		
		return inventory;
		
	}
	
	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("inventories/{itemID}")
	public String deleteInventory(@PathParam("itemID")Integer itemID)
	{
		
		String response = null;
		try
		{
			response =  new TreezService().deleteInventory(itemID);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return response;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("orders")
	public String createOrder(CustomerOrder order)
	{
		String response = null;
		 
		try{
			order = new TreezService().createOrder(order);
			if(order == null)
				response = "\n Sorry - Order couldn't be processed";
			else
				response = "\n Thank you! Order has been accepted - Your orderID is " + order.getOrderID();
			return response;
		}
		
		catch(Exception e){
			e.printStackTrace();			
		}
		return response;
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("orders/{orderID}")
	public CustomerOrder readOrder(@PathParam("orderID") int orderID)
	{
		CustomerOrder order = null;
		 
		try{
			 order = new TreezService().readOrder(orderID);
			 return order;		 
		}
		
		catch(Exception e){
			e.printStackTrace();			
		}
		
		return order;
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("orders")
	public List <CustomerOrder> readOrders()
	{
		List <CustomerOrder> ordersList = null;
		 
		try{
			 ordersList = new TreezService().readOrders();
			 return ordersList;		 
		}
		
		catch(Exception e){
			e.printStackTrace();			
		}
		
		return ordersList;
		
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_XML)
	@Path("orders/{orderID}")
	public String updateOrder(@PathParam("orderID") int orderID, CustomerOrder customerOrder)
	{
		List<OrderItem> orderItems = customerOrder.getOrderItemList();
		String response = null;
		
		try{		
			if(orderItems.size() > 0)
				response = new TreezService().updateOrder(orderID, orderItems);			
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		
		return response;
		
	}
	
	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("orders/{orderID}")
	public String deleteOrder(@PathParam("orderID")Integer orderID)
	{
		String response = null;
		
		try
		{
			response =  new TreezService().deleteOrder(orderID);		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}
	
}
