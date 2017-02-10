package com.gomeplus.grpc.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gomeplus.grpc.model.GroupCollection;
import com.gomeplus.grpc.mongo.GroupCollectionDao;
import com.gomeplus.grpc.protobuf.GroupCollectionServiceGrpc;
import com.gomeplus.grpc.protobuf.GroupCollectionServices.RequestGroupCollection;
import com.gomeplus.grpc.protobuf.GroupCollectionServices.ResponseGroupCollection;
import com.gomeplus.grpc.protobuf.GroupCollectionServices.ResponseGroupCollection.Builder;
import com.gomeplus.grpc.utils.GomeImBeanUtils;

import io.grpc.stub.StreamObserver;

public class GroupCollectionSerciceImpl extends GroupCollectionServiceGrpc.GroupCollectionServiceImplBase{
	private final static Logger logger = LoggerFactory.getLogger(GroupCollectionSerciceImpl.class);
	
	private GroupCollectionDao GROUP_COLLECTION_DAO=new GroupCollectionDao();
	@Override
	public void updateOrInsertGroupCollection(RequestGroupCollection request,StreamObserver<ResponseGroupCollection> responseObserver) {
		Builder builder = ResponseGroupCollection.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			String groupId=request.getGroupId();
			long userId=request.getUserId();
			int isDel=request.getIsDel();
			long collectionTime=request.getCollectionTime();
			logger.error("updateOrInsertGroupCollection appId:{},groupId:{},userId:{},isDel:{},traceId:{},collectionTime:{},extra:{}",appId,groupId,userId,isDel,traceId,collectionTime,extra);
			boolean isSuccess = GROUP_COLLECTION_DAO.updateOrInsertGroupCollection(appId, groupId, userId, isDel,collectionTime);
			builder.setSuccess(isSuccess);
			logger.error("updateOrInsertGroupCollection isSuccess:{}, appId:{},groupId:{},userId:{},isDel:{},traceId:{},extra:{}",isSuccess,appId,groupId,userId,isDel,traceId,extra);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("updateOrInsertGroupCollection error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateOrInsertGroupCollection success!");
	}

	@Override
	public void getCollectGroupsByUserId(RequestGroupCollection request,StreamObserver<ResponseGroupCollection> responseObserver) {
		Builder builder = ResponseGroupCollection.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId=request.getAppId();
			long userId=request.getUserId();
			logger.error("getCollectGroupsByUserId appId:{},userId:{},traceId:{},extra:{}",appId,userId,traceId,extra);
			List<GroupCollection> dbCollectGroups = GROUP_COLLECTION_DAO.getCollectGroupsByUserId(appId, userId);
			if (CollectionUtils.isEmpty(dbCollectGroups)) {
				logger.error("getCollectGroupsByUserId dbCollectGroups is empty appId:{},userId:{},traceId:{},extra:{}",appId,userId,traceId,extra);
			}else {
				for (GroupCollection dbGroupCollection:dbCollectGroups) {
					builder.addGroupCollections(GomeImBeanUtils.convertPBGroupCollectionFromDBCollection(dbGroupCollection));
				}
				logger.error("getCollectGroupsByUserId collectGroups result appId:{},userId:{},traceId:{},extra:{}",appId,userId,traceId,extra);
			}
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getCollectGroupsByUserId error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getCollectGroupsByUserId success!");
	}
	
	
}
