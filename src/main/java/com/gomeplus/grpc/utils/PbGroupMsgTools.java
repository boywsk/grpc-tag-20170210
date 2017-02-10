package com.gomeplus.grpc.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gomeplus.grpc.message.AtMsg;
import com.gomeplus.grpc.message.Attachment;
import com.gomeplus.grpc.message.GroupMsgModel;
import com.gomeplus.grpc.message.MsgLocation;
import com.gomeplus.grpc.model.Group;
import com.gomeplus.grpc.model.GroupMember;
import com.gomeplus.grpc.model.ProtoIM.ImAtMsg;
import com.gomeplus.grpc.model.ProtoIM.ImGroupMsg;
import com.gomeplus.grpc.model.ProtoIM.ImMsg;
import com.gomeplus.grpc.model.ProtoIM.ImMsgAttach;
import com.gomeplus.grpc.model.ProtoIM.ImMsgLocation;
import com.google.common.base.Strings;

/**
 * 群组消息对象和pb对象互转工具
 */
public class PbGroupMsgTools {
	private final static Logger log = LoggerFactory.getLogger(PbGroupMsgTools.class);
	
	/**
	 * 聊天消息转pb格式
	 * @param msg
	 * @return
	 */
	public static ImMsg groupMsg2PbImMsg(GroupMsgModel msg) {
		ImMsg.Builder pbImMsg = ImMsg.newBuilder();
		if(msg == null) {
			return pbImMsg.build();
		}
		String groupId = msg.getGroupId();
		if(!Strings.isNullOrEmpty(groupId)) {
			pbImMsg.setGroupId(groupId);
		}
		int groupType = msg.getGroupType();
		if(groupType > 0) {
			pbImMsg.setGroupType(msg.getGroupType());
		}
		String groupName = msg.getGroupName();
		if(!Strings.isNullOrEmpty(groupName)) {
			pbImMsg.setGroupName(groupName);
		}
		pbImMsg.setMsgId(msg.getMsgId());
		if(msg.getMsgType() > 0) {
			pbImMsg.setMsgType(msg.getMsgType());
		}
		String msgBody = msg.getMsgBody();
		if(!Strings.isNullOrEmpty(msgBody)) {
			pbImMsg.setMsgBody(msgBody);
		}
		pbImMsg.setFromUid(msg.getFromUid());
		String framName = msg.getFromName();
		if(!Strings.isNullOrEmpty(framName)) {
			pbImMsg.setFromName(framName);
		}
		String fromRemark = msg.getFromRemark();
		if(!Strings.isNullOrEmpty(fromRemark)) {
			pbImMsg.setFromRemark(fromRemark);
		}
		long sendTime = msg.getSendTime();
		if(sendTime > 0) {
			pbImMsg.setSendTime(sendTime);
		}
		pbImMsg.setMsgSeqId(msg.getMsgSeqId());
		String msgUrl = msg.getMsgUrl();
		if(!Strings.isNullOrEmpty(msgUrl)) {
			pbImMsg.setMsgUrl(msgUrl);
		}
		List<Attachment> attachs = msg.getMsgAttch();
		if(attachs != null) {
			for(Attachment attach : attachs) {
				ImMsgAttach pbAttach = imMsgAttach2PbAttach(attach);
				pbImMsg.addAttch(pbAttach);
			}
		}
		MsgLocation location = msg.getMsgLocation();
		if(location != null) {
			ImMsgLocation pbLocaltion =  PbMsgTools.location2PbLocation(location);
			if (pbLocaltion!=null) {
				pbImMsg.setLocation(pbLocaltion);
			}
		}
		int num = 0;
		List<Long> list= msg.getUnReadUids();
		if(list != null) {
			num = list.size();
		}
		pbImMsg.setUnReadNum(num);
		AtMsg atMsg = msg.getAtMsg();
		if(atMsg != null) {
			ImAtMsg pbAtMsg = imAtMsg2PbAtMsg(atMsg);
			pbImMsg.setAtMsg(pbAtMsg);
		}
		int isPushBlock = 0;
		if(msg.isPushBlock()){
			isPushBlock = 1;
		}
		if(isPushBlock >= 0) {
			pbImMsg.setIsPushBlock(isPushBlock);
		}
		int origiImg = msg.getOrigiImg();
		if(origiImg == 1) {
			pbImMsg.setOrigiImg(true);
		}
		String extra = msg.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			pbImMsg.setExtra(extra);
		}
		
