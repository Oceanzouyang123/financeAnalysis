package com.bootstrap.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "monthbill")
public class Monthbill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	private String billno;
	private String chargedweight;
	private String producttype;
	private String price;
	private String discount;
	private String destination;
	private String increment;
	private String titledate;
	private String customername;
	private String city;
	private int pageSize;
	private int rowStart;
	//二期需求增加
	private String eachweight;
	private String prediscount;
	private String distance;
	private String incrementprice;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public String getChargedweight() {
		return chargedweight;
	}
	public void setChargedweight(String chargedweight) {
		this.chargedweight = chargedweight;
	}
	public String getProducttype() {
		return producttype;
	}
	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getIncrement() {
		return increment;
	}
	public void setIncrement(String increment) {
		this.increment = increment;
	}
	public String getTitledate() {
		return titledate;
	}
	public void setTitledate(String titledate) {
		this.titledate = titledate;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getRowStart() {
		return rowStart;
	}
	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}
	public String getEachweight() {
		return eachweight;
	}
	public void setEachweight(String eachweight) {
		this.eachweight = eachweight;
	}
	public String getPrediscount() {
		return prediscount;
	}
	public void setPrediscount(String prediscount) {
		this.prediscount = prediscount;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getIncrementprice() {
		return incrementprice;
	}
	public void setIncrementprice(String incrementprice) {
		this.incrementprice = incrementprice;
	}
	
	
}
