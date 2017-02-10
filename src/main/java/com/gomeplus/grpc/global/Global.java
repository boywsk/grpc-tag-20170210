package com.gomeplus.grpc.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 存放文件配置信息或者全局变量配置信息
 */
public class Global {
	private static Logger log = LoggerFactory.getLogger(Global.class);

	public static long TOKEN_EXPIRE_TIME = 7*24*60*60*1000;
	
	public static String REDIS_LOCK_KEY = "lock"; 
	//
	public static String ALL_APP_KEY = "all_appId"; 
	public static final String TOKEN_SUFFIX = "_token";
	
	/** 用户不同端发送消息 */
	public static final String PLATFORM_SUFFIX = "_platform";
	
	public static final int TOKEN_EXPIRES_REDIS = 7*24*60*60;

	public static final String GROUP_INITSEQ_INCR_SUFFIX = "_inc";

	public static final String GROUP_MEMBERS_SUFFIX = "_members";
	
	public static final String GROUP_MEMBER_INITSEQ_SUFFIX = "_initSeqId";
	
	/** 已读消息seqId */
	public static final String GROUP_READ_SEQ_ID_INITSEQ_SUFFIX = "_readSeqId";
	
	/** 客户端接收到聊天消息最大seqId */
	public static final String GROUP_RECEIVE_SEQ_ID_SUFFIX = "_receiveSeqId";

	public static final String  PUSH_TOKEN_APNS= "_apns"; 
	
	public static final String  GLOBAL_MSG_SETTINGS= "_globalMsgSettings"; //消息免打扰和新消息提醒
	
	/**
	 * redis中的临时有修改的昵称用户IDs的集合的Key
	 */
	public static final String TEMP_CHANGE_NIKENAME_IDS="_temp_change_nickname_ids";
	
	/**
	 * redis中的临时有修改的昵称（用户ID-昵称）key值后缀
	 */
	public static final String TEMP_CHANGE_USERID_NICKNAME_SUFFIX="_change_nikename";
	
	public static final String TEMP_USERID_NICKNAME_SUFFIX="_nikename";

	public static final String APPKEY_SUFFIX="_appKey";
	
//	appId+"_"+uid+"_userGroups"
	
	/** 用户(ID)->群ID列表(Set)组相关的key */
	public static final String USER_GROUPS_KEY="_userGroups";

	public static final String DATABASE_PREFIX = "gomeplus_im_";
	

	public static int MSG_DB_MODULO = 64;
	public static int MSG_TABLE_MODULO = 2;

	//二维码配置
	public static int QRCODE_HEIGHT = 200; //默认
	public static int QRCODE_WIDTH = 200; //默认
	public static String QRCODE_URL_1 = "";
	public static String QRCODE_URL_2 = "";
	
	//分包后最大包长度
	public static final int MAX_PACK_SIZE = 32600;
	//分包后最小包长度
	public static final int MIN_PACK_SIZE = 20000;
	
}
