package com.bootstrap.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "alarmtype")
public class Alarmtype {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	private String alarmname;
	private int alarmtypeid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAlarmname() {
		return alarmname;
	}
	public void setAlarmname(String alarmname) {
		this.alarmname = alarmname;
	}
	public int getAlarmtypeid() {
		return alarmtypeid;
	}
	public void setAlarmtypeid(int alarmtypeid) {
		this.alarmtypeid = alarmtypeid;
	}
	
	
}
