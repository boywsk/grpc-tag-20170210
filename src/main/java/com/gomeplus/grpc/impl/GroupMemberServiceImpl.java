package com.gomeplus.grpc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.JSONScanner;
import com.gomeplus.grpc.global.Constant;
import com.gomeplus.grpc.model.GroupMember;
import com.gomeplus.grpc.mongo.GroupCollectionDao;
import com.gomeplus.grpc.mongo.GroupMemberDao;
import com.gomeplus.grpc.protobuf.GroupMemberSericeGrpc;
import com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMemberCollection;
import com.gomeplus.grpc.protobuf.GroupMemberServices.RequestGroupMember;
import com.gomeplus.grpc.protobuf.GroupMemberServices.RespnoseGroupMember;
import com.gomeplus.grpc.protobuf.GroupMemberServices.RespnoseGroupMember.Builder;
import com.gomeplus.grpc.utils.GomeImBeanUtils;

import io.grpc.stub.StreamObserver;

public class GroupMemberServiceImpl extends GroupMemberSericeGrpc.GroupMemberSericeImplBase{
	private static Logger logger = LoggerFactory.getLogger(GroupMemberServiceImpl.class);
	private GroupMemberDao GROUP_MEMBER_DAO=new GroupMemberDao();
	private GroupCollectionDao GROUP_COLLECTION_DAO=new  GroupCollectionDao();
	@Override
	public void saveGroupMember(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			GroupMember dBGroup=GomeImBeanUtils.convertDBGroupMemberFromPBGroupMember(request.getGroupMember());
			String appId=request.getAppId();
			long groupCreaterUid=request.getGroupCreaterUid();
			boolean isSaveToRedis=request.getIsSaveToRedis();
			if (dBGroup==null) {
				logger.info("saveGroupMember dBGroup is null appId:{},groupCreaterUid:{},isSaveToRedis:{},"
						+ "traceId:{},extra:{}",appId,groupCreaterUid,isSaveToRedis,traceId,extra);
			}else {
				logger.info("saveGroupMember appId:{},groupCreaterUid:{},isSaveToRedis:{},dBGroup:{},"
						+ "traceId:{},extra:{}",appId,groupCreaterUid,isSaveToRedis,JSON.toJSONString(dBGroup),traceId,extra);
			}
			boolean	isSuccess = GROUP_MEMBER_DAO.save(appId, dBGroup, groupCreaterUid, isSaveToRedis);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("saveGroupMember error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		logger.info("saveGroupMember success!");
		responseObserver.onCompleted();
	}


	@Override
	public void saveBacthGroupMembers(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			List<com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember> groupMembersList = request.getGroupMembersList();
			if (CollectionUtils.isEmpty(groupMembersList)) {
				builder.setSuccess(false);
				logger.error("saveBacthGroupMembers groupMembersList is empty,traceId:{},extra:{}",traceId,extra);
			}else {
				String appId=request.getAppId();
				long groupCreaterUid=request.getGroupCreaterUid();
				boolean isSaveToRedis=request.getIsSaveToRedis();
				List<GroupMember> dBGroupList=new ArrayList<GroupMember>();
				for (com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember pBGroupMember : groupMembersList) {
					dBGroupList.add(GomeImBeanUtils.convertDBGroupMemberFromPBGroupMember(pBGroupMember));
				}
				logger.info("saveBacthGroupMembers,appId:{},groupCreaterUid:{},"
						+ "isSaveToRedis:{},traceId:{},extra:{}",appId,groupCreaterUid,isSaveToRedis,traceId,extra);
				boolean isSuccess = GROUP_MEMBER_DAO.save(appId, dBGroupList, groupCreaterUid, isSaveToRedis);
				builder.setSuccess(isSuccess);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("saveBacthGroupMembers error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		
		logger.info("saveBacthGroupMembers success!");
		
	}

	@Override
	public void updateNickName(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			String nickName=request.getNickName();
			logger.info("updateNickName appId:{},groupId:{},userId:{},nickName:{},traceId:{},extra:{}"
																,appId,groupId,userId,nickName,traceId,extra);
			boolean isSuccess = GROUP_MEMBER_DAO.updateNickName(appId, groupId, userId, nickName);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("updateNickName error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateNickName success!");
	}

	@Override
	public void updateGroupNickname(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			String groupNickname=request.getGroupNickName();
			logger.info("updateGroupNickname appId:{},groupId:{},userId:{}"
					+ ",groupNickname:{},traceId:{},extra:{}",appId,groupId,userId,groupNickname,traceId,extra);
			boolean isSuccess = GROUP_MEMBER_DAO.updateGroupNickname(appId, groupId, userId, groupNickname);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("updateGroupNickname error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateGroupNickname success!");
	}

	@Override
	public void updateIdentity(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			int identity=request.getIdentity();
			logger.info("updateIdentity appId:{},groupId:{},userId:{},identity:{}"
					+ ",traceId:{},extra:{}",appId,groupId,userId,identity,traceId,extra);
			boolean isSuccess = GROUP_MEMBER_DAO.updateIdentity(appId, groupId, userId, identity);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("updateIdentity error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateIdentity success!");
	}

	@Override
	public void updateShield(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			int isShield=request.getIsShield();
			logger.info("updateShield appId:{},groupId:{},userId:{},isShield:{}"
					+ ",traceId:{},extra:{}",appId,groupId,userId,isShield,traceId,extra);
			boolean isSuccess = GROUP_MEMBER_DAO.updateShield(appId, groupId, userId, isShield);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("updateShield error exception",e);
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateShield success!");
	}

	@Override
	@Deprecated
	public void updateIsTop(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			int isTop=request.getIsTop();
			
			logger.info("updateIsTop appId:{},groupId:{},userId:{},isTop:{},traceId:{},extra:{}",appId,groupId,userId,isTop,traceId,extra);
			boolean isSuccess = GROUP_MEMBER_DAO.updateIsTop(appId, groupId, userId, isTop);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("updateIsTop error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateIsTop success!");
	}

	@Override
	public void updateBacthStatus(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			List<Long> userIdList=request.getMemberIdsList();
			if (CollectionUtils.isEmpty(userIdList)) {
				builder.setSuccess(false);
				logger.error("updateBacthStatus userIdList is empty ,appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			}else {
				long groupCreaterUid=request.getGroupCreaterUid();
				int status=request.getStatus();
				logger.info("updateBacthStatus ,appId:{},groupId:{},groupCreaterUid:{},status:{},traceId:{},extra:{}"
						,appId,groupId,groupCreaterUid,status,traceId,extra);
				boolean isSuccess = GROUP_MEMBER_DAO.updateBacthStatus(appId, groupId, userIdList, groupCreaterUid, status);
				builder.setSuccess(isSuccess);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("updateBacthStatus error exception ",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateBacthStatus success!");
	}
	
	@Override
	public void delGroupMemberByUserId(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			logger.info("delGroupMemberByUserId appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
			boolean isSuccess = GROUP_MEMBER_DAO.delGroupMember(appId, groupId, userId);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			logger.error("delGroupMemberByUserId error exceotion",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("delGroupMemberByUserId success!");
	}

	@Override
	public void delBatchGroupMembersByUids(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			List<Long> memberIdList=request.getMemberIdsList();
			if (CollectionUtils.isEmpty(memberIdList)) {
				builder.setSuccess(false);
				logger.error("delBatchGroupMembersByUids memberIdList is empty appId:{}"
						+ ",groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			}else {
				logger.error("delBatchGroupMembersByUids , appId:{}"
						+ ",groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
				boolean isSuccess = GROUP_MEMBER_DAO.delGroupMembers(appId, groupId, memberIdList);
				builder.setSuccess(isSuccess);
			}
			
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("delBatchGroupMembersByUids error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("delBatchGroupMembersByUids success!");
	}

	@Override
	public void listGroupMembers(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			List<GroupMember> listGroupMembers = GROUP_MEMBER_DAO.listGroupMembers(appId, groupId);
			if (CollectionUtils.isNotEmpty(listGroupMembers)) {
				int i=0;
				for (GroupMember dBGroupMember : listGroupMembers) {
					builder.addGroupMembers(i++, GomeImBeanUtils.convertPBGroupMemberFromDBGroupMember(dBGroupMember));
				}
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("listGroupMembers error",e);
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("listGroupMembers success!");
	}

	@Override
	public void listGroupMembersPageByTimeAndStatus(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long time=request.getTime();
			int status=request.getStatus();
			int page=request.getPage();
			int size=request.getPageSize();
			logger.info("listGroupMembersPageByTimeAndStatus appId:{},groupId:{},time:{},status:{},page:{}"
					+ ",size:{},traceId:{},extra:{}",appId,groupId,time,status,page,size,traceId,extra);
			List<GroupMember> listGroupMembers =null;
			listGroupMembers = GROUP_MEMBER_DAO.listGroupMembers(appId, groupId, time, status, page, size);
			if (CollectionUtils.isEmpty(listGroupMembers)) {
				logger.info("listGroupMembersPageByTimeAndStatus listGroupMembers is empty appId:{},groupId:{},time:{},status:{},page:{}"
						+ ",size:{},traceId:{},extra:{}",appId,groupId,time,status,page,size,traceId,extra);
			}else {
				for (GroupMember dBGroupMember : listGroupMembers) {
					builder.addGroupMembers(GomeImBeanUtils.convertPBGroupMemberFromDBGroupMember(dBGroupMember));
				}
				logger.info("listGroupMembersPageByTimeAndStatus listGroupMembers:{}, appId:{},groupId:{},time:{},status:{},page:{}"
						+ ",size:{},traceId:{},extra:{}",appId,groupId,time,status,page,size,traceId,extra);
				
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("listGroupMembersPageByTimeAndStatus error fail exception!",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("listGroupMembersPageByTimeAndStatus success!");
		
	}

	@Override
	public void getGroupMemberByUid(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder =RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			logger.info("getGroupMemberByUid appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
			GroupMember dBGroupMember = GROUP_MEMBER_DAO.getGroupMemberByUid(appId, groupId, userId);
			logger.info("getGroupMemberByUid dBGroupMember:{},appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
			if (dBGroupMember==null) {
				logger.info("getGroupMemberByUid dBGroupMember is empty,appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
			}else {
				com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember pbGroupMember = GomeImBeanUtils.convertPBGroupMemberFromDBGroupMember(dBGroupMember);
				builder.setGroupMember(pbGroupMember);
				logger.info("getGroupMemberByUid dBGroupMember:{},appId:{},groupId:{},userId:{},traceId:{},extra:{}",JSON.toJSONString(dBGroupMember),appId,groupId,userId,traceId,extra);
			}
			builder.setSuccess(true);
			
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupMemberByUid error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupMemberByUid success!");
		
	}
	
	@Override
	public void getGroupMemberByUidContainStatus(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder =RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			logger.info("getGroupMemberByUidContainStatus appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
			GroupMember dBGroupMember = GROUP_MEMBER_DAO.getGroupMemberByUidContainStatus(appId, groupId, userId);
			logger.info("getGroupMemberByUidContainStatus dBGroupMember:{},appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
			if (dBGroupMember==null) {
				logger.info("getGroupMemberByUidContainStatus dBGroupMember is empty,appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
			}else {
				com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember pbGroupMember = GomeImBeanUtils.convertPBGroupMemberFromDBGroupMember(dBGroupMember);
				builder.setGroupMember(pbGroupMember);
				logger.info("getGroupMemberByUidContainStatus dBGroupMember:{},appId:{},groupId:{},userId:{},traceId:{},extra:{}",JSON.toJSONString(dBGroupMember),appId,groupId,userId,traceId,extra);
			}
			builder.setSuccess(true);
			
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupMemberByUidContainStatus error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupMemberByUidContainStatus success!");
	}
	

	@Override
	public void getGroupMemberByUids(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			List<Long> idList=request.getMemberIdsList();
			int status = request.getStatus();
			if (CollectionUtils.isEmpty(idList)) {
				builder.setSuccess(false);
				logger.error("getGroupMemberByUids idList is empty,appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			}else {
				List<GroupMember> listGroupMembers = GROUP_MEMBER_DAO.getGroupMemberByUidList(appId, groupId, idList,status);
				if (CollectionUtils.isEmpty(listGroupMembers)) {
					logger.error("getGroupMemberByUids listGroupMembers is empty,appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
				}else {
					for (GroupMember dBGroupMember : listGroupMembers) {
						builder.addGroupMembers(GomeImBeanUtils.convertPBGroupMemberFromDBGroupMember(dBGroupMember));
					}
					logger.info("getGroupMemberByUids,appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
				}
				builder.setSuccess(true);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.info("getGroupMemberByUids error exception ",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupMemberByUids success!");
		
	}

	@Override
	public void listMemberGroups(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			long userId=request.getUserId();
			logger.info("listMemberGroups appId:{},userId:{},traceId:{},extra:{}",appId,userId,traceId,extra);
			List<GroupMember> listGroupMembers = GROUP_MEMBER_DAO.listMemberGroups(appId, userId);
			if (CollectionUtils.isEmpty(listGroupMembers)) {
				logger.info("listMemberGroups listGroupMembers is empty appId:{},userId:{},traceId:{},extra:{}",appId,userId,traceId,extra);

			}else {
				for (GroupMember dBGroupMember : listGroupMembers) {
					builder.addGroupMembers(GomeImBeanUtils.convertPBGroupMemberFromDBGroupMember(dBGroupMember));
				}
				logger.info("listMemberGroups,appId:{},userId:{},traceId:{},extra:{}",appId,userId,traceId,extra);
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("listMemberGroups error exception!",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("listMemberGroups success!");
	}

	@Override
	public void countGroupMemberByGroupId(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long time=request.getTime();
			int status = request.getStatus();
			logger.info("countGroupMemberByGroupId appId:{},groupId:{},traceId:{},extra:{},status:{}",appId,groupId,traceId,extra,status);
			long countGroupMember = GROUP_MEMBER_DAO.countGroupMember(appId, groupId,time,status);
			builder.setGroupMemberCount(countGroupMember);
			builder.setSuccess(true);
			logger.info("countGroupMemberByGroupId countGroupMember:{},appId:{},groupId:{},traceId:{},extra:{}",countGroupMember,appId,groupId,traceId,extra);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("countGroupMemberByGroupId error exception",e);
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("countGroupMemberByGroupId success!");
		
	}

	@Override
	public void countGroupMemberFromRedis(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId = request.getGroupId();
			logger.info("countGroupMemberFromRedis appId:{},traceId:{},extra:{},status:{}",appId,traceId,extra);
			long count = GROUP_MEMBER_DAO.countGroupMemberFromRedis(appId, groupId);
			builder.setGroupMemberCount(count);
			builder.setSuccess(true);
			logger.info("countGroupMemberFromRedis appId:{}traceId:{},extra:{}",appId,traceId,extra);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("countGroupMemberFromRedis error exception",e);
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("countGroupMemberByGroupId success!");
	}


	@Override
	public void delGroupAllMember(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			logger.info("delGroupAllMember appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			boolean isSuccess = GROUP_MEMBER_DAO.delGroupAllMember(appId, groupId);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("delGroupAllMember error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("delGroupAllMember success!");
	}

	@Override
	public void listMemberMaxSeq(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			long uid=request.getUserId();
			logger.info("listMemberMaxSeq appId:{},uid:{},traceId:{},extra:{}",appId,uid,traceId,extra);
			Map<String, String> memberMaxSeqList = GROUP_MEMBER_DAO.listMemberMaxSeq(NumberUtils.toInt(appId), uid);
			if (MapUtils.isEmpty(memberMaxSeqList)) {
				builder.putAllMemberMaxSeqMap(new HashMap<String, String>());
				logger.info("listMemberMaxSeq memberMaxSeqList is empty appId:{},uid:{},traceId:{},extra:{}",appId,uid,traceId,extra);
			}else{
				logger.info("listMemberMaxSeq,appId:{},uid:{},traceId:{},extra:{}",appId,uid,traceId,extra);
				builder.putAllMemberMaxSeqMap(memberMaxSeqList);
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("listMemberMaxSeq error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("listMemberMaxSeq success!");
	}

	@Override
	public void listMemberSeq(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			long uid=request.getUserId();
			byte clientId=(byte) request.getClientId();
			int groupSize = request.getGroupSize();
			logger.info("listMemberSeq appId:{},uid:{},clientId:{},traceId:{},extra:{},groupSize:{}",appId,uid,clientId,traceId,extra,groupSize);

			List<GroupMember> members = GROUP_MEMBER_DAO.listMemberSeq(NumberUtils.toInt(appId), uid, clientId,groupSize);
			if (CollectionUtils.isEmpty(members)) {
				logger.info("listMemberSeq members is empty appId:{},uid:{},clientId:{},traceId:{},extra:{}",appId,uid,clientId,traceId,extra);
			}else {
				for (GroupMember pBGroupMember : members) {
					builder.addGroupMembers(GomeImBeanUtils.convertPBGroupMemberFromDBGroupMember(pBGroupMember));
				}
				logger.info("listMemberSeq success appId:{},uid:{},clientId:{},traceId:{},extra:{}",appId,uid,clientId,traceId,extra);
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.info("listMemberSeq error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("listMemberSeq success!");
	}
	//TODO 没有分表之前的方法，之后才删除
	@Override
	@Deprecated
	public void updateGroupCollection(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			long userId=request.getUserId();
			String groupId=request.getGroupId();
			int isCollection=request.getIsCollection();
			logger.info("updateGroupCollection appId:{},userId:{},groupId:{},isCollection:{},traceId:{},extra:{}"
											,appId,userId,groupId,isCollection,traceId,extra);
			
			boolean isUpdateGroupSuccess = GROUP_MEMBER_DAO.updateGroupCollection(appId, groupId, userId, isCollection);
			boolean isCollectSuccess=false;
			if (isCollection==Constant.GROUP_COLLECTION.COLLECTION_YES.value) {
				isCollectSuccess=GROUP_COLLECTION_DAO.updateOrInsertGroupCollection(appId, groupId
						,userId,Constant.GROUP_COLLECTION_ISDEL.COLLECTION_DEL_NO.value,-1L);
			}else {
				isCollectSuccess=GROUP_COLLECTION_DAO.updateOrInsertGroupCollection(appId, groupId
						,userId,Constant.GROUP_COLLECTION_ISDEL.COLLECTION_DEL_YES.value,-1L);
			}
			logger.info("updateGroupCollection appId:{},userId:{},groupId:{},isCollection:{},isUpdateGroupSuccess:{},isCollectSuccess:{},traceId:{},extra:{}"
					,appId,userId,groupId,isCollection,isUpdateGroupSuccess,isCollectSuccess,traceId,extra);
			builder.setSuccess(isUpdateGroupSuccess&&isCollectSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.info("updateGroupCollection error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateGroupCollection success!");
	}

	@Override
	@Deprecated
	public void getCollectionGroups(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		
		
	
	}


	@Override
	public void listMemberGroupIds(RequestGroupMember request, StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			long uid=request.getUserId();
			logger.info("listMemberGroupIds appId:{},uid:{},traceId:{},extra:{}",appId,uid,traceId,extra);
			//TODO 查询我的群组--单聊会有问题，-1只查redis中的数据，因为单聊数据有问题，在_useGroups中没有数据
			List<String> memberGroupIds = GROUP_MEMBER_DAO.listMemberGroupIds(appId, uid,-1);
			if (CollectionUtils.isEmpty(memberGroupIds)) {
				logger.info("listMemberGroupIds memberGroupIds is empty appId:{},uid:{},traceId:{},extra:{}",appId,uid,traceId,extra);
			}else {
				builder.addAllGroupIds(memberGroupIds);
				logger.info("listMemberGroupIds,appId:{},uid:{},traceId:{},extra:{}",appId,uid,traceId,extra);
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.info("listMemberGroupIds error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("listMemberSeq success!");
	}


	@Override
	public void listGroupMembersByGroupIds(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			List<com.gomeplus.grpc.protobuf.GomeplusRpcServices.ReqBatchGroupIdInfo> reqBatchGroupIdInfosList = request.getReqBatchGroupIdInfosList();
			if (CollectionUtils.isEmpty(reqBatchGroupIdInfosList)) {
				builder.setSuccess(false);
				logger.info("listGroupMembersByGroupIds reqBatchGroupIdInfosList is empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				List<GroupMember> listGroupMembers = GROUP_MEMBER_DAO.listGroupMembersByGroupIds(appId, reqBatchGroupIdInfosList);
				if (CollectionUtils.isEmpty(listGroupMembers)) {
					logger.info("listGroupMembersByGroupIds listGroupMembers is empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
				}else {
					for (GroupMember dBGroupMember : listGroupMembers) {
						builder.addGroupMembers(GomeImBeanUtils.convertPBGroupMemberFromDBGroupMember(dBGroupMember));
					}
					logger.info("listGroupMembersByGroupIds ,appId:{},traceId:{},extra:{}",appId,traceId,extra);
				}
				builder.setSuccess(true);
				
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("listGroupMembersByGroupIds error",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("listGroupMembersByGroupIds success!");
	}


	@Override
	public void getGroupId2GroupMemberMap(RequestGroupMember request, StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			List<com.gomeplus.grpc.protobuf.GomeplusRpcServices.ReqBatchGroupIdInfo> reqBatchGroupIdInfosList = request.getReqBatchGroupIdInfosList();
			if (CollectionUtils.isEmpty(reqBatchGroupIdInfosList)) {
				builder.setSuccess(false);
				logger.info("getGroupId2GroupMemberMap reqBatchGroupIdInfosList is empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				
				Map<String, GroupMemberCollection> group2GroupMemberMap = GROUP_MEMBER_DAO.getBatchIncreasePullGroupId2GroupMemberMap(appId, reqBatchGroupIdInfosList);
				if (MapUtils.isEmpty(group2GroupMemberMap)) {
					logger.info("getGroupId2GroupMemberMap group2GroupMemberMap is empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
				}else {
					builder.putAllGroupId2GroupMember(group2GroupMemberMap);
					logger.info("getGroupId2GroupMemberMap group2GroupMemberMap is not empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
				}
				builder.setSuccess(true);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupId2GroupMemberMap error",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupId2GroupMemberMap success!");
	}

	//仅数据同步使用，与业务无关
	@Override
	public void getAllGroupCollections(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			logger.info("listMemberGroups appId:{},traceId:{},extra:{}",appId,traceId,extra);
			List<GroupMember> listGroupMembers = GROUP_MEMBER_DAO.getAllGroupCollections(appId);
			if (CollectionUtils.isEmpty(listGroupMembers)) {
				logger.info("listMemberGroups listGroupMembers is empty appId:{},traceId:{},extra:{}",appId,traceId,extra);

			}else {
				for (GroupMember dBGroupMember : listGroupMembers) {
					builder.addGroupMembers(GomeImBeanUtils.convertPBGroupMemberFromDBGroupMember(dBGroupMember));
				}
				logger.info("listMemberGroups appId:{},userId:{},traceId:{},extra:{}",appId,traceId,extra);
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("listMemberGroups error exception!",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("listMemberGroups success!");
		
	}


	@Override
	public void updateStatusDelByGroupId(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			logger.info("delGroupAllMember appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			boolean isSuccess = GROUP_MEMBER_DAO.updateDelStatusByGroupId(appId, groupId);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("delGroupAllMember error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("delGroupAllMember success!");
	}


	@Override
	public void countGroupMemberByGroupIds(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			List<String> groupIdList=request.getGroupIdsList();
			logger.info("countGroupMemberByGroupId appId:{},traceId:{},extra:{},status:{}",appId,traceId,extra);
			Map<String, Long> map = GROUP_MEMBER_DAO.countGroupMemberByGroupIds(appId, groupIdList);
			builder.putAllGroupId2Count(map);
			builder.setSuccess(true);
			logger.info("countGroupMemberByGroupId appId:{}traceId:{},extra:{}",appId,traceId,extra);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("countGroupMemberByGroupId error exception",e);
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("countGroupMemberByGroupId success!");
		
		
	}


	@Override
	public void updateStickie(RequestGroupMember request, StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			int stickie=request.getStickie();
			
			logger.info("updateStickie appId:{},groupId:{},userId:{},stickie:{},traceId:{},extra:{}",appId,groupId,userId,stickie,traceId,extra);
			boolean isSuccess = GROUP_MEMBER_DAO.updateStickie(appId, groupId, userId, stickie);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("updateStickie error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateStickie success!");
		
	}


	@Override
	public void getGroupMembersByUidAndGroupIds(RequestGroupMember request, StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			long userId=request.getUserId();
			List<String> groupIds=request.getGroupIdsList();
			if (CollectionUtils.isEmpty(groupIds)) {
				builder.setSuccess(false);
				logger.info("getGroupsByGroupIds appId:{},groupIds is empty,traceId:{},extra:{}",appId,traceId,extra);
			}else {
				logger.info("getGroupsByGroupIds appId:{},traceId:{},extra:{}",appId,traceId,extra);
				List<GroupMember> dbGroupMembers = GROUP_MEMBER_DAO.getGroupMembersByUidAndGroupIds(appId, userId, groupIds);
				if (CollectionUtils.isEmpty(dbGroupMembers)) {
					logger.info("getGroupsByGroupIds dbGroupMembers is empty appId:{},traceId:{},extra:{}",appId,traceId,extra);
				}else {
					for (GroupMember dbGroupMember : dbGroupMembers) {
						builder.addGroupMembers(GomeImBeanUtils.convertPBGroupMemberFromDBGroupMember(dbGroupMember));
					}
				}
				builder.setSuccess(true);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupsByGroupIds error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateStickie success!");
	}


	@Override
	public void getEarliestAddGroupMember(RequestGroupMember request,StreamObserver<RespnoseGroupMember> responseObserver) {
		Builder builder = RespnoseGroupMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			logger.info("getEarliestAddGroupMember appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			GroupMember groupMember = GROUP_MEMBER_DAO.getEarliestAddGroupMember(appId, groupId);
			if (groupMember==null) {
				logger.info("getEarliestAddGroupMember groupMember is null appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			}else {
				builder.setGroupMember(GomeImBeanUtils.convertPBGroupMemberFromDBGroupMember(groupMember));
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getEarliestAddGroupMember error",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getEarliestAddGroupMember success!");
	}


	
	
}
