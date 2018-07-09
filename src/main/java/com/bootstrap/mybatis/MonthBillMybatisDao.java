package com.bootstrap.mybatis;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.bootstrap.entity.Destination;
import com.bootstrap.entity.Monthbill;

@Transactional
public interface MonthBillMybatisDao {

	//根据id查询monthbill对象
	Monthbill getMonthbill(int id);
	
	//查询所有
	List<Monthbill> getMonthbillList();
	
	//批量插入
	@Transactional
	void insertBatch(List<Monthbill> list);
	
	//分页
	List<Monthbill> getMonthbillPager(int rowStart,int pageSize);
	
	//多条件匹配查询
	List<Monthbill> getMonthbillSqlWhere(Monthbill monthBill);
	//List<MonthBill> getMonthbillSqlWhere(String sqlwhere);
	
	//分页+多条件匹配查询
	List<Monthbill> getMonthbillPagerContent(Monthbill monthBill);
	
	//根据条件查询获取记录数
	int getCountSqlwhere(Monthbill monthBill);
	
	//获取目的地组别
	List<Monthbill> getDestination();
	
	//复制数据到表monthbillbak
	void insertMonthbillbak();
	
	//根据地名获取距离
	List<Destination> getRange(String name);
	
	//统计总和
	List<Monthbill> getTotal();
	
}
