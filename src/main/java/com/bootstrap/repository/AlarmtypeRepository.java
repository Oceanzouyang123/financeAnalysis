package com.bootstrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bootstrap.entity.Alarmtype;



@Repository
public interface AlarmtypeRepository extends JpaRepository<Alarmtype,Long>{
	
	@Modifying
	@Transactional
	@Query("delete from Alarmtype u where u.id = :id")
    void deleteById(@Param("id") int id);
	
	@Query("SELECT u FROM Alarmtype u where u.id = :id") 
	Alarmtype getById(@Param("id") int id);
	
}
