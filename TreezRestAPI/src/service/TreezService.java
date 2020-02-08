package service;


import java.util.List;

import model.CustomerOrder;
import model.Inventory;
import model.OrderItem;
import db.TreezDAO;


public class TreezService {

    
 	public Inventory createInventory(Inventory inventory)
	{
		return new TreezDAO().createInventory(inventory);
		
	}
 	
	public Inventory readInventory(int itemID)
	{
		return new TreezDAO().readInventory(itemID);
		
	}
	
	public List<Inventory> readInventories()
	{
		return new TreezDAO().readInventories();
		
	}
	
	public Inventory updateInventory(int itemID, Inventory inventory)
	{
		if ((new TreezDAO()).updateInventory(itemID, inventory))
			return new TreezDAO().readInventory(itemID);
				
		return null;
		
	}
	
	public String deleteInventory(int itemID)
	{
		//Delete inventory REST call should ideally be called by Admin/Management and not by Customers.
		
		//Query if current orderItems for this order has any record with this item-ID and if yes, then check if its has status = 'open'.
		//If status is 'open', then don't delete and notify caller with failure response.
		
		if ((new TreezDAO()).deleteInventory(itemID))
			return "Message from Treez service - Delete SUCCESS for Inventory with ItemID = " + itemID;
		return "Message from Treez service - FAILURE - Issues with deleting Inventory with ItemID = " + itemID;
	}
	
	public CustomerOrder createOrder(CustomerOrder order)
	{
		return new TreezDAO().createOrder(order);
		
	}
	
	public String deleteOrder(int orderID)
	{
		//This deletes all the order-items for this order but 'orders' table record is not deleted and it's status is updated as 'canceled'
		//We can cancel only 'open' orders, not 'completed' or already 'canceled' orders.
		
		if ((new TreezDAO()).deleteOrder(orderID))
			return "Message from Treez service - Delete SUCCESS for Order = " + orderID;
		return "Message from Treez service - FAILURE - Issues with deleting ORDER = " + orderID;
	}
	
 	public CustomerOrder readOrder(int orderID)
	{
		return new TreezDAO().readOrder(orderID);
		
	}
 	
	public List <CustomerOrder> readOrders()
	{
		return new TreezDAO().readOrders();
		
	}
 	
	public String updateOrder(int orderID, List<OrderItem> orderItems)
	{
		if ((new TreezDAO()).updateOrder(orderID, orderItems))
			return "Message from Treez service - Update SUCCESS for Order = " + orderID;
		
		return "Message from Treez service - FAILURE - Issues with updating of ORDER = " + orderID;
		
	}
	
}
