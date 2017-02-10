package com.gomeplus.grpc.model;


import java.io.Serializable;
import java.util.List;

import com.gomeplus.grpc.message.GroupMsgModel;


/**
 *
 * 群组
 * Created by wangshikai on 2016/2/19.
 */
public class Group implements Serializable {
    private String groupId;
    private long userId; //群主id
    private int type; //群组类型;1:单聊,2:群聊
    private String groupName;
    private String groupDesc;//群描述
    private String avatar;    //头像
    private String qRcode;     //二维码
    private int capacity;//容量
    private int isAudit;     //是否需要审核
    private int isDele;
    private long createTime;
    private long updateTime;
    private long seq; // 递增id
    
    /** 群公告 */
    private String subject;

    private List<GroupMsgModel> msgs; // 最新20条消息
    
    /** OA-服务器的扩展信息*/
    private String extraInfo;
    
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

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getqRcode() {
        return qRcode;
    }

    public void setqRcode(String qRcode) {
        this.qRcode = qRcode;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(int isAudit) {
        this.isAudit = isAudit;
    }

    public int getIsDele() {
        return isDele;
    }

    public void setIsDele(int isDele) {
        this.isDele = isDele;
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

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public List<GroupMsgModel> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<GroupMsgModel> msgs) {
        this.msgs = msgs;
    }

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
}
