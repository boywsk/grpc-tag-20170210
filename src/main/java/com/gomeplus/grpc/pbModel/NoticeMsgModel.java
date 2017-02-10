package com.gomeplus.grpc.pbModel;

import java.io.Serializable;

public class NoticeMsgModel implements Serializable {
	/**
	 * 功能消息 CDM:0x0100
	 */
	private static final long serialVersionUID = 1L;
	// private String msgId; // 消息id
	// private short cmd;
	private int noticeType;// 通知； 100:申请添加好友、101:删除好友、102:同意/拒绝好友申请，
							// 200:申请加入群、201:通知管理员审核加入成员、202:邀请加入群
							// 、203:通知被邀请加入群、204:退/踢出群、205:修改群信息、206:解散群
							//207:变更群主通知、208：用户修改通知
	private AddFriendMsgModel addFriend; // 添加好友
	private DelFriendMsgModel delFriend; // 删除好友
	private AgreeFriendMsgModel agreeFriend;// 是否同意对方加为好友
	private ApplyJoinGroupMsgModel applyJoinGroup; // 申请加入群
	private NoticeManagerMsgModel noticeManager; // 通知管理员审核加入成员
	private InvitedJoinGroupMsgModel invitedJoinGroup; // 邀请加入群
	private NoticeApplicantMsgModel noticeApplicant; // 通知被邀请加入群
	private QuitGroupMsgModel quitGroup; // 退/踢出群
	private EditGroupMsgModel editGroup; // 修改群信息
	private DisbandGroupMsgModel disbandGroup; // 解散群
	private ChangeGroupManagerMsgModel changeGroupManager;// 变更群主通知
	private UserModifyMsgModel userModify;// 用户修改通知
	private IssueRevokeModel issueRevoke;

	public int getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
	}

	public AddFriendMsgModel getAddFriend() {
		return addFriend;
	}

	public void setAddFriend(AddFriendMsgModel addFriend) {
		this.addFriend = addFriend;
	}

	public DelFriendMsgModel getDelFriend() {
		return delFriend;
	}

	public void setDelFriend(DelFriendMsgModel delFriend) {
		this.delFriend = delFriend;
	}

	public AgreeFriendMsgModel getAgreeFriend() {
		return agreeFriend;
	}

	public void setAgreeFriend(AgreeFriendMsgModel agreeFriend) {
		this.agreeFriend = agreeFriend;
	}

	public ApplyJoinGroupMsgModel getApplyJoinGroup() {
		return applyJoinGroup;
	}

	public void setApplyJoinGroup(ApplyJoinGroupMsgModel applyJoinGroup) {
		this.applyJoinGroup = applyJoinGroup;
	}

	public NoticeManagerMsgModel getNoticeManager() {
		return noticeManager;
	}

	public void setNoticeManager(NoticeManagerMsgModel noticeManager) {
		this.noticeManager = noticeManager;
	}

	public InvitedJoinGroupMsgModel getInvitedJoinGroup() {
		return invitedJoinGroup;
	}

	public void setInvitedJoinGroup(InvitedJoinGroupMsgModel invitedJoinGroup) {
		this.invitedJoinGroup = invitedJoinGroup;
	}

	public NoticeApplicantMsgModel getNoticeApplicant() {
		return noticeApplicant;
	}

	public void setNoticeApplicant(NoticeApplicantMsgModel noticeApplicant) {
		this.noticeApplicant = noticeApplicant;
	}

	public QuitGroupMsgModel getQuitGroup() {
		return quitGroup;
	}

	public void setQuitGroup(QuitGroupMsgModel quitGroup) {
		this.quitGroup = quitGroup;
	}

	public EditGroupMsgModel getEditGroup() {
		return editGroup;
	}

	public void setEditGroup(EditGroupMsgModel editGroup) {
		this.editGroup = editGroup;
	}

	public DisbandGroupMsgModel getDisbandGroup() {
		return disbandGroup;
	}

	public void setDisbandGroup(DisbandGroupMsgModel disbandGroup) {
		this.disbandGroup = disbandGroup;
	}

	public ChangeGroupManagerMsgModel getChangeGroupManager() {
		return changeGroupManager;
	}

	public void setChangeGroupManager(ChangeGroupManagerMsgModel changeGroupManager) {
		this.changeGroupManager = changeGroupManager;
	}

	public UserModifyMsgModel getUserModify() {
		return userModify;
	}

	public void setUserModify(UserModifyMsgModel userModify) {
		this.userModify = userModify;
	}

	public IssueRevokeModel getIssueRevoke() {
		return issueRevoke;
	}

	public void setIssueRevoke(IssueRevokeModel issueRevoke) {
		this.issueRevoke = issueRevoke;
	}

}
