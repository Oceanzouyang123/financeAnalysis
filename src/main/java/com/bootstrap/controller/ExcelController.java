package com.bootstrap.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bootstrap.dao.MonthBillDao;
import com.bootstrap.entity.Monthbill;
import com.bootstrap.entity.Province;
import com.bootstrap.mybatis.MonthBillMybatisDao;
import com.bootstrap.util.ExcelUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 *  Author:zouyang
 * 	Data:2018-4-3
 *  读取excel文件并导入到数据库
 */

@Controller
public class ExcelController {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
	MonthBillMybatisDao monthBillMybatisDao;
	
	@Value(value="${com.windows}")
	private String wuploadfile;
	
	
	@Value(value="${com.linux}")
	private String luploadfile;

	
	MonthBillDao monthBillDao = new MonthBillDao();
	
	//点击确认按钮，进入bootstrapLocation.html页面
	@RequestMapping("excelImport")
	public String test(Model model,HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException{
		//System.out.println("进入excel导入Controller");
		//String addr = InetAddress.getLocalHost().getHostAddress();//获得本机IP
		//System.out.println("本机ip:"+addr);		
		return "MonthBill/excelFunction";
	}
	
	//进入到分页显示月结清单列表页面
	@RequestMapping("billListHtml")
	public String billListHtml(Model model,HttpServletRequest request, HttpServletResponse response){
		String name = (String) request.getSession().getAttribute("username");
		//System.out.println("跳转billListHtml页面"+name);
		//如果用户名为空，则表示session超时，页面返回login
		if(name == null||name.length()<=0){
			return "MonthBill/login";
		}else{
			return "MonthBill/billList";
		}		
	}
	
	//进入到分页显示月结清单列表页面
	@RequestMapping("billListHtmlBatch")
	public String billListHtmlBatch(Model model,HttpServletRequest request, HttpServletResponse response){
//		String name = (String) request.getSession().getAttribute("username");
//		if(name == null||name.length()<=0){
//			return "MonthBill/login";
//		}else{
//			
//		}	
		return "MonthBill/billListBatch";
	}
	
	//清除数据
	@RequestMapping("clearData")
	@ResponseBody
	public void clearData(){
		System.out.println("清理数据");
		//清空表
		monthBillDao.delete();
	}
	
    //上传excel文件并插入到数据库
	@RequestMapping("give")
	public synchronized String give(Model model,HttpServletRequest request, HttpServletResponse response, @RequestParam() MultipartFile file) throws IOException{		
		int startCount = 0;
		int endCount = 0;
		int count = 0;
		//System.out.println("获取上传文件"+file.getName()+"=="+file.getOriginalFilename());
		String[] ss = file.getOriginalFilename().split("-");
		String titledate= ss[0];
		String[] s = titledate.split("\\.");
		titledate = s[0];
		//清空表
		//monthBillDao.delete();
		//System.out.println("结果:"+deleteResult);
		//String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		String path = "D:/uploadFile";
		System.out.println(path);
		File filepath = new File("D:/uploadFile");
        //判断上传文件的保存目录是否存在
        if (!filepath.exists() && !filepath.isDirectory()) {
            System.out.println("D:/uploadFile目录不存在，需要创建");
            //创建目录
            filepath.mkdir();
        }
		path = path+"/"+file.getOriginalFilename();
		System.out.println(path+"--上传");
		//上传文件
		upload(path,file);
		System.out.println("文件上传完毕");
		File getFile = new File(path);  
		//获取客户名称
		String customername = "";
		if(getFile.getName().endsWith("xlsx")){
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(getFile));  
	        XSSFSheet sheet = wb.getSheetAt(0); 
	        XSSFRow row5 = sheet.getRow(3);// 获得工作薄的第五行
	        XSSFCell cell4 = row5.getCell(4);// 获得第五行的第四个单元格
	        customername = cell4.getStringCellValue();
		}else{
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(getFile));  
	        HSSFSheet sheet = wb.getSheetAt(0);  
	        HSSFRow row5 = sheet.getRow(3); // 获得工作薄的第五行
	        HSSFCell cell4 = row5.getCell(4);// 获得第五行的第四个单元格
	        customername = cell4.getStringCellValue();
		}
		long start = System.currentTimeMillis();	
		ArrayList<ArrayList<Object>> result = ExcelUtil.readExcel(getFile);
		//System.out.println(result.size()+"集合的结果");
		String[] sqls = new String[result.size()];
        for(int i = 0 ;i < result.size() ;i++){ 
        	//判断获取数据之后，直到出现小计则退出循环
        	if(endCount==1&&i>=startCount){
        		break;
        	}
            for(int j = 0;j<result.get(i).size(); j++){  
                //System.out.println("第"+i+"行,第"+j+"列："+ result.get(i).get(j).toString());  
                if(result.get(i).get(j).toString().equals("序号")){
                	//System.out.println("搜索到序号，开始计数");
                	startCount = i+1;
                }
                if(i>=startCount){
                	//j==2获取运单号码
                	if(j==2){
                		String billno = result.get(i).get(j).toString();
                		//System.out.println(result.get(i).get(0).toString()+"~~"+result.get(i).get(j).toString()+"=-=");
                		if(result.get(i).get(0).toString().contains("计")&&result.get(i).get(0).toString().contains("合")){
                			//System.out.println("找到了合计");
                			//如果到了合计，则不用再继续往下获取excel表格中的数据
                			endCount = 1;
                			break;
                		}
                		if(billno == null || billno.length() <= 0){
                			//运单号码为空，则不写入数据库
                		}else{
                			//3、5、6、8、9、12、29表示需要获取的数据列
                			String city = result.get(i).get(3).toString();
                			String chargedWeight = result.get(i).get(5).toString();
                			String productType = result.get(i).get(6).toString();
                			String price = result.get(i).get(8).toString();
                			String discount = result.get(i).get(9).toString();
                			String increment = result.get(i).get(12).toString();
                			String destination = result.get(i).get(29).toString();             			
                			//System.out.println("第"+i+"行,第"+j+"列："+ billno+"--"+chargedWeight+"--"+productType+"--"+price+"--"+discount+"--"+destination+"--"+increment);               			             			
                			//封装sql语句   
                			sqls[count] = "INSERT INTO monthbill(billno,city,chargedweight,destination,discount,price,producttype,increment,titledate,customername) VALUES ('"+billno+"','"+city+"','"+chargedWeight+"', '"+destination+"','"+discount+"', '"+price+"', '"+productType+"', '"+increment+"', '"+titledate+"', '"+customername+"')";
                			count = count + 1;
                		} 
                	} 
                }
            }
        }
        //批量执行sql，如果全部成功则commit提交
        boolean resultBoolean = monthBillDao.AddMonthBillBatch(sqls);
        long end = System.currentTimeMillis();
        System.out.println("耗时："+(start-end));
        if(resultBoolean){
        	//做表平衡(修改重复单号，增值服务为保价的重复重量)
        	System.out.println("保价(所有增值服务都更新重量为0.000)");
        	monthBillDao.executeChargedWeightUpdate("0.000", "");
        }
        //判断目的如果是中国大陆，则修改为对方地区城市所在的省份名称
        List<Monthbill> city = monthBillDao.QueryCitysqlwhere();
        for (int c = 0; c < city.size(); c++) {
			//System.out.println(city.get(c).getId()+"--"+city.get(c).getCity()+"--城市");
			String cityName = city.get(c).getCity();
			String[] cityNameGroup = cityName.split("/");
			//System.out.println(cityTrim+"~~剥离之后"+cityTrim.length());
			List<Province> listProvince = monthBillDao.QueryProvincesqlwhere(cityNameGroup[0].trim());
			//System.out.println("listProvince=="+listProvince.size());
			//修改中国大陆为省份名称
			if(listProvince.size()>0){
				monthBillDao.executeProvinceUpdate(listProvince.get(0).getName(), city.get(c).getId());
			}						
		}
        
