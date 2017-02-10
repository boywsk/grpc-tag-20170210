package com.gomeplus.grpc.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gomeplus.grpc.global.Global;
import com.gomeplus.grpc.protobuf.UserRpcServices.RequestUser;
import com.gomeplus.grpc.protobuf.UserRpcServices.ResponseUser;
import com.gomeplus.grpc.protobuf.UserRpcServices.ResponseUser.Builder;
import com.gomeplus.grpc.protobuf.UserServiceGrpc;
import com.gomeplus.grpc.utils.JedisClusterClient;

import io.grpc.stub.StreamObserver;
import redis.clients.jedis.JedisCluster;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase{
	
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override
	public void getToken(RequestUser request, StreamObserver<ResponseUser> responseObserver) {
		Builder builder = ResponseUser.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			JedisCluster jedis = JedisClusterClient.INSTANCE.getJedisCluster();
			String appId=request.getAppId();
			long userId=request.getUserId();
			logger.info("getToken,appId:{},userId:{},traceId:{},extra:{}",appId,userId,traceId,extra);
			String tokenKey = appId + "_" + userId + Global.TOKEN_SUFFIX;
			String token = jedis.get(tokenKey);
			if (StringUtils.isBlank(token)) {
				logger.error("getToken is null or empty,appId:{},userId:{},traceId:{},extra:{}",appId,userId,traceId,extra);
				builder.setToken(StringUtils.EMPTY);
			}else {
				builder.setToken(token);
			}
			builder.setSuccess(true);
			logger.info("getToken,appId:{},userId:{},tokenKey:{},traceId:{},extra:{}",appId,userId,token,traceId,extra);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getToken error exception,traceId:{},extra:{},e:{}",traceId,extra,e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void checkUserToken(RequestUser request, StreamObserver<ResponseUser> responseObserver) {
		Builder builder = ResponseUser.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			JedisCluster jedis = JedisClusterClient.INSTANCE.getJedisCluster();
			String appId=request.getAppId();
			long userId=request.getUserId();
			String requestToken = request.getToken();
			
			logger.info("checkUserToken,appId:{},userId:{},requestToken:{},traceId:{},extra:{}",appId,userId,requestToken,traceId,extra);
			String tokenKey = appId + "_" + userId + Global.TOKEN_SUFFIX;
			String redisToken = jedis.get(tokenKey);
			if (StringUtils.equals(requestToken, redisToken)) {
				logger.error("checkUserToken requestToken is not equal redisToken,appId:{},userId:{},requestToken:{},redisToken:{},traceId:{},extra:{}",appId,userId,requestToken,redisToken,traceId,extra);
				builder.setIsVaild(false);
			}else {
				builder.setIsVaild(true);
			}
			builder.setSuccess(true);
			logger.info("checkUserToken appId:{},userId:{},requestToken:{},redisToken:{},traceId:{},extra:{}",appId,userId,requestToken,redisToken,traceId,extra);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("checkUserToken error exception,traceId:{},extra:{},e:{}",traceId,extra,e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}
	
}
