package com.gomeplus.grpc.model;

import java.io.Serializable;

/**
 *@Description: 群收藏
 *@author liuzhenhuan
 *@date 2016年12月8日 上午11:00:59
 * 
 */
public class GroupCollection implements Serializable {
	private String groupId;
	
	private long userId;
	
	
	private long updateTime;
	
	private int isDel;//是否删除，0：否，1：是
	
	public GroupCollection() {
	}

	public GroupCollection(String groupId, long userId, long updateTime, int isDel) {
		super();
		this.groupId = groupId;
		this.userId = userId;
		this.updateTime = updateTime;
		this.isDel = isDel;
	}


	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}


	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}


	public int getIsDel() {
		return isDel;
	}


	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	
}
