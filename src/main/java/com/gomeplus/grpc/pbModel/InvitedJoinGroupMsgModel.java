package com.gomeplus.grpc.pbModel;

import java.io.Serializable;
import java.util.List;

public class InvitedJoinGroupMsgModel implements Serializable {
	/**
	 * 邀请加入群，需要个人审核通知个人，不需要直接进去群
	 */
	private static final long serialVersionUID = 1L;
	protected long fromUid; // 主动邀请用户ID
	protected String fromName; // 主动邀请用户昵称
	protected List<Long> invitedUids; // 被邀请用户ID列表
	protected List<String> invitedNames; // 被邀请用户昵称列表
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

	public List<Long> getInvitedUids() {
		return invitedUids;
	}

	public void setInvitedUids(List<Long> invitedUids) {
		this.invitedUids = invitedUids;
	}

	public List<String> getInvitedNames() {
		return invitedNames;
	}

	public void setInvitedNames(List<String> invitedNames) {
		this.invitedNames = invitedNames;
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
