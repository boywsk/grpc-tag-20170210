package com.gomeplus.grpc.pbModel;

import java.io.Serializable;

public class ScanQRJoinGroupMsgModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected long scannerUid; // 主动扫码用户ID
	protected String scannerName; // 主动扫码用户昵称
	protected long createQRUid; // 被扫码用户ID
	protected String createQRName; // 被扫码用户昵称
	protected String content; // 内容
	protected String groupId; // 群组id
	protected long optTime; // 操作时间(毫秒)
	protected String extra; // 扩展

	public long getScannerUid() {
		return scannerUid;
	}

	public void setScannerUid(long scannerUid) {
		this.scannerUid = scannerUid;
	}

	public String getScannerName() {
		return scannerName;
	}

	public void setScannerName(String scannerName) {
		this.scannerName = scannerName;
	}

	public long getCreateQRUid() {
		return createQRUid;
	}

	public void setCreateQRUid(long createQRUid) {
		this.createQRUid = createQRUid;
	}

	public String getCreateQRName() {
		return createQRName;
	}

	public void setCreateQRName(String createQRName) {
		this.createQRName = createQRName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public long getOptTime() {
		return optTime;
	}

	public void setOptTime(long optTime) {
		this.optTime = optTime;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

}
