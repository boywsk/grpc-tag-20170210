package com.gomeplus.grpc.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gomeplus.grpc.message.AtMsg;
import com.gomeplus.grpc.message.Attachment;
import com.gomeplus.grpc.message.GroupMsgModel;
import com.gomeplus.grpc.message.MsgLocation;
import com.gomeplus.grpc.model.Group;
import com.gomeplus.grpc.model.GroupCollection;
import com.gomeplus.grpc.model.GroupMember;
import com.gomeplus.grpc.model.GroupMemberMark;
import com.gomeplus.grpc.model.GroupNotice;
import com.gomeplus.grpc.model.GroupQuitMember;
import com.gomeplus.grpc.model.SaveNoticeMsg;
import com.gomeplus.grpc.protobuf.GroupCollectionServices.GroupCollection.Builder;
import com.gomeplus.grpc.protobuf.GroupServices;

public class GomeImBeanUtils {
	private static final Logger logger = LoggerFactory.getLogger(GomeImBeanUtils.class);
	/**
	 * DBGroup->PBGroup
	 * @param dBgroup
	 * @return
	 */
	public static com.gomeplus.grpc.protobuf.GroupServices.Group convertPBGroupFromDBGroup(Group dBgroup){
		if (dBgroup==null) {
			return com.gomeplus.grpc.protobuf.GroupServices.Group.getDefaultInstance();
		}
		com.gomeplus.grpc.protobuf.GroupServices.Group.Builder groupBuilder = GroupServices.Group.newBuilder();
		String groupId = dBgroup.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			groupBuilder.setGroupId(groupId);
		}
		String avatar = dBgroup.getAvatar();
		if (StringUtils.isNotBlank(avatar)) {
			groupBuilder.setAvatar(avatar);
		}
		int capacity = dBgroup.getCapacity();
		if (capacity>=0) {
			groupBuilder.setCapacity(capacity);
		}
		long createTime = dBgroup.getCreateTime();
		if (createTime>=0) {
			groupBuilder.setCreateTime(createTime);
		}
		String groupDesc = dBgroup.getGroupDesc();
		if (StringUtils.isNotBlank(groupDesc)) {
			groupBuilder.setGroupDesc(groupDesc);
		}
		
		String groupName = dBgroup.getGroupName();
		if (StringUtils.isNotBlank(groupName)) {
			groupBuilder.setGroupName(groupName);
		}
		int isAudit = dBgroup.getIsAudit();
		if (isAudit>=0) {
			groupBuilder.setIsAudit(isAudit);
		}
		int isDele = dBgroup.getIsDele();
		if (isDele>=0) {
			groupBuilder.setIsDele(isDele);
		}
		String qRcode = dBgroup.getqRcode();
		if (StringUtils.isNotBlank(qRcode)) {
			groupBuilder.setQRcode(qRcode);
		}
		long seq = dBgroup.getSeq();
		if(seq>=0){
			groupBuilder.setSeq(seq);
		}
		String subject = dBgroup.getSubject();
		if (StringUtils.isNotBlank(subject)) {
			groupBuilder.setSubject(subject);
		}
		int type = dBgroup.getType();
		if (type>=0) {
			groupBuilder.setType(type);
		}
		long updateTime = dBgroup.getUpdateTime();
		if(updateTime>=0) {
			groupBuilder.setUpdateTime(updateTime);
		}
		long userId = dBgroup.getUserId();
		if(userId>=0){
			groupBuilder.setUserId(userId);
		}
		String extraInfo = dBgroup.getExtraInfo();//OA-服务器扩展信息
		if (StringUtils.isNotBlank(extraInfo)) {
			groupBuilder.setExtraInfo(extraInfo);
		}
		
