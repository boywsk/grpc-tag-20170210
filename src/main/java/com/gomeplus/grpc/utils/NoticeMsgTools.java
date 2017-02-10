package com.gomeplus.grpc.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gomeplus.grpc.model.ProtoIM.AddFriendMsg;
import com.gomeplus.grpc.model.ProtoIM.AgreeFriendMsg;
import com.gomeplus.grpc.model.ProtoIM.ApplyJoinGroupMsg;
import com.gomeplus.grpc.model.ProtoIM.ChangeGroupManagerMsg;
import com.gomeplus.grpc.model.ProtoIM.DelFriendMsg;
import com.gomeplus.grpc.model.ProtoIM.DisbandGroupMsg;
import com.gomeplus.grpc.model.ProtoIM.EditGroupMsg;
import com.gomeplus.grpc.model.ProtoIM.InvitedJoinGroupMsg;
import com.gomeplus.grpc.model.ProtoIM.IssueRevokeMsg;
import com.gomeplus.grpc.model.ProtoIM.NoticeApplicantMsg;
import com.gomeplus.grpc.model.ProtoIM.NoticeManagerMsg;
import com.gomeplus.grpc.model.ProtoIM.NoticeMsg;
import com.gomeplus.grpc.model.ProtoIM.QuitGroupMsg;
import com.gomeplus.grpc.model.ProtoIM.ScanQRJoinGroupMsg;
import com.gomeplus.grpc.model.ProtoIM.UserModifyMsg;
import com.gomeplus.grpc.model.SaveNoticeMsg;
import com.gomeplus.grpc.pbModel.AddFriendMsgModel;
import com.gomeplus.grpc.pbModel.AgreeFriendMsgModel;
import com.gomeplus.grpc.pbModel.ApplyJoinGroupMsgModel;
import com.gomeplus.grpc.pbModel.ChangeGroupManagerMsgModel;
import com.gomeplus.grpc.pbModel.DelFriendMsgModel;
import com.gomeplus.grpc.pbModel.DisbandGroupMsgModel;
import com.gomeplus.grpc.pbModel.EditGroupMsgModel;
import com.gomeplus.grpc.pbModel.InvitedJoinGroupMsgModel;
import com.gomeplus.grpc.pbModel.IssueRevokeModel;
import com.gomeplus.grpc.pbModel.NoticeApplicantMsgModel;
import com.gomeplus.grpc.pbModel.NoticeManagerMsgModel;
import com.gomeplus.grpc.pbModel.QuitGroupMsgModel;
import com.gomeplus.grpc.pbModel.ScanQRJoinGroupMsgModel;
import com.gomeplus.grpc.pbModel.UserModifyMsgModel;
import com.google.common.base.Strings;

public class NoticeMsgTools {
	private final static Logger log = LoggerFactory.getLogger(NoticeMsgTools.class);
	
