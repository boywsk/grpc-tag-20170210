package com.gomeplus.grpc.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gomeplus.grpc.message.GroupMsgModel;
import com.gomeplus.grpc.mongo.MsgDao;
import com.gomeplus.grpc.protobuf.GroupMsgServiceGrpc;
import com.gomeplus.grpc.protobuf.GroupMsgServices.RequestGroupMsg;
import com.gomeplus.grpc.protobuf.GroupMsgServices.ResponseGroupMsg;
import com.gomeplus.grpc.protobuf.GroupMsgServices.ResponseGroupMsg.Builder;
import com.gomeplus.grpc.utils.GomeImBeanUtils;

import io.grpc.stub.StreamObserver;

public class GroupMsgServiceImpl extends GroupMsgServiceGrpc.GroupMsgServiceImplBase{
	private final static Logger logger=LoggerFactory.getLogger(GroupMsgServiceImpl.class);
	private MsgDao GROUP_MSG_DAO=new MsgDao();
	@Override
	public void listGroupMsg(RequestGroupMsg request,StreamObserver<ResponseGroupMsg> responseObserver) {
		Builder builder = ResponseGroupMsg.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			int appId=request.getAppId();
			long uid=request.getUserId();
			String groupId=request.getGroupId();
			long seqId=request.getSeqId();
			int size=request.getSize();
			long time=request.getTime();
			logger.info("listGroupMsg appId:{},uid:{},groupId:{},seqId:{},size:{},time:{},traceId:{},extra:{}"
									,appId,uid,groupId,seqId,size,time,traceId,extra);
			List<GroupMsgModel> groupMsgModelList = GROUP_MSG_DAO.listGroupMsg(appId, uid, groupId, seqId, size, time);
			if (CollectionUtils.isEmpty(groupMsgModelList)) {
				logger.info("listGroupMsg groupMsgModelList is empty,appId:{},uid:{},groupId:{},seqId:{},size:{},time:{},traceId:{},extra:{}"
						,appId,uid,groupId,seqId,size,time,traceId,extra);
			}else {
				for (GroupMsgModel dBGroupMsg : groupMsgModelList) {
					builder.addGrouMsgs(GomeImBeanUtils.convertPBGroupMsgFromDBGroupMsg(dBGroupMsg));
				}
				logger.info("listGroupMsg ,appId:{},uid:{},groupId:{},seqId:{},size:{},time:{},traceId:{},extra:{}"
						,appId,uid,groupId,seqId,size,time,traceId,extra);
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("listGroupMsg error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("listGroupMsg success!");
	}
	
	@Override
	public void getInitSeqByGroupIdAndUid(RequestGroupMsg request,StreamObserver<ResponseGroupMsg> responseObserver) {
		Builder builder = ResponseGroupMsg.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			int appId=request.getAppId();
			long uid=request.getUserId();
			String groupId=request.getGroupId();
			logger.info("getInitSeqByGroupIdAndUid appId:{},uid:{},traceId:{},extra:{}",appId,uid,traceId,extra);
			long initSeq = GROUP_MSG_DAO.getInitSeqByGroupIdAndUid(appId, groupId, uid);
			builder.setInitSeq(initSeq);
			builder.setSuccess(true);
			logger.info("getInitSeqByGroupIdAndUid result initSeq:{} appId:{},uid:{},traceId:{},extra:{}",initSeq,appId,uid,traceId,extra);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getInitSeqByGroupIdAndUid error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getInitSeqByGroupIdAndUid success!");
	}

	@Override
	public void getMarkReadMsg(RequestGroupMsg request, StreamObserver<ResponseGroupMsg> responseObserver) {
		Builder builder = ResponseGroupMsg.newBuilder();
		try {
			String msgId = request.getMsgId();
			int appId=request.getAppId();
			long uid=request.getUserId();
			String groupId=request.getGroupId();
			logger.info("[getMarkReadMsg] appId=[{}], groupId=[{}], uid=[{}],msgId=[{}]", appId, groupId, uid, msgId);
			GroupMsgModel markReadMsg = GROUP_MSG_DAO.getMarkReadMsg(appId, uid, groupId, msgId);
			builder.setGroupMsg(GomeImBeanUtils.convertPBGroupMsgFromDBGroupMsg(markReadMsg));
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("[getMarkReadMsg] error exception",e);
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("[getMarkReadMsg] success!");
	}
}
