package com.service.impl;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.service.ExcelService;
import com.util.ReadExcelUtils;

@Service
public class ExcelServiceImpl implements ExcelService{
    final long awaitTime = 5 * 1000;//超时关闭线程时间  
    
	final static String filepathMaps = "E:\\svn\\excel\\MSP.xlsx"; 
	//msp 数据
	private static Map<Integer, Map<Integer,Object>> maps=new HashMap<Integer, Map<Integer,Object>>();
	final static String filepathStsi = "E:\\svn\\excel\\stsi.xlsx";
	//stsi//数据
	private static Map<Integer, Map<Integer,Object>> stsi=new HashMap<Integer, Map<Integer,Object>>();
	final static String filepath1K = "E:\\svn\\excel\\1k.xlsx";
	//1k 数据
	private static Map<Integer, Map<Integer,Object>> k=new HashMap<Integer, Map<Integer,Object>>();
	
	private static LinkedBlockingQueue<Gen> linkBlackMap=null;
	private static Map<String,Gen> tempEnd=null;
	
	//线程数
	private final static Integer maxPool=1;//Runtime.getRuntime().availableProcessors();
	//定义线程池
	private  static ExecutorService cacheThreadPool=null;
	
	@Override
	public void readExcel(String key) {
		init();
		long beginTime=System.currentTimeMillis();
		//模板总数
		int size=maps.size();
		Map<Integer,Object> mapKey=null;
		for (int i = 1; i <=size; i++) {
			Map<Integer,Object> temp1=maps.get(i);
			if(temp1!=null){
				if(key.equals(temp1.get(0).toString())){
					mapKey=temp1;
					break;
				}
			}
		}
		//char
		//begin
		//end
		String chars=mapKey.get(2).toString().split("chr")[1];
		long begin=Long.valueOf(mapKey.get(3).toString());
		long end=Long.valueOf(mapKey.get(4).toString());
		
		
	
		size=stsi.size();//1
		for (int i = 1; i <= size; i++) {
			Map<Integer,Object> temp1=stsi.get(i);
			if(temp1!=null){
				Gen gen=new Gen();
				if(temp1.get(0).toString().equals(chars)){
					long temKey=Long.valueOf(temp1.get(1).toString());
					if(temKey>begin && temKey<end){
						gen.setGenName(key);
						gen.setChr(mapKey.get(2).toString());
						gen.setGenCounts(temKey);
						linkBlackMap.add(gen);
					}
				}
			}
		}
		size=k.size();//2
		for (int i = 1; i <= size; i++) {
			Map<Integer,Object> temp1=k.get(i);
			if(temp1!=null){
				Gen gen=new Gen();
				if(temp1.get(0).toString().equals(chars)){
					long temKey=Long.valueOf(temp1.get(1).toString());
					if(temKey>begin && temKey<end){
						gen.setGenName(key);
						gen.setChr(mapKey.get(2).toString());
						gen.setGenCounts(temKey);
						String len=gen.getChr()+temKey;
						tempEnd.put(len, gen);
					}
				}
			}
		}
		//线程池
		executorService();
		try {  
			cacheThreadPool.shutdown();  
	   
	        if(!cacheThreadPool.awaitTermination(awaitTime, TimeUnit.MILLISECONDS)){  
	            // 超时的时候向线程池中所有的线程发出中断(interrupted)。  
	        	cacheThreadPool.shutdownNow();  
	        }  
	    } catch (InterruptedException e) {  
	        // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。  
	        System.out.println("awaitTermination interrupted: " + e);  
	        cacheThreadPool.shutdownNow();  
	    }  
		
//		System.out.println("结束");
		long endTime=System.currentTimeMillis();
		System.out.println("计算:=="+(endTime-beginTime));
	}
	
	public void executorService(){
		Iterator<Gen> it=linkBlackMap.iterator();
		while (it.hasNext()) {
			final Gen gen=it.next();
			cacheThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					String key=gen.getChr()+gen.getGenCounts();
					if(tempEnd.containsKey(key)){
						System.out.println("tempEnd="+key);
						System.out.println("线程"+Thread.currentThread().getId()+"存在变异基因》》》基因名称："+gen.getGenName()+"--染色体："+gen.getChr()+"--值："+gen.getGenCounts());
					}
				}
			});
		}
	}
	
	class Gen{
		private String genName;
		private String chr;
		private Long  genCounts;
		public String getGenName() {
			return genName;
		}
		public void setGenName(String genName) {
			this.genName = genName;
		}
		
		public String getChr() {
			return chr;
		}
		public void setChr(String chr) {
			this.chr = chr;
		}
		public Long getGenCounts() {
			return genCounts;
		}
		public void setGenCounts(Long genCounts) {
			this.genCounts = genCounts;
		}
		
	}
	public static void init(){
		 try {  
			 if(maps.size()==0){
	            ReadExcelUtils excel1 = new ReadExcelUtils(filepathMaps);  
	            // 对读取Excel表格内容测试  
	            maps= excel1.readExcelContent();
			 }
			 if(stsi.size()==0){
				 ReadExcelUtils excel2 = new ReadExcelUtils(filepathStsi);  
		            // 对读取Excel表格内容测试  
		            stsi= excel2.readExcelContent();
			 }
	         if(k.size()==0){   
	            ReadExcelUtils excel3 = new ReadExcelUtils(filepath1K);  
	            // 对读取Excel表格内容测试  
	            k= excel3.readExcelContent();
	         }
	        } catch (FileNotFoundException e) {  
	            System.out.println("未找到指定路径的文件!");  
	            e.printStackTrace();  
	        }catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		 
		 linkBlackMap=new  LinkedBlockingQueue<>(); //缓存1k
		 tempEnd=new HashMap<>(); //缓存2excel
		 cacheThreadPool= Executors.newFixedThreadPool(maxPool);//初始化线程数
	}
}