		return groupBuilder.build();
	}
	
	/**
	 * PBGroup->DBGroup
	 * @param pbGroup
	 * @return
	 */
	public static Group convertDBGroupFromPBGroup(com.gomeplus.grpc.protobuf.GroupServices.Group pbGroup){
		com.gomeplus.grpc.protobuf.GroupServices.Group groupDefaultInstance = com.gomeplus.grpc.protobuf.GroupServices.Group.getDefaultInstance();
		if (pbGroup==null||pbGroup.equals(groupDefaultInstance)) {
			logger.info("GomeImBeanUtils.convertDBGroupFromPBGroup pbGroup is defaultInstance");
			return null;
		}
		Group dbGroup = new Group();
		String groupId = pbGroup.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			dbGroup.setGroupId(groupId);
		}
		String avatar = pbGroup.getAvatar();
		
		if (StringUtils.isNotBlank(avatar)) {
			dbGroup.setAvatar(avatar);
		}
		int capacity = pbGroup.getCapacity();
		if (capacity>=0) {
			dbGroup.setCapacity(capacity);
		}
		long createTime = pbGroup.getCreateTime();
		if (createTime>=0) {
			dbGroup.setCreateTime(createTime);
		}
		String groupDesc = pbGroup.getGroupDesc();
		if (StringUtils.isNotBlank(groupDesc)) {
			dbGroup.setGroupDesc(groupDesc);
		}
		
		String groupName = pbGroup.getGroupName();
		if (StringUtils.isNotBlank(groupName)) {
			dbGroup.setGroupName(groupName);
		}
		int isAudit = pbGroup.getIsAudit();
		if (isAudit>=0) {
			dbGroup.setIsAudit(isAudit);
		}
		int isDele = pbGroup.getIsDele();
		if (isDele>=0) {
			dbGroup.setIsDele(isDele);
		}
		String qRcode = pbGroup.getQRcode();
		if (StringUtils.isNotBlank(qRcode)) {
			dbGroup.setqRcode(qRcode);
		}
		long seq = pbGroup.getSeq();
		if(seq>=0){
			dbGroup.setSeq(seq);
		}
		String subject = pbGroup.getSubject();
		if (StringUtils.isNotBlank(subject)) {
			dbGroup.setSubject(subject);
		}
		int type = pbGroup.getType();
		if (type>=0) {
			dbGroup.setType(type);
		}
		long updateTime = pbGroup.getUpdateTime();
		if(updateTime>=0) {
			dbGroup.setUpdateTime(updateTime);
		}
		long userId = pbGroup.getUserId();
		if(userId>=0){
			dbGroup.setUserId(userId);
		}
		String extraInfo = pbGroup.getExtraInfo();//OA-服务器扩展信息
		if (StringUtils.isNotBlank(extraInfo)) {
			dbGroup.setExtraInfo(extraInfo);
		}
		return dbGroup;
	}
	
	/**
	 * DBGroupMember->PBGrouMember
	 * @param dBGroupMember
	 * @return
	 */
	public static com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember convertPBGroupMemberFromDBGroupMember(GroupMember dBGroupMember){
		if (dBGroupMember==null) {
			return com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember.getDefaultInstance();
		}
		com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember.Builder builder = com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember.newBuilder();
		long createTime = dBGroupMember.getCreateTime();
		if (createTime>=0) {
			builder.setCreateTime(createTime);
		}
		String groupId = dBGroupMember.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			builder.setGroupId(groupId);
		}
		String groupNickName = dBGroupMember.getGroupNickName();
		if (StringUtils.isNotBlank(groupNickName)) {
			builder.setGroupNickName(groupNickName);
		}
		int identity = dBGroupMember.getIdentity();
		if (identity>=0) {
			builder.setIdentity(identity);
		}
		long initSeq = dBGroupMember.getInitSeq();
		if (initSeq>=0) {
			builder.setInitSeq(initSeq);
		}
		int isCollectionGroup = dBGroupMember.getIsCollectionGroup();
		if (isCollectionGroup>=0) {
			builder.setIsCollectionGroup(isCollectionGroup);
		}
		builder.setGroupCollectTime(dBGroupMember.getGroupCollectTime());
		
		int isMsgBlocked = dBGroupMember.getIsMsgBlocked();
		
		if (isMsgBlocked>=0) {
			builder.setIsMsgBlocked(isMsgBlocked);
		}
		int isShield = dBGroupMember.getIsShield();
		if (isShield>=0) {
			builder.setIsShield(isShield);
		}
		int isTop = dBGroupMember.getIsTop();
		if (isTop>=0) {
			builder.setIsTop(isTop);
		}
		long joinTime = dBGroupMember.getJoinTime();
		if (joinTime>=0) {
			builder.setJoinTime(joinTime);
		}
		long maxSeq = dBGroupMember.getMaxSeq();
		if (maxSeq>=0) {
			builder.setMaxSeq(maxSeq);
		}
		String nickName = dBGroupMember.getNickName();
		if (StringUtils.isNotBlank(nickName)) {
			builder.setNickName(nickName);
		}
		long readSeq = dBGroupMember.getReadSeq();
		if (readSeq>=0) {
			builder.setReadSeq(readSeq);
		}
		long receiveSeqId = dBGroupMember.getReceiveSeqId();
		if (receiveSeqId>=0) {
			builder.setReceiveSeqId(receiveSeqId);
		}
		int status = dBGroupMember.getStatus();
		if (status>=0) {
			builder.setStatus(status);
		}
		long stickies = dBGroupMember.getStickies();
		if (stickies>=0) {
			builder.setStickies(stickies);
		}
		long updateTime = dBGroupMember.getUpdateTime();
		if (updateTime>=0) {
			builder.setUpdateTime(updateTime);
		}
		long userId = dBGroupMember.getUserId();
		if (userId>=0) {
			builder.setUserId(userId);
		}
		
		return builder.build();
	}
	
	/**
	 * PBGroupMember->DBGroupMember
	 * @param pBGroupMember
	 * @return
	 */
	public static GroupMember convertDBGroupMemberFromPBGroupMember(com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember pBGroupMember){
		com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember defaultInstance = com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember.getDefaultInstance();
		if (pBGroupMember==null||pBGroupMember.equals(defaultInstance)) {
			return null;
		}
		GroupMember dBGroup = new GroupMember();
		long createTime = pBGroupMember.getCreateTime();
		if (createTime>=0) {
			dBGroup.setCreateTime(createTime);
		}
		String groupId = pBGroupMember.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			dBGroup.setGroupId(groupId);
		}
		String groupNickName = pBGroupMember.getGroupNickName();
		if (StringUtils.isNotBlank(groupNickName)) {
			dBGroup.setGroupNickName(groupNickName);
		}
		int identity = pBGroupMember.getIdentity();
		if (identity>=0) {
			dBGroup.setIdentity(identity);
		}
		long initSeq = pBGroupMember.getInitSeq();
		if (initSeq>=0) {
			dBGroup.setInitSeq(initSeq);
		}
		int isCollectionGroup = pBGroupMember.getIsCollectionGroup();
		if (isCollectionGroup>=0) {
			dBGroup.setIsCollectionGroup(isCollectionGroup);
		}
		dBGroup.setGroupCollectTime(pBGroupMember.getGroupCollectTime());
		int isMsgBlocked = pBGroupMember.getIsMsgBlocked();
		
		if (isMsgBlocked>=0) {
			dBGroup.setIsMsgBlocked(isMsgBlocked);
		}
		int isShield = pBGroupMember.getIsShield();
		if (isShield>=0) {
			dBGroup.setIsShield(isShield);
		}
		int isTop = pBGroupMember.getIsTop();
		if (isTop>=0) {
			dBGroup.setIsTop(isTop);
		}
		long joinTime = pBGroupMember.getJoinTime();
		if (joinTime>=0) {
			dBGroup.setJoinTime(joinTime);
		}
		long maxSeq = pBGroupMember.getMaxSeq();
		if (maxSeq>=0) {
			dBGroup.setMaxSeq(maxSeq);
		}
		String nickName = pBGroupMember.getNickName();
		if (StringUtils.isNotBlank(nickName)) {
			dBGroup.setNickName(nickName);
		}
		long readSeq = pBGroupMember.getReadSeq();
		if (readSeq>=0) {
			dBGroup.setReadSeq(readSeq);
		}
		long receiveSeqId = pBGroupMember.getReceiveSeqId();
		if (receiveSeqId>=0) {
			dBGroup.setReceiveSeqId(receiveSeqId);
		}
		int status = pBGroupMember.getStatus();
		if (status>=0) {
			dBGroup.setStatus(status);
		}
		long stickies = pBGroupMember.getStickies();
		if (stickies>=0) {
			dBGroup.setStickies(stickies);
		}
		long updateTime = pBGroupMember.getUpdateTime();
		if (updateTime>=0) {
			dBGroup.setUpdateTime(updateTime);
		}
		long userId = pBGroupMember.getUserId();
		if (userId>=0) {
			dBGroup.setUserId(userId);
		}
		return dBGroup;
	}
	
	
	/**
	 * DBGroupMemberMark->PBGroupMemberMark
	 * @param dbGroupMemberMark
	 * @return
	 */
	public static com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMark convertPBGroupMemberMarkFromDBGroupMemberMark(GroupMemberMark dbGroupMemberMark){
		if (dbGroupMemberMark==null) {
			return com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMark.getDefaultInstance();
		}
		com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMark.Builder builder =  com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMark.newBuilder();
		long createTime = dbGroupMemberMark.getCreateTime();
		if (createTime>=0) {
			builder.setCreateTime(createTime);
		}
		String groupId = dbGroupMemberMark.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			builder.setGroupId(groupId);
		}
		String mark = dbGroupMemberMark.getMark();
		if (StringUtils.isNotBlank(mark)) {
			builder.setMark(mark);
		}
		long markedUserId = dbGroupMemberMark.getMarkedUserId();
		if (markedUserId>=0) {
			builder.setMarkedUserId(markedUserId);
		}
		long updateTime = dbGroupMemberMark.getUpdateTime();
		if (updateTime>=0) {
			builder.setUpdateTime(updateTime);
		}
		long userId = dbGroupMemberMark.getUserId();
		if (userId>=0) {
			builder.setUserId(userId);
		}
		return builder.build();
	}
	
	/**
	 * PBGroupMemberMark->DBGroupMemberMark
	 * @param pBGroupMemberMark
	 * @return
	 */
	public static GroupMemberMark convertDBGroupMemberMarkFromPBGroupMemberMark(com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMark pBGroupMemberMark){
		com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMark defaultInstance = com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMark.getDefaultInstance();
		
		if (pBGroupMemberMark==null||pBGroupMemberMark.equals(defaultInstance)) {
			return null;
		}
		GroupMemberMark dBGroupMemberMark=new GroupMemberMark();
		long createTime = pBGroupMemberMark.getCreateTime();
		if (createTime>=0) {
			dBGroupMemberMark.setCreateTime(createTime);
		}
		String groupId = pBGroupMemberMark.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			dBGroupMemberMark.setGroupId(groupId);
		}
		String mark = pBGroupMemberMark.getMark();
		if (StringUtils.isNotBlank(mark)) {
			dBGroupMemberMark.setMark(mark);
		}
		long markedUserId = pBGroupMemberMark.getMarkedUserId();
		if (markedUserId>=0) {
			dBGroupMemberMark.setMarkedUserId(markedUserId);
		}
		long updateTime = pBGroupMemberMark.getUpdateTime();
		if (updateTime>=0) {
			dBGroupMemberMark.setUpdateTime(updateTime);
		}
		long userId = pBGroupMemberMark.getUserId();
		if (userId>=0) {
			dBGroupMemberMark.setUserId(userId);
		}
		
		return dBGroupMemberMark;
	}
	
    /**
     * DBGroupQuitMember ->PBGroupQuitMember
     * @param dBGroupQuitMember
     * @return
     */
    public static com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMember convertPBQuitMemberFromDBQuitMember(GroupQuitMember dBGroupQuitMember){
    	if (dBGroupQuitMember==null) {
			return com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMember.getDefaultInstance();
		}
    	com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMember.Builder builder = com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMember.newBuilder();
    	long createTime = dBGroupQuitMember.getCreateTime();
    	if (createTime>=0) {
    		builder.setCreateTime(createTime);
		}
    	String groupId = dBGroupQuitMember.getGroupId();
    	if (StringUtils.isNotBlank(groupId)) {
    		builder.setGroupId(groupId);
		}
    	long updateTime = dBGroupQuitMember.getUpdateTime();
    	if (updateTime>=0) {
    		builder.setUpdateTime(updateTime);
		}
    	long userId = dBGroupQuitMember.getUserId();
    	if (userId>=0) {
    		builder.setUserId(userId);
		}
    	return builder.build();
    }
    /**
     * PBGroupQuitMember ->DBGroupQuitMember
     * @param pBGroupQuitMember
     * @return
     */
    public static GroupQuitMember convertDBQuitMemberFromPBQuitMember(com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMember pBGroupQuitMember){
    	com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMember defaultInstance = com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMember.getDefaultInstance();
    	
    	if (pBGroupQuitMember==null||pBGroupQuitMember.equals(defaultInstance)) {
    		return null;
    	}
    	GroupQuitMember groupQuitMember =new GroupQuitMember();
    	long createTime = pBGroupQuitMember.getCreateTime();
    	if (createTime>=0) {
    		groupQuitMember.setCreateTime(createTime);
		}
    	String groupId = pBGroupQuitMember.getGroupId();
    	if (StringUtils.isNotBlank(groupId)) {
    		groupQuitMember.setGroupId(groupId);
		}
    	long updateTime = pBGroupQuitMember.getUpdateTime();
    	if (updateTime>=0) {
    		groupQuitMember.setUpdateTime(updateTime);
		}
    	long userId = pBGroupQuitMember.getUserId();
    	if (userId>=0) {
    		groupQuitMember.setUserId(userId);
		}
    	return groupQuitMember;
    }
	
	/**
	 * DBGroupMsg->PBGroupMsg
	 * @param dBGroupMsg
	 * @return
	 */
	public static com.gomeplus.grpc.protobuf.GroupMsgServices.GroupMsg convertPBGroupMsgFromDBGroupMsg(GroupMsgModel dBGroupMsg){
		if (dBGroupMsg==null) {
			return com.gomeplus.grpc.protobuf.GroupMsgServices.GroupMsg.getDefaultInstance();
		}
		com.gomeplus.grpc.protobuf.GroupMsgServices.GroupMsg.Builder builder = com.gomeplus.grpc.protobuf.GroupMsgServices.GroupMsg.newBuilder();
		com.gomeplus.grpc.protobuf.GroupMsgServices.AtMsg pbAtMsg = convertPBAtMsgFromDBAtMsg(dBGroupMsg.getAtMsg());
		if (pbAtMsg!=null) {
			builder.setAtMsg(pbAtMsg);
			
		}
		List<Attachment> msgAttch = dBGroupMsg.getMsgAttch();
		if (!CollectionUtils.isEmpty(msgAttch)) {
			int i=0;
			for (Attachment attachment : msgAttch) {
				builder.addMsgAttch(i++, convertPBAttachmentFormDBAttachment(attachment));
			}
		}
		com.gomeplus.grpc.protobuf.GroupMsgServices.MsgLocation pBMsgLocation = GomeImBeanUtils.convertPBMsgLocationFormDBMsgLocation(dBGroupMsg.getMsgLocation());
		if (pBMsgLocation!=null) {
			builder.setMsgLocation(pBMsgLocation);
		}
		String groupId = dBGroupMsg.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			builder.setGroupId(groupId);
		}
		String groupName = dBGroupMsg.getGroupName();
		if (StringUtils.isNotBlank(groupName)) {
			builder.setGroupName(groupName);
		}
		int groupType = dBGroupMsg.getGroupType();
		if (groupType>=0) {
			builder.setGroupType(groupType);
		}
		String msgId = dBGroupMsg.getMsgId();
		if (StringUtils.isNotBlank(msgId)) {
			builder.setMsgId(msgId);
		}
		int msgType = dBGroupMsg.getMsgType();
		if (msgType>=0) {
			builder.setMsgType(msgType);
		}
		String msgBody = dBGroupMsg.getMsgBody();
		if (StringUtils.isNotBlank(msgBody)) {
			builder.setMsgBody(msgBody);
		}
		long fromUid = dBGroupMsg.getFromUid();
		if (fromUid>=0) {
			builder.setFromUid(fromUid);
		}
		String fromName = dBGroupMsg.getFromName();
		if (StringUtils.isNotBlank(fromName)) {
			builder.setFromName(fromName);
		}
		String fromRemark = dBGroupMsg.getFromRemark();
		if (StringUtils.isNotBlank(fromRemark)) {
			builder.setFromRemark(fromRemark);
		}
		long toUid = dBGroupMsg.getToUid();
		if (toUid>=0) {
			builder.setToUid(toUid);
		}
		long msgSeqId = dBGroupMsg.getMsgSeqId();
		if (msgSeqId>=0) {
			builder.setMsgSeqId(msgSeqId);
		}
		String msgUrl = dBGroupMsg.getMsgUrl();
		if (StringUtils.isNotBlank(msgUrl)) {
			builder.setMsgUrl(msgUrl);
		}
		long sendTime = dBGroupMsg.getSendTime();
		if (sendTime>=0) {
			builder.setSendTime(sendTime);
		}
		List<Long> atUids = dBGroupMsg.getAtUids();
		List<Long> unReadUids = dBGroupMsg.getUnReadUids();
		List<Long> readUids = dBGroupMsg.getReadUids();
		
		if (!CollectionUtils.isEmpty(atUids)) {
			builder.addAllAtUids(atUids);
		}
		if (!CollectionUtils.isEmpty(unReadUids)) {
			builder.addAllUnReadUids(unReadUids);
		}
		if (!CollectionUtils.isEmpty(readUids)) {
			builder.addAllReadUids(readUids);
		}
		int isRevoke = dBGroupMsg.getIsRevoke();
		if (isRevoke>=0) {
			builder.setIsRevoke(isRevoke);
		}
		String extra = dBGroupMsg.getExtra();
		if (StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		int origiImg = dBGroupMsg.getOrigiImg();
		if (origiImg>=0) {
			builder.setOrigiImg(origiImg);
		}
		builder.setIsPushBlock(dBGroupMsg.isPushBlock());
//		transient private int appId;
//		transient private int systemMsgType;// 消息类型 群量广播 1-普通系统消息 2-全量广播消息
//		transient private boolean isPersist;// 是否持久化
//		transient private boolean containSelf;// 消息是否包含自己
		return builder.build();
	}
	
	
	/**
	 * DBGroupMsg->PBGroupMsg
	 * @param pBGroupMsg
	 * @return
	 */
	public static GroupMsgModel convertDBGroupMsgFromPBGroupMsg(com.gomeplus.grpc.protobuf.GroupMsgServices.GroupMsg pBGroupMsg){
		com.gomeplus.grpc.protobuf.GroupMsgServices.GroupMsg defaultInstance = com.gomeplus.grpc.protobuf.GroupMsgServices.GroupMsg.getDefaultInstance();
		if (pBGroupMsg==null||pBGroupMsg.equals(defaultInstance)) {
			return null;
		}
		
		GroupMsgModel dbGroupMsg = new GroupMsgModel();
		AtMsg pbAtMsg = convertDBAtMsgFromPBAtMsg(pBGroupMsg.getAtMsg());
		if (pbAtMsg!=null) {
			dbGroupMsg.setAtMsg(pbAtMsg);
		}
		List<com.gomeplus.grpc.protobuf.GroupMsgServices.Attachment> msgAttch = pBGroupMsg.getMsgAttchList();
		if (!CollectionUtils.isEmpty(msgAttch)) {
			List<Attachment> dbAttachmentList=new ArrayList<Attachment>();
			for (com.gomeplus.grpc.protobuf.GroupMsgServices.Attachment attachment : msgAttch) {
				dbAttachmentList.add(convertDBAttachmentFormPBAttachment(attachment));
			}
			dbGroupMsg.setMsgAttch(dbAttachmentList);
		}
		MsgLocation pBMsgLocation = GomeImBeanUtils.convertDBMsgLocationFormPBMsgLocation(pBGroupMsg.getMsgLocation());
		if (pBMsgLocation!=null) {
			dbGroupMsg.setMsgLocation(pBMsgLocation);
		}
		String groupId = pBGroupMsg.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			dbGroupMsg.setGroupId(groupId);
		}
		String groupName = pBGroupMsg.getGroupName();
		if (StringUtils.isNotBlank(groupName)) {
			dbGroupMsg.setGroupName(groupName);
		}
		int groupType = pBGroupMsg.getGroupType();
		if (groupType>=0) {
			dbGroupMsg.setGroupType(groupType);
		}
		String msgId = pBGroupMsg.getMsgId();
		if (StringUtils.isNotBlank(msgId)) {
			dbGroupMsg.setMsgId(msgId);
		}
		int msgType = pBGroupMsg.getMsgType();
		if (msgType>=0) {
			dbGroupMsg.setMsgType(msgType);
		}
		String msgBody = pBGroupMsg.getMsgBody();
		if (StringUtils.isNotBlank(msgBody)) {
			dbGroupMsg.setMsgBody(msgBody);
		}
		long fromUid = pBGroupMsg.getFromUid();
		if (fromUid>=0) {
			dbGroupMsg.setFromUid(fromUid);
		}
		String fromName = pBGroupMsg.getFromName();
		if (StringUtils.isNotBlank(fromName)) {
			dbGroupMsg.setFromName(fromName);
		}
		String fromRemark = pBGroupMsg.getFromRemark();
		if (StringUtils.isNotBlank(fromRemark)) {
			dbGroupMsg.setFromRemark(fromRemark);
		}
		long toUid = pBGroupMsg.getToUid();
		if (toUid>=0) {
			dbGroupMsg.setToUid(toUid);
		}
		long msgSeqId = pBGroupMsg.getMsgSeqId();
		if (msgSeqId>=0) {
			dbGroupMsg.setMsgSeqId(msgSeqId);
		}
		String msgUrl = pBGroupMsg.getMsgUrl();
		if (StringUtils.isNotBlank(msgUrl)) {
			dbGroupMsg.setMsgUrl(msgUrl);
		}
		long sendTime = pBGroupMsg.getSendTime();
		if (sendTime>=0) {
			dbGroupMsg.setSendTime(sendTime);
		}
		List<Long> atUids = pBGroupMsg.getAtUidsList();
		List<Long> unReadUids = pBGroupMsg.getUnReadUidsList();
		List<Long> readUids = pBGroupMsg.getReadUidsList();
		
		if (!CollectionUtils.isEmpty(atUids)) {
			dbGroupMsg.setAtUids(atUids);
		}
		if (!CollectionUtils.isEmpty(unReadUids)) {
			dbGroupMsg.setUnReadUids(unReadUids);
		}
		if (!CollectionUtils.isEmpty(readUids)) {
			dbGroupMsg.setReadUids(readUids);
		}
		int isRevoke = pBGroupMsg.getIsRevoke();
		if (isRevoke>=0) {
			dbGroupMsg.setIsRevoke(isRevoke);
		}
		String extra = pBGroupMsg.getExtra();
		if (StringUtils.isNotBlank(extra)) {
			dbGroupMsg.setExtra(extra);
		}
		int origiImg = pBGroupMsg.getOrigiImg();
		if (origiImg>=0) {
			dbGroupMsg.setOrigiImg(origiImg);
		}
