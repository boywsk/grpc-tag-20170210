package com.gomeplus.grpc.model;

import java.io.Serializable;

/**
 * 群组退出成员  mongodb
 */
public class GroupQuitMember implements Serializable {

	private long userId; // 用户id
	private String groupId; // 群组id
	private long createTime;// 创建时间
	private long updateTime;//
	public GroupQuitMember() {
	}

	public GroupQuitMember(long userId, String groupId) {
		this.userId = userId;
		this.groupId = groupId;
	}
	public GroupQuitMember(long userId, String groupId, long createTime,
			long updateTime) {
		super();
		this.userId = userId;
		this.groupId = groupId;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}


	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