	/**
	 * NoticeMsg转pb
	 * @param model
	 * @return
	 */
	public static NoticeMsg notice2PbMsg (SaveNoticeMsg model) {
		NoticeMsg.Builder msgBuilder = NoticeMsg.newBuilder();
		int noticeType = model.getNoticeType();
		String json = model.getNoticeMsgJson();
		msgBuilder.setNoticeType(noticeType);
		msgBuilder.setMsgId(model.getMsgId());
		switch (noticeType) {
			case 100:
				AddFriendMsgModel addFriend = JSON.parseObject(json, AddFriendMsgModel.class);
				AddFriendMsg addMsg = addFriend2PbMsg(addFriend);
				msgBuilder.setAddFriend(addMsg);
				break;
			case 101:
				DelFriendMsgModel delFriend = JSON.parseObject(json, DelFriendMsgModel.class);
				DelFriendMsg delMsg = delFriend2PbMsg(delFriend);
				msgBuilder.setDelFriend(delMsg);
				break;
			case 102:
				AgreeFriendMsgModel agreeFriend = JSON.parseObject(json, AgreeFriendMsgModel.class);
				AgreeFriendMsg agreeMsg = agreeFriend2PbMsg(agreeFriend);
				msgBuilder.setAgreeFriend(agreeMsg);
				break;
			case 200:
				ApplyJoinGroupMsgModel applyJoin = JSON.parseObject(json, ApplyJoinGroupMsgModel.class);
				ApplyJoinGroupMsg applyJoinMsg = applyJoinGroup2PbMsg(applyJoin);
				msgBuilder.setApplyJoinGroup(applyJoinMsg);
				break;
			case 201:
				NoticeManagerMsgModel noticeManager = JSON.parseObject(json, NoticeManagerMsgModel.class);
				NoticeManagerMsg noticeManagerMsg = noticeManager2PbMsg(noticeManager);
				msgBuilder.setNoticeManager(noticeManagerMsg);
				break;
			case 202:
				InvitedJoinGroupMsgModel invitedJoin = JSON.parseObject(json, InvitedJoinGroupMsgModel.class);
				InvitedJoinGroupMsg invitedJoinMsg = invitedJoinGroup2PbMsg(invitedJoin);
				msgBuilder.setInvitedJoinGroup(invitedJoinMsg);
				break;
			case 203:
				NoticeApplicantMsgModel noticeApplicant = JSON.parseObject(json, NoticeApplicantMsgModel.class);
				NoticeApplicantMsg noticeApplicantMsg = noticeApplicant2PbMsg(noticeApplicant);
				msgBuilder.setNoticeApplicant(noticeApplicantMsg);
				break;
			case 204:
				QuitGroupMsgModel quitGroup = JSON.parseObject(json, QuitGroupMsgModel.class);
				QuitGroupMsg quitGroupMsg = quitGroup2PbMsg(quitGroup);
				msgBuilder.setQuitGroup(quitGroupMsg);
				break;
			case 205:
				EditGroupMsgModel editGroup = JSON.parseObject(json, EditGroupMsgModel.class);
				EditGroupMsg editGroupMsg = editGroup2PbMsg(editGroup);
				msgBuilder.setEditGroup(editGroupMsg);
				break;
			case 206:
				DisbandGroupMsgModel disbandGroup = JSON.parseObject(json, DisbandGroupMsgModel.class);
				DisbandGroupMsg disbandGroupMsg = disbandGroup2PbMsg(disbandGroup);
				msgBuilder.setDisbandGroup(disbandGroupMsg);
				break;
			case 207:
				ChangeGroupManagerMsgModel changeGroupManager = JSON.parseObject(json, ChangeGroupManagerMsgModel.class);
				ChangeGroupManagerMsg changeGroupManagerMsg = changeGroupManager2PbMsg(changeGroupManager);
				msgBuilder.setChangeGroupManager(changeGroupManagerMsg);
				break;
			case 208:
				UserModifyMsgModel userModify = JSON.parseObject(json, UserModifyMsgModel.class);
				UserModifyMsg userModifyMsg = userModify2PbMsg(userModify);
				msgBuilder.setUserModify(userModifyMsg);
				break;
			case 209:
				IssueRevokeModel issueRevoke = JSON.parseObject(json, IssueRevokeModel.class);
				IssueRevokeMsg issueRevokeMsg = issueRevoke2PbMsg(issueRevoke);
				msgBuilder.setIssueRevoke(issueRevokeMsg);
				break;
			case 210:
				ScanQRJoinGroupMsgModel scanQRJoinGroup = JSON.parseObject(json, ScanQRJoinGroupMsgModel.class);
				ScanQRJoinGroupMsg scanQRJoinGroupMsg = scanQRJoinGroup2PbMsg(scanQRJoinGroup);
				msgBuilder.setScanQRJoinGroup(scanQRJoinGroupMsg);
				break;
			default :
				log.error("not find noticeType!!!");
		}
		
		return msgBuilder.build();
	}
	
