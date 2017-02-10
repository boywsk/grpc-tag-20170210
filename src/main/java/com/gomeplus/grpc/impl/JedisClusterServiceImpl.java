package com.gomeplus.grpc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gomeplus.grpc.protobuf.JedisClusterServiceGrpc;
import com.gomeplus.grpc.protobuf.JedisClusterServices.ReqJedisCulster;
import com.gomeplus.grpc.protobuf.JedisClusterServices.RspJedisCluster;
import com.gomeplus.grpc.protobuf.JedisClusterServices.RspJedisCluster.Builder;
import com.gomeplus.grpc.utils.JedisClusterClient;

import io.grpc.stub.StreamObserver;
import redis.clients.jedis.JedisCluster;

/**
 *
 * @author liuzhenhuan
 * @date 2016年11月7日 下午1:37:55
 * @version v1.0
 */
public class JedisClusterServiceImpl extends JedisClusterServiceGrpc.JedisClusterServiceImplBase{
	private static Logger logger = LoggerFactory.getLogger(JedisClusterServiceImpl.class);
	@Override
	public void isExistKey(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			logger.info("isExistKey,key:{},traceId:{},extra:{}",key,traceId,extra);
			Boolean exists = jedisCluster.exists(key);
			if (BooleanUtils.isTrue(exists)) {
				builder.setIsExist(true);
			}else {
				builder.setIsExist(false);
			}
			builder.setSuccess(true);
			logger.info("isExistKey success,key:{},exists:{},traceId:{},extra:{}",key,exists,traceId,extra);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("isExistKey error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void getValue(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		long traceId = request.getTraceId();
		String extra = request.getExtra();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			logger.info("getValue,key:{},traceId:{},extra:{}",key,traceId,extra);
			String value = jedisCluster.get(key);
			if (StringUtils.isBlank(value)) {
				builder.setValue(StringUtils.EMPTY);
			}else {
				builder.setValue(value);
			}
			builder.setSuccess(true);
			logger.info("getValue success,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getValue error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();

	}

	@Override
	public void getAllList(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			logger.info("getAllList,key:{},traceId:{},extra:{}",key,traceId,extra);
			Long listLength = jedisCluster.llen(key);
			if (listLength==null||listLength==0){
				builder.setSuccess(true);
				logger.info("getAllList listLength=0 success,key:{},traceId:{},extra:{}",key,traceId,extra);
			}else {
				List<String> resultList = jedisCluster.lrange(key, 0, listLength);
				if (CollectionUtils.isEmpty(resultList)) {
					builder.setSuccess(true);
					logger.info("getAllList success,key:{},resultList is empty,traceId:{},extra:{}",key,traceId,extra);
				}else {
					builder.addAllValues(resultList);
					builder.setSuccess(true);
					logger.info("getAllList success,key:{},traceId:{},extra:{}",key,traceId,extra);
				}
			}

		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getAllList error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void getAllSet(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			logger.info("getAllSet,key:{},traceId:{},extra:{}",key,traceId,extra);
			Set<String> resultSet = jedisCluster.smembers(key);
			if (CollectionUtils.isEmpty(resultSet)) {
				builder.setSuccess(true);
				logger.info("getAllSet success,key:{},resultSet is Empty,traceId:{},extra:{}",key,traceId,extra);
			}else {
				builder.addAllValues(resultSet);
				builder.setSuccess(true);
				logger.info("getAllSet success,key:{},traceId:{},extra:{}",key,traceId,extra);
			}

		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getAllSet error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void getAllMap(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			logger.info("getAllMap,key:{},traceId:{},extra:{}",key,traceId,extra);
			Map<String, String> resultMap = jedisCluster.hgetAll(key);
			if (MapUtils.isEmpty(resultMap)) {
				builder.setSuccess(true);
				logger.info("getAllMap success,key:{},resultMap is Empty,traceId:{},extra:{}",key,traceId,extra);
			}else {
				builder.putAllResultMap(resultMap);
				builder.setSuccess(true);
				logger.info("getAllMap success,key:{},traceId:{},extra:{}",key,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getAllMap() error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void getValueInMapByField(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			String field = request.getField();
			logger.info("getAllMap,key:{},field:{},traceId:{},extra:{}",key,field,traceId,extra);
			String resultString = jedisCluster.hget(key, field);
			if (StringUtils.isEmpty(resultString)) {
				builder.setSuccess(true);
				builder.setValue(StringUtils.EMPTY);
				logger.info("getAllMap success,key:{},field:{},resultString is Empty,traceId:{},extra:{}",key,field,traceId,extra);
			}else {
				builder.setValue(resultString);
				builder.setSuccess(true);
				logger.info("getAllMap() success,key:{},field:{},resultString:{},traceId:{},extra:{}",key,field,resultString,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getAllMap error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void setValue(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			String value = request.getValue();
			logger.info("setValue,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			String set = jedisCluster.set(key, value);
			if (set==null) {
				builder.setSuccess(false);
				logger.info("setValue error,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			}else {
				builder.setSuccess(true);
				logger.info("setValue success,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("setValue error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void setExValue(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			String value = request.getValue();
			int seconds = request.getSeconds();

			logger.info("setExValue,key:{},value:{},seconds:{},traceId:{},extra:{}",key,value,seconds,traceId,extra);
			String set = jedisCluster.setex(key,seconds,value);
			if (set==null) {
				builder.setSuccess(false);
				logger.info("setExValue error,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			}else {
				builder.setSuccess(true);
				logger.info("setExValue success,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("setExValue error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();

	}

	@Override
	public void setNxValue(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			String value = request.getValue();
			logger.info("setNxValue,key:{},value,traceId:{},extra:{}",key,value,traceId,extra);
			Long lockCode = jedisCluster.setnx(key, value);
			if (lockCode==null) {
				builder.setSuccess(false);
				logger.info("setNxValue error,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			}else {
				builder.setSuccess(true);
				builder.setLockCode(traceId);
				logger.info("setNxValue success,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("setNxValue error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void appendList(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			String value = request.getValue();
			logger.info("appendList,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			Long lpush_result = jedisCluster.lpush(key, value);
			if (lpush_result==null) {
				builder.setSuccess(false);
				logger.info("appendList error,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			}else {
				builder.setSuccess(true);
				builder.setLockCode(traceId);
				logger.info("appendList success,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("appendList error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void addToSet(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			String value = request.getValue();
			logger.info("addToSet,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			Long set_add_result = jedisCluster.sadd(key, value);
			if (set_add_result==null) {
				builder.setSuccess(false);
				logger.info("addToSet error,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			}else {
				builder.setSuccess(true);
				builder.setLockCode(traceId);
				logger.info("addToSet success,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("addToSet error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void putToMap(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			String field = request.getField();
			String value = request.getValue();
			logger.info("putToMap,key:{},field:{},value:{},traceId:{},extra:{}",key,field,value,traceId,extra);
			Long push_to_map_result = jedisCluster.hset(key, field, value);
			if (push_to_map_result==null) {
				builder.setSuccess(false);
				logger.info("putToMap error,key:{},field:{},value:{},traceId:{},extra:{}",key,field,value,traceId,extra);
			}else {
				builder.setSuccess(true);
				builder.setLockCode(traceId);
				logger.info("putToMap success,key:{},field:{},value:{},,traceId:{},extra:{}",key,field,value,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("putToMap error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void deleteByKey(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			logger.info("deleteByKey,key:{},traceId:{},extra:{}",key,traceId,extra);
			Long push_to_map_result = jedisCluster.del(key);
			if (push_to_map_result==null) {
				builder.setSuccess(false);
				logger.info("deleteByKey error,key:{},traceId:{},extra:{}",key,traceId,extra);
			}else {
				builder.setSuccess(true);
				builder.setLockCode(traceId);
				logger.info("deleteByKey success,key:{},traceId:{},extra:{}",key,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("deleteByKey error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}


	@Override
	public void deleteValueInSet(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			String value=request.getValue();
			logger.info("deleteValueInSet,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			Long srem_result = jedisCluster.srem(key, value);
			if (srem_result==null) {
				builder.setSuccess(false);
				logger.info("deleteValueInSet error,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			}else {
				builder.setSuccess(true);
				builder.setLockCode(traceId);
				logger.info("deleteValueInSet success,key:{},value:{},traceId:{},extra:{}",key,value,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("deleteValueInSet error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void deleteFieldInMap(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String key=request.getKey();
			String field=request.getField();
			logger.info("deleteFieldInMap,key:{},field:{},traceId:{},extra:{}",key,field,traceId,extra);
			Long srem_result = jedisCluster.hdel(key, field);
			if (srem_result==null) {
				builder.setSuccess(false);
				logger.info("deleteFieldInMap error,key:{},field:{},traceId:{},extra:{}",key,field,traceId,extra);
			}else {
				builder.setSuccess(true);
				builder.setLockCode(traceId);
				logger.info("deleteFieldInMap success,key:{},field:{},traceId:{},extra:{}",key,field,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("deleteFieldInMap error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}
	@Override
	public void getNowNickName(ReqJedisCulster request, StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			String appId=request.getAppId();
			long userId=request.getUserId();
			String nickName=request.getNickName();
			logger.info("getNowNickName,appId:{},userId:{},nickName:{},traceId:{},extra:{}",appId,userId,nickName,traceId,extra);
			String nowNickName = JedisClusterClient.getNowNickName(appId, userId, nickName);
			if (nowNickName==null) {
				builder.setSuccess(false);
				logger.info("getNowNickName,appId:{},userId:{},nickName:{},nowNickName is null,traceId:{},extra:{}",appId,userId,nickName,traceId,extra);
			}else {
				builder.setSuccess(true);
				builder.setNickName(nowNickName);
				logger.info("getNowNickName,appId:{},userId:{},nickName:{},nowNickName:{},traceId:{},extra:{}",appId,userId,nickName,nowNickName,traceId,extra);
			}
		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getNowNickName error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void getId2NickNameMapByUserIdList(ReqJedisCulster request,StreamObserver<RspJedisCluster> responseObserver) {
		Builder builder = RspJedisCluster.newBuilder();
		String extra = request.getExtra();
		long traceId = request.getTraceId();
		try {
			JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
			String appId=request.getAppId();
			List<Long> usrIdsList = request.getUsrIdsList();
			if (CollectionUtils.isEmpty(usrIdsList)) {
				builder.setSuccess(false);
				logger.info("getId2NickNameMapByUserIdList userIdsList is empty,appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}else {
				logger.info("getId2NickNameMapByUserIdList appId:{},traceId:{},extra:{}",appId,traceId,extra);
				Map<Long, String> userIdNickNameMap=new HashMap<Long, String>();
				for (Long uid : usrIdsList) {
					String key=appId + "_" + uid + "_nikename";
					String nickName = jedisCluster.get(key);
					if (StringUtils.isNotBlank(nickName)) {
						userIdNickNameMap.put(uid, nickName);
					}
				}
				builder.putAllIdNickNameMap(userIdNickNameMap);
				logger.info("getId2NickNameMapByUserIdList,appId:{},traceId:{},extra:{}",appId,traceId,extra);
			}

		} catch (Exception e) {
			builder.setSuccess(false);
			logger.error("getId2NickNameMapByUserIdList error exception",e);
		}
		if(StringUtils.isNotBlank(extra)) {
			builder.setExtra(extra);
		}
		builder.setTraceId(traceId);
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();

	}
	
	



}