        //复制数据到monthbillbak中,历史记录查询monthbillbak表，数据分析查monthbill表
        //monthBillMybatisDao.insertMonthbillbak();
        
        //excel文件中的数据插入数据库后，删除上传的excel文件      
        File deleteFile = new File(path);
        if (deleteFile.exists()) {
        	deleteFile.delete();
		}
        return "redirect:/billListHtml";
	}
	
	
    //上传excel文件并插入到数据库
	@RequestMapping("giveBatch")
	public synchronized String giveBatch(Model model,HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile[] files) throws IOException{		
		//MultipartFile file;
		for (int f = 0; f < files.length; f++) {
			int startCount = 0;
			int endCount = 0;
			int count = 0;
			System.out.println(files.length+"==多文件名"+files[f].getOriginalFilename());
			//System.out.println("获取上传文件"+file.getName()+"=="+file.getOriginalFilename());
			String[] ss = files[f].getOriginalFilename().split("-");
			String titledate= ss[0];
			String[] s = titledate.split("\\.");
			titledate = s[0];
			//清空表
			//monthBillDao.delete();
			//System.out.println("结果:"+deleteResult);
			//String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
			String path = "D:/uploadFile";
			System.out.println(path);
			File filepath = new File("D:/uploadFile");
	        //判断上传文件的保存目录是否存在
	        if (!filepath.exists() && !filepath.isDirectory()) {
	            System.out.println("D:/uploadFile目录不存在，需要创建");
	            //创建目录
	            filepath.mkdir();
	        }
			path = path+"/"+files[f].getOriginalFilename();
			System.out.println(path+"--上传");
			//上传文件
			//MultipartFile file = files[f];
			upload(path,files[f]);
			System.out.println("文件上传完毕");
			try {
				Thread.sleep(1000);
				System.out.println("waiting for upload excel 1sec...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			File getFile = new File(path);  
			//获取客户名称
			String customername = "";
			if(getFile.getName().endsWith("xlsx")){
				XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(getFile));  
		        XSSFSheet sheet = wb.getSheetAt(0); 
		        XSSFRow row5 = sheet.getRow(3);// 获得工作薄的第五行
		        XSSFCell cell4 = row5.getCell(4);// 获得第五行的第四个单元格
		        customername = cell4.getStringCellValue();
			}else{
				HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(getFile));  
		        HSSFSheet sheet = wb.getSheetAt(0);  
		        HSSFRow row5 = sheet.getRow(3); // 获得工作薄的第五行
		        HSSFCell cell4 = row5.getCell(4);// 获得第五行的第四个单元格
		        customername = cell4.getStringCellValue();
			}
			long start = System.currentTimeMillis();	
			ArrayList<ArrayList<Object>> result = ExcelUtil.readExcel(getFile);
			//System.out.println(result.size()+"集合的结果");
			String[] sqls = new String[result.size()];
	        for(int i = 0 ;i < result.size() ;i++){ 
	        	//判断获取数据之后，直到出现小计则退出循环
	        	if(endCount==1&&i>=startCount){
	        		break;
	        	}
	            for(int j = 0;j<result.get(i).size(); j++){  
	                //System.out.println("第"+i+"行,第"+j+"列："+ result.get(i).get(j).toString());  
	                if(result.get(i).get(j).toString().equals("序号")){
	                	//System.out.println("搜索到序号，开始计数");
	                	startCount = i+1;
	                }
	                if(i>=startCount){
	                	//j==2获取运单号码
	                	if(j==2){
	                		String billno = result.get(i).get(j).toString();
	                		//System.out.println(result.get(i).get(0).toString()+"~~"+result.get(i).get(j).toString()+"=-=");
	                		if(result.get(i).get(0).toString().contains("计")&&result.get(i).get(0).toString().contains("合")){
	                			//System.out.println("找到了合计");
	                			//如果到了合计，则不用再继续往下获取excel表格中的数据
	                			endCount = 1;
	                			break;
	                		}
	                		if(billno == null || billno.length() <= 0){
	                			//运单号码为空，则不写入数据库
	                		}else{
	                			//5、6、8、9、12、29表示需要获取的数据列
	                			String chargedWeight = result.get(i).get(5).toString();
	                			String productType = result.get(i).get(6).toString();
	                			String price = result.get(i).get(8).toString();
	                			String discount = result.get(i).get(9).toString();
	                			String increment = result.get(i).get(12).toString();
	                			String destination = result.get(i).get(29).toString();             			
	                			//System.out.println("第"+i+"行,第"+j+"列："+ billno+"--"+chargedWeight+"--"+productType+"--"+price+"--"+discount+"--"+destination+"--"+increment);               			             			
	                			//封装sql语句   
	                			sqls[count] = "INSERT INTO monthbill(billno,chargedweight,destination,discount,price,producttype,increment,titledate,customername) VALUES ('"+billno+"','"+chargedWeight+"', '"+destination+"','"+discount+"', '"+price+"', '"+productType+"', '"+increment+"', '"+titledate+"', '"+customername+"')";
	                			count = count + 1;
	                		} 
	                	} 
	                }
	            }
	        }
	        //批量执行sql，如果全部成功则commit提交
	        boolean resultBoolean = monthBillDao.AddMonthBillBatch(sqls);
	        long end = System.currentTimeMillis();
	        System.out.println("耗时："+(start-end));
	        if(resultBoolean){
	        	//做表平衡(修改重复单号，增值服务为保价的重复重量)
	        	System.out.println("保价");
	        	monthBillDao.executeChargedWeightUpdate("0.000", "保价");
	        }
	        
	        //excel文件中的数据插入数据库后，删除上传的excel文件      
//	        File deleteFile = new File(path);
//	        if (deleteFile.exists()) {
//	        	deleteFile.delete();
//			}
		}
		

        return "redirect:/billListHtmlBatch";
	}
	
	//判断文件是否是2003版本
	public static boolean isExcel2003(String filePath)
    {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

	//判断文件是否是2007版本
    public static boolean isExcel2007(String filePath)
    {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }
    
    //上传文件,先将excel文件写入到服务器端，然后从服务器端再获取
    public synchronized void upload(String filename,MultipartFile file) throws IOException{
    	File outFile = new File(filename);
    	BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(outFile)); // 创建文件输出流
    	//根据上传的文件，创建缓冲区，生成字符流，存入缓冲区
        byte[] buffer = file.getBytes();//创建文件流       
        //把缓冲区的字符流 写入 字符流目的地(把输入流写入输出流)
        stream.write(buffer);
        //第6步：刷新流，关闭流
        stream.close();
    }
    
    //进入月结清单页面(分页查询)
    @RequestMapping("monthBillList")
    @ResponseBody
    public JSONArray MonthBillList(ModelMap model,HttpSession session,int currentPage){
    	//String path = wuploadfile != null ? luploadfile : null;
    	//System.out.println("获取配置文件上传excel路径配置: "+wuploadfile+"~~"+path);
    	//查询最后一个月结清单数据
    	int pageSize = 15;//初始化值，每页显示几行数据，当前页为第一页	
		Monthbill monthBill = new Monthbill();
		//自定义算法分页
		int totalRows = monthBillDao.QueryCount("");
		int countPage = (totalRows + pageSize-1)/pageSize;
		int rowStart = (currentPage - 1) * pageSize;
		monthBill.setPageSize(pageSize);
		monthBill.setRowStart(rowStart);
		//优化分页查询，以id为索引
		//List<Monthbill> monthBillList = monthBillDao.QueryPagerMonthbillsqlwhere("where id >= (select id FROM monthbill ORDER BY id LIMIT "+rowStart+", 1) LIMIT "+pageSize);
		List<Monthbill> monthBillList = monthBillMybatisDao.getMonthbillPagerContent(monthBill);
		//WHERE  id >= (SELECT id FROM monthbill ORDER BY id LIMIT 10000, 1) LIMIT 15;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonList = new JSONObject();
		jsonList.put("billcountPage", countPage);
		jsonList.put("billcurrentPage", currentPage);
		jsonList.put("billNum",totalRows);
		jsonList.put("data",monthBillList);
		jsonArray.add(jsonList);//将结果封装到JsonArray里
		//model.addAttribute("data", jsonArray);
		return jsonArray;
    }

}