	/**
	 * 加好友转pb
	 * @param model
	 * @return
	 */
	public static AddFriendMsg addFriend2PbMsg (AddFriendMsgModel model) {
		AddFriendMsg.Builder msgBuilder = AddFriendMsg.newBuilder();
		msgBuilder.setFromUid(model.getFromUid());
		String fromName = model.getFromName();
		if(!Strings.isNullOrEmpty(fromName)) {
			msgBuilder.setFromName(fromName);
		}
		msgBuilder.setToUid(model.getToUid());
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)) {
			msgBuilder.setContent(content);
		}
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		
		return msgBuilder.build();
	}
	
	/**
	 * 加好友转javaBean
	 * @param model
	 * @return
	 */
	public static AddFriendMsgModel pbMsg2AddFriend (AddFriendMsg msg) {
		AddFriendMsgModel model = new AddFriendMsgModel();
		model.setFromUid(msg.getFromUid());
		String fromName = msg.getFromName();
		if(!Strings.isNullOrEmpty(fromName)){
			model.setFromName(msg.getFromName());
		}
		model.setToUid(msg.getToUid());
		String content = msg.getContent();
		if(!Strings.isNullOrEmpty(content)){
			model.setContent(msg.getContent());
		}
		model.setOptTime(System.currentTimeMillis());
		if(msg.hasExtra()) {
			model.setExtra(msg.getExtra());
		}
		
		return model;
	}
	
	/**
	 * 删好友转pb
	 * @param model
	 * @return
	 */
	public static DelFriendMsg delFriend2PbMsg (DelFriendMsgModel model) {
		DelFriendMsg.Builder msgBuilder = DelFriendMsg.newBuilder();
		msgBuilder.setFromUid(model.getFromUid());
		msgBuilder.setToUid(model.getToUid());
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)) {
			msgBuilder.setContent(content);
		}
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		
		return msgBuilder.build();
	}
	
	/**
	 * 删好友转javaBean
	 * @param msg
	 * @return
	 */
	public static DelFriendMsgModel pbMsg2AddFriend (DelFriendMsg msg) {
		DelFriendMsgModel model = new DelFriendMsgModel();
		model.setFromUid(msg.getFromUid());
		model.setToUid(msg.getToUid());
		model.setContent(msg.getContent());
		model.setOptTime(System.currentTimeMillis());
		if(msg.hasExtra()) {
			model.setExtra(msg.getExtra());
		}
		
		return model;
	}
	
	/**
	 * 是否同意对方加为好友转pb
	 * @param model
	 * @return
	 */
	public static AgreeFriendMsg agreeFriend2PbMsg (AgreeFriendMsgModel model) {
		AgreeFriendMsg.Builder msgBuilder = AgreeFriendMsg.newBuilder();
		msgBuilder.setFromUid(model.getFromUid());
		String fromName = model.getFromName();
		if(!Strings.isNullOrEmpty(fromName)) {
			msgBuilder.setFromName(fromName);
		}
		msgBuilder.setToUid(model.getToUid());
		msgBuilder.setAgreeType(model.getAgreeType());
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)) {
			msgBuilder.setContent(content);
		}
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		
		return msgBuilder.build();
	}
	
	/**
	 * 是否同意对方加为好友转javaBean
	 * @param msg
	 * @return
	 */
	public static AgreeFriendMsgModel pbMsg2AgreeFriend (AgreeFriendMsg msg) {
		AgreeFriendMsgModel model = new AgreeFriendMsgModel();
		model.setFromUid(msg.getFromUid());
		model.setFromName(msg.getFromName());
		model.setToUid(msg.getToUid());
		model.setAgreeType(msg.getAgreeType());
		model.setContent(msg.getContent());
		model.setOptTime(System.currentTimeMillis());
		if(msg.hasExtra()) {
			model.setExtra(msg.getExtra());
		}
		
		return model;
	}
	
	/**
	 * 申请加入群转pb
	 * @param model
	 * @return
	 */
	public static ApplyJoinGroupMsg applyJoinGroup2PbMsg(ApplyJoinGroupMsgModel model) {
		ApplyJoinGroupMsg.Builder msgBuilder = ApplyJoinGroupMsg.newBuilder();
		msgBuilder.setApplicantId(model.getApplicantId());
		if(!Strings.isNullOrEmpty(model.getApplicantName())){
			msgBuilder.setApplicantName(model.getApplicantName());
		}
		msgBuilder.setGroupId(model.getGroupId());
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)) {
			msgBuilder.setContent(content);
		}
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		
		return msgBuilder.build();
	}
	
	/**
	 * 申请加入群转javaBean
	 * @param msg
	 * @return
	 */
	public static ApplyJoinGroupMsgModel pbMsg2ApplyJoinGroup (ApplyJoinGroupMsg msg) {
		ApplyJoinGroupMsgModel model = new ApplyJoinGroupMsgModel();
		model.setApplicantId(msg.getApplicantId());
		model.setApplicantName(msg.getApplicantName());
		model.setGroupId(msg.getGroupId());
		model.setContent(msg.getContent());
		model.setOptTime(System.currentTimeMillis());
		if(msg.hasExtra()) {
			model.setExtra(msg.getExtra());
		}
		
		return model;
	}
	
	/**
	 * 通知管理员审核加入成员转pb
	 * @param model
	 * @return
	 */
	public static NoticeManagerMsg noticeManager2PbMsg(NoticeManagerMsgModel model) {
		NoticeManagerMsg.Builder msgBuilder = NoticeManagerMsg.newBuilder();
		msgBuilder.setApplicantId(model.getApplicantId());
		if(!Strings.isNullOrEmpty(model.getApplicantName())){
			msgBuilder.setApplicantName(model.getApplicantName());
		}		
		msgBuilder.setGroupId(model.getGroupId());
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)) {
			msgBuilder.setContent(content);
		}
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		
		return msgBuilder.build();
	}
	
	/**
	 * 通知管理员审核加入成员转javaBean
	 * @param msg
	 * @return
	 */
	public static NoticeManagerMsgModel pbMsg2NoticeManager (NoticeManagerMsg msg) {
		NoticeManagerMsgModel model = new NoticeManagerMsgModel();
		model.setApplicantId(msg.getApplicantId());
		model.setApplicantName(msg.getApplicantName());
		model.setGroupId(msg.getGroupId());
		model.setContent(msg.getContent());
		model.setOptTime(System.currentTimeMillis());
		if(msg.hasExtra()) {
			model.setExtra(msg.getExtra());
		}
		
		
		return model;
	}
	
	/**
	 * 邀请加入群转pb
	 * @param model
	 * @return
	 */
	public static InvitedJoinGroupMsg invitedJoinGroup2PbMsg(InvitedJoinGroupMsgModel model) {
		InvitedJoinGroupMsg.Builder msgBuilder = InvitedJoinGroupMsg.newBuilder();
		msgBuilder.setFromUid(model.getFromUid());
		String fromName = model.getFromName();
		if(!Strings.isNullOrEmpty(fromName)) {
			msgBuilder.setFromName(fromName);
		}
		msgBuilder.setGroupId(model.getGroupId());
		List<Long> uids = model.getInvitedUids();
		if(uids != null) {
			msgBuilder.addAllInvitedUids(uids);
		}
		List<String> names = model.getInvitedNames();
		if(names != null) {
			msgBuilder.addAllInvitedNames(names);
		}
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)) {
			msgBuilder.setContent(content);
		}
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		
		return msgBuilder.build();
	}
	
	/**
	 * 通知管理员审核加入成员转javaBean
	 * @param msg
	 * @return
	 */
	public static InvitedJoinGroupMsgModel pbMsg2InvitedJoinGroup (InvitedJoinGroupMsg msg) {
		InvitedJoinGroupMsgModel model = new InvitedJoinGroupMsgModel();
		model.setFromUid(msg.getFromUid());
		model.setFromName(msg.getFromName());
		model.setInvitedUids(msg.getInvitedUidsList());
		model.setInvitedNames(msg.getInvitedNamesList());
		model.setGroupId(msg.getGroupId());
		model.setContent(msg.getContent());
		model.setOptTime(System.currentTimeMillis());
		if(msg.hasExtra()) {
			model.setExtra(msg.getExtra());
		}
		
		return model;
	}
	
	/**
	 * 通知被邀请加入群pb
	 * @param model
	 * @return
	 */
	public static NoticeApplicantMsg noticeApplicant2PbMsg(NoticeApplicantMsgModel model) {
		NoticeApplicantMsg.Builder msgBuilder = NoticeApplicantMsg.newBuilder();
		msgBuilder.setFromUid(model.getFromUid());
		String fromName = model.getFromName();
		if(!Strings.isNullOrEmpty(fromName)) {
			msgBuilder.setFromName(fromName);
		}
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)) {
			msgBuilder.setContent(content);
		}
		msgBuilder.setGroupId(model.getGroupId());
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		
		return msgBuilder.build();
	}
	
	/**
	 * 通知被邀请加入群转javaBean
	 * @param msg
	 * @return
	 */
	public static NoticeApplicantMsgModel pbMsg2NoticeApplicant (NoticeApplicantMsg msg) {
		NoticeApplicantMsgModel model = new NoticeApplicantMsgModel();
		model.setFromUid(msg.getFromUid());
		model.setFromName(msg.getFromName());
		model.setGroupId(msg.getGroupId());
		model.setContent(msg.getContent());
		model.setOptTime(System.currentTimeMillis());
		if(msg.hasExtra()) {
			model.setExtra(msg.getExtra());
		}
		
		return model;
	}
	
	/**
	 * 退/踢出群转pb
	 * @param model
	 * @return
	 */
	public static QuitGroupMsg quitGroup2PbMsg(QuitGroupMsgModel model) {
		QuitGroupMsg.Builder msgBuilder = QuitGroupMsg.newBuilder();
		msgBuilder.setQuitType(model.getQuitType());
		msgBuilder.setFromUid(model.getFromUid());
		String fromName = model.getFromName();
		if(!Strings.isNullOrEmpty(fromName)) {
			msgBuilder.setFromName(fromName);
		}
		List<Long> uids = model.getKickedUids();
		if(uids != null) {
			msgBuilder.addAllKickedUids(uids);
		}
		List<String> names = model.getKickedNames();
		if(names != null) {
			msgBuilder.addAllKickedNames(names);
		}
		msgBuilder.setGroupId(model.getGroupId());
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)) {
			msgBuilder.setContent(content);
		}
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		
		return msgBuilder.build();
	}
	
	/**
	 * 退/踢出群转javaBean
	 * @param msg
	 * @return
	 */
	public static QuitGroupMsgModel pbMsg2QuitGroup(QuitGroupMsg msg) {
		QuitGroupMsgModel model = new QuitGroupMsgModel();
		model.setQuitType(msg.getQuitType());
		model.setFromUid(msg.getFromUid());
		model.setFromName(msg.getFromName());
		model.setKickedUids(msg.getKickedUidsList());
		model.setKickedNames(msg.getKickedNamesList());
		model.setGroupId(msg.getGroupId());
		model.setContent(msg.getContent());
		model.setOptTime(System.currentTimeMillis());
		if(msg.hasExtra()) {
			model.setExtra(msg.getExtra());
		}
		
		return model;
	}
	
	/**
	 * 修改群信息转pb
	 * @param model
	 * @return
	 */
	public static EditGroupMsg editGroup2PbMsg(EditGroupMsgModel model) {
		EditGroupMsg.Builder msgBuilder = EditGroupMsg.newBuilder();
		msgBuilder.setFromUid(model.getFromUid());
		String fromName = model.getFromName();
		if(!Strings.isNullOrEmpty(fromName)) {
			msgBuilder.setFromName(fromName);
		}
		msgBuilder.setGroupId(model.getGroupId());
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)) {
			msgBuilder.setContent(content);
		}
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		
		return msgBuilder.build();
	}
	
	/**
	 * 修改群信息转javaBean
	 * @param msg
	 * @return
	 */
	public static EditGroupMsgModel pbMsg2EditGroup (EditGroupMsg msg) {
		EditGroupMsgModel model = new EditGroupMsgModel();
		model.setFromUid(msg.getFromUid());
		model.setFromName(msg.getFromName());
		model.setGroupId(msg.getGroupId());
		model.setContent(msg.getContent());
		model.setOptTime(System.currentTimeMillis());
		if(msg.hasExtra()) {
			model.setExtra(msg.getExtra());
		}
		
		return model;
	}
	
	/**
	 * 解散群转pb
	 * @param model
	 * @return
	 */
	public static DisbandGroupMsg disbandGroup2PbMsg(DisbandGroupMsgModel model) {
		DisbandGroupMsg.Builder msgBuilder = DisbandGroupMsg.newBuilder();
		msgBuilder.setFromUid(model.getFromUid());
		String fromName = model.getFromName();
		if(!Strings.isNullOrEmpty(fromName)) {
			msgBuilder.setFromName(fromName);
		}
		msgBuilder.setGroupId(model.getGroupId());
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)) {
			msgBuilder.setContent(content);
		}
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		
		return msgBuilder.build();
	}
	
	/**
	 * 解散群转javaBean
	 * @param msg
	 * @return
	 */
	public static DisbandGroupMsgModel pbMsg2DisbandGroup (DisbandGroupMsg msg) {
		DisbandGroupMsgModel model = new DisbandGroupMsgModel();
		model.setFromUid(msg.getFromUid());
		model.setFromName(msg.getFromName());
		model.setGroupId(msg.getGroupId());
		model.setContent(msg.getContent());
		model.setOptTime(System.currentTimeMillis());
		if(msg.hasExtra()) {
			model.setExtra(msg.getExtra());
		}
	
		return model;
	}
	
	/**
	 * 申请加入群 封装 GroupMsg
	 * @param msg
	 * @return
	 */
	/*public static GroupMsg applyJoinGroup2GroupMsg (MQMsg  msg) {
		GroupMsg groupMsg = new GroupMsg();
		NoticeMsgModel noticeMsgModel = msg.getNoticeMsgModel();
		ApplyJoinGroupMsgModel applyJoinGroupModle = noticeMsgModel.getApplyJoinGroup();
		groupMsg.setGroupId(applyJoinGroupModle.getGroupId());
		groupMsg.setGroupType(CHAT_TYPE.E_CHAT_TYP_GROUP.value); // 群组类型，1:单聊，2:群聊，3： 系统信息，4：小秘书，5：客服
		groupMsg.setMsgId(msg.getMsgId()); // 消息id
		groupMsg.setMsgType(MESSAGE_TYPE.E_MESSAG_TYPE_TEXT.value); // 1:文本、2:语音、3:图片、4:附件、5:分享/转发(通过url)、...
		String content = applyJoinGroupModle.getContent();
		if(!Strings.isNullOrEmpty(content)){
			groupMsg.setMsgBody(content); // 消息体
		}
		groupMsg.setFromUid(applyJoinGroupModle.getApplicantId()); // 发送者id
		String fromName = applyJoinGroupModle.getApplicantName();
		if(!Strings.isNullOrEmpty(fromName)){
			groupMsg.setFromName(applyJoinGroupModle.getApplicantName()); // 发送者名称
		}
		groupMsg.setSendTime(System.currentTimeMillis()); // 发送服务器时间
		String extra = applyJoinGroupModle.getExtra();
		if (!Strings.isNullOrEmpty(extra)) {
			groupMsg.setExtra(extra); // 扩展
		}
		return groupMsg;
	}*/
	
	/**
	 * 邀请加入群 封装 GroupMsg
	 * @param msg
	 * @return
	 */