		return pbImMsg.build();
	}
	
	/**
	 * pb格式转聊天消息
	 * @param pbImMsg
	 * @return
	 */
	public static GroupMsgModel pbImMsg2GroupMsg(ImMsg pbImMsg) {
		GroupMsgModel msg = new GroupMsgModel();
		if(pbImMsg == null) {
			return msg;
		}
		String groupId = pbImMsg.getGroupId();
		msg.setGroupId(groupId);
		int groupType = pbImMsg.getGroupType();
		msg.setGroupType(groupType);
		if(pbImMsg.hasGroupName()) {
			String groupName = pbImMsg.getGroupName();
			msg.setGroupName(groupName);
		}
		String msgId = pbImMsg.getMsgId();
		msg.setMsgId(msgId);
		int msgType = pbImMsg.getMsgType();
		msg.setMsgType(msgType);
		if(pbImMsg.hasMsgBody()) {
			String msgBody = pbImMsg.getMsgBody();
			msg.setMsgBody(msgBody);
		}

		long fromUid = pbImMsg.getFromUid();
		msg.setFromUid(fromUid);
		if(pbImMsg.hasFromName()) {
			String fromName = pbImMsg.getFromName();
			msg.setFromName(fromName);
		}
		if(pbImMsg.hasFromRemark() && pbImMsg.getFromRemark().length() > 0) {
			msg.setFromRemark(pbImMsg.getFromRemark());
		}
		msg.setSendTime(msg.getSendTime());
		long msgSeqId = pbImMsg.getMsgSeqId();
		msg.setMsgSeqId(msgSeqId);
		if(pbImMsg.hasMsgUrl() && pbImMsg.getMsgUrl().length() > 0) {
			String msgUrl = pbImMsg.getMsgUrl();
			msg.setMsgUrl(msgUrl);
		}
		if(pbImMsg.getAttchCount() > 0) {
			List<ImMsgAttach> pbAttachList = pbImMsg.getAttchList();
			List<Attachment> list = new ArrayList<Attachment>();
			if(pbAttachList != null) {
				for(ImMsgAttach msgAttach : pbAttachList) {
					Attachment attach = pbAttach2Attachment(msgAttach);
					if(attach != null) {
						list.add(attach);
					}
				}
			}
			msg.setMsgAttch(list);
		}
		if(pbImMsg.hasLocation()) {
			MsgLocation location = PbMsgTools.pbLocation2Location(pbImMsg.getLocation());
			msg.setMsgLocation(location);
		}
		if(pbImMsg.getOrigiImg()) {
			msg.setOrigiImg(1);
		}
		if(pbImMsg.hasAtMsg()) {
			ImAtMsg pbAtMsg = pbImMsg.getAtMsg();
			AtMsg atMsg = pbIMAtMsg2AtMsg(pbAtMsg);
			msg.setAtMsg(atMsg);
		}
		if(pbImMsg.hasExtra()) {
			String extra = pbImMsg.getExtra();
			msg.setExtra(extra);
		}
		return msg;
	}
	
	/**
	 * 附件转pb格式
	 * @param attach
	 * @return
	 */
	public static ImMsgAttach imMsgAttach2PbAttach(Attachment attach) {
		ImMsgAttach.Builder pbAttach = ImMsgAttach.newBuilder();
		if(attach == null) {
			return pbAttach.build();
		}
		String id = attach.getId();
		if(!Strings.isNullOrEmpty(id)) {
			pbAttach.setAttachId(id);
		}
		String name = attach.getAttachName();
		if(!Strings.isNullOrEmpty(name)) {
			pbAttach.setAttachName(name);
		}
		pbAttach.setAttachType(attach.getAttachType());
		String attachUrl = attach.getAttachUrl();
		if(!Strings.isNullOrEmpty(attachUrl)) {
			pbAttach.setAttachUrl(attachUrl);
		}
		pbAttach.setAttachSize(attach.getAttachSize());
		pbAttach.setWidth(attach.getWidth());
		pbAttach.setHeight(attach.getHeight());
		pbAttach.setAttachPlaytime(attach.getAttachPlaytime());
		pbAttach.setAttachUploadtime(attach.getAttachUploadtime());
		String extra = attach.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			pbAttach.setExtra(extra);
		}
		
		return pbAttach.build();
	}
	
	/**
	 * 附件pb格式转对象
	 * @param pbAttach
	 * @return
	 */
	public static Attachment pbAttach2Attachment(ImMsgAttach pbAttach) {
		Attachment attach = new Attachment();
		if(pbAttach == null) {
			return attach;
		}
		String id = pbAttach.getAttachId();
		attach.setId(id);
		String attachName = pbAttach.getAttachName();
		attach.setAttachName(attachName);
		int attachType = pbAttach.getAttachType();
		attach.setAttachType(attachType);
		String attachUrl = pbAttach.getAttachUrl();
		attach.setAttachUrl(attachUrl);
		int attachSize = pbAttach.getAttachSize();
		attach.setAttachSize(attachSize);
		int width = pbAttach.getWidth();
		attach.setWidth(width);
		int Height = pbAttach.getHeight();
		attach.setHeight(Height);
		int attachPlaytime = pbAttach.getAttachPlaytime();
		attach.setAttachPlaytime(attachPlaytime);
		long attachUploadtime = pbAttach.getAttachUploadtime();
		attach.setAttachUploadtime(attachUploadtime);
		if(pbAttach.hasExtra()) {
			String extra = pbAttach.getExtra();
			attach.setExtra(extra);
		}
		
		return attach;
	}
	
	/**
	 * 聊天@消息转pb格式
	 * @param atMsg
	 * @return
	 */
	public static ImAtMsg imAtMsg2PbAtMsg(AtMsg atMsg) {
		ImAtMsg.Builder atMsgBuilder = ImAtMsg.newBuilder();
		atMsgBuilder.setAtType(atMsg.getAtType());
		List<Long> uids = atMsg.getAtUids();
		if(uids != null) {
			for(long uid : uids) {
				atMsgBuilder.addAtUids(uid);
			}
		}
		String extra = atMsg.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			atMsgBuilder.setExtra(extra);
		}
		
		return atMsgBuilder.build();
	}
	
	/**
	 * 聊天@消息pb格式转对象
	 * @param pbMsg
	 * @return
	 */
	public static AtMsg pbIMAtMsg2AtMsg(ImAtMsg pbMsg) {
		AtMsg atMsg = new AtMsg();
		atMsg.setAtType(pbMsg.getAtType());
		if(pbMsg.getAtUidsCount() > 0) {
			List<Long> uids = pbMsg.getAtUidsList();
			if(uids != null) {
				atMsg.setAtUids(uids);
			}
		}
		if(pbMsg.hasExtra()) {
			String extra = pbMsg.getExtra();
			atMsg.setExtra(extra);
		}
		
		return atMsg;
	}
	
	
	/**
	 * 群组对象转pb格式
	 * @param group
	 * @param member
	 * @return
	 */
	public static ImGroupMsg grou2PbGroup(Group group, GroupMember member) {
		if(group == null) {
			return null;
		}
		ImGroupMsg.Builder pbGroup = ImGroupMsg.newBuilder();
		pbGroup.setGroupId(group.getGroupId());
		//pbGroup.setGroupName(group.getGroupName());
		int type = group.getType();
		pbGroup.setGroupType(type);
		long seqId = group.getSeq();
		pbGroup.setSeqId(seqId);
		long initSeq = 0L;
		long readSeq = 0L;
		long receiveSeqId = 0L;
		if(member != null) {
			initSeq = member.getInitSeq(); 
			readSeq = member.getReadSeq();
			receiveSeqId = member.getReceiveSeqId();
		}
		pbGroup.setInitSeqId(initSeq);
		pbGroup.setReadSeqId(readSeq);
		pbGroup.setReceiveSeqId(receiveSeqId);
		List<GroupMsgModel> msgs = group.getMsgs();
		if(msgs != null) {
			for(GroupMsgModel msg : msgs) {
				ImMsg pbMsg = groupMsg2PbImMsg(msg);
				if (pbMsg!=null) {
					pbGroup.addMsg(pbMsg);
				}
			}
		}
		
		return pbGroup.build();
	}
	
	/**
	 * 群组对象转pb格式
	 * @param groupId
	 * @return
	 */
	public static ImGroupMsg grou2PbGroup(String groupId) {
		ImGroupMsg.Builder pbGroup = ImGroupMsg.newBuilder();
		pbGroup.setGroupId(groupId);
		pbGroup.setIsQuit(true);
		return pbGroup.build();
	}
	
