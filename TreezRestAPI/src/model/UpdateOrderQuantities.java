package model;

public class UpdateOrderQuantities {
	
	private int itemID = 0;
	private int currentQuantity = 0;
	private int newQuantity = 0;
	

	public final int getItemID() {
		return itemID;
	}

	public final void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public final int getCurrentQuantity() {
		return currentQuantity;
	}

	public final void setCurrentQuantity(int currentQuantity) {
		this.currentQuantity = currentQuantity;
	}

	public final int getNewQuantity() {
		return newQuantity;
	}

	public final void setNewQuantity(int newQuantity) {
		this.newQuantity = newQuantity;
	}
	
	
}
