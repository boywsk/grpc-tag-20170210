package com.gomeplus.grpc.model;

/**
 * 群组信息-存在redis中的
 * @author liuzhenhuan
 * @date 2016年6月30日 下午3:48:22 
 * @version V1.0  
 */
public class GroupInfo {
	private int isMsgBlock;//是否免打扰：0-否，1-是
	
	private long stickies;//置顶时间
	
	public GroupInfo() {
	}
	public int getIsMsgBlock() {
		return isMsgBlock;
	}

	public void setIsMsgBlock(int isMsgBlock) {
		this.isMsgBlock = isMsgBlock;
	}
	public long getStickies() {
		return stickies;
	}
	public void setStickies(long stickies) {
		this.stickies = stickies;
	}
	
	
}
