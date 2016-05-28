package hrd.sr.team3;

import java.io.Serializable;

public class Product implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2116580319256880995L;
	private int id;
	private String name;
	private double unitPrice;
	private int sQty;
	private String iDate;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product(int id, String name, double unitPrice, int sQty, String iDate) {
		super();
		this.id = id;
		this.name = name;
		this.unitPrice = unitPrice;
		this.sQty = sQty;
		this.iDate = iDate;
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getsQty() {
		return sQty;
	}

	public void setdQty(int sQty) {
		this.sQty = sQty;
	}

	public String getiDate() {
		return iDate;
	}

	public void setiDate(String iDate) {
		this.iDate = iDate;
	}

}
