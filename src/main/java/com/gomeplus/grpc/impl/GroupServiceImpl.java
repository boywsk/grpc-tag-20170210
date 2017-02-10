package com.gomeplus.grpc.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gomeplus.grpc.model.Group;
import com.gomeplus.grpc.mongo.GroupDao;
import com.gomeplus.grpc.protobuf.GroupServiceGrpc;
import com.gomeplus.grpc.protobuf.GroupServices.RequestGroup;
import com.gomeplus.grpc.protobuf.GroupServices.RespnoseGroup;
import com.gomeplus.grpc.protobuf.GroupServices.RespnoseGroup.Builder;
import com.gomeplus.grpc.utils.GomeImBeanUtils;

import io.grpc.stub.StreamObserver;

/**
 * 群组相关操作
 * 
 * @author liuzhenhuan
 * @date 2016年10月18日 下午6:04:17
 * @version v1.0
 */
public class GroupServiceImpl extends GroupServiceGrpc.GroupServiceImplBase {
	private static Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);
  
	private static GroupDao GROUP_DAO = new GroupDao();
	
	@Override
	public void saveGroup(RequestGroup request,StreamObserver<RespnoseGroup> responseObserver) {
		Builder builder = RespnoseGroup.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId = request.getAppId();
			Group dbGroup=GomeImBeanUtils.convertDBGroupFromPBGroup(request.getGroup());
			if (dbGroup==null) {
				builder.setSuccess(false);
				logger.info("saveGroup dbGroup is null,appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				boolean isSuccess = GROUP_DAO.save(appId, dbGroup);
				builder.setSuccess(isSuccess);
				logger.info("saveGroup dbGroup:{},appId:{},traceId:{},extra:{}",JSON.toJSONString(dbGroup),appId,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("saveGroup error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("saveGroup success!");
	}


	@Override
	public void updateGroup(RequestGroup request,StreamObserver<RespnoseGroup> responseObserver) {
		Builder builder = RespnoseGroup.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId = request.getAppId();
			Group dbGroup=GomeImBeanUtils.convertDBGroupFromPBGroup(request.getGroup());
			if (dbGroup==null) {
				builder.setSuccess(false);
				logger.info("updateGroup dbGroup is null, appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				GROUP_DAO.update(appId, dbGroup);
				builder.setSuccess(true);
				logger.info("updateGroup dbGroup:{}, appId:{},traceId:{},extra:{}",JSON.toJSONString(dbGroup),appId,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("updateGroup error:",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("updateGroup success!");
	}

	@Override
	public void getGroupNoSeqInfoByGroupId(RequestGroup request,StreamObserver<RespnoseGroup> responseObserver) {
		Builder builder = RespnoseGroup.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId = request.getAppId();
			String groupId = request.getGroupId();
			logger.info("getGroupNoSeqInfoByGroupId appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			Group group = GROUP_DAO.getGroup(appId, groupId);
			com.gomeplus.grpc.protobuf.GroupServices.Group pbGroup = GomeImBeanUtils.convertPBGroupFromDBGroup(group);
			builder.setGroup(pbGroup);
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupNoSeqInfoByGroupId error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupNoSeqInfoByGroupId success!");
	}
	

	@Override
	public void getGroupContainDel(RequestGroup request, StreamObserver<RespnoseGroup> responseObserver) {
		Builder builder = RespnoseGroup.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId = request.getAppId();
			String groupId = request.getGroupId();
			logger.info("getGroupContainDel appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			Group group = GROUP_DAO.getGroupContainDel(appId, groupId);
			com.gomeplus.grpc.protobuf.GroupServices.Group pbGroup = GomeImBeanUtils.convertPBGroupFromDBGroup(group);
			builder.setGroup(pbGroup);
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupContainDel error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupContainDel success!");
	}


	@Override
	public void getGroupContainSeqInfoByGroupId(RequestGroup request,StreamObserver<RespnoseGroup> responseObserver) {
		Builder builder = RespnoseGroup.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId = request.getAppId();
			String groupId = request.getGroupId();
			logger.info("getGroupContainSeqInfoByGroupId appId:{},groupId:{},traceId:{},extra:{}",appId,groupId,traceId,extra);
			Group group = GROUP_DAO.getGroupById(NumberUtils.toInt(appId), groupId);
			com.gomeplus.grpc.protobuf.GroupServices.Group pbGroup = GomeImBeanUtils.convertPBGroupFromDBGroup(group);
			builder.setGroup(pbGroup);
			builder.setSuccess(true);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupNoSeqInfoByGroupId error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupNoSeqInfoByGroupId success!");
	}

	@Override
	public void setGroupIsDel(RequestGroup request,StreamObserver<RespnoseGroup> responseObserver) {
		Builder builder = RespnoseGroup.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId = request.getAppId();
			String groupId = request.getGroupId();
			int isDel = request.getIsDel();
			logger.info("setGroupIsDel appId:{},groupId:{},isDel:{},traceId:{},extra:{}",appId,groupId,isDel,traceId,extra);
			boolean isSuccess = GROUP_DAO.setGroupIsDel(appId, groupId, isDel);
			builder.setSuccess(isSuccess);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("setGroupIsDel error error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("setGroupIsDel success!");
	}
	
	@Override
	public void getGroupsByGroupIds(RequestGroup request,StreamObserver<RespnoseGroup> responseObserver) {
		Builder builder = RespnoseGroup.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId = request.getAppId();
			List<String> groupIdList=request.getGroupIdsList();
			if (CollectionUtils.isEmpty(groupIdList)) {
				builder.setSuccess(false);
				logger.error("getGroupsByGroupIds groupIdList is empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else{
				logger.info("getGroupsByGroupIds appId:{},traceId:{},extra:{}",appId,traceId,extra);
				List<Group> dbGroupList = GROUP_DAO.getGroupsByGroupIds(appId, groupIdList);
				if (CollectionUtils.isEmpty(dbGroupList)) {
					logger.info("getGroupsByGroupIds dbGroupList is empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
				}else {
					for (Group dbGroup : dbGroupList) {
						com.gomeplus.grpc.protobuf.GroupServices.Group pbGroup = GomeImBeanUtils.convertPBGroupFromDBGroup(dbGroup);
						builder.addGroups(pbGroup);
					}
					logger.info("getGroupsByGroupIds appId:{},traceId:{},extra:{}",appId,traceId,extra);
				}
				builder.setSuccess(true);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupNoSeqInfoByGroupId error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupNoSeqInfoByGroupId success!");
	}


	@Override
	public void getGroupId2GroupMap(RequestGroup request, StreamObserver<RespnoseGroup> responseObserver) {
		Builder builder = RespnoseGroup.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId = request.getAppId();
			List<String> groupIdList=request.getGroupIdsList();
			if (CollectionUtils.isEmpty(groupIdList)) {
				builder.setSuccess(false);
				logger.error("getGroupId2GroupMap groupIdList is empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else{
				logger.info("getGroupId2GroupMap appId:{},traceId:{},extra:{}",appId,traceId,extra);
				Map<String,com.gomeplus.grpc.protobuf.GroupServices.Group> pbGroupId2GroupMap = GROUP_DAO.getGroupId2GroupMap(appId,traceId, groupIdList);
				if (MapUtils.isEmpty(pbGroupId2GroupMap)) {
					logger.info("getGroupId2GroupMap pbGroupId2GroupMap is empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
				}else {
					builder.putAllGroupId2Group(pbGroupId2GroupMap);
					logger.info("getGroupsByGroupIds pbGroupId2GroupMap is not empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
				}
				builder.setSuccess(true);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getGroupId2GroupMap error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("getGroupId2GroupMap success!");
	}


	@Override
	public void saveOrUpdateGroupById(RequestGroup request, StreamObserver<RespnoseGroup> responseObserver) {
		Builder builder = RespnoseGroup.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			String appId = request.getAppId();
			String groupId=request.getGroupId();
			com.gomeplus.grpc.protobuf.GroupServices.Group pBGroup = request.getGroup();
			if (pBGroup==null) {
				logger.info("saveOrUpdateGroupById appId:{},groupId:{},group is null,traceId:{},extra:{}",appId,groupId,traceId,extra);
				builder.setSuccess(false);
			}else {
				Group group=GomeImBeanUtils.convertDBGroupFromPBGroup(pBGroup);
				logger.info("saveOrUpdateGroupById appId:{},groupId:{},group:{},traceId:{},extra:{}",appId,groupId,JSON.toJSONString(group),traceId,extra);
				boolean isSuccess = GROUP_DAO.saveOrUpdateGroupById(appId, groupId, group);
				logger.info("saveOrUpdateGroupById isSuccess:{},appId:{},groupId:{},group:{},traceId:{},extra:{}",isSuccess,appId,groupId,JSON.toJSONString(group),traceId,extra);
				builder.setSuccess(isSuccess);
			}
			
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("saveOrUpdateGroupById error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
		logger.info("saveOrUpdateGroupById success!");
		
		
	}
	
	
	
}








