package com.gomeplus.grpc.pbModel;

import java.io.Serializable;
import java.util.List;

public class QuitGroupMsgModel implements Serializable {
	/**
	 * 退/踢出群
	 */
	private static final long serialVersionUID = 1L;
	protected int quitType; // 加入类型，1:为被踢，2:为主动退群
	protected long fromUid; // 主动用户ID
	protected String fromName; // 主动退用户昵称
	protected List<Long> kickedUids; // 被退/踢出群用户ID列表
	protected List<String> kickedNames; // 被退/踢出群用户昵称列表
	protected String content; // 内容
	protected String groupId; // 群组id
	protected long optTime; // 操作时间(毫秒)
	protected String extra; // 扩展

	public int getQuitType() {
		return quitType;
	}

	public void setQuitType(int quitType) {
		this.quitType = quitType;
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

	public List<Long> getKickedUids() {
		return kickedUids;
	}

	public void setKickedUids(List<Long> kickedUids) {
		this.kickedUids = kickedUids;
	}

	public List<String> getKickedNames() {
		return kickedNames;
	}

	public void setKickedNames(List<String> kickedNames) {
		this.kickedNames = kickedNames;
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
