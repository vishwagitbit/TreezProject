package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OrderItem implements Comparable<OrderItem>{

    private int itemID;
    private String itemName;
    private int quantity;
    private double price;
    
    
    public final String getItemName() {
		return itemName;
	}
	public final void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public final double getPrice() {
		return price;
	}
	public final void setPrice(double price) {
		this.price = price;
	}
	public int getItemID() {
		return itemID;
	}
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public int compareTo(OrderItem item) {
		return this.itemID - item.itemID;
	}

	 @Override
	   public String toString() {
	        return ("ItemID:"+this.getItemID()+
	                    " ItemName: "+ this.getItemName() +
	                    " Price: "+ this.getPrice() +
	                    " Quantity : " + this.getQuantity());
	   }
    

}