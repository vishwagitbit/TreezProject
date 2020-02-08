package model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CustomerOrder {
	
	private int orderID;
	private String customerEmailID;
	private List<OrderItem> orderItemList;
	private String status;
	private Date dateOfOrder;
	
		
	public final Date getDateOfOrder() {
		return dateOfOrder;
	}
	public final void setDateOfOrder(Date dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
	}
	public final String getStatus() {
		return status;
	}
	public final void setStatus(String status) {
		this.status = status;
	}
	public int getOrderID() {
		return orderID;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	public String getCustomerEmailID() {
		return customerEmailID;
	}
	public void setCustomerEmailID(String customerEmailID) {
		this.customerEmailID = customerEmailID;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
		
	
}