/*	*//**
	 * 客服消息转pb
	 * @param msg
	 * @return
	 *//*
	public static ConsultImMsg consultMsg2Pb(ConsultMsg msg) {
		ConsultImMsg.Builder pbConsultMsg = ConsultImMsg.newBuilder();
		pbConsultMsg.setCustomerId(msg.getCustomerId());
		GroupMsg groupMsg = msg.getGroupMsg();
		ImMsg pbImMsg = groupMsg2PbImMsg(groupMsg);
		pbConsultMsg.setImMsg(pbImMsg);
		String extra = msg.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			pbConsultMsg.setExtra(extra);
		}
		return pbConsultMsg.build();
	}
	
	*//**
	 * pb转客服消息
	 * @param pbMsg
	 * @return
	 *//*
	public static ConsultMsg pb2ConsultMsg(ConsultImMsg pbMsg) {
		ConsultMsg consultMsg = new ConsultMsg();
		consultMsg.setCustomerId(pbMsg.getCustomerId());
		ImMsg pbImMsg = pbMsg.getImMsg();
		GroupMsg groupMsg = pbImMsg2GroupMsg(pbImMsg);
		consultMsg.setGroupMsg(groupMsg);
		if(pbMsg.hasExtra()) {
			String extra = pbMsg.getExtra();
			consultMsg.setExtra(extra);
		}
		
		return consultMsg;
	}*/
}
