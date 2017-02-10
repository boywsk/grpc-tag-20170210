package com.gomeplus.grpc.global;

/**
 * 常量、枚举等定义
 */
public class Constant {

	/**
	 * 群组容量
	 * 
	 */
	public static final int GROUP_CAPACITY = 200;
	
	/**
	 * 默认的好友分组ID
	 */
	public static final long DEFAULT_FRIEND_GROUP_ID=0L;
	
	public static final String STRING_NULL="NULL";

	/**
	 * 好友状态
	 * 0 未通过（未审核）， 1 通过状态， 2 解除好友关系
	 */
	public enum FRIEND_STATUS {
		E_FRIEND_STATUS_NOT(0),//未通过(未审核)
		E_FRIEND_STATUS_OK(1),//通过状态
		E_FRIEND_STATUS_DISBAND(2);//解除好友关系

		public int value;
		FRIEND_STATUS(int value) {
			this.value = value;
		}
	}
	
	// 服务器端返回包类型
	public enum PACK_ACK {
		NO((byte) 0), // 请求应打包
		YES((byte) 1); // 转发包

		public byte value;

		PACK_ACK(byte value) {
			this.value = value;
		}
	}
	// 报文超长时分包，分包是否是最后一个包
	public enum LAST_PACK {
		NO((byte) 0), // 否
		YES((byte) 1); // 是

		public byte value;

		LAST_PACK(byte value) {
			this.value = value;
		}
	}
	

	/**
	 * 用户与群的关系状态 0 未通过， 1 通过状态， 2删除状态
	 * TODO 群成员的状态在数据库中只有 0 和1 ，删除群成员为物理删除，但会保存到t_group_quit_member表
	 *	客户端在增量拉取群成员时，会到删除表中查询出删除的信息，并返回给客户端，以后改为逻辑删除更好
	 * 	liuzhenhuan 20160804
	 */
	
	public enum GROUP_STATUS {
		E_GROUP_STATUS_NOT(0),//未通过
		E_GROUP_STATUS_OK(1),//通过状态
		
		E_GROUP_STATUS_DEL(2);//删除状态
		public int value;
		GROUP_STATUS(int value) {
			this.value = value;
		}
	}

	/**
	 * 用户在群中是否置顶
	 * 0 未置顶， 1 置顶 
	 */
	public enum GROUP_STICKIE {
		E_GROUP_STICKIE_NOT(0),//未置顶
		E_GROUP_STICKIE_OK(1);//置顶

		public int value;
		GROUP_STICKIE(int value) {
			this.value = value;
		}
	}

	/**
	 * 用户是否屏蔽群消息
	 * 0 未屏蔽， 1 屏蔽
	 */
	public enum GROUP_SHIELD {
		E_GROUP_SHIELD_NOT(0),//未屏蔽
		E_GROUP_SHIELD_OK(1);//屏蔽

		public int value;
		GROUP_SHIELD(int value) {
			this.value = value;
		}
	}

	/**
	 * 用户加入群是否需要审核
	 * 0 入群不需要审核， 1 入群需要审核
	 */
	public enum GROUP_AUDIT {
		E_GROUP_AUDIT_NOT(0),//不需要审核
		E_GROUP_AUDIT_NEED(1);//需要审核

		public int value;
		GROUP_AUDIT(int value) {
			this.value = value;
		}
	}

	/**
	 * 群的删除状态
	 * 0:未删除， 1:已删除
	 */
	public enum GROUP_DEL {
		E_GROUP_DEL_NOT(0),//未删除
		E_GROUP_DEL_OK(1);//删除

		public int value;
		GROUP_DEL(int value) {
			this.value = value;
		}
	}

	/**
	 * 群成员身份
	 */
	public enum GROUP_MEMBER_IDENTITY {
		E_MEMBER_ORDINARY(0), // 普通成员
		E_MEMBER_CREATOR(1), // 创建者
		E_MEMBER_MANAGER(2); // 管理员

		public int value;
		GROUP_MEMBER_IDENTITY(int value) {
			this.value = value;
		}
	}
	/**
	 * 群成员是否置顶 0-否，1-是
	 */
	public enum GROUP_MEMBER_TOP {
		E_MEMBER_TOP_NO(0), //否
		E_MEMBER_TOP_YES(1); // 是
		
