package com.gomeplus.grpc.model;

import java.io.Serializable;

/**
 *
 * 群组成员
 * Created by wangshikai on 2016/2/19.
 */
public class GroupMember implements Serializable {
    private String groupId;
    private  long userId;
    private String nickName;
    private int identity;//身份;0:普通成员,1:创建者,2:管理员
    private int isTop; //置顶  0:否  1:是 暂时不用
    private int isShield; //屏蔽群消息 0:否  1:是
    
    private int status;//0:未通过 1:通过 2:拒绝
    //private long initSeq;
    //private long readSeq;
    private String groupNickName; //群昵称
    private long createTime;//创建的时间
    private long joinTime;//加入某个群的时间
    private long updateTime;

    private long maxSeq; // 成员群seq
    private long initSeq;// 加入群时，当前群消息seq
    private long readSeq;// 读取到的群消息最大seq
    private long receiveSeqId;//客户端接收到聊天消息最大seqId
    private long stickies; // 置顶;0:否,1:是
    private int isMsgBlocked;// 是否是免打扰;0:否,1:是

    private int isCollectionGroup;	//是否收藏 0：否，1：是

    private long groupCollectTime;//群收藏时间
    private int isDel;
    
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getIsShield() {
        return isShield;
    }

    public void setIsShield(int isShield) {
        this.isShield = isShield;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

//    public long getInitSeq() {
//        return initSeq;
//    }
//
//    public void setInitSeq(long initSeq) {
//        this.initSeq = initSeq;
//    }

//    public long getReadSeq() {
//        return readSeq;
//    }
//
//    public void setReadSeq(long readSeq) {
//        this.readSeq = readSeq;
//    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

    public String getGroupNickName() {
		return groupNickName;
	}

	public void setGroupNickName(String groupNickName) {
		this.groupNickName = groupNickName;
	}

	public long getMaxSeq() {
        return maxSeq;
    }

    public void setMaxSeq(long maxSeq) {
        this.maxSeq = maxSeq;
    }

    public long getInitSeq() {
        return initSeq;
    }

    public void setInitSeq(long initSeq) {
        this.initSeq = initSeq;
    }

    public long getReadSeq() {
        return readSeq;
    }

    public void setReadSeq(long readSeq) {
        this.readSeq = readSeq;
    }

    public long getReceiveSeqId() {
        return receiveSeqId;
    }

    public void setReceiveSeqId(long receiveSeqId) {
        this.receiveSeqId = receiveSeqId;
    }

    public long getStickies() {
		return stickies;
	}

	public void setStickies(long stickies) {
		this.stickies = stickies;
	}

	public int getIsMsgBlocked() {
        return isMsgBlocked;
    }

    public void setIsMsgBlocked(int isMsgBlocked) {
        this.isMsgBlocked = isMsgBlocked;
    }

	public int getIsCollectionGroup() {
		return isCollectionGroup;
	}

	public void setIsCollectionGroup(int isCollectionGroup) {
		this.isCollectionGroup = isCollectionGroup;
	}

	public long getGroupCollectTime() {
		return groupCollectTime;
	}

	public void setGroupCollectTime(long groupCollectTime) {
		this.groupCollectTime = groupCollectTime;
	}
}
