package com.bootstrap.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "logoperation")
public class Logoperation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String operatecontent;
	String operatetime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOperatecontent() {
		return operatecontent;
	}
	public void setOperatecontent(String operatecontent) {
		this.operatecontent = operatecontent;
	}
	public String getOperatetime() {
		return operatetime;
	}
	public void setOperatetime(String operatetime) {
		this.operatetime = operatetime;
	}
	
	
}
