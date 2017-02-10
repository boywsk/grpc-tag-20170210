package com.gomeplus.grpc.message;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 系统消息，广播消息
 */
public class BoardcastMsg implements Serializable,Runnable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(BoardcastMsg.class);
	
	private String msgId;	//消息id
	private int msgType;	//消息类型；1:文本、2:语音、3:图片、4:视频、5:位置、6:附件、7:名片、8:系统消息、
							//9:分享/转发(通过url)、10:商品、11:店铺、12:群组操作、99:消息透传、100:全量广播
	private boolean isMsgBlock;//是否免打扰
	private boolean isPersist;//是否持久化
	private List<Long> idList;//
	private String msgBody;	// 消息体
	private String extra;	//扩展信息
	
	private long senderId;	// 发送者id
	private int senderType; //发送者类型；0:普通、1:系统/小秘书等；2:咨询者
	private String senderName;	// 发送者名称
	private String senderRemark;// 消息发送者在该群中的昵称	

	private long sponserId; //发起者id	
	private long sendTime;//发送时间(由服务器端生成)
	
	transient private int appId;
	
	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
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

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderRemark() {
		return senderRemark;
	}

	public void setSenderRemark(String senderRemark) {
		this.senderRemark = senderRemark;
	}


	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
	
	public String toString() {
		return JSON.toJSONString(this);
	}


	public long getSponserId() {
		return sponserId;
	}

	public void setSponserId(long sponserId) {
		this.sponserId = sponserId;
	}

	public int getSenderType() {
		return senderType;
	}

	public void setSenderType(int senderType) {
		this.senderType = senderType;
	}

	public boolean isMsgBlock() {
		return isMsgBlock;
	}

	public void setMsgBlock(boolean isMsgBlock) {
		this.isMsgBlock = isMsgBlock;
	}

	public boolean isPersist() {
		return isPersist;
	}

	public void setPersist(boolean isPersist) {
		this.isPersist = isPersist;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

	@Override
	public void run() {
		try {
//			NoticeMsg noticeMsg = new NoticeMsg();
//			noticeMsg.setMsgId(com.gomeplus.im.api.utils.StringUtils.getUuid());
//			noticeMsg.setNoticeType(Constant.MSG_TASK_TYPE.BOARDCAST_MSG.value);
//			noticeMsg.setAppId(appId);
//			noticeMsg.setCmd(Command.CMD_BROADCAST_MSG);
//			noticeMsg.setBoardcastMsg(this);
//			String msgJson = JSON.toJSONString(noticeMsg);
//			MQSender.getInstance().sendMsg(msgJson);
		} catch (Exception e) {
			logger.error("BoardcastMsg error:",e);
		}
	}
}
