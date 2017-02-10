package com.gomeplus.grpc.impl;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gomeplus.grpc.model.GroupQuitMember;
import com.gomeplus.grpc.mongo.GroupQuitMemberDao;
import com.gomeplus.grpc.protobuf.GomeplusRpcServices;
import com.gomeplus.grpc.protobuf.GroupQuitMemberServiceGrpc;
import com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMemberCollection;
import com.gomeplus.grpc.protobuf.GroupQuitMemberServices.RespnoseGroupQuitMember;
import com.gomeplus.grpc.protobuf.GroupQuitMemberServices.RespnoseGroupQuitMember.Builder;
import com.gomeplus.grpc.protobuf.GroupQuitMemberServices.RequestGroupQuitMember;
import com.gomeplus.grpc.utils.GomeImBeanUtils;

public class GroupQuitMemberServiceImpl extends GroupQuitMemberServiceGrpc.GroupQuitMemberServiceImplBase{
	private static Logger logger = LoggerFactory.getLogger(GroupQuitMemberServiceImpl.class);
	private GroupQuitMemberDao GROUP_QUIT_MEMBER_DAO=new GroupQuitMemberDao();
	
	@Override
	public void saveGroupQuitMember(RequestGroupQuitMember request,StreamObserver<RespnoseGroupQuitMember> responseObserver) {
		Builder builder = RespnoseGroupQuitMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			GroupQuitMember dBGroupQuitMember=GomeImBeanUtils.convertDBQuitMemberFromPBQuitMember(request.getGroupQuitMember());
			if (dBGroupQuitMember==null) {
				builder.setSuccess(false);
				logger.error("saveGroupQuitMember dBGroupQuitMember is null,appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				GROUP_QUIT_MEMBER_DAO.save(appId, dBGroupQuitMember);
				builder.setSuccess(true);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("saveGroupQuitMember error exception:",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("saveGroupQuitMember success!");
	}

	@Override
	public void saveGroupQuitMembers(RequestGroupQuitMember request,StreamObserver<RespnoseGroupQuitMember> responseObserver) {
		Builder builder = RespnoseGroupQuitMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			List<com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMember> pBGroupQuitMembersList = request.getGroupQuitMembersList();
			if (CollectionUtils.isEmpty(pBGroupQuitMembersList)) {
				builder.setSuccess(false);
				logger.error("saveGroupQuitMembers groupQuitMembersList is empty,traceId:{},extra:{}",traceId,extra);
			}else {
				List<GroupQuitMember> dBGroupQuitMembers=new ArrayList<GroupQuitMember>();
				for (com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMember pBGroupQuitMember : pBGroupQuitMembersList) {
					dBGroupQuitMembers.add(GomeImBeanUtils.convertDBQuitMemberFromPBQuitMember(pBGroupQuitMember));
				}
				String appId=request.getAppId();
				boolean isSuccess = GROUP_QUIT_MEMBER_DAO.saveGroupQuitMembers(appId, dBGroupQuitMembers);
				builder.setSuccess(isSuccess);
				logger.error("saveGroupQuitMembers dBGroupQuitMembers:{},traceId:{},extra:{}",JSON.toJSONString(dBGroupQuitMembers),traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("saveGroupQuitMembers error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("GroupMemberServiceImpl.saveGroupQuitMembers success!");
	}

	@Override
	public void delQuitMember(RequestGroupQuitMember request,StreamObserver<RespnoseGroupQuitMember> responseObserver) {
		Builder builder = RespnoseGroupQuitMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			logger.info("delQuitMember appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
			boolean isSuccess = GROUP_QUIT_MEMBER_DAO.delQuitMember(appId, groupId, userId);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("delQuitMember error reception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("delQuitMember success!");
	}

	@Override
	public void delQuitMembers(RequestGroupQuitMember request,StreamObserver<RespnoseGroupQuitMember> responseObserver) {
		Builder builder = RespnoseGroupQuitMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			List<Long> memberIdList=request.getMemberIdsList();
			if (CollectionUtils.isEmpty(memberIdList)) {
				builder.setSuccess(false);
				logger.error("getGroupQuitMember memberIdList is empty");
			}else {
				boolean isSuccess = GROUP_QUIT_MEMBER_DAO.delQuitMembers(appId, groupId, memberIdList);
				builder.setSuccess(isSuccess);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("delQuitMembers error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("delQuitMembers success!");
	}

	@Override
	public void getGroupQuitMember(RequestGroupQuitMember request,StreamObserver<RespnoseGroupQuitMember> responseObserver) {
		Builder builder = RespnoseGroupQuitMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			
			logger.info("getGroupQuitMember appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
			GroupQuitMember groupQuitMember = GROUP_QUIT_MEMBER_DAO.getGroupQuitMember(appId, groupId, userId);
			if (groupQuitMember==null) {
				builder.setSuccess(false);
				logger.info("getGroupQuitMember groupQuitMember is null ,appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
			}else {
				com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMember pbGroupQuitMember = GomeImBeanUtils.convertPBQuitMemberFromDBQuitMember(groupQuitMember);
				builder.setGroupQuitMember(pbGroupQuitMember);
				builder.setSuccess(true);
				logger.info("getGroupQuitMember groupQuitMember:{} appId:{},groupId:{},userId:{},traceId:{},extra:{}",JSON.toJSONString(groupQuitMember),appId,groupId,userId,traceId,extra);
			}
			
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupQuitMember error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupQuitMember success!");
	}

	@Override
	public void listGroupQuitMemberByGroupId(RequestGroupQuitMember request,StreamObserver<RespnoseGroupQuitMember> responseObserver) {
		Builder builder = RespnoseGroupQuitMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long time=request.getTime();
			logger.info("listGroupQuitMemberByGroupId appId:{},groupId:{},time:{},traceId:{},extra:{}",appId,groupId,time,traceId,extra);
			List<GroupQuitMember> dBGroupQuitMemberList = GROUP_QUIT_MEMBER_DAO.listGroupQuitMemberByGroupId(appId, groupId, time);
			if (CollectionUtils.isEmpty(dBGroupQuitMemberList)) {
				logger.info("listGroupQuitMemberByGroupId dBGroupQuitMemberList is empty, appId:{},groupId:{},time:{},traceId:{},extra:{}",appId,groupId,time,traceId,extra);
			}else {
				for (GroupQuitMember dBgroupQuitMember : dBGroupQuitMemberList) {
					builder.addGroupQuitMembers(GomeImBeanUtils.convertPBQuitMemberFromDBQuitMember(dBgroupQuitMember));
				}
				builder.setSuccess(true);
				logger.info("listGroupQuitMemberByGroupId dBGroupQuitMemberList:{},appId:{},groupId:{},time:{},traceId:{},extra:{}",JSON.toJSONString(dBGroupQuitMemberList),appId,groupId,time,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("listGroupQuitMemberByGroupId error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("listGroupQuitMemberByGroupId success!");
		
	}

	@Override
	public void getGroupId2GroupQuitMemberMap(RequestGroupQuitMember request,StreamObserver<RespnoseGroupQuitMember> responseObserver) {
		Builder builder = RespnoseGroupQuitMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			List<com.gomeplus.grpc.protobuf.GomeplusRpcServices.ReqBatchGroupIdInfo> reqBatchGroupIdInfosList = request.getReqBatchGroupIdInfosList();
			if (CollectionUtils.isEmpty(reqBatchGroupIdInfosList)) {
				builder.setSuccess(false);
				logger.info("GroupMemberQuitServiceImpl.reqBatchGroupIdInfosList is empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				Map<String, GroupQuitMemberCollection> group2GroupMemberMap =  GROUP_QUIT_MEMBER_DAO.getBatchIncreasePullGroupId2GroupQuitMemberMap(appId, reqBatchGroupIdInfosList);
				if (MapUtils.isEmpty(group2GroupMemberMap)) {
					logger.info("GroupMemberQuitServiceImpl.group2GroupMemberMap is empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
				}else {
					builder.putAllGroupId2GroupQuitMember(group2GroupMemberMap);
					logger.info("GroupMemberQuitServiceImpl.group2GroupMemberMap is not empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
				}
				builder.setSuccess(true);
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupId2GroupQuitMemberMap error exception",e);
			
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupId2GroupQuitMemberMap success!");
	}

	@Override
	public void countQuitGroupMember(RequestGroupQuitMember request,StreamObserver<RespnoseGroupQuitMember> responseObserver) {
		Builder builder = RespnoseGroupQuitMember.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long time=request.getTime();
			logger.info("countQuitGroupMember traceId:{},extra:{},appId:{},groupId:{},time:{}",traceId,extra,appId,groupId,time);
			long countQuitGroupMember = GROUP_QUIT_MEMBER_DAO.countQuitGroupMember(appId, groupId, time);
			logger.info("countQuitGroupMember traceId:{},extra:{},appId:{},groupId:{},time:{},count:{}",traceId,extra,appId,groupId,time,countQuitGroupMember);
			builder.setCount(countQuitGroupMember);
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("countQuitGroupMember error exception",e);
			
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("countQuitGroupMember success!");
	}
	
	
	
}
