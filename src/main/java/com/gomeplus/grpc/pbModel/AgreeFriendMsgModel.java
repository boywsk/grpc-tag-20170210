package com.gomeplus.grpc.pbModel;

import java.io.Serializable;

public class AgreeFriendMsgModel implements Serializable {
	/**
	 * 是否同意对方加为好友
	 */
	private static final long serialVersionUID = 1L;
	protected long fromUid; // 审批用户ID
	protected String fromName; // 审批用户昵称
	protected long toUid; // 申请用户ID
	protected int agreeType; // 审批结果；0:为拒绝，1:为同意
	protected String content; // 消息内容
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

	public long getToUid() {
		return toUid;
	}

	public void setToUid(long toUid) {
		this.toUid = toUid;
	}

	public int getAgreeType() {
		return agreeType;
	}

	public void setAgreeType(int agreeType) {
		this.agreeType = agreeType;
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
