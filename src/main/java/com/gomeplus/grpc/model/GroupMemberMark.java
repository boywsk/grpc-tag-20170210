package com.gomeplus.grpc.model;

import java.io.Serializable;

/**
 *
 * 群组成员备注
 * Created by wangshikai on 2016/2/19.
 */
public class GroupMemberMark implements Serializable {
    private String groupId;
	private long userId;
    private long markedUserId;  //备注的用户id
    private String mark;        //备注用户名
    private long createTime;
    private long updateTime;

    
    public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
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

    public long getMarkedUserId() {
        return markedUserId;
    }

    public void setMarkedUserId(long markedUserId) {
        this.markedUserId = markedUserId;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
