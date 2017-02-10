package com.gomeplus.grpc.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gomeplus.grpc.model.SaveNoticeMsg;
import com.gomeplus.grpc.mongo.NoticeMsgDao;
import com.gomeplus.grpc.protobuf.NoticeMsgServiceGrpc;
import com.gomeplus.grpc.protobuf.NoticeMsgServices.RequestNoticeMsg;
import com.gomeplus.grpc.protobuf.NoticeMsgServices.RespnseNoticeMsg;
import com.gomeplus.grpc.protobuf.NoticeMsgServices.RespnseNoticeMsg.Builder;
import com.gomeplus.grpc.utils.GomeImBeanUtils;

import io.grpc.stub.StreamObserver;

public class NoticeMsgServiceImpl extends NoticeMsgServiceGrpc.NoticeMsgServiceImplBase{
	private static final Logger logger=LoggerFactory.getLogger(NoticeMsgServiceImpl.class);
	private NoticeMsgDao NOTICE_MSG_DAO=new NoticeMsgDao();
	
	@Override
	public void listOfflineNoticeMsg(RequestNoticeMsg request,StreamObserver<RespnseNoticeMsg> responseObserver) {
		Builder builder = RespnseNoticeMsg.newBuilder();
		long traceId=request.getTraceId();
		String extra = request.getExtra();
		try {
			long time=request.getTime();
			int pageSize=request.getPageSize();
			byte clientId=(byte) request.getClientId();
			long uid=request.getUserId();
			int appId=request.getAppId();
			logger.info("listOfflineNoticeMsg appId:{},uid:{},clientId:{},pageSize:{},traceId:{},extra:{}"
											,appId,uid,clientId,pageSize,traceId,extra);
			List<SaveNoticeMsg> dbListOfflineNoticeMsg = NOTICE_MSG_DAO.listOfflineNoticeMsg(appId, uid, clientId, traceId, pageSize, time);
			if (CollectionUtils.isEmpty(dbListOfflineNoticeMsg)) {
				logger.info("listOfflineNoticeMsg dbListOfflineNoticeMsg is empty appId:{},uid:{},clientId:{},pageSize:{},traceId:{},extra:{}"
						,appId,uid,clientId,pageSize,traceId,extra);
			}else {
				for (SaveNoticeMsg pbSaveNoticeMsg : dbListOfflineNoticeMsg) {
					builder.addSaveNoticeMsgs(GomeImBeanUtils.convertPBSaveNoticeMsgFromDBSaveNoticeMsg(pbSaveNoticeMsg));
				}
				logger.info("listOfflineNoticeMsg, appId:{},uid:{},clientId:{},pageSize:{},traceId:{},extra:{}"
						,appId,uid,clientId,pageSize,traceId,extra);
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("listOfflineNoticeMsg error fail exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("NoticeMsgServiceImpl.listOfflineNoticeMsg success!");
	}

	@Override
	public void getOfflineNoticeMsgTotalCount(RequestNoticeMsg request,StreamObserver<RespnseNoticeMsg> responseObserver) {
		Builder builder = RespnseNoticeMsg.newBuilder();
		long traceId=request.getTraceId();
		String extra = request.getExtra();
		
		try {
			long time=request.getTime();
			byte clientId=(byte) request.getClientId();
			long uid=request.getUserId();
			int appId=request.getAppId();
			logger.info("getOfflineNoticeMsgTotalCount appId:{},uid:{},clientId:{},time:{},traceId:{},extra:{}",
													appId,uid,clientId,time,traceId,extra);
			long offlineNoticeMsgTotalCount = NOTICE_MSG_DAO.getOfflineNoticeMsgTotalCount(appId, uid, clientId, traceId, time);
			logger.info("getOfflineNoticeMsgTotalCount result offlineNoticeMsgTotalCount:{}, appId:{},uid:{},clientId:{},time:{},traceId:{},extra:{}"
															,offlineNoticeMsgTotalCount,appId,uid,clientId,time,traceId,extra);
			builder.setOfflineNoticeMsgTotalCount(offlineNoticeMsgTotalCount);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getOfflineNoticeMsgTotalCount error exception",e);
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("NoticeMsgServiceImpl.getOfflineNoticeMsgTotalCount success!");
		
	}

	
}
