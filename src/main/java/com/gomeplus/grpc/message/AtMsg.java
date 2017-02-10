package com.gomeplus.grpc.message;

import java.io.Serializable;
import java.util.List;

/**
 * im@消息
 */
public class AtMsg implements Serializable {
	private static final long serialVersionUID = 1L;

	private int atType;// 消息@类型；1:指定某个/些人、2:所有人
	private List<Long> atUids;// 被@的用户id列表
	private String extra; // 扩展信息

	public int getAtType() {
		return atType;
	}

	public void setAtType(int atType) {
		this.atType = atType;
	}

	public List<Long> getAtUids() {
		return atUids;
	}

	public void setAtUids(List<Long> atUids) {
		this.atUids = atUids;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
}
