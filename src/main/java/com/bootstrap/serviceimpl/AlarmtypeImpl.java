package com.bootstrap.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bootstrap.entity.Alarmtype;
import com.bootstrap.repository.AlarmtypeRepository;
import com.bootstrap.service.IAlarmtype;

@Service
@Transactional

//报警类型
public class AlarmtypeImpl implements IAlarmtype{

	@Autowired
	AlarmtypeRepository alarmtypeRepository;
	
	@Override
	public int save(Alarmtype alarmtype) {
		alarmtypeRepository.save(alarmtype);
		return 0;
	}

	@Override
	public Alarmtype getById(int id) {
		return alarmtypeRepository.getById(id);
	}

	@Override
	public List<Alarmtype> findAll() {
		return alarmtypeRepository.findAll();
	}

	@Override
	public Page<Alarmtype> findByPage(Pageable pageable) {
		return alarmtypeRepository.findAll(pageable);
	}

	@Override
	public void delete(Long id) {
		alarmtypeRepository.delete(id);
	}

	@Override
	public void delete(int id) {
		alarmtypeRepository.delete((long)id);
	}

	@Override
	public void delete(Alarmtype alarmtype) {
		alarmtypeRepository.delete(alarmtype);
	}
	
}