//		int appId = pBGroupMsg.getAppId();
//		if (appId>=0) {
//			dbGroupMsg.setAppId(appId);
//		}
//		int systemMsgType = pBGroupMsg.getSystemMsgType();
//		if (systemMsgType>=0) {
//			dbGroupMsg.setSystemMsgType(systemMsgType);
//		}
//		dbGroupMsg.setPersist(pBGroupMsg.getIsPersist());
		dbGroupMsg.setPushBlock(pBGroupMsg.getIsPushBlock());
//		dbGroupMsg.setContainSelf(pBGroupMsg.getContainSelf());
		
		return dbGroupMsg;
	}
	
	/**
	 * DBSaveNoticeMsg->PBSaveNoticeMsg
	 * @param dbSaveNoticeMsg
	 * @return
	 */
	public static com.gomeplus.grpc.protobuf.NoticeMsgServices.SaveNoticeMsg convertPBSaveNoticeMsgFromDBSaveNoticeMsg(SaveNoticeMsg dbSaveNoticeMsg){
		if (dbSaveNoticeMsg==null) {
			return com.gomeplus.grpc.protobuf.NoticeMsgServices.SaveNoticeMsg.getDefaultInstance();
		}
		com.gomeplus.grpc.protobuf.NoticeMsgServices.SaveNoticeMsg.Builder builder = com.gomeplus.grpc.protobuf.NoticeMsgServices.SaveNoticeMsg.newBuilder();
		String msgId = dbSaveNoticeMsg.getMsgId();
		if (StringUtils.isNotBlank(msgId)) {
			builder.setMsgId(msgId);
		}
		long fromUid = dbSaveNoticeMsg.getFromUid();
		if (fromUid>=0) {
			builder.setFromUid(fromUid);
		}
		long toUid = dbSaveNoticeMsg.getToUid();
		if (toUid>=0) {
			builder.setToUid(toUid);
		}
		String groupId = dbSaveNoticeMsg.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			builder.setGroupId(groupId);
		}
		int noticeType = dbSaveNoticeMsg.getNoticeType();
		if (noticeType>=0) {
			builder.setNoticeType(noticeType);
		}
		String noticeMsgJson = dbSaveNoticeMsg.getNoticeMsgJson();
		if (StringUtils.isNotBlank(noticeMsgJson)) {
			builder.setNoticeMsgJson(noticeMsgJson);
		}
		long sendTime = dbSaveNoticeMsg.getSendTime();
		if (sendTime>=0) {
			builder.setSendTime(sendTime);
		}
		int platform = dbSaveNoticeMsg.getPlatform();
		if (platform>=0) {
			builder.setPlatform(platform);
		}
		return builder.build();
	}
	/**
	 * PBSaveNoticeMsg->DBSaveNoticeMsg
	 * @param pbSaveNoticeMsg
	 * @return
	 */
	public static SaveNoticeMsg convertDBSaveNoticeMsgFromPBSaveNoticeMsg(com.gomeplus.grpc.protobuf.NoticeMsgServices.SaveNoticeMsg pbSaveNoticeMsg){
		com.gomeplus.grpc.protobuf.NoticeMsgServices.SaveNoticeMsg defaultInstance = com.gomeplus.grpc.protobuf.NoticeMsgServices.SaveNoticeMsg.getDefaultInstance();
		
		if (pbSaveNoticeMsg==null||pbSaveNoticeMsg.equals(defaultInstance)) {
			return null;
		}
		SaveNoticeMsg dbSaveNoticeMsg = new SaveNoticeMsg();
		
		String msgId = pbSaveNoticeMsg.getMsgId();
		if (StringUtils.isNotBlank(msgId)) {
			dbSaveNoticeMsg.setMsgId(msgId);
		}
		long fromUid = pbSaveNoticeMsg.getFromUid();
		if (fromUid>=0) {
			dbSaveNoticeMsg.setFromUid(fromUid);
		}
		long toUid = pbSaveNoticeMsg.getToUid();
		if (toUid>=0) {
			dbSaveNoticeMsg.setToUid(toUid);
		}
		String groupId = pbSaveNoticeMsg.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			dbSaveNoticeMsg.setGroupId(groupId);
		}
		int noticeType = pbSaveNoticeMsg.getNoticeType();
		if (noticeType>=0) {
			dbSaveNoticeMsg.setNoticeType(noticeType);
		}
		String noticeMsgJson = pbSaveNoticeMsg.getNoticeMsgJson();
		if (StringUtils.isNotBlank(noticeMsgJson)) {
			dbSaveNoticeMsg.setNoticeMsgJson(noticeMsgJson);
		}
		long sendTime = pbSaveNoticeMsg.getSendTime();
		if (sendTime>=0) {
			dbSaveNoticeMsg.setSendTime(sendTime);
		}
		int platform = pbSaveNoticeMsg.getPlatform();
		if (platform>=0) {
			dbSaveNoticeMsg.setPlatform(platform);
		}
		return dbSaveNoticeMsg;
	}
	
	/**
	 * DBAttachment->PBAttachment
	 * @param dbAttachment
	 * @return
	 */
	private static com.gomeplus.grpc.protobuf.GroupMsgServices.Attachment convertPBAttachmentFormDBAttachment(Attachment dbAttachment){
		if (dbAttachment==null) {
			return com.gomeplus.grpc.protobuf.GroupMsgServices.Attachment.getDefaultInstance();
		}
		com.gomeplus.grpc.protobuf.GroupMsgServices.Attachment.Builder builder =  com.gomeplus.grpc.protobuf.GroupMsgServices.Attachment.newBuilder();
		String id = dbAttachment.getId();
		if (StringUtils.isNotBlank(id)) {
			builder.setId(id);
		}
		String attachName = dbAttachment.getAttachName();
		if (StringUtils.isNotBlank(attachName)) {
			builder.setAttachName(attachName);
		}
		int attachType = dbAttachment.getAttachType();
		if (attachType>=0) {
			builder.setAttachType(attachType);
		}
		String attachUrl = dbAttachment.getAttachUrl();
		if (StringUtils.isNotBlank(attachUrl)) {
			builder.setAttachUrl(attachUrl);
		}
		int attachSize = dbAttachment.getAttachSize();
		if (attachSize>=0) {
			builder.setAttachSize(attachSize);
		}
		int width = dbAttachment.getWidth();
		if (width>=0) {
			builder.setWidth(width);
		}
		int height = dbAttachment.getHeight();
		if (height>=0) {
			builder.setHeight(height);
		}
		int attachPlaytime = dbAttachment.getAttachPlaytime();
		if (attachPlaytime>=0) {
			builder.setAttachPlaytime(attachPlaytime);
		}
		long attachUploadtime = dbAttachment.getAttachUploadtime();
		if (attachUploadtime>=0) {
			builder.setAttachUploadtime(attachUploadtime);
		}
		String extra = dbAttachment.getExtra();
		if (StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		return builder.build();
	} 
	/**
	 * PBAttachment->DBAttachment
	 * @param pbAttachment
	 * @return
	 */
	private static Attachment convertDBAttachmentFormPBAttachment(com.gomeplus.grpc.protobuf.GroupMsgServices.Attachment pBAttachment){
		com.gomeplus.grpc.protobuf.GroupMsgServices.Attachment defaultInstance = com.gomeplus.grpc.protobuf.GroupMsgServices.Attachment.getDefaultInstance();
		
		if (pBAttachment==null||pBAttachment.equals(defaultInstance)) {
			return null;
		}
		Attachment dbAttachment = new Attachment();
		
		String id = pBAttachment.getId();
		if (StringUtils.isNotBlank(id)) {
			dbAttachment.setId(id);
		}
		String attachName = pBAttachment.getAttachName();
		if (StringUtils.isNotBlank(attachName)) {
			dbAttachment.setAttachName(attachName);
		}
		int attachType = pBAttachment.getAttachType();
		if (attachType>=0) {
			dbAttachment.setAttachType(attachType);
		}
		String attachUrl = pBAttachment.getAttachUrl();
		if (StringUtils.isNotBlank(attachUrl)) {
			dbAttachment.setAttachUrl(attachUrl);
		}
		int attachSize = pBAttachment.getAttachSize();
		if (attachSize>=0) {
			dbAttachment.setAttachSize(attachSize);
		}
		int width = pBAttachment.getWidth();
		if (width>=0) {
			dbAttachment.setWidth(width);
		}
		int height = pBAttachment.getHeight();
		if (height>=0) {
			dbAttachment.setHeight(height);
		}
		int attachPlaytime = pBAttachment.getAttachPlaytime();
		if (attachPlaytime>=0) {
			dbAttachment.setAttachPlaytime(attachPlaytime);
		}
		long attachUploadtime = pBAttachment.getAttachUploadtime();
		if (attachUploadtime>=0) {
			dbAttachment.setAttachUploadtime(attachUploadtime);
		}
		String extra = pBAttachment.getExtra();
		if (StringUtils.isNotBlank(extra)) {
			dbAttachment.setExtra(extra);
		}
		return dbAttachment;
	} 
	
	/**
	 * DBMsgLocation->PBMsgLocation
	 * @param dbMsgLocation
	 * @return
	 */
	private static com.gomeplus.grpc.protobuf.GroupMsgServices.MsgLocation convertPBMsgLocationFormDBMsgLocation(MsgLocation dbMsgLocation){
		if (dbMsgLocation==null) {
			return com.gomeplus.grpc.protobuf.GroupMsgServices.MsgLocation.getDefaultInstance();
		}
		com.gomeplus.grpc.protobuf.GroupMsgServices.MsgLocation.Builder builder = com.gomeplus.grpc.protobuf.GroupMsgServices.MsgLocation.newBuilder();
		String msgId = dbMsgLocation.getMsgId();
		if (StringUtils.isNotBlank(msgId)) {
			builder.setMsgId(msgId);
		}
		double longitude = dbMsgLocation.getLongitude();
		if (longitude>=0) {
			builder.setLongitude(longitude);
		}
		double latitude = dbMsgLocation.getLatitude();
		if (latitude>=0) {
			builder.setLatitude(latitude);
		}
		String imgUrl = dbMsgLocation.getImgUrl();
		if (StringUtils.isNotBlank(imgUrl)) {
			builder.setImgUrl(imgUrl);
		}
		String content = dbMsgLocation.getContent();
		if (StringUtils.isNotBlank(content)) {
			builder.setContent(content);
		}
		String extra = dbMsgLocation.getExtra();
		if (StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		return builder.build();
	}
	
	/**
	 * DBMsgLocation->PBMsgLocation
	 * @param pBMsgLocation
	 * @return
	 */
	private static MsgLocation convertDBMsgLocationFormPBMsgLocation(com.gomeplus.grpc.protobuf.GroupMsgServices.MsgLocation pBMsgLocation){
		com.gomeplus.grpc.protobuf.GroupMsgServices.MsgLocation defaultInstance = com.gomeplus.grpc.protobuf.GroupMsgServices.MsgLocation.getDefaultInstance();
		
		if (pBMsgLocation==null||pBMsgLocation.equals(defaultInstance)) {
			return null;
		}
		MsgLocation dbMsgLocation = new MsgLocation();
		String msgId = pBMsgLocation.getMsgId();
		if (StringUtils.isNotBlank(msgId)) {
			dbMsgLocation.setMsgId(msgId);
		}
		double longitude = pBMsgLocation.getLongitude();
		if (longitude>=0) {
			dbMsgLocation.setLongitude(longitude);
		}
		double latitude = pBMsgLocation.getLatitude();
		if (latitude>=0) {
			dbMsgLocation.setLatitude(latitude);
		}
		String imgUrl = pBMsgLocation.getImgUrl();
		if (StringUtils.isNotBlank(imgUrl)) {
			dbMsgLocation.setImgUrl(imgUrl);
		}
		String content = pBMsgLocation.getContent();
		if (StringUtils.isNotBlank(content)) {
			dbMsgLocation.setContent(content);
		}
		String extra = pBMsgLocation.getExtra();
		if (StringUtils.isNotBlank(extra)) {
			dbMsgLocation.setExtra(extra);
		}
		return dbMsgLocation;
	}
	
	/**
	 * DBAtMsg->PBAtMsg
	 * @param dbAtMsg
	 * @return
	 */
	private static com.gomeplus.grpc.protobuf.GroupMsgServices.AtMsg convertPBAtMsgFromDBAtMsg(AtMsg dbAtMsg){
		if (dbAtMsg==null) {
			return com.gomeplus.grpc.protobuf.GroupMsgServices.AtMsg.getDefaultInstance();
		}
		com.gomeplus.grpc.protobuf.GroupMsgServices.AtMsg.Builder builder = com.gomeplus.grpc.protobuf.GroupMsgServices.AtMsg.newBuilder();
		int atType = dbAtMsg.getAtType();
		if (atType>=0) {
			builder.setAtType(atType);
		}
		List<Long> dbAtUids = dbAtMsg.getAtUids();
		if (!CollectionUtils.isEmpty(dbAtUids)) {
			builder.addAllAtUids(dbAtUids);
		}
		String extra = dbAtMsg.getExtra();
		if (StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		return builder.build();
	}
	
	/**
	 * DBAtMsg->PBAtMsg
	 * @param pBAtMsg
	 * @return
	 */
	private static AtMsg convertDBAtMsgFromPBAtMsg(com.gomeplus.grpc.protobuf.GroupMsgServices.AtMsg pBAtMsg){
		com.gomeplus.grpc.protobuf.GroupMsgServices.AtMsg defaultInstance = com.gomeplus.grpc.protobuf.GroupMsgServices.AtMsg.getDefaultInstance();
		
		if (pBAtMsg==null||pBAtMsg.equals(defaultInstance)) {
			return null;
		}
		AtMsg dbAtMsg = new AtMsg();
		int atType = pBAtMsg.getAtType();
		if (atType>=0) {
			dbAtMsg.setAtType(atType);
		}
		List<Long> atUidsList = pBAtMsg.getAtUidsList();
		if (!CollectionUtils.isEmpty(atUidsList)) {
			dbAtMsg.setAtUids(atUidsList);
		}
		String extra = pBAtMsg.getExtra();
		if (StringUtils.isNotBlank(extra)) {
			dbAtMsg.setExtra(extra);
		}
		return dbAtMsg;
	}
	
	
	/**
	 * DBGroupNotice->PBGroupNotice
	 * @param dbGroupNotice
	 * @return
	 */
	public static com.gomeplus.grpc.protobuf.GroupNoticeServices.GroupNotice convertPBGroupNoticeFromDBGroupNotice(GroupNotice dbGroupNotice){
		if (dbGroupNotice==null) {
			return com.gomeplus.grpc.protobuf.GroupNoticeServices.GroupNotice.getDefaultInstance();
		}
		com.gomeplus.grpc.protobuf.GroupNoticeServices.GroupNotice.Builder builder = com.gomeplus.grpc.protobuf.GroupNoticeServices.GroupNotice.newBuilder();
		long createTime = dbGroupNotice.getCreateTime();
		String groupId = dbGroupNotice.getGroupId();
		String noticeContent = dbGroupNotice.getNoticeContent();
		long updateTime = dbGroupNotice.getUpdateTime();
		long userId = dbGroupNotice.getUserId();
		if (createTime>=0) {
			builder.setCreateTime(createTime);
		}
		if (StringUtils.isNotBlank(groupId)) {
			builder.setGroupId(groupId);
		}
		if (StringUtils.isNotBlank(noticeContent)) {
			builder.setNoticeContent(noticeContent);
		}
		if (updateTime>=0) {
			builder.setUpdateTime(updateTime);
		}
		if (userId>=0) {
			builder.setUserId(userId);
		}
		return builder.build();
	}
	/**
	 * PBGroupNotice->DBGroupNotice
	 * @param pbGroupNotice
	 * @return
	 */
	public static GroupNotice convertDBGroupNoticeFromPBGroupNotice(com.gomeplus.grpc.protobuf.GroupNoticeServices.GroupNotice pbGroupNotice){
		com.gomeplus.grpc.protobuf.GroupNoticeServices.GroupNotice defaultInstance = com.gomeplus.grpc.protobuf.GroupNoticeServices.GroupNotice.getDefaultInstance();
		
		if (pbGroupNotice==null||pbGroupNotice.equals(defaultInstance)) {
			return null;
		}
		GroupNotice dbGroupNotice = new GroupNotice();
		long createTime = pbGroupNotice.getCreateTime();
		String groupId = pbGroupNotice.getGroupId();
		String noticeContent = pbGroupNotice.getNoticeContent();
		long updateTime = pbGroupNotice.getUpdateTime();
		long userId = pbGroupNotice.getUserId();
		if (createTime>=0) {
			dbGroupNotice.setCreateTime(createTime);
		}
		if (StringUtils.isNotBlank(groupId)) {
			dbGroupNotice.setGroupId(groupId);
		}
		if (StringUtils.isNotBlank(noticeContent)) {
			dbGroupNotice.setNoticeContent(noticeContent);
		}
		if (updateTime>=0) {
			dbGroupNotice.setUpdateTime(updateTime);
		}
		if (userId>=0) {
			dbGroupNotice.setUserId(userId);
		}
		return dbGroupNotice;
	}
	
	/**
	 *@Description: PBGroupCollection->DBGroupCollection
	 *@param pbGroupCollection
	 *@return
	 */
	public static GroupCollection convertDBGroupCollectionFromPBCollection(com.gomeplus.grpc.protobuf.GroupCollectionServices.GroupCollection pbGroupCollection){
		GroupCollection dbGroupCollection=new GroupCollection();
		String groupId = pbGroupCollection.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			dbGroupCollection.setGroupId(groupId);
		}
		dbGroupCollection.setUserId(pbGroupCollection.getUserId());
		dbGroupCollection.setUpdateTime(pbGroupCollection.getUpdateTime());
		dbGroupCollection.setIsDel(pbGroupCollection.getIsDel());
		return dbGroupCollection;
	}
	
	/**
	 *@Description: DBGroupCollection->PBGroupCollection
	 *@param pbGroupCollection
	 *@return
	 */
	public static com.gomeplus.grpc.protobuf.GroupCollectionServices.GroupCollection convertPBGroupCollectionFromDBCollection(GroupCollection dbGroupCollection){
		Builder builder = com.gomeplus.grpc.protobuf.GroupCollectionServices.GroupCollection.newBuilder();
		
		String groupId = dbGroupCollection.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			builder.setGroupId(groupId);
		}
		builder.setUserId(dbGroupCollection.getUserId());
		builder.setUpdateTime(dbGroupCollection.getUpdateTime());
		builder.setIsDel(dbGroupCollection.getIsDel());
		return builder.build();
	}
	
}
