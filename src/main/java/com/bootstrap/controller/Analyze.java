package com.bootstrap.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bootstrap.dao.MonthBillDao;
import com.bootstrap.entity.Destination;
import com.bootstrap.entity.Monthbill;
import com.bootstrap.mybatis.MonthBillMybatisDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *  Author:zouyang
 * 	Data:2018-4-10
 *  统计分析，按产品类型和目的地双向匹配
 */

@Controller
public class Analyze {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
	MonthBillMybatisDao monthBillMybatisDao;
	
	MonthBillDao monthBillDao = new MonthBillDao();
	
	//进入统计分析页面
	@RequestMapping("analyzeHtml")
	public String analyzeHtml(Model model,HttpServletRequest request, HttpServletResponse response){
		//System.out.println("跳转analyzeHtml页面");
		return "MonthBill/analyzeResult";
	}
	
	//统计分析
	@RequestMapping("analyze")
	@ResponseBody
	public synchronized JSONArray analyze(Model model,HttpServletRequest request, HttpServletResponse response){
		//System.out.println("开始分析");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonList = new JSONObject();
		List<Monthbill> billList = new ArrayList<Monthbill>();
		String sqlType = "SELECT distinct producttype FROM monthbill";
		Query queryType = entityManager.createNativeQuery(sqlType);
		//typeList获取所有产品类型
		List<String> typeList = queryType.getResultList();		
		String sqlDestination = "SELECT distinct destination FROM monthbill";
		Query queryDestination = entityManager.createNativeQuery(sqlDestination);
		//destinationList获取所有目的地
		List<String> destinationList = queryDestination.getResultList();
		DecimalFormat decimalFormat=new DecimalFormat("0.00");
		Monthbill monthbill = new Monthbill();
		//外循环是产品类别，内循环是目的地
		for (int i = 0; i < typeList.size(); i++) {
			//System.out.println(typeList.get(i));
			for (int j = 0; j < destinationList.size(); j++) {
				List<Monthbill> sumList = monthBillDao.QueryAnalyzesqlwhere(" where destination='"+destinationList.get(j)+"' and producttype='"+typeList.get(i)+"'");
				//System.out.println("每次计算的总价List:"+priceList.size());
				//根据产品和目的地获取到的统计集合
				for(int k = 0; k < sumList.size();k++){
					if(sumList.get(k).getBillno().equals("0")){
						
					}else{
						monthbill = new Monthbill();
						monthbill.setProducttype(typeList.get(i));
						monthbill.setDestination(destinationList.get(j));
						monthbill.setChargedweight(sumList.get(k).getChargedweight());
						monthbill.setPrice(sumList.get(k).getPrice());
						monthbill.setDiscount(sumList.get(k).getDiscount());
						monthbill.setBillno(sumList.get(k).getBillno());
						monthbill.setCustomername(sumList.get(k).getCustomername());
					    //计算单票重量=计费重量  ÷ 票数
						float eachweight = Float.parseFloat(sumList.get(k).getChargedweight())/Float.parseFloat(sumList.get(k).getBillno());
						monthbill.setEachweight(decimalFormat.format(eachweight));
						//统计增值费用
						List<Monthbill> incrementPriceList = monthBillDao.QueryAnalyzeIncrementPricesqlwhere(" where destination='"+destinationList.get(j)+"' and producttype='"+typeList.get(i)+"' and increment<>''");
						monthbill.setIncrementprice(incrementPriceList.get(0).getIncrementprice());
						//计算折扣率=折扣  ÷ 费用
						float prediscount = Float.parseFloat(sumList.get(k).getDiscount())/Float.parseFloat(sumList.get(k).getPrice())*100;
						monthbill.setPrediscount(decimalFormat.format(prediscount)+"%");
						//统计距离
						List<Destination> rangList = monthBillMybatisDao.getRange(destinationList.get(j));
						if(rangList.size()==0){
							monthbill.setDistance("-");
						}else{
							monthbill.setDistance(rangList.get(0).getDis());
						}
						billList.add(monthbill);
					}	
				}				
			}
		}		
		//统计合计
		List<Monthbill> totalList = monthBillMybatisDao.getTotal();
		//统计增值费用
		List<Monthbill> incrementPriceTotalList = monthBillDao.QueryAnalyzeIncrementPricesqlwhere(" where increment<>'' ");		
		//计算合计单票重量
		float eachChargeweightTotal = Float.parseFloat(totalList.get(0).getChargedweight())/Float.parseFloat(totalList.get(0).getBillno());
		//计算合计折扣率
		float prediscountTotal = Float.parseFloat(totalList.get(0).getDiscount())/Float.parseFloat(totalList.get(0).getPrice())*100;
		monthbill = new Monthbill();
		monthbill.setProducttype("合计");
		monthbill.setBillno(totalList.get(0).getBillno());
		monthbill.setPrice(totalList.get(0).getPrice());
		monthbill.setDiscount(totalList.get(0).getDiscount());
		monthbill.setChargedweight(totalList.get(0).getChargedweight());
		monthbill.setIncrementprice(incrementPriceTotalList.get(0).getIncrementprice());
		monthbill.setEachweight(decimalFormat.format(eachChargeweightTotal));
		monthbill.setPrediscount(decimalFormat.format(prediscountTotal)+"%");
		billList.add(monthbill);
		//将统计结果放入session中
		request.getSession().setAttribute("result", billList);
		jsonList.put("data", billList);
		jsonArray.add(jsonList);//将结果封装到JsonArray里
		return jsonArray;
	}
	
