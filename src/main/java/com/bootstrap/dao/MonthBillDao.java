package com.bootstrap.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.bootstrap.entity.Monthbill;
import com.bootstrap.entity.Province;

@Transactional
public class MonthBillDao {	
	
	MySqlHelper sqlHelper = new MySqlHelper();
	
	//根据条件查询月结表
	public List<Monthbill> QueryMonthbillsqlwhere(String sqlwhere)
    {
    	String sql = "select * from monthbill "+sqlwhere;
    	ResultSet rs = sqlHelper.executeQuery(sql);
    	List<Monthbill> list = new ArrayList<Monthbill>();
    	try
		{
			while(rs.next())
			{
				Monthbill monthBill = new Monthbill();
				monthBill.setId(rs.getInt(1));
				monthBill.setBillno(rs.getString(2));
				monthBill.setChargedweight(rs.getString(3));
				monthBill.setDestination(rs.getString(4));
				monthBill.setDiscount(rs.getString(5));
				monthBill.setPrice(rs.getString(6));
				monthBill.setProducttype(rs.getString(7));
				list.add(monthBill);
			}
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}finally {
			sqlHelper.closeResource();
		}
		return list;
    }
	
	
	//统计查询
	public List<Monthbill> QueryAnalyzesqlwhere(String sqlwhere)
    {
    	String sql = "select count(DISTINCT billno), cast(sum(price) AS DECIMAL (19, 2)), cast(sum(chargedweight) AS DECIMAL (19, 3)), cast(sum(discount) AS DECIMAL (19, 2)),customername from monthbill "+sqlwhere;
    	ResultSet rs = sqlHelper.executeQuery(sql);
    	List<Monthbill> list = new ArrayList<Monthbill>();
    	try
		{
			while(rs.next())
			{
				Monthbill monthBill = new Monthbill();
				monthBill.setBillno(rs.getString(1));
				monthBill.setPrice(rs.getString(2));
				monthBill.setChargedweight(rs.getString(3));
				monthBill.setDiscount(rs.getString(4));	
				monthBill.setCustomername(rs.getString(5));
				list.add(monthBill);
			}
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}finally {
			sqlHelper.closeResource();
		}
		return list;
    }
	
	//统计增值费用
	public List<Monthbill> QueryAnalyzeIncrementPricesqlwhere(String sqlwhere)
    {
    	String sql = "select cast(sum(price) AS DECIMAL (19, 2))as price from monthbill "+sqlwhere;
    	ResultSet rs = sqlHelper.executeQuery(sql);
    	List<Monthbill> list = new ArrayList<Monthbill>();
    	try
		{
			while(rs.next())
			{
				Monthbill monthBill = new Monthbill();
				monthBill.setIncrementprice(rs.getString(1));
				list.add(monthBill);
			}
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}finally {
			sqlHelper.closeResource();
		}
		return list;
    }
	
	//统计重复单号的重量
	public List<Monthbill> QueryChargedWeightsqlwhere(String sqlwhere)
    {
    	String sql = sqlwhere;
    	ResultSet rs = sqlHelper.executeQuery(sql);
    	List<Monthbill> list = new ArrayList<Monthbill>();
    	try
		{
			while(rs.next())
			{
				Monthbill monthBill = new Monthbill();
				monthBill.setBillno(rs.getString(1));
				monthBill.setDestination(rs.getString(2));
				monthBill.setProducttype(rs.getString(3));
				monthBill.setDiscount(rs.getString(4));			
				list.add(monthBill);
			}
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}finally {
			sqlHelper.closeResource();
		}
		return list;
    }
	
	//插入机器人状态数据
	public int AddMonthBill(Monthbill monthBill){
		String sql = "INSERT INTO monthbill(billno,chargedweight,destination,discount,price,producttype,increment,titledate) VALUES ('"+monthBill.getBillno()+"','"+monthBill.getChargedweight()+"', '"+monthBill.getDestination()+"','"+monthBill.getDiscount()+"', '"+monthBill.getPrice()+"', '"+monthBill.getProducttype()+"', '"+monthBill.getIncrement()+"', '"+monthBill.getTitledate()+"')";
		return sqlHelper.executeUpdate(sql);
	}
	
	//批量事务插入数据
	public boolean AddMonthBillBatch(String[] sqls){
		//批量插入
		return sqlHelper.executeBatch(sqls);
	}
	
	//表平衡，修改重复重量
	//执行修改部门参数
    public int executeChargedWeightUpdate(String chargedWeight,String increment)
    {
    	String sql ="update monthbill set chargedweight=? where increment<>?";
    	return sqlHelper.executeUpdate(sql, new String[]{chargedWeight,increment});
    }
	
    //删除表
    public int delete()
    {
        String sql = "delete from monthbill";
        return sqlHelper.executeUpdate(sql);
    }
    
    //查询记录数
    public int QueryCount(String sqlwhere){
    	String sql = "select count(*) from monthbill "+sqlwhere;
    	return sqlHelper.executeCountQuery(sql);
    }
    
    //分页查询start,end
	public List<Monthbill> QueryPagerMonthbillsqlwhere(String sqlwhere)
    {
    	String sql = "select id,billno,chargedweight,destination,discount,price,producttype from monthbill "+sqlwhere;
    	ResultSet rs = sqlHelper.executeQuery(sql);
    	List<Monthbill> list = new ArrayList<Monthbill>();
    	try
		{
			while(rs.next())
			{
				Monthbill monthBill = new Monthbill();
				monthBill.setId(rs.getInt(1));
				monthBill.setBillno(rs.getString(2));
				monthBill.setChargedweight(rs.getString(3));
				monthBill.setDestination(rs.getString(4));
				monthBill.setDiscount(rs.getString(5));
				monthBill.setPrice(rs.getString(6));
				monthBill.setProducttype(rs.getString(7));
				list.add(monthBill);
			}
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}finally {
			sqlHelper.closeResource();
		}
		return list;
    }
	
    //查询中国大陆
	public List<Monthbill> QueryCitysqlwhere()
    {
    	String sql = "select id,city from monthbill where destination='中国大陆'";
    	ResultSet rs = sqlHelper.executeQuery(sql);
    	List<Monthbill> list = new ArrayList<Monthbill>();
    	try
		{
			while(rs.next())
			{
				Monthbill monthBill = new Monthbill();
				monthBill.setId(rs.getInt(1));
				monthBill.setCity(rs.getString(2));
				list.add(monthBill);
			}
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}finally {
			sqlHelper.closeResource();
		}
		return list;
    }
	
	//根据城市查询省份
	public List<Province> QueryProvincesqlwhere(String cityName)
	{
		String sql = "select p.name from city c,province p where c.name like '"+cityName+"%' and c.province=p.id";
		ResultSet rs = sqlHelper.executeQuery(sql);
	    List<Province> list = new ArrayList<Province>();
	    try
		{
			while(rs.next())
			{
				Province province = new Province();
				province.setName(rs.getString(1));
				list.add(province);
			}
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}finally {
			sqlHelper.closeResource();
		}
		return list;
	}
	
	//修改中国大陆为省份(根据城市)
	public int executeProvinceUpdate(String city,int id)
    {
    	String sql ="update monthbill set destination='"+city+"' where id="+id;
    	return sqlHelper.executeUpdate(sql);
    }
	
}
