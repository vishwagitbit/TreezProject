package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Inventory {

	private int itemID;
	private String itemName;
	private String itemDescription;
	private double price;
	private int quantity;
	
	public Inventory(){
		
	}
	
	public Inventory(int itemQuantity){
		this.quantity = itemQuantity;
	}
	
	public final int getItemID() {
		return itemID;
	}
	public final void setItemID(int itemID) {
		this.itemID = itemID;
	}
	public final String getItemName() {
		return itemName;
	}
	public final void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public final String getItemDescription() {
		return itemDescription;
	}
	public final void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public final double getPrice() {
		return price;
	}
	public final void setPrice(double price) {
		this.price = price;
	}
	public final int getQuantity() {
		return quantity;
	}
	public final void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