	//下载统计结果
	@RequestMapping("downloadResult")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException{
		List<Monthbill> resultList = (List<Monthbill>) request.getSession().getAttribute("result");
		//resultList.remove(resultList.size()-1);
		//System.out.println("进入下载downloadResult");      
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String fileName= df.format(new Date());
		String xlsFile = fileName+".xls"; //产生的Excel文件的名称
		HSSFWorkbook workbook = new HSSFWorkbook(); //产生工作簿对象
		//设置表格边框
		HSSFCellStyle style=workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		
		HSSFSheet sheet = workbook.createSheet(); //产生工作表对象,设置第一个工作表的名称为firstSheet//为了工作表能支持中文，设置字符编码为UTF_16
		workbook.setSheetName(0,"firstSheet");//产生一行
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell = row.createCell(0);
		cell.setCellValue("产品类型");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("目的地");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("计费重量");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("票数");
		cell.setCellStyle(style);	
		cell = row.createCell(4);
		cell.setCellValue("单票重量");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("费用");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("增值费用");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("折扣");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("折扣率");
		cell.setCellStyle(style);
		cell = row.createCell(9);
		cell.setCellValue("距离");
		cell.setCellStyle(style);
		
		String porductTypeName = "";
		String merge = "";
		for (int i = 0; i < resultList.size(); i++) {		
			//System.out.println(resultList.get(i).getProducttype()+"--"+resultList.get(i).getDestination()+"--"+resultList.get(i).getChargedweight()+"--"+resultList.get(i).getPrice()+"--"+resultList.get(i).getDiscount()+"--次数"+i);
			HSSFRow circle = sheet.createRow(i+1);
			cell = circle.createCell(0);
			cell.setCellStyle(style);
			//控制循环行和列		
			//判断产品类型的名称是否重复,如果重复则名称为空
			if(resultList.get(i).getProducttype().equals(porductTypeName)){
				cell.setCellValue("");
				//System.out.println(i+"空行统计合并");
			}else{
				porductTypeName = resultList.get(i).getProducttype();
				cell.setCellValue(resultList.get(i).getProducttype());
				cell.setCellStyle(style);
				//System.out.println(i+"非空行统计合并");
				merge = merge + i+",";
			}
  			cell = circle.createCell(1);
  			cell.setCellStyle(style);
  			cell.setCellValue(resultList.get(i).getDestination());
  			cell = circle.createCell(2);	
  			cell.setCellStyle(style);
  			cell.setCellValue(resultList.get(i).getChargedweight());
  			cell = circle.createCell(3);
  			cell.setCellStyle(style);
  			cell.setCellValue(resultList.get(i).getBillno());
  			cell = circle.createCell(4);
  			cell.setCellStyle(style);
  			cell.setCellValue(resultList.get(i).getEachweight());
  			cell = circle.createCell(5);
  			cell.setCellStyle(style);
  			cell.setCellValue(resultList.get(i).getPrice());
  			cell = circle.createCell(6);
  			cell.setCellStyle(style);
  			cell.setCellValue(resultList.get(i).getIncrementprice());
  			cell = circle.createCell(7);
  			cell.setCellStyle(style);
  			cell.setCellValue(resultList.get(i).getDiscount());
  			cell = circle.createCell(8);
			cell.setCellStyle(style);
  			cell.setCellValue(resultList.get(i).getPrediscount());
  			cell = circle.createCell(9);
  			cell.setCellStyle(style);
  			cell.setCellValue(resultList.get(i).getDistance());
  			cell = circle.createCell(10);
		}		
		//System.out.println(merge+"--"+"##"+merge.length()+"$$"+resultList.size());
		int mergelength = merge.length();
		if(mergelength<=2){
			int resultLength = resultList.size()-1;
			merge = merge+resultLength+",";
		}		
		merge = merge.substring(2, merge.length()-1);
		String[] s = merge.split(",");
		int start = 0;
		int end = 0;
		//产品类型合并单元格
		for (int i = 0; i < s.length; i++) {
			if(i==0){
				start = 1;
			}else{
				start = Integer.parseInt(s[i-1])+1;
			}
			if(mergelength<=2){
				end = Integer.parseInt(s[i])+1;
			}else{
				end = Integer.parseInt(s[i]);
			}			
			//System.out.println(start+"--------"+end);
			CellRangeAddress cellRangeAddress = new CellRangeAddress(start, end, 0, 0);
			sheet.addMergedRegion(cellRangeAddress);
			//水平居中  
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
	        //垂直居中  
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
		}
		//最后一个类型合并
		start = Integer.parseInt(s[s.length-1])+1;
		end = resultList.size();
		//System.out.println(start+"+++++"+s[s.length-1]+"+++++"+end);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(start, end, 0, 1);
		//合并单元格
		sheet.addMergedRegion(cellRangeAddress);
		//水平居中  
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        //垂直居中  
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
		//CellRagionAddress的参数为（开始行，结束行，开始列，结束列）
		//CellRangeAddress cellRangeAddress = new CellRangeAddress(3, 5, 0, 0);
		//Ragion的参数为（开始行，（short）开始列），结束行，（short）结束列）
		//Region region0 = new Region(3, (short) 5, 0, (short) 0);
		//开始合并
		//sheet.addMergedRegion(cellRangeAddress);
		//将设置合并单元格的样式添加到sheet中
		//以下语句读取生成的Excel文件内容
		xlsFile = resultList.get(0).getCustomername() +" "+ xlsFile;
		System.out.println(xlsFile);
		//response.setHeader("Content-Disposition","attachment;filename="+xlsFile+"固定资产");//指定下载的文件名
		response.setHeader("Content-Disposition", "attachment;filename="+ new String(xlsFile.getBytes(),"iso-8859-1"));  
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		OutputStream output = response.getOutputStream();
		BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
		try {
			bufferedOutPut.flush();
			workbook.write(bufferedOutPut);
			bufferedOutPut.close();
		}catch(IOException e){
			e.printStackTrace();
			System.out.println( "Output   is   closed ");
		}		
	}
}
