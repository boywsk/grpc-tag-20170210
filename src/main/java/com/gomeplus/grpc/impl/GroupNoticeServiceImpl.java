package com.gomeplus.grpc.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gomeplus.grpc.model.GroupNotice;
import com.gomeplus.grpc.mongo.GroupNoticeDao;
import com.gomeplus.grpc.protobuf.GomeplusRpcServices;
import com.gomeplus.grpc.protobuf.GroupNoticeServiceGrpc;
import com.gomeplus.grpc.protobuf.GroupNoticeServices.RequestGroupNotice;
import com.gomeplus.grpc.protobuf.GroupNoticeServices.ResponseGroupNotice;
import com.gomeplus.grpc.protobuf.GroupNoticeServices.ResponseGroupNotice.Builder;
import com.gomeplus.grpc.utils.GomeImBeanUtils;

import io.grpc.stub.StreamObserver;


public class GroupNoticeServiceImpl extends GroupNoticeServiceGrpc.GroupNoticeServiceImplBase{
	private static Logger logger = LoggerFactory.getLogger(GroupNoticeServiceImpl.class);
	private GroupNoticeDao GROUP_NOTICE_DAO=new GroupNoticeDao();
	@Override
	public void saveGroupNotice(RequestGroupNotice request,StreamObserver<ResponseGroupNotice> responseObserver) {
		Builder builder = ResponseGroupNotice.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			
			String appId=request.getAppId();
			GroupNotice pbGroupNotice = GomeImBeanUtils.convertDBGroupNoticeFromPBGroupNotice(request.getGroupNotice());
			if (pbGroupNotice==null) {
				builder.setSuccess(true);
				logger.error("saveGroupNotice pbGroupNotice is null ,appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				boolean isSuccess = GROUP_NOTICE_DAO.saveGroupNotice(appId, pbGroupNotice);
				builder.setSuccess(isSuccess);
				logger.info("saveGroupNotice pbGroupNotice:{}, appId:{},traceId:{},extra:{}",JSON.toJSON(pbGroupNotice),appId,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("saveGroupNotice error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("saveGroupNotice success!");
	}

	@Override
	public void getGroupNotice(RequestGroupNotice request,StreamObserver<ResponseGroupNotice> responseObserver) {
		Builder builder = ResponseGroupNotice.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			logger.info("getGroupNotice appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			GroupNotice groupNotice = GROUP_NOTICE_DAO.getGroupNotice(appId, groupId);
			if (groupNotice==null) {
				logger.info("getGroupNotice groupNotice is null appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			}else {
				builder.setGroupNotice(GomeImBeanUtils.convertPBGroupNoticeFromDBGroupNotice(groupNotice));
				logger.info("getGroupNotice groupNotice:{}, appId:{},groupId:{},traceId:{},extra:{}",JSON.toJSONString(groupNotice),appId,groupId,traceId,extra);
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupNotice error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupNotice success!");
	}

	@Override
	public void updateGroupNotice(RequestGroupNotice request,StreamObserver<ResponseGroupNotice> responseObserver) {
		Builder builder = ResponseGroupNotice.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			String noticeContent=request.getNoticeContent();
			
			logger.info("updateGroupNotice appId:{},groupId:{},userId:{},noticeContent:{},traceId:{},extra:{}"
											,appId,groupId,userId,noticeContent,traceId,extra);
			boolean isSuccess = GROUP_NOTICE_DAO.updateGroupNotice(appId, groupId, userId, noticeContent);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("updateGroupNotice error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateGroupNotice success!");
	}

	@Override
	public void updateOrInsertGroupNotice(RequestGroupNotice request,StreamObserver<ResponseGroupNotice> responseObserver) {
		Builder builder = ResponseGroupNotice.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			String noticeContent=request.getNoticeContent();
			logger.info("updateOrInsertGroupNotice appId:{},groupId:{},userId:{},noticeContent:{},traceId:{},extra:{}"
					,appId,groupId,userId,noticeContent,traceId,extra);
			boolean isSuccess = GROUP_NOTICE_DAO.updateOrInsertGroupNotice(appId, groupId, userId, noticeContent);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("updateOrInsertGroupNotice error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateOrInsertGroupNotice success!");
	}

	@Override
	public void getGroupNoticeByGroupIds(RequestGroupNotice request,StreamObserver<ResponseGroupNotice> responseObserver) {
		Builder builder = ResponseGroupNotice.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			List<String> groupIdList =request.getGroupIdsList();
			if (CollectionUtils.isEmpty(groupIdList)) {
				logger.info("getGroupNoticeByGroupIds groupIdList is empty appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				logger.info("getGroupNoticeByGroupIds groupIdList:{} appId:{},traceId:{},extra:{}",JSON.toJSONString(groupIdList),appId,traceId,extra);
			}
			List<GroupNotice> groupNoticeList = GROUP_NOTICE_DAO.getGroupNoticeByGroupIds(appId, groupIdList);
			if (CollectionUtils.isEmpty(groupNoticeList)) {
				logger.info("getGroupNoticeByGroupIds groupNoticeList is empty, appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				for (GroupNotice groupNotice : groupNoticeList) {
					builder.addGroupNotices((GomeImBeanUtils.convertPBGroupNoticeFromDBGroupNotice(groupNotice)));
				}
				logger.info("getGroupNoticeByGroupIds appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupNoticeByGroupIds error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupNoticeByGroupIds success!");
	}

	@Override
	public void getGroupId2NoticeMap(RequestGroupNotice request, StreamObserver<ResponseGroupNotice> responseObserver) {
		Builder builder = ResponseGroupNotice.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			List<String> groupIdList =request.getGroupIdsList();
			if (CollectionUtils.isEmpty(groupIdList)) {
				logger.info("getGroupId2NoticeMap groupIdList is empty appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				logger.info("getGroupId2NoticeMap appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}
			Map<String,GroupNotice> groupNoticeMap = GROUP_NOTICE_DAO.getGroupId2NoticeMap(appId, groupIdList);
			if (MapUtils.isEmpty(groupNoticeMap)) {
				logger.info("getGroupId2NoticeMap groupNoticeList is empty, appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				for (String groupId : groupNoticeMap.keySet()) {
					builder.putGroupId2Notice(groupId, GomeImBeanUtils.convertPBGroupNoticeFromDBGroupNotice(groupNoticeMap.get(groupId)));
				}
				logger.info("getGroupId2NoticeMap appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupId2NoticeMap error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupId2NoticeMap success!");
	}
	
	
	
}
