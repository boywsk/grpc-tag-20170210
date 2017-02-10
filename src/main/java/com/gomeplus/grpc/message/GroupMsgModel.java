package com.gomeplus.grpc.message;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 聊天消息
 */
public class GroupMsgModel implements Serializable{
	private static final long serialVersionUID = 1L;

	
	private String groupId; // 群组id
	private int groupType; // 群组类型，1:单聊，2:群聊
	private String groupName; // 群组名称
	private String msgId; // 消息id
	private int msgType; // 1:文本、2:语音、3:图片、4:附件、5:分享/转发(通过url)、...
	private String msgBody; // 消息体
	private long fromUid; // 发送者id
	private String fromName; // 发送者名称
	private String fromRemark;// 消息发送者在该群中的昵称
	private long toUid;// 接收者id
	private long msgSeqId; // 自增计数
	private String msgUrl;// 消息url
	private List<Attachment> msgAttch; // 附件列表
	private MsgLocation msgLocation; // 转发和分享链接URL
	private List<Long> atUids;// 被@的用户id列表
	private long sendTime; // 发送服务器时间
	private List<Long> unReadUids; //未读用户id 暂时存储idList中
	private List<Long> readUids;//已读用户id
	private int isRevoke;//是否撤销；0:否，1:是
	private String extra; // 扩展信息
	private int origiImg;//消息类型为图片时，是否有原图；0:否，1:是
	
	private boolean isPushBlock;// 是否 apns push
	private AtMsg atMsg;//im@消息
//	transient private int appId;
//	transient private int systemMsgType;// 消息类型 群量广播 1-普通系统消息 2-全量广播消息
//	transient private boolean isPersist;// 是否持久化
//	transient private boolean containSelf;// 消息是否包含自己


	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
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

	public String getFromRemark() {
		return fromRemark;
	}

	public void setFromRemark(String fromRemark) {
		this.fromRemark = fromRemark;
	}

	public long getToUid() {
		return toUid;
	}

	public void setToUid(long toUid) {
		this.toUid = toUid;
	}

	public long getMsgSeqId() {
		return msgSeqId;
	}

	public void setMsgSeqId(long msgSeqId) {
		this.msgSeqId = msgSeqId;
	}

	public String getMsgUrl() {
		return msgUrl;
	}

	public void setMsgUrl(String msgUrl) {
		this.msgUrl = msgUrl;
	}

	public List<Attachment> getMsgAttch() {
		return msgAttch;
	}

	public void setMsgAttch(List<Attachment> msgAttch) {
		this.msgAttch = msgAttch;
	}

	public MsgLocation getMsgLocation() {
		return msgLocation;
	}

	public void setMsgLocation(MsgLocation msgLocation) {
		this.msgLocation = msgLocation;
	}

	public List<Long> getAtUids() {
		return atUids;
	}

	public void setAtUids(List<Long> atUids) {
		this.atUids = atUids;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public List<Long> getUnReadUids() {
		return unReadUids;
	}

	public void setUnReadUids(List<Long> unReadUids) {
		this.unReadUids = unReadUids;
	}

	public List<Long> getReadUids() {
		return readUids;
	}

	public void setReadUids(List<Long> readUids) {
		this.readUids = readUids;
	}

	public int getIsRevoke() {
		return isRevoke;
	}

	public void setIsRevoke(int isRevoke) {
		this.isRevoke = isRevoke;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	
	
	public boolean isPushBlock() {
		return isPushBlock;
	}

	public void setPushBlock(boolean isPushBlock) {
		this.isPushBlock = isPushBlock;
	}


	public String toString() {
		return JSON.toJSONString(this);
	}

	public AtMsg getAtMsg() {
		return atMsg;
	}

	public void setAtMsg(AtMsg atMsg) {
		this.atMsg = atMsg;
	}

	public int getOrigiImg() {
		return origiImg;
	}

	public void setOrigiImg(int origiImg) {
		this.origiImg = origiImg;
	}

}
