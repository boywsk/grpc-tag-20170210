package com.gomeplus.grpc.pbModel;

import java.io.Serializable;

public class AddFriendMsgModel implements Serializable {
	/**
	 * 添加好友
	 */
	private static final long serialVersionUID = 1L;
	protected long fromUid; 			// 申请用户ID
	protected String fromName; 		// 申请用户昵称
	protected long toUid; 			// 审批用户ID
	protected String content; 		// 内容
	protected long optTime; 			// 操作时间(毫秒)
	protected String extra; 			// 扩展

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
