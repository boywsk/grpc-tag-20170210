package com.gomeplus.grpc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gomeplus.grpc.model.GroupMemberMark;
import com.gomeplus.grpc.mongo.GroupMemberMarkDao;
import com.gomeplus.grpc.protobuf.GomeplusRpcServices;
import com.gomeplus.grpc.protobuf.GroupMemberMarkSericeGrpc;
import com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMarkCollection;
import com.gomeplus.grpc.protobuf.GroupMemberMarkServices.RequestGroupMemberMark;
import com.gomeplus.grpc.protobuf.GroupMemberMarkServices.RespnoseGroupMemberMark;
import com.gomeplus.grpc.protobuf.GroupMemberMarkServices.RespnoseGroupMemberMark.Builder;
import com.gomeplus.grpc.utils.GomeImBeanUtils;

import io.grpc.stub.StreamObserver;

public class GroupMemberMarkServiceImpl extends GroupMemberMarkSericeGrpc.GroupMemberMarkSericeImplBase{

	private static Logger logger = LoggerFactory.getLogger(GroupMemberMarkServiceImpl.class);
	private GroupMemberMarkDao GROUP_MEMBER_MARK_DAO=new GroupMemberMarkDao();
	@Override
	public void saveMemberMark(RequestGroupMemberMark request,StreamObserver<RespnoseGroupMemberMark> responseObserver) {
		Builder builder = RespnoseGroupMemberMark.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			GroupMemberMark memberMark = GomeImBeanUtils.convertDBGroupMemberMarkFromPBGroupMemberMark(request.getMemberMark());
			if (memberMark==null) {
				logger.error("saveMemberMark() memberMark is null,traceId:{},extra:{}",traceId,extra);
				builder.setSuccess(false);
			}else {
				String appId=request.getAppId();
				logger.info("saveMemberMark() requset param,appId:{},memberMark:{},traceId:{},extra:{}",appId,JSON.toJSONString(memberMark),traceId,extra);
				boolean isSuccess = GROUP_MEMBER_MARK_DAO.saveMemberMark(appId, memberMark);
				builder.setSuccess(isSuccess);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("saveMemberMark error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("saveMemberMark success!");
	}

	@Override
	public void saveBatchMemberMark(RequestGroupMemberMark request,StreamObserver<RespnoseGroupMemberMark> responseObserver) {
		Builder builder = RespnoseGroupMemberMark.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			List<com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMark> groupMemberMarksList = request.getGroupMemberMarksList();
			if (CollectionUtils.isEmpty(groupMemberMarksList)) {
				builder.setSuccess(false);
				logger.error("saveBatchMemberMark groupMemberMarksList is empty,traceId:{},extra:{}",traceId,extra);
			}else {
				List<GroupMemberMark> groupMemberMarkList=new ArrayList<GroupMemberMark>();
				for (com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMark pBgroupMemberMark : groupMemberMarksList) {
					groupMemberMarkList.add(GomeImBeanUtils.convertDBGroupMemberMarkFromPBGroupMemberMark(pBgroupMemberMark));
				}
				logger.info("saveBatchMemberMark groupMemberMarksList is not empty,traceId:{},extra:{}",traceId,extra);
				String appId=request.getAppId();
				boolean isSuccess = GROUP_MEMBER_MARK_DAO.save(appId, groupMemberMarkList);
				builder.setSuccess(isSuccess);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("saveBatchMemberMark error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("saveBatchMemberMark success!");
	}

	@Override
	public void updateMemberMark(RequestGroupMemberMark request,StreamObserver<RespnoseGroupMemberMark> responseObserver) {
		Builder builder = RespnoseGroupMemberMark.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			long markedUserId=request.getMarkedUserId();
			String mark=request.getMark();
			logger.info("updateMemberMark() appId:{},groupId:{},userId:{}"
					+ ",markedUserId:{},mark:{},traceId:{},extra:{}",appId,groupId,userId,markedUserId,mark,traceId,extra);
			boolean isSuccess = GROUP_MEMBER_MARK_DAO.updateMemberMark(appId, groupId, userId, markedUserId, mark);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("updateMemberMark error exception",e);
			
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateMemberMark success!");
	}

	@Override
	public void delMemberMark(RequestGroupMemberMark request,StreamObserver<RespnoseGroupMemberMark> responseObserver) {
		Builder builder = RespnoseGroupMemberMark.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			//删除用户备注时，只要userId和MarkUserId有一个有效即可
			long userId=request.getUserId();
			long markedUserId = request.getMarkedUserId();
			logger.info("delMemberMark userId:{},markedUserId:{},traceId:{},extra:{}",userId,markedUserId,traceId,extra);
			if (userId<=0) {
				userId=markedUserId;
			}
			boolean isSuccess = GROUP_MEMBER_MARK_DAO.delMemberMark(appId, groupId, userId);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.info("delMemberMark error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("delMemberMark success!");
	}

	@Override
	public void delMemberMarkBatch(RequestGroupMemberMark request,StreamObserver<RespnoseGroupMemberMark> responseObserver) {
		Builder builder = RespnoseGroupMemberMark.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			List<Long> idList=request.getIdListList();
			if (CollectionUtils.isEmpty(idList)) {
				builder.setSuccess(false);
				logger.error("delMemberMarkBatch idList is empty,appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			}else {
				logger.error("delMemberMarkBatch,empty,appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
				boolean isSuccess = GROUP_MEMBER_MARK_DAO.delMemberMarkBatch(appId, groupId, idList);
				builder.setSuccess(isSuccess);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("delMemberMarkBatch error exception",e);
		}
		
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("delMemberMarkBatch success!");
	}

	@Override
	public void delAllMemberMark(RequestGroupMemberMark request,StreamObserver<RespnoseGroupMemberMark> responseObserver) {
		Builder builder = RespnoseGroupMemberMark.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			logger.info("delAllMemberMark appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			boolean isSuccess = GROUP_MEMBER_MARK_DAO.delAllMemberMark(appId, groupId);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("delAllMemberMark error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("delAllMemberMark success!");
	}

	@Override
	public void getMemberMarks(RequestGroupMemberMark request,StreamObserver<RespnoseGroupMemberMark> responseObserver) {
		Builder builder = RespnoseGroupMemberMark.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			logger.info("getMemberMarks appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
			List<GroupMemberMark> dbMemberMarks = GROUP_MEMBER_MARK_DAO.getMemberMarks(appId, groupId, userId);
			if (CollectionUtils.isEmpty(dbMemberMarks)) {
				logger.info("getMemberMarks result deMemberMarks is empty appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
			}else {
				logger.info("getMemberMarks result, appId:{},groupId:{},userId:{},traceId:{},extra:{}",appId,groupId,userId,traceId,extra);
				for (GroupMemberMark groupMemberMark : dbMemberMarks) {
					builder.addGroupMemberMarks(GomeImBeanUtils.convertPBGroupMemberMarkFromDBGroupMemberMark(groupMemberMark));
				}
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getMemberMarks error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getMemberMarks success!");
	}

	@Override
	public void getMemberMark(RequestGroupMemberMark request,StreamObserver<RespnoseGroupMemberMark> responseObserver) {
		Builder builder = RespnoseGroupMemberMark.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			long markedUserId=request.getMarkedUserId();
			logger.info("getMemberMark appId:{},groupId:{},userId:{}"
					+ ",markedUserId:{},traceId:{},extra:{}",appId,groupId,userId,markedUserId,traceId,extra);
			GroupMemberMark dBMemberMark = GROUP_MEMBER_MARK_DAO.getMemberMark(appId, groupId, userId, markedUserId);
			logger.info("getMemberMark result dBMemberMark:{} appId:{},groupId:{},userId:{}"
					+ ",markedUserId:{},traceId:{},extra:{}",dBMemberMark,appId,groupId,userId,markedUserId,traceId,extra);
			
			builder.setGroupMemberMark(GomeImBeanUtils.convertPBGroupMemberMarkFromDBGroupMemberMark(dBMemberMark));
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getMemberMark error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getMemberMark success!");
	}

	@Override
	public void getGroupId2GroupMemberMarkMap(RequestGroupMemberMark request,StreamObserver<RespnoseGroupMemberMark> responseObserver) {
		Builder builder = RespnoseGroupMemberMark.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			List<com.gomeplus.grpc.protobuf.GomeplusRpcServices.ReqBatchGroupIdInfo> reqBatchGroupIdInfosList = request.getReqBatchGroupIdInfosList();
			if (CollectionUtils.isEmpty(reqBatchGroupIdInfosList)) {
				builder.setSuccess(false);
				logger.error("GroupMemberServiceImpl.reqBatchGroupIdInfosList is empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				long userId=request.getUserId();
				logger.info("GroupMemberServiceImpl.reqBatchGroupIdInfosList appId:{},userId:{},traceId:{},extra:{}",appId,userId,traceId,extra);
				Map<String, GroupMemberMarkCollection> group2GroupMemberMap =GROUP_MEMBER_MARK_DAO.getBatchPullGroupId2GroupMembeMarkMap(appId, userId, reqBatchGroupIdInfosList);
				if (MapUtils.isEmpty(group2GroupMemberMap)) {
					logger.info("GroupMemberServiceImpl.reqBatchGroupIdInfosList result group2GroupMemberMap is empty,appId:{},"
							+ "userId:{},traceId:{},extra:{}",appId,userId,traceId,extra);
				}else {
					logger.info("GroupMemberServiceImpl.reqBatchGroupIdInfosList result, appId:{},"
							+ "userId:{},traceId:{},extra:{}",appId,userId,traceId,extra);
					builder.putAllGroupId2GroupMemberMark(group2GroupMemberMap);
				}
				builder.setSuccess(true);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupId2GroupMemberMarkMap error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupId2GroupMemberMarkMap success!");
		
		
	}
	
	
	
	
}
