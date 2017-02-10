package com.gomeplus.grpc.pbModel;

import java.io.Serializable;

public class DelFriendMsgModel implements Serializable {
	/**
	 * 删除好友
	 */
	private static final long serialVersionUID = 1L;
	protected long fromUid; // 申请用户ID
	protected long toUid; // 审批用户ID
	protected String content; // 内容
	protected long optTime; // 操作时间(毫秒)
	protected String extra; // 扩展

	public long getFromUid() {
		return fromUid;
	}

	public void setFromUid(long fromUid) {
		this.fromUid = fromUid;
	}

	public long getToUid() {
		return toUid;
	}

	public void setToUid(long toUid) {
		this.toUid = toUid;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
