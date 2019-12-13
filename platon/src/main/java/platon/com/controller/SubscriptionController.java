package platon.com.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import platon.com.po.PlatSubscription;
import platon.com.service.PlatSubscriptionService;
import platon.com.util.Const;

import com.jdbcTemplateTool.page.Pagination;
import com.util.JsonResp;


@Controller
@RequestMapping("subscription")
public class SubscriptionController {
	 private static final String EXCEL_NAME="订阅管理";
	 private static final String EXCEL_TYPE=".xls";
	 private static final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 
	
	 @Autowired
	 private PlatSubscriptionService platSubscriptionService;
	 

	/**
	 * 获取订阅列表
	 * @return
	 */
	@RequestMapping(value="findPlatSubscription" , method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatSubscription>> findPlatSubscription(
			@RequestBody PlatSubscription platSubscription) {
		JsonResp<Pagination<PlatSubscription>> json = new JsonResp<>();
		try {
			Pagination<PlatSubscription> list = platSubscriptionService.findPlatSubscription(platSubscription);
			if(list==null){
				json.setData(null);
				json.setCode(1);
				json.setMessage("获取失败");
				return json;
			}
			json.setData(list);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
		
	
	
	/**
	 * 新增订阅
	 * @return
	 */
	@RequestMapping(value="insSubscriptionOnline", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Integer> insSubscriptionOnline(@RequestBody PlatSubscription platSubscription) {
		JsonResp<Integer> json = new JsonResp<>();
		try {
			if(platSubscription.getEmail()!=null && !"".equals(platSubscription.getEmail())){
				platSubscription.setEmail(platSubscription.getEmail().replace("'","\\'"));
				
				platSubscription.setCreateTime(new Date());
				Integer i=platSubscriptionService.insSubscription(platSubscription);
				if(i==null){
					json.setData(null);
					json.setCode(1);
					json.setMessage("不能重复订阅");
					return json;
				}
				json.setData(i);
				json.setCode(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
		

	/**
	 * 更新订阅
	 * @return
	 */
	@RequestMapping(value="uptSubscriptionOnline", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> uptSubscriptionOnline(@RequestBody PlatSubscription platSubscription) {
		JsonResp<Boolean> json = new JsonResp<>();
		try {
			if(platSubscription.getId()>0){
				if(platSubscription.getName()!=null && !"".equals(platSubscription.getName()))
					platSubscription.setName(platSubscription.getName().replace("'","\\'"));
				if(platSubscription.getCompanyName()!=null && !"".equals(platSubscription.getCompanyName()))
					platSubscription.setCompanyName(platSubscription.getCompanyName().replace("'","\\'"));
						
				Integer i=platSubscriptionService.uptSubscription(platSubscription);
				if(i==null || i==0){
					json.setData(false);
					json.setCode(1);
					json.setMessage("更新失败");
					return json;
				}
				json.setData(true);
				json.setCode(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
		
	
	/**
	 * 删除订阅
	 * @return
	 */
	@RequestMapping(value="removeSubscription", method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> removeSubscription(@RequestBody PlatSubscription platSubscription) {
		JsonResp<Boolean> json = new JsonResp<>();
		try {
			if(platSubscription.getId()==null){
				json.setData(false);
				json.setCode(0);
				json.setMessage("删除失败");
				return json;
			}
			platSubscriptionService.removeSubscription(platSubscription);
			json.setData(true);
			json.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			json.setCode(-1);
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
		

	/**
	 * 导出订阅
	 * @param req
	 * @param resp
	 * @param platSubscription
	 */
	@SuppressWarnings("unused")
	@RequestMapping("downloadExecl.do")
	public void downloadFile(HttpServletRequest req, HttpServletResponse resp,PlatSubscription platSubscription) {
		try {
			
			String fileNameLast=EXCEL_NAME+sdfTime.format(new Date())+EXCEL_TYPE;
			if (StringUtils.isNotBlank(fileNameLast)) {
				if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
					resp.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(fileNameLast, "UTF-8"));// IE
				} else {
					resp.setHeader("content-disposition", "attachment;fileName=" + new String(fileNameLast.getBytes("UTF-8"), "ISO8859-1"));// 非IE
				}
			}
			//结果集
			List<Object[]> list=new ArrayList<>();
			if(platSubscription.getIdTypes()!=null && !"".equals(platSubscription.getIdTypes()) && !"null".equals(platSubscription.getIdTypes()))
				platSubscription.setIdType(Integer.valueOf(platSubscription.getIdTypes()));
			List<PlatSubscription> keyList=platSubscriptionService.findListPlatSubscription(platSubscription);
			//初始化excel数据。
			Object[] obj=null;
			obj=excelColumnNameByType();
			list.add(obj);
			if(keyList==null)
				return;
			//组装excel
			for (int i = 0; i < keyList.size(); i++) {
				//拼装数据
					obj=new Object[6];
					if(keyList.get(i).getDescNo()!=null && !"".equals(keyList.get(i).getDescNo())){
						obj[0]=keyList.get(i).getDescNo();
					}else{
						obj[0]="";
					}
					if(keyList.get(i).getEmail()!=null && !"".equals(keyList.get(i).getEmail())){
						obj[1]=keyList.get(i).getEmail();
					}else{
						obj[1]="";
					}
					if(keyList.get(i).getName()!=null && !"".equals(keyList.get(i).getName())){
						obj[2]=keyList.get(i).getName();
					}else{
						obj[2]="";
					}
					if(keyList.get(i).getCompanyName()!=null && !"".equals(keyList.get(i).getCompanyName())){
						obj[3]=keyList.get(i).getCompanyName();
					}else{
						obj[3]="";
					}
					if(keyList.get(i).getIdType()!=null && !"".equals(keyList.get(i).getIdType())){
						obj[4]=typeToName(keyList.get(i).getIdType());
					}else{
						obj[4]="";
					}
					if(keyList.get(i).getCreateTime()!=null && !"".equals(keyList.get(i).getCreateTime())){
						obj[5]=sdfTime.format(keyList.get(i).getCreateTime());
					}else{
						obj[5]="";
					}
				list.add(obj);
			}
			 WritableWorkbook wwb = null;
				try {
					// 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
					wwb = Workbook.createWorkbook(resp.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (wwb != null) {
					// 创建一个可写入的工作表
					// Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
					WritableSheet ws = wwb.createSheet("sheet1", 0);

					// 下面开始添加单元格
					for (int i = 0; i < list.size(); i++) {
						Object[] obj1=list.get(i);
						for (int j = 0; j <obj1.length; j++) {
							// 这里需要注意的是，在Excel中，第一个参数表示列，第二个表示行
							Label labelC = new Label(j, i, obj1[j].toString());
							try {
								// 将生成的单元格添加到工作表中
								ws.addCell(labelC);
							} catch (RowsExceededException e) {
								e.printStackTrace();
							} catch (WriteException e) {
								e.printStackTrace();
							}

						}
					}
					try {
						// 从内存中写入文件中
						wwb.write();
						// 关闭资源，释放内存
						wwb.close();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}
				}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public Object[] excelColumnNameByType(){
		Object[] obj=null;
			obj=new Object[6];
			obj[0]="序号";
			obj[1]="邮箱地址";
			obj[2]="姓名";
			obj[3]="公司名称";
			obj[4]="身份";
			obj[5]="提交时间";
		return obj;
	}
	
	
	public String typeToName(Integer id){
		if(Const.ID_TYPE_kaifa.getType()==id){
			return Const.ID_TYPE_kaifa.getName();
		}else if(Const.ID_TYPE_touzi.getType()==id){
			return Const.ID_TYPE_touzi.getName();
		}else if(Const.ID_TYPE_meiti.getType()==id){
			return Const.ID_TYPE_meiti.getName();
		}else if(Const.ID_TYPE_qita.getType()==id){
			return Const.ID_TYPE_qita.getName();
		}else{
			return Const.ID_TYPE_qita.getName();
		}
	}
	
}