//	public static GroupMsg invitedJoinGroup2GroupMsg (MQMsg  msg) {
//		GroupMsg groupMsg = new GroupMsg();
//		NoticeMsg noticeMsgModel = msg.getNoticeMsgModel();
//		InvitedJoinGroupMsg invitedJoinGroupMsgModel = noticeMsgModel.getInvitedJoinGroup();
//		groupMsg.setGroupId(invitedJoinGroupMsgModel.getGroupId()); // 群组id
//		groupMsg.setGroupType(CHAT_TYPE.E_CHAT_TYP_GROUP.value); // 群组类型，1:单聊，2:群聊，3： 系统信息，4：小秘书，5：客服
//		groupMsg.setMsgId(msg.getMsgId()); // 消息id
//		groupMsg.setMsgType(MESSAGE_TYPE.E_MESSAG_TYPE_TEXT.value); // 1:文本、2:语音、3:图片、4:附件、5:分享/转发(通过url)、...
//		groupMsg.setMsgBody(invitedJoinGroupMsgModel.getContent()); // 消息体
//		groupMsg.setFromUid(invitedJoinGroupMsgModel.getFromUid()); // 发送者id
//		String fromName = invitedJoinGroupMsgModel.getFromName();
//		if(!Strings.isNullOrEmpty(fromName)){
//			groupMsg.setFromName(fromName); // 发送者名称
//		}
//		groupMsg.setSendTime(System.currentTimeMillis()); // 发送服务器时间
//		String extra = invitedJoinGroupMsgModel.getExtra();
//		if (!Strings.isNullOrEmpty(extra)) {
//			groupMsg.setExtra(extra); // 扩展
//		}
//		return groupMsg;
//	}
	
	/**
	 * 通知被邀请加入群 封装 GroupMsg
	 * @param msg
	 * @return
	 */
	/*public static GroupMsg noticeApplicant2GroupMsg (MQMsg  msg) {
		GroupMsg groupMsg = new GroupMsg();
		NoticeMsgModel noticeMsgModel = msg.getNoticeMsgModel();
		NoticeApplicantMsgModel noticeApplicantMsgModel = noticeMsgModel.getNoticeApplicant();
		groupMsg.setGroupId(noticeApplicantMsgModel.getGroupId()); // 群组id
		groupMsg.setGroupType(CHAT_TYPE.E_CHAT_TYP_GROUP.value); // 群组类型，1:单聊，2:群聊
		groupMsg.setMsgId(msg.getMsgId()); // 消息id
		groupMsg.setMsgType(MESSAGE_TYPE.E_MESSAG_TYPE_TEXT.value); // 1:文本、2:语音、3:图片、4:附件、5:分享/转发(通过url)、...
		groupMsg.setMsgBody(noticeApplicantMsgModel.getContent()); // 消息体
		groupMsg.setFromUid(noticeApplicantMsgModel.getFromUid()); // 发送者id
		String fromName = noticeApplicantMsgModel.getFromName();
		if(!Strings.isNullOrEmpty(fromName)){
			groupMsg.setFromName(fromName); // 发送者名称
		}
		groupMsg.setSendTime(System.currentTimeMillis()); // 发送服务器时间
		String extra = noticeApplicantMsgModel.getExtra();
		if (!Strings.isNullOrEmpty(extra)) {
			groupMsg.setExtra(extra); // 扩展
		}
		return groupMsg;
	}*/
	
	/**
	 * 修改群信息 封装 GroupMsg
	 * @param msg
	 * @return
	 */
	/*public static GroupMsg editGroup2GroupMsg (MQMsg  msg) {
		GroupMsg groupMsg = new GroupMsg();
		NoticeMsgModel noticeMsgModel = msg.getNoticeMsgModel();
		EditGroupMsgModel editGroupMsgModel = noticeMsgModel.getEditGroup();
		groupMsg.setGroupId(editGroupMsgModel.getGroupId()); // 群组id
		groupMsg.setGroupType(CHAT_TYPE.E_CHAT_TYP_GROUP.value); // 群组类型，1:单聊，2:群聊
		groupMsg.setMsgId(msg.getMsgId()); // 消息id
		groupMsg.setMsgType(MESSAGE_TYPE.E_MESSAG_TYPE_TEXT.value); // 1:文本、2:语音、3:图片、4:附件、5:分享/转发(通过url)、...
		groupMsg.setMsgBody(editGroupMsgModel.getContent()); // 消息体
		groupMsg.setFromUid(editGroupMsgModel.getFromUid()); // 发送者id
		String fromName = editGroupMsgModel.getFromName();
		if(!Strings.isNullOrEmpty(fromName)){
			groupMsg.setFromName(fromName); // 发送者名称
		}
		groupMsg.setSendTime(System.currentTimeMillis()); // 发送服务器时间
		String extra = editGroupMsgModel.getExtra();
		if (!Strings.isNullOrEmpty(extra)) {
			groupMsg.setExtra(extra); // 扩展
		}
		return groupMsg;
	}*/
	
	/**
	 * 修改群主转pb
	 * @param model
	 * @return
	 */
	public static ChangeGroupManagerMsg changeGroupManager2PbMsg(ChangeGroupManagerMsgModel model) {
		ChangeGroupManagerMsg.Builder msgBuilder = ChangeGroupManagerMsg.newBuilder();
		msgBuilder.setFromUid(model.getFromUid());
		String fromName = model.getFromName();
		if(!Strings.isNullOrEmpty(fromName)) {
			msgBuilder.setFromName(fromName);
		}
		int toUid = (int) model.getToUid();
		if(toUid >= 0){
			msgBuilder.setToUid(toUid);
		}
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)) {
			msgBuilder.setContent(content);
		}
		msgBuilder.setGroupId(model.getGroupId());
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		
		return msgBuilder.build();
	}
	
	/**
	 * 用户修改信息pb
	 * @param model
	 * @return
	 */
	public static UserModifyMsg userModify2PbMsg(UserModifyMsgModel model) {
		UserModifyMsg.Builder msgBuilder = UserModifyMsg.newBuilder();
		msgBuilder.setFromUid(model.getFromUid());
		String fromName = model.getFromName();
		if(!Strings.isNullOrEmpty(fromName)) {
			msgBuilder.setFromName(fromName);
		}		
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)) {
			msgBuilder.setContent(content);
		}
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		return msgBuilder.build();
	}
	
	/**
	 * 撤消消息转pb
	 * @param model
	 * @return
	 */
	public static IssueRevokeMsg issueRevoke2PbMsg(IssueRevokeModel model) {
		IssueRevokeMsg.Builder msgBuilder = IssueRevokeMsg.newBuilder();
		long uid = model.getUid();
		if(uid > 0){
			msgBuilder.setUid(uid);
		}
		String nickName = model.getNickName();
		if(!Strings.isNullOrEmpty(nickName)) {
			msgBuilder.setNickName(nickName);
		}
		String groupId = model.getGroupId();
		if(!Strings.isNullOrEmpty(groupId)){
			msgBuilder.setGroupId(groupId);
		}
		String msgId = model.getMsgId();
		if(!Strings.isNullOrEmpty(msgId)) {
			msgBuilder.setMsgId(msgId);
		}
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		return msgBuilder.build();
	}
	/**
	 * 扫码加入群转pb
	 * @param model
	 * @return
	 */
	public static ScanQRJoinGroupMsg scanQRJoinGroup2PbMsg(ScanQRJoinGroupMsgModel model) {
		ScanQRJoinGroupMsg.Builder msgBuilder = ScanQRJoinGroupMsg.newBuilder();
		long scannerUid = model.getScannerUid();
		if(scannerUid > 0){
			msgBuilder.setScannerUid(scannerUid);
		}
		String scannerName = model.getScannerName();
		if(!Strings.isNullOrEmpty(scannerName)) {
			msgBuilder.setScannerName(scannerName);
		}
		long createQRUid = model.getCreateQRUid();
		if(createQRUid > 0){
			msgBuilder.setCreateQRUid(createQRUid);
		}
		String createQRName = model.getCreateQRName();
		if(!Strings.isNullOrEmpty(createQRName)){
			msgBuilder.setCreateQRName(createQRName);
		}
		String content = model.getContent();
		if(!Strings.isNullOrEmpty(content)){
			msgBuilder.setContent(content);
		}
		String groupId = model.getGroupId();
		if(!Strings.isNullOrEmpty(groupId)){
			msgBuilder.setGroupId(groupId);
		}		
		msgBuilder.setOptTime(model.getOptTime());
		String extra = model.getExtra();
		if(!Strings.isNullOrEmpty(extra)) {
			msgBuilder.setExtra(extra);
		}
		return msgBuilder.build();
	}
}
