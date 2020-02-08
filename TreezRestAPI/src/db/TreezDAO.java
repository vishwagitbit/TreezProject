package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import exceptions.AutoIncrementException;
import exceptions.DeleteRowException;
import exceptions.InsertTableException;
import exceptions.OrderPrcosessingException;
import exceptions.UpdateTableException;
import model.CustomerOrder;
import model.Inventory;
import model.OrderItem;
import model.UpdateOrderQuantities;

public class TreezDAO {


	public Inventory createInventory(Inventory inventory)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			con = TreezDBManager.getDBConnection();
				
			String query = "insert into inventory (ItemID, ItemName, ItemDesc, ItemQuantity, ItemPrice) values (?,?,?,?,?)" ;
						 
			pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, null);
			pstmt.setString(2, inventory.getItemName());
			pstmt.setString(3, inventory.getItemDescription());
			pstmt.setInt(4, inventory.getQuantity());
			pstmt.setDouble(5, inventory.getPrice());
			
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			int itemID = 0;
			while(rs!=null && rs.next())
			{
				itemID = rs.getInt(1);				
			}
					
			if (itemID > 0)
			inventory.setItemID(itemID);
			else 
				throw new AutoIncrementException();
			
			
			return inventory;
	
		}
		catch(AutoIncrementException e)
		{
			System.out.print("\nIssue with auto increment - Please deal with ItemID for this record - " + inventory.getItemName());
			inventory = null;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		    try { rs.close(); } catch (Exception e) {}
		    try { pstmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return inventory;
		
	
	}

	
	public Inventory readInventory(int id)
	{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		Inventory inventory = null;
		
		try{
			con = TreezDBManager.getDBConnection();
					
			String query = "select * from inventory where ItemID = '" + id + "'";
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				int itemID = rs.getInt(1);
				String itemName = rs.getString(2);
				String itemDescription = rs.getString(3);
				int quantity = rs.getInt(4);
				double price = rs.getDouble(5);
				
				inventory = new Inventory();
				inventory.setItemID(itemID);
				inventory.setItemName(itemName);
				inventory.setItemDescription(itemDescription);
				inventory.setQuantity(quantity);
				inventory.setPrice(price);
				
			}
				
			return inventory;
	
		}
	
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		    try { rs.close(); } catch (Exception e) {}
		    try { stmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return inventory;
		
	
	}
	
	

	
	public List<Inventory> readInventories()
	{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		List<Inventory> inventories = new ArrayList<Inventory>() ;
		Inventory inventory = null;
		
		try{
			con = TreezDBManager.getDBConnection();
					
			String query = "select * from inventory";
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				
				int itemID = rs.getInt(1);
				String itemName = rs.getString(2);
				String itemDescription = rs.getString(3);
				int quantity = rs.getInt(4);
				double price = rs.getDouble(5);
				
				inventory = new Inventory();
				inventory.setItemID(itemID);
				inventory.setItemName(itemName);
				inventory.setItemDescription(itemDescription);
				inventory.setQuantity(quantity);
				inventory.setPrice(price);
				
				inventories.add(inventory);
				
			}
				
			return inventories;
	
		}
	
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		    try { rs.close(); } catch (Exception e) {}
		    try { stmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return inventories;
		
	
	}

	public boolean updateInventory(int itemID, Inventory inventory)
	{
		Connection con = null;
		Statement stmt = null;
		boolean isSuccess = false;
		
		try{
			con = TreezDBManager.getDBConnection();
			stmt=con.createStatement();
			int newQuantity = inventory.getQuantity();
			
			String query = "update inventory set ItemQuantity = " + newQuantity + " where ItemID = " + itemID;
			
			/*The below commented query could be used for update inventory quantity and also 'prices' - by Treez admin/sale group
			but this method is called by multiple methods including 'adjustInventory' ans user might input new price in their input, hence not the below query is not used.
			As enhancement this query can be used in a specific method to handle admin call when prices are intended to be updated.
			
			String query = "update inventory set ItemQuantity = " + newQuantity + ", ItemPrice = " + newPrice + " where ItemID = " + itemID + " ";
			
			*/
			
			
			int retCount = stmt.executeUpdate(query);
			
			if (retCount == 1)
				isSuccess = true;
			else
			{
				 throw new UpdateTableException();
			}
			
			return isSuccess;
			
		}
		catch(UpdateTableException e)
		{
			System.out.print("\nError in PUT - update - Please check for Inventory ID " + itemID);
						
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		     try { stmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return isSuccess;
		
	
	}


	public boolean deleteInventory(int itemID)
	{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		boolean isSuccess = false;
		
		try{
			con = TreezDBManager.getDBConnection();
			stmt=con.createStatement();
					
			//Query to find if order status is open for the item, we can delete only open orders
			String orderStatusQuery = "select * from orderitem where OrderItemID = " + itemID + " and OrderItemStatus = 'open'";
			rs = stmt.executeQuery(orderStatusQuery);
			
			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();			  
			}
			
			if(rowcount > 0)
			{
				System.out.print("\n Delete of item:  " + itemID + " is aborted -- There are still " + rowcount + " open order items ");
				return false;
				
			}
					
			String query = "delete from inventory where ItemID = " + itemID + "";
				
			int retCount = stmt.executeUpdate(query);
			
			if (retCount == 1)
				isSuccess = true;
			else
			{
				 throw new DeleteRowException();
			}
			
			return isSuccess;
			
		}
		catch(DeleteRowException e)
		{
			System.out.print("\nError in DELETE - delete - Please check for Inventory ID " + itemID);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		     try { stmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return isSuccess;
		
	}
	
	
	public CustomerOrder createOrder(CustomerOrder order)
	{
		try{
				
			List <OrderItem> itemsList = order.getOrderItemList();
			
			//Used comparable interface compareTo - 'ItemID'
			Collections.sort(itemsList);
			
			/*
			for(OrderItem orderItem : itemsList) {
			  System.out.println("\n Inventories list sorted - display  " + orderItem.getItemID());
			}*/
			
					
			//Check inventory for all Item IDs with required quantities
			//If at least of them not available - return
			ArrayList<Integer> quantitiesAvailable = inventoriesAvailable(itemsList);
			if (quantitiesAvailable == null)
			{
				System.out.print("\n Inventories check returned FAILUE - at least of them not available\n ");
			}
			
			
			//If all quantities are more or equal than requested then process new order requested
			//Insert new order - update 'inventory' - insert 'orderItems'
			
			int orderIDgenerated = insertOrder(order.getCustomerEmailID());
			if(orderIDgenerated == 0)
				return null;
			order.setOrderID(orderIDgenerated);
			
			for (int i = 0 ; i<itemsList.size() ;i++){
				
				int itemIDforOrder = itemsList.get(i).getItemID();
				int quantityRequested = itemsList.get(i).getQuantity();
				
				//adjust/update inventory && insert orderItems
				 if (! ( updateInventory(itemIDforOrder, new Inventory(quantitiesAvailable.get(i) - quantityRequested)) && 			
					insertOrderItems(orderIDgenerated, itemIDforOrder, quantityRequested)) )
				 {
					 // return customerOrder with orderID
					 throw new OrderPrcosessingException();
				}
					 
			}			
				
			return order;
				
		}
		catch(OrderPrcosessingException e)
		{
			System.out.print("\nIssue with Order Proessing - Please deal with order and DB chek for Customer with emailID - " + order.getCustomerEmailID());
									
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		  
		}
		
		return null;
		
	
	}
	
	private ArrayList<Integer> inventoriesAvailable(List <OrderItem> itemsList)
	{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
				
		try{
			con = TreezDBManager.getDBConnection();
		
			if(itemsList.size() == 0)
				return null;
						
			//ArrayList<Integer> itemIDs = new ArrayList<Integer>();
			ArrayList<Integer> quantitiesAvailable = new ArrayList<Integer>();
			String idParams = ""; 
			
			for (int i = 0 ; i<itemsList.size() ;i++){
				// itemIDs.add(itemsList.get(i).getItemID()) ;
				 int id = itemsList.get(i).getItemID();
				 if(i>0)
					 idParams = idParams + " OR ";
				 idParams = idParams + "ItemID = '" + id + "'";				
			}
			
			String query = "select ItemQuantity from inventory where " + idParams + " group by ItemID";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			
			
			if(rowcount != itemsList.size())
				return null;
						
			int count = 0;
			while(rs.next()){
				
				if (itemsList.get(count++).getQuantity() > rs.getInt("ItemQuantity"))
					return null;
				else
					quantitiesAvailable.add(rs.getInt("ItemQuantity"));				
				
			}
				
		return quantitiesAvailable;
	
		}
	
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		    try { rs.close(); } catch (Exception e) {}
		    try { stmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return null;
		
	
	}
	
	private int insertOrder(String customerEmailID)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			con = TreezDBManager.getDBConnection();				
			
			String query = "insert into orders (OrderID, CustomerEmailID, DateofOrder, OrderStatus) values (?,?,?,?)" ;
		
			pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			
			pstmt.setString(1, null);
			pstmt.setString(2, customerEmailID);
			pstmt.setTimestamp(3, null);
			pstmt.setString(4, "open");
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			int orderID = 0;
			while(rs!=null && rs.next())
			{
				orderID = rs.getInt(1);
				
			}
								
			if (orderID > 0)
			return orderID;
			else 
				throw new AutoIncrementException();
				
			
		}
		catch(AutoIncrementException e)
		{
			System.out.print("\nIssue with auto increment - Please deal with orderID for this email record - " + customerEmailID);
						
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		finally {
		    try { rs.close(); } catch (Exception e) {}
		    try { pstmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return 0;
		
	
	}

	private boolean insertOrderItems(int orderID, int itemID, int itemQuantity)
	{
		Connection con = null;
		Statement stmt = null;
		boolean isSuccess =  false;
		
		try{
			con = TreezDBManager.getDBConnection();
			
			System.out.print("\n MAIN TEST here..  in POST - create - insert of orderItems : sample \n" );
			
			
			String query = "insert into orderitem (OrderID, OrderItemID, OrderItemName, OrderItemQuantity, OrderItemPrice, OrderItemStatus)"
					+"select '" + orderID + "', '" + itemID + "', inventory.ItemName, '" + itemQuantity + "', inventory.ItemPrice, orders.OrderStatus "
					+ "from inventory, orders where inventory.ItemID = '" + itemID + "' and orders.orderID = '" + orderID + "'";
			
			System.out.print("\n MAIN TEST here..  in POST - create - insert of orderItems : \n" + query + "\n\n");
			
			stmt = con.createStatement();
			int retCount = stmt.executeUpdate(query);
			if (retCount == 1)
				isSuccess = true;
			else
				throw new InsertTableException();
			
			return isSuccess;
		}
		catch(InsertTableException e)
		{
			System.out.print("\nIssue with insert orderItems - Please deal with orderItem with orderID - ItemID " + orderID + " - " + itemID);
						
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		    try { stmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return isSuccess;
			
	}

	public CustomerOrder readOrder(int orderID)
	{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		CustomerOrder order = null;
		
		try{
			con = TreezDBManager.getDBConnection();
			stmt = con.createStatement();
			String orderQuery = "select * from orders where OrderID = '" + orderID + "'";
						
			rs = stmt.executeQuery(orderQuery);
			
			while(rs.next()){
							
				order = new CustomerOrder();
				order.setOrderID(rs.getInt("OrderID"));
				order.setCustomerEmailID(rs.getString("CustomerEmailID"));
				
				Timestamp stamp = rs.getTimestamp("DateOfOrder");
				order.setDateOfOrder(new Date(stamp.getTime()));
				order.setStatus(rs.getString("OrderStatus"));
								
			}
			try { rs.close(); } catch (Exception e) {}
				
			String orderItemsQuery = "select OrderItemID, OrderItemName, OrderItemQuantity, OrderItemPrice from orderitem where OrderID = '" + orderID + "'";
			rs = stmt.executeQuery(orderItemsQuery);
			List <OrderItem> orderItems =  new ArrayList<OrderItem>();
			
			while(rs.next()){
				
				OrderItem orderItem = new OrderItem();
				orderItem.setItemID(rs.getInt("OrderItemID"));
				orderItem.setItemName(rs.getString("OrderItemName"));
				orderItem.setQuantity(rs.getInt("OrderItemQuantity"));
				orderItem.setPrice(rs.getDouble("OrderItemPrice"));
				
				orderItems.add(orderItem);
					
			}
			
			order.setOrderItemList(orderItems);
			
			return order;
	
		}
	
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		    try { rs.close(); } catch (Exception e) {}
		    try { stmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return order;
		
	
	}

	public List <CustomerOrder> readOrders()
	{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		List <CustomerOrder> orderList = new ArrayList<CustomerOrder>();
		CustomerOrder order = null;
		
		try{
			con = TreezDBManager.getDBConnection();
			stmt = con.createStatement();
			
			String orderQuery = "select OrderID from orders";
						
			rs = stmt.executeQuery(orderQuery);
			
			while(rs.next()){
							
				order = readOrder(rs.getInt("OrderID"));
				orderList.add(order);
								
			}
			
			return orderList;
	
		}
	
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		    try { rs.close(); } catch (Exception e) {}
		    try { stmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return orderList;
		
	
	}
	
	public boolean updateOrder(int orderID, List<OrderItem> requestedorderItems)
	{
		Connection con = null;
		Statement stmt = null;
		boolean isSuccess = false;
		
		try{
			con = TreezDBManager.getDBConnection();
			stmt=con.createStatement();
	
			
			//Sort the received orderlist
			//Retrieve  current order list for this orderID from orderItem table
			
			CustomerOrder customerOrder = readOrder(orderID);
			List<OrderItem> currentOrderList = customerOrder.getOrderItemList();
			Collections.sort(currentOrderList);
			Collections.sort(requestedorderItems);
					
			ArrayList<Integer> currentItemIDList = getItemIDList(currentOrderList);
			
			List<OrderItem> newItemOrderList = new ArrayList<OrderItem>();
			List<UpdateOrderQuantities> existingItemOrderList = new ArrayList<UpdateOrderQuantities>();
			
			//Check for no new itemIDs in received list - if there is add to 'InsertList()'
			for(int i=0; i<requestedorderItems.size(); i++)
			{
				OrderItem requestedOrderitem = requestedorderItems.get(i);
				
				if (! currentItemIDList.contains(requestedOrderitem.getItemID()))
					newItemOrderList.add(requestedOrderitem);
				else
				{
					for(int j=0; j<currentOrderList.size(); j++)
					{
						if(currentOrderList.get(j).getItemID() == requestedOrderitem.getItemID())
						{
							UpdateOrderQuantities updateOrderQuantities = new UpdateOrderQuantities();
							updateOrderQuantities.setItemID(currentOrderList.get(j).getItemID());
							updateOrderQuantities.setCurrentQuantity(currentOrderList.get(j).getQuantity());
							updateOrderQuantities.setNewQuantity(requestedOrderitem.getQuantity());
							
							existingItemOrderList.add(updateOrderQuantities);
						}
						
					}
					
				}
			}
				
			//For new item orders - check for availability and update the inventory and insert into orderitems
			 
			ArrayList<Integer> quantitiesAvailable = null;
			
			if(newItemOrderList.size() > 0)
				quantitiesAvailable = inventoriesAvailable(newItemOrderList);
			
			if (quantitiesAvailable == null)
			{
				System.out.print("\n No new items requested or Some/all of New items requested are not available - Not processing new item update \n ");
			}
			for (int i=0; i<newItemOrderList.size() ; i++)
			{
				int itemIDforOrder = newItemOrderList.get(i).getItemID();
				int quantityRequested = newItemOrderList.get(i).getQuantity();
				
					//adjust/update inventory && insert orderItems
				if (! ( updateInventory(itemIDforOrder, new Inventory(quantitiesAvailable.get(i) - quantityRequested)) && 			
					insertOrderItems(orderID, itemIDforOrder, quantityRequested)) )
				{
				// return customerOrder with orderID
				throw new OrderPrcosessingException();
				}
		
			}
			
			
			//For each of the existing itemID, compare with prepare a new model --> one variable with needMore quantity other with releaseQuantity () (use new model)
			//prepare NeedMore list check if all the quantities are available - if all good proceed
			//Use the new model list to update inventory and orderItem tables
			boolean adjusted = adjustInventroyAndUpdateOrder(orderID, existingItemOrderList);
			
			
			//return success and ultimately send back the received order as accepted
			
			if(adjusted)		
					isSuccess = true;
			
			return isSuccess;
			
		}
		catch(OrderPrcosessingException e)
		{
			System.out.print("\nError in PUT - update - Please check for orderID " + orderID);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		     try { stmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return isSuccess;
		
	
	}

	private boolean adjustInventroyAndUpdateOrder(int orderID, List<UpdateOrderQuantities> existingItemOrderList) 
	{

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
				
		try{
			con = TreezDBManager.getDBConnection();
		
			if(existingItemOrderList.size() == 0)
				return true;
			
			//Get the inventory db quantity for each itemID
			String idParams = ""; 
			for(int i=0; i<existingItemOrderList.size() ; i++)
			{
				int itemID = existingItemOrderList.get(i).getItemID();
				
				if(i>0)
					 idParams = idParams + " OR ";
				 idParams = idParams + "ItemID = '" + itemID + "'";
				 
				 
			}
			
			//ArrayList<Integer> itemIDs = new ArrayList<Integer>();
			//ArrayList<Integer> quantitiesAvailable = new ArrayList<Integer>();
			
			String query = "select ItemID, ItemQuantity from inventory where " + idParams + " group by ItemID";
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			
			if(rowcount != existingItemOrderList.size())
				return false;
			
			int count = 0;
			while(rs.next()){
				
				int orderItemID = existingItemOrderList.get(count).getItemID();
												
				if (orderItemID == rs.getInt("ItemID"))
				{
					
					UpdateOrderQuantities orderQuantities = existingItemOrderList.get(count);
					int inventoryQuantity =  rs.getInt("ItemQuantity");
					if(orderQuantities.getNewQuantity() - orderQuantities.getCurrentQuantity() <= inventoryQuantity)
					{
						//If available then update both tables
						//adjust/update inventory && insert orderItems
						
						inventoryQuantity = inventoryQuantity - ( orderQuantities.getNewQuantity() - orderQuantities.getCurrentQuantity() );
												
						if (! ( updateInventory(orderItemID, new Inventory(inventoryQuantity)) && 			
								updateOrderItems(orderID, orderItemID, orderQuantities.getNewQuantity())) )
						{
							// return customerOrder with orderID
							throw new OrderPrcosessingException();
						}
			
						
					}
					else
					{
						System.out.print("\n Some/all of exisitng items requested are not available - Not processing existing items update \n ");
						return false;
					}
						
					
				}
				else
					return false;
				
				count++;
							
			}
			
			return true;
			
		}
	
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		    try { rs.close(); } catch (Exception e) {}
		    try { stmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return false;
		
	}

	private boolean updateOrderItems(int orderID, int orderItemID, int quantity)
	{
		Connection con = null;
		Statement stmt = null;
		boolean isSuccess = false;
		
		try{
			con = TreezDBManager.getDBConnection();
			stmt=con.createStatement();
			
			String query = "update orderitem set OrderItemQuantity = " + quantity + " where OrderID = " + orderID + " and OrderItemID = " + orderItemID;
			
			int retCount = stmt.executeUpdate(query);
			if (retCount == 1)
				isSuccess = true;
			else
			{
				 throw new UpdateTableException();
			}
			
			return isSuccess;
			
		}
		catch(UpdateTableException e)
		{
			System.out.print("\nError in PUT - update - Please check for Inventory ID " + orderItemID);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		     try { stmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return isSuccess;
		
	
	}



	private ArrayList<Integer> getItemIDList(List <OrderItem> orderitemList)
	{
		ArrayList<Integer> itemIDlist = new ArrayList<Integer>();
		for(OrderItem orderItem : orderitemList)
			itemIDlist.add(orderItem.getItemID());
			
		return itemIDlist;
		
	}
	
	public boolean deleteOrder(int orderID)
	{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		boolean isSuccess = false;
		
		try{
			con = TreezDBManager.getDBConnection();
			stmt=con.createStatement();
									
			//Query if order status is open, we can delete only open orders
			String orderStatusQuery = "select OrderStatus from orders where OrderID = " + orderID;
			rs = stmt.executeQuery(orderStatusQuery);
					
			while(rs.next()){
							
				if(! rs.getString("OrderStatus").contains("open"))
					return false;
			
			}
			
			//Get currentOrder items quantities for all the ordersItems with orderID
			String orderItemsQuery = "select OrderItemID, OrderItemQuantity from orderitem where OrderID = '" + orderID + "' group by OrderItemID";
			rs = stmt.executeQuery(orderItemsQuery);
			
			List <OrderItem> orderItems =  new ArrayList<OrderItem>();
			List <OrderItem> inventoryQuantities = new ArrayList<OrderItem>();
			
			String idParams = "";
			
			int paramCount=0;
			while(rs.next()){
				
				OrderItem orderItem = new OrderItem();
				orderItem.setItemID(rs.getInt("OrderItemID"));
				orderItem.setQuantity(rs.getInt("OrderItemQuantity"));
				
				orderItems.add(orderItem);
				
				if(paramCount>0)
				idParams = idParams + " OR ";
				idParams = idParams + "ItemID = '" + rs.getInt("OrderItemID") + "'";
					
				paramCount++;
			}
			
			//Get quantities for all the ordersItems with itemID from inventory
						
			String query = "select ItemID, ItemQuantity from inventory where " + idParams + " group by ItemID";
						
			
			if( paramCount != 0)
			{
				rs = stmt.executeQuery(query);
			
				int rowcount = 0;
				if (rs.last()) {
					rowcount = rs.getRow();
					rs.beforeFirst(); 
				}
			
				if(rowcount != orderItems.size())
					return false;
						
				while(rs.next()){
				
					OrderItem item =  new OrderItem();
				
					item.setItemID(rs.getInt("ItemID"));
					item.setQuantity(rs.getInt("ItemQuantity")); 
				
					inventoryQuantities.add(item);			
				}
			
			}
			
			//Update Inventory table with new quantities for all items
			//Delete rows in OrderItem table
			
			if ( ! (orderItems.size() == inventoryQuantities.size()))
			{
				return false;
			}
			
			for (int i=0; i< inventoryQuantities.size() ; i++)
			{
				int itemIDforOrder = inventoryQuantities.get(i).getItemID();
				
				if( itemIDforOrder != orderItems.get(i).getItemID())
					return false;
				
				//adjust/update inventory && delete orderItems
				if (! ( updateInventory(itemIDforOrder, new Inventory( inventoryQuantities.get(i).getQuantity() + orderItems.get(i).getQuantity())) && 			
					deleteOrderItem(orderID, itemIDforOrder)))
				{
				// return customerOrder with orderID
				throw new OrderPrcosessingException();
				}
		
			}
			
			
			//Set the orderStatus as 'canceled' in order table
			
			String cancelQuery = "update orders set orderStatus = 'canceled' where orderID = " + orderID;
				
			int retCount = stmt.executeUpdate(cancelQuery);
			
			if (retCount == 1)
				isSuccess = true;
			else
			{
				 throw new UpdateTableException();
			}
			
			return isSuccess;
			
		}
		catch(UpdateTableException e)
		{
			System.out.print("\nError in Update - Please check for order ID " + orderID);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		finally {
		     try { stmt.close(); } catch (Exception e) {}
		    //try { con.close(); } catch (Exception e) {}
		}
		
		return isSuccess;
		
	}
	
	
	private boolean deleteOrderItem(int orderID, int itemID)
	{
			Connection con = null;
			Statement stmt = null;
			boolean isSuccess = false;
			
			try{
				con = TreezDBManager.getDBConnection();
				stmt=con.createStatement();
			
				String query = "delete from OrderItem where OrderID = " + orderID + " and OrderItemID = " + itemID;
					
				int retCount = stmt.executeUpdate(query);
				
				if (retCount == 1)
					isSuccess = true;
				else
					{
						 throw new DeleteRowException();
					}
					
					return isSuccess;
					
				}
				catch(DeleteRowException e)
				{
					System.out.print("\nError in DELETE - delete - Please check for orderItem ID " + itemID);
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
					
				}
				
				finally {
				     try { stmt.close(); } catch (Exception e) {}
				    //try { con.close(); } catch (Exception e) {}
				}
				
				return isSuccess;
				
			}
			
			
	
}
