package com.gomeplus.grpc.pbModel;

import java.io.Serializable;

/**
 * 撤销消息通知消息
 */
public class IssueRevokeModel implements Serializable {

	private static final long serialVersionUID = 1L;
	protected long uid; // 用户id
	protected String nickName; // 用户昵称
	protected String groupId; // 群组Id
	protected String msgId; // 消息id
	protected long optTime;//操作时间
	protected String extra; // 扩展

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
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
