package com.bootstrap.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bootstrap.entity.Alarmtype;


//导航地图接口
public interface IAlarmtype {
	
	public int save(Alarmtype alarmtype);
	
	public Alarmtype getById(int id);
	
	public List<Alarmtype> findAll();
	
	public Page<Alarmtype> findByPage(Pageable pageable);
	
	public void delete(Long id);
	
	public void delete(int id);
	
	public void delete(Alarmtype alarmtype);

}
