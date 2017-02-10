package com.gomeplus.grpc.global;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gomeplus.grpc.utils.PropertiesUtils;

/**
 * 存放文件配置信息或者全局变量配置信息
 */
public class RpcGlobal {
	private static Logger log = LoggerFactory.getLogger(RpcGlobal.class);

	public final static int TWO_MINUTES = 2 * 60 * 1000; //客户端汇报时间 2分钟

    //zookeeper
    public static String ZK_IP_PORT;
    public static String ZK_PATH;
	public static String RPC_PACKAGE;
	public static double RPC_WEIGHT;
	public static String DISPACHER_ZK_PATH;

	public static final String RPC_WEIGHT_REDIS_KEY = "_RPC_WEIGHT_REDIS_KEY";

	//mongondb库名
	public static String MONGODB_DBNAME="db_im";
	public static String GLOBAL_CONF_FILE_NAME = "config.properties";
	static {
		try{
			Properties config = PropertiesUtils.LoadProperties(GLOBAL_CONF_FILE_NAME);
			GLOBAL_CONF_FILE_NAME = config.getProperty("config-file");
			log.info("全局配置文件:"+GLOBAL_CONF_FILE_NAME);
			ZK_PATH = config.getProperty("zookeeper-path");
			log.info("服务集群资源zk 根节点:{}",ZK_PATH);

			RPC_PACKAGE = config.getProperty("rpc-package");
			String weight = config.getProperty("rpc-weight");
			RPC_WEIGHT = Double.parseDouble(weight.trim());
			DISPACHER_ZK_PATH = config.getProperty("dispacher-zk-path");

			Properties properties = PropertiesUtils.LoadProperties(GLOBAL_CONF_FILE_NAME);

			MONGODB_DBNAME = properties.getProperty("mongodb.dbName");
			ZK_IP_PORT = properties.getProperty("zookeeperAddress");


		}catch (Exception e){
			log.error("常量加载出现异常",e);
		}
	}

	/**
	 * 请求类型
	 */
	public static enum REQUEST_TYPE {

		REPORT(1),          // 汇报IM服务资源
		GET_RESOURCES(2),   // 获取IM服务资源

		RPC_REPORT(3),      //汇报RPC服务资源
		RPC_PULL(4);        //拉取RPC服务资源

		public int value;
		private REQUEST_TYPE(int value) {
			this.value = value;
		}
	}


	/**
	 * 汇报的服务器类型
	 */
	public static enum SERVER_TYPE {

		GATEWAY(1), // 接入
		LOGIC(2), // 逻辑
		API(3), // api
		FILE(4), // 文件
		ALL(5),  //全部
		RPC(6);  //RPC服务类型

		public int value;

		private SERVER_TYPE(int value) {
			this.value = value;
		}
	}

}
