package com.gomeplus.grpc.utils;


import org.apache.commons.lang.StringUtils;

import com.gomeplus.grpc.message.MsgLocation;
import com.gomeplus.grpc.model.ProtoIM.ImMsgLocation;
import com.gomeplus.grpc.model.ProtoIM.KickUser;

/**
 * 消息对象和pb对象互转工具
 */
public class PbMsgTools {
	
	/**
	 * 位置对象转pb对象
	 * @return
	 */
	public static ImMsgLocation location2PbLocation(MsgLocation location) {
		ImMsgLocation.Builder pbLocation = ImMsgLocation.newBuilder();
		if(location == null) {
			return pbLocation.build();
		}
		String msgId = location.getMsgId();
		if (StringUtils.isNotBlank(msgId)) {
			pbLocation.setMsgId(msgId);
		}
		double longitude = location.getLongitude();
		pbLocation.setLongitude(longitude);
		
		double latitude = location.getLatitude();
		pbLocation.setLatitude(latitude);
		
		String imgUrl = location.getImgUrl();
		if(StringUtils.isNotBlank(imgUrl)) {
			pbLocation.setImgUrl(imgUrl);
		}
		
		String content = location.getContent();
		if(StringUtils.isNotBlank(content)) {
			pbLocation.setContent(content);
		}

		String extra = location.getExtra();
		if(StringUtils.isNotBlank(extra)) {
			pbLocation.setExtra(extra);
		}
		
		return pbLocation.build();
	}
	
	/**
	 * 位置pb对象转对象
	 * @return
	 */
	public static MsgLocation pbLocation2Location(ImMsgLocation pbLocation) {
		MsgLocation location = new MsgLocation();
		if(pbLocation == null) {
			return location;
		}
		String msgId = pbLocation.getMsgId();
		if (StringUtils.isNotBlank(msgId)) {
			location.setMsgId(msgId);
		}
		double longitude = pbLocation.getLongitude();
		location.setLongitude(longitude);
		double latitude = pbLocation.getLatitude();
		location.setLatitude(latitude);
		String imgUrl = pbLocation.getImgUrl();
		if (StringUtils.isNotBlank(imgUrl)) {
			location.setImgUrl(imgUrl);
		}
		String content = pbLocation.getContent();
		location.setContent(content);
		if(pbLocation.hasExtra()) {
			String extra = pbLocation.getExtra();
			if (StringUtils.isNotBlank(extra)) {
				location.setExtra(extra);
			}
		}
		
		return location;
	}
	
	/**
	 * 生成踢出用户pb包
	 * @param uid
	 * @param deviceId
	 * @return
	 */
	public static KickUser pbKickUser(long uid, String deviceId, String extra) {
		KickUser.Builder pbKickUser = KickUser.newBuilder();
		pbKickUser.setUid(uid);
		pbKickUser.setDeviceId(deviceId);
		pbKickUser.setExtra(extra);
		
		return pbKickUser.build();
	}
}
