package com.gomeplus.grpc.pbModel;

import java.io.Serializable;

public class NoticeApplicantMsgModel implements Serializable {
	/**
	 * 通知被邀请加入群
	 */
	private static final long serialVersionUID = 1L;
	protected long fromUid; // 主动邀请用户ID
	protected String fromName; // 主动邀请用户昵称
	protected String content; // 内容
	protected String groupId; // 群组id
	protected long optTime; // 操作时间(毫秒)
	protected String extra; // 扩展

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
