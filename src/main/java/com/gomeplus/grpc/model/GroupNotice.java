package com.gomeplus.grpc.model;

import java.io.Serializable;

/**
 * 群公告
 * @author Administrator
 *
 */
public class GroupNotice implements Serializable{
	
	private String groupId;
	
	/**群公告内容*/
	private String noticeContent;
	/**创建者ID*/
	private long userId;
	
	private long createTime;
	
	private long updateTime;
	public GroupNotice() {
	}
	public GroupNotice(String groupId, String noticeContent, long userId,
			long createTime, long updateTime) {
		super();
		this.groupId = groupId;
		this.noticeContent = noticeContent;
		this.userId = userId;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getNoticeContent() {
		return noticeContent;
	}
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	

}
