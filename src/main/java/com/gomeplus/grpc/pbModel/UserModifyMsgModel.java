package com.gomeplus.grpc.pbModel;

import java.io.Serializable;
import java.util.List;

public class UserModifyMsgModel implements Serializable {
	private static final long serialVersionUID = 1L;
	protected long fromUid; // 触发用户ID
	protected String fromName; // 触发用户昵称
	protected int modifyType; // 修改类型：1、昵称；2、头像；3、签名；4、地区；5、二维码；6、出生日期；7、性别(暂时不需要此字段，与客户端协商，使用extra放Json数据)
	protected List<Long> toUids; // 接收消息用户ID，即用户好友
	protected List<String> toNames; // 用户好友昵称【考虑是否删除】
	protected String content; // 内容
	protected long optTime; // 操作时间(毫秒)
	protected String extra; // 扩展

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

	public int getModifyType() {
		return modifyType;
	}

	public void setModifyType(int modifyType) {
		this.modifyType = modifyType;
	}

	public List<Long> getToUids() {
		return toUids;
	}

	public void setToUids(List<Long> toUids) {
		this.toUids = toUids;
	}

	public List<String> getToNames() {
		return toNames;
	}

	public void setToNames(List<String> toNames) {
		this.toNames = toNames;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
