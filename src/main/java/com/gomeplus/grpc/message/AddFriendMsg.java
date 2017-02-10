package com.gomeplus.grpc.message;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddFriendMsg  implements Serializable{
	private static final Logger logger = LoggerFactory.getLogger(AddFriendMsg.class);
	
	/**
	 * 添加好友
	 */
	private static final long serialVersionUID = 1L;
	private transient int appId;
	protected long fromUid; 			// 申请用户ID
	private String fromName; 		// 申请用户昵称
	private long toUid; 			// 审批用户ID
	private String content; 		// 内容
	private long optTime; 			// 操作时间(毫秒)
	private String extra; 			// 扩展

	
	
	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public long getFromUid() {
		return fromUid;
	}

	public void setFromUid(long fromUid) {
		this.fromUid = fromUid;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public long getToUid() {
		return toUid;
	}

	public void setToUid(long toUid) {
		this.toUid = toUid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getOptTime() {
		return optTime;
	}

	public void setOptTime(long optTime) {
		this.optTime = optTime;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

}
