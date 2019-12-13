package com.util;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperUtils {
	
	private Logger logger=Logger.getLogger(ZookeeperUtils.class);
	
	
	private  Watcher watcher=new Watcher(){
		@Override
		public void process(WatchedEvent event){
			logger.debug("-----------zk watcher-----------");
		}
	};
	
	
	private ZooKeeper zooKeeper;
	
	public void zookeeperInit() throws IOException {
		//多个ip使用，号分割
		zooKeeper = new ZooKeeper("192.168.230.130:2181", 20000 , watcher);
	} 

	public void ZKOption() throws Exception, InterruptedException {
		logger.debug("-----ZK Option-----");
		//参数一：创建的路径/zoo1          参数二：节点上的数据       参数三：权限    参数四：节点类型
		zooKeeper.create("/zoo1", "zoo1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		//添加watcher ，对zoo1进行监控变化-->获取数据 ,这个监控只会对下次操作节点起到作用
		zooKeeper.getData("/zoo1", this.watcher, null);
		//修改zoo1节点
		zooKeeper.setData("/zoo1", "hello".getBytes(), -1);
		//----------------------获取数据----------------------------
		logger.debug("获取第一次节点信息："+zooKeeper.getData("/zoo1", this.watcher, null));
		System.out.println("获取第二次数据信息："+zooKeeper.getData("/zoo1", this.watcher, null));
		//-----------------这次操作的节点不会进行监控-----------------------
		zooKeeper.setData("/zoo1", "world".getBytes(), -1);
		//----------------------获取数据----------------------------
		logger.debug("获取第二次数据信息："+zooKeeper.getData("/zoo1", this.watcher, null));
		System.out.println("获取第二次数据信息："+zooKeeper.getData("/zoo1", this.watcher, null));
		//查看节点状态
		logger.debug("节点状态："+zooKeeper.getState());
		System.out.println("节点状态："+zooKeeper.getState());
		//删除节点
		zooKeeper.delete("/zoo1", -1);
		System.out.println(zooKeeper.exists("/zoo1", false));
	}
	
	//关闭Zookeeper
	public void closeZookeeper() throws Exception {
		zooKeeper.close();
	}
	
	
	public static void main(String[] args) throws Exception {
		ZookeeperUtils utils = new ZookeeperUtils();
		utils.zookeeperInit();
		utils.ZKOption();
		utils.closeZookeeper();
	}

	
	
	
	
}