		public int value;
		GROUP_MEMBER_TOP(int value) {
			this.value = value;
		}
	}
	/**
	 * 群成员-是否屏蔽群消息：0-否，1-是
	 */
	public enum GROUP_MEMBER_SHIELD {
		E_MEMBER_SHIELD_NO(0), //否
		E_MEMBER_SHIELD_YES(1); // 是
		
		public int value;
		GROUP_MEMBER_SHIELD(int value) {
			this.value = value;
		}
	}
	
	/**
	 * 群成员-状态  0:未通过 1:通过 2:拒绝
	 */
/*	public enum GROUP_MEMBER_STATUS {
		E_MEMBER_STATUS_NO(0), //未通过
		E_MEMBER_STATUS_YES(1);//通过
		public int value;
		GROUP_MEMBER_STATUS(int value) {
			this.value = value;
		}
	}*/
	

	/**
	 * 群组类型 1 单聊 ，2 群聊 ，3 系统信息， 4 小秘书
	 */
	public enum CHAT_TYPE {
		E_CHAT_TYP_SINGLE(1), // 单聊
		E_CHAT_TYP_GROUP(2), // 群聊
		E_CHAT_TYP_SYS(3), // 系统信息
		E_CHAT_TYP_HELPER(4), // 小秘书

		E_CHAT_TYP_GLOBAL_SHIELD(101),//全局消息免打扰
		E_CHAT_TYP_REMIND_MSG(102);//新消息显示
		public int value;
		CHAT_TYPE(int value) {
			this.value = value;
		}
	}

	// 消息类型
	public enum MESSAGE_TYPE {
		E_MESSAG_TYPE_TEXT(1), // 文本
		E_MESSAG_TYPE_VOICE(2), // 语音
		E_MESSAG_TYPE_IMG(3), // 图片
		E_MESSAG_TYPE_VIDEO(4), // 视频
		E_MESSAG_TYPE_POSITION(5), // 位置
		E_MESSAG_TYPE_ATTACH(6), // 附件
		E_MESSAG_TYPE_CARD(7), // 名片
		E_MESSAG_TYPE_SYS(8), // 系统消息
		// E_MESSAG_TYPE_NOTICE(9), // 通知消息
		E_MESSAG_TYPE_SHARE(9), // 分享/转发(通过url)
		E_MESSAG_TYPE_GOODS(10), // 商品
		E_MESSAG_TYPE_SHOP(11), // 商店
		E_MESSAG_TYPE_GROUP_OPT(12); // 群组操作通知

		public int value;
		MESSAGE_TYPE(int value) {
			this.value = value;
		}
	}

	// 群组操作类型
	// 1:加入群，2:退群，3:修改群，4:解散群
	public enum GROUP_OPT_TYPE {
		E_OPT_TYPE_JOIN(1), // 加入群
		E_OPT_TYPE_QUIT(2), // 退群
		E_OPT_TYPE_EDIT(3), // 修改群
		E_OPT_TYPE_DISB(4); // 解散群

		public int value;
		GROUP_OPT_TYPE(int value) {
			this.value = value;
		}
	}

//	// 群组加入类型
//	public enum GROUP_JOIN_TYPE {
//		E_JOIN_TYPE_INVITE(1), // 邀请加入群
//		E_JOIN_TYPE_QR(2), // 扫二维码加入群
//		E_JOIN_TYPE_SEARCH(3); // 通过群名/号加入群
//
//		public int value;
//		GROUP_JOIN_TYPE(int value) {
//			this.value = value;
//		}
//	}
	// 群组加入类型
	public enum GROUP_JOIN_TYPE {
		E_JOIN_TYPE_COMMON(1), // 普通加群
		E_JOIN_TYPE_QR(2); // 扫二维码加入群

		public int value;
		GROUP_JOIN_TYPE(int value) {
			this.value = value;
		}
	}

	/**
	 * 文件类型 1 图片 2 音频 3 视频
	 */
	public enum FILE_TYPE {
		IMAGE(1), // 图片
		VOICE(2), // 音频
		VIDEO(3);// 视频
		
		public int value;
		FILE_TYPE(int value) {
			this.value = value;
		}
	}

	/**
	 * 文件格式 (后缀扩展名)
	 */
	public enum FILE_SUFFIX {
		JPG("jpg"), PNG("png"), GIF("gif"), AMR("amr"), AVI("avi");

		public String value;
		FILE_SUFFIX(String value) {
			this.value = value;
		}
	}

	// 群消息接收类型
	public enum GROUP_MESG_RECEIVE_TYPE {
		E_MESG_RECEIVE_TYPE_ALL(1), // 全部
		E_MESG_RECEIVE_TYPE_MANAGER(2); // 管理员

		public int value;
		GROUP_MESG_RECEIVE_TYPE(int value) {
			this.value = value;
		}
	}

	// API发送群消息类型 //消息类型；1:文本、2:语音、3:图片、4:视频、5:位置、6:附件、7:名片、8:系统消息、
	//9:分享/转发(通过url)、10:商品、11:店铺、99:消息透传
	public enum GROUP_MSG_TYPE {
		TEXT(1), // 文本
		VOICE(2), // 语音
		IMAGE(3), // 图片
		VIDEO(4), // 视频
		LOCATION(5), // 位置
		ATTACHMENT(6), // 附件
		CARD(7), // 名片
		SYSTEM(8), // 系统消息
		SHARE(9), // 分享/转发(通过url)
		GOODS(10), // 商品
		SHOP(11), // 店铺
		TRANSIMIT(99); // 消息透传
		public int value;
		GROUP_MSG_TYPE(int value) {
			this.value = value;
		}
	}

	public  enum MSG_TASK_TYPE {
		APPLY_ADD_FRIEND(100),//申请添加好友
		DEL_FRIEND(101),//删除好友
		AUDIT_FRIEND_RESULT(102),//好友审核结果 同意或拒绝
		JOIN_GROUP(200),//申请加入群
		AUDIT_GORUP_NOTIFY(201),//通知管理员审核加入成员
		INVITE_GROUP(202),//邀请加入群
		INVITED_GROUP(203),//被邀请加入群
		QUIT_GROUP(204),//退/踢出群
		UPDATE_GROUP_INFO(205),//修改群信息
		DISBAND_GROUP(206),
		CHANGE_GROUP_MANANGER(207),//变更群主
		USER_INFO_MODIFY(208),//用户消息修改
		SCAN_RQ_JOIN_GROUP(210),//扫描加入群
		//TODO 暂时使用 , 之后修改 liuzhenhuan 20160701
		SYSTEM_MSG(307),  //系统消息
		BOARDCAST_MSG(2308), //广播消息
		;//解散群
		public int value;
		MSG_TASK_TYPE(int value) {
			this.value = value;
		}
	}
	public enum GROUP_MEMEBER_QUIR_TYPE {
		KICK(1),//踢群
		QUIT(2);//退群
		public int value;
		GROUP_MEMEBER_QUIR_TYPE(int value) {
			this.value = value;
		}
	}
	
	public enum SYSTEM_MSG_TYPE {//消息推送
		COMMON_SYSTEM(1),//普通系统消息
		ALL_BOARDCAST(2);//全量广播消息
		public int value;
		SYSTEM_MSG_TYPE(int value) {
			this.value = value;
		}
	}
	public enum GLOBAL_SHIELD {//是否开启全局消息免打扰
		SHIELD_YES(1),//是
		SHIELD_NOT(2);//否
		public int value;
		GLOBAL_SHIELD(int value) {
			this.value = value;
		}
	}
	public enum REMIND_MSG {//是否开启新消息提醒
		REMIND_YES(1),//是
		REMIND_NOT(2);//否
		public int value;
		REMIND_MSG(int value) {
			this.value = value;
		}
	}
	public enum GROUP_COLLECTION {//是否设置群收藏
		COLLECTION_NOT(0),//否
		COLLECTION_YES(1);//是
		public int value;
		GROUP_COLLECTION(int value) {
			this.value = value;
		}
	}
	public enum GROUP_COLLECTION_ISDEL {//是否设置群收藏
		COLLECTION_DEL_NO(0),//否
		COLLECTION_DEL_YES(1);//是
		public int value;
		GROUP_COLLECTION_ISDEL(int value) {
			this.value = value;
		}
	}
	
	
}
