package com.gomeplus.grpc.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gomeplus.grpc.global.Global;
import com.gomeplus.grpc.model.SaveNoticeMsg;
import com.gomeplus.grpc.utils.BeanTransUtils;
import com.gomeplus.grpc.utils.CheckClientTypeUtils;
import com.gomeplus.grpc.utils.GomeImStringUtils;
import com.gomeplus.grpc.utils.JedisClusterClient;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class NoticeMsgDao extends BaseDao {
	private static final Logger log = LoggerFactory.getLogger(NoticeMsgDao.class);
	private final static String dbName = "db_msg_";
	private final static String collName = "t_notice_msg_";
	
	
	/**
	 * 分页获取功能性离线消息
	 *
	 * @param groupId
	 * @param seqId
	 * @param size
	 * @return
	 */
	public List<SaveNoticeMsg> listOfflineNoticeMsg(int appId, long uid, byte clientId, long traceId,int pageSize,long time) {
		log.info("[listOfflineNoticeMsg] traceId=[{}], appId=[{}],uid=[{}],clientId=[{}]",traceId,appId, uid, clientId);
		List<SaveNoticeMsg> list = new ArrayList<SaveNoticeMsg>();
		MongoCursor<Document> cursor=null;
		try {
			Bson orBson = this.getWether((int)clientId);
			
			if(orBson == null) {
				return list;
			}
			String[] names = getDBAndTableName(appId, uid);
			log.info("[listOfflineNoticeMsg] tranceId=[{}], dbName=[{}],tableName=[{}]", traceId, names[0], names[1]);
			MongoCollection<Document> coll = this.getCollection(names[0], names[1]);
			Bson andBson = Filters.eq("toUid", uid);
			Bson sort = new BasicDBObject("sendTime", 1);
			if (time<=0) {//第一次
				Bson where = Filters.and(andBson, orBson);
				cursor = coll.find(where).sort(sort).limit(pageSize).iterator();
			}else {
				Bson timeBson = Filters.gt("sendTime", time);
				Bson where = Filters.and(andBson, orBson,timeBson);
				cursor = coll.find(where).sort(sort).limit(pageSize).iterator();
			}
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				SaveNoticeMsg msg = (SaveNoticeMsg) BeanTransUtils.document2Bean(doc, SaveNoticeMsg.class);
				list.add(msg);
			}
		} catch(Exception e) {
			log.error("[listOfflineNoticeMsg]: ", e);
		}finally{
			cursorClose(cursor);
		}
		return list;
	}
	
	/**
	 * 得到离线消息的总记录数
	 * @param appId
	 * @param uid
	 * @param clientId
	 * @param traceId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public long getOfflineNoticeMsgTotalCount(int appId, long uid, byte clientId, long traceId,long time) {
		
		log.info("[listOfflineNoticeMsg] traceId=[{}], appId=[{}],uid=[{}],clientId=[{}]",traceId,appId, uid, clientId);
		try {
			Bson orBson = this.getWether((int)clientId);
			if(orBson == null) {
				return 0L;
			}
			String[] names = getDBAndTableName(appId, uid);
			log.info("[listOfflineNoticeMsg] tranceId=[{}], dbName=[{}],tableName=[{}]", traceId, names[0], names[1]);
			MongoCollection<Document> coll = this.getCollection(names[0], names[1]);
			Bson andBaon = Filters.eq("toUid", uid);
			Bson timeBaon = Filters.gt("sendTime", time);
			Bson where = Filters.and(andBaon, orBson,timeBaon);
			long count = coll.count(where);
			log.info("[listOfflineNoticeMsg] tranceId=[{}], result size=[{}]", traceId, count);
			return count;
		} catch(Exception e) {
			log.error("[listOfflineNoticeMsg]: ", e);
		}
		return 0L;
	}


	/**
	 * 根据uid计算库名和表名
	 * @return
	 */
	private String[] getDBAndTableName(int appId, long uid) {
		log.info("[getDBAndTableName] appId=[{}],uid=[{}]", appId, uid);
		String[] arr = new String[2];
		int hashValue = GomeImStringUtils.FNVHash1("" + uid);
		arr[0] = dbName + appId + "_" + hashValue % Global.MSG_DB_MODULO;
		hashValue = GomeImStringUtils.SDBMHash("" + uid);
		arr[1] = collName + hashValue % Global.MSG_TABLE_MODULO;
		return arr;
	}
	
	/**
	 * 
	 * @param requestPlatform
	 * @return
	 */
	private Bson getWether(int clientId) {
		int platform = 0;
		if (CheckClientTypeUtils.clientType_mobile(clientId)) {
			platform = 1;
		} else if (CheckClientTypeUtils.clientType_pc(clientId)) {
			platform = 2;
		} else if (clientId == 30) {// Web端--30:Web
			platform = 4;
		} else if (clientId == 40) {// Pad端--40:Pad
			platform = 8;
		}
		
		if (platform == 1) {// 1-M-移动端
//			int[] mobile = new int[] { 1, 3, 5, 7, 9, 11, 13, 15 };
			Bson orBson = Filters.or(Filters.eq("platform", 1), Filters.eq("platform", 3), Filters.eq("platform", 5)
					,Filters.eq("platform", 7), Filters.eq("platform", 9), Filters.eq("platform", 11)
					,Filters.eq("platform", 13), Filters.eq("platform", 15));
			
			return orBson;
		} else if (platform == 2) {// 2-PC-电脑端
//			int[] pc = new int[] { 2, 3, 6, 7, 10, 11, 14, 15 };
			Bson orBson = Filters.or(Filters.eq("platform", 2), Filters.eq("platform", 3), Filters.eq("platform", 6)
					,Filters.eq("platform", 7), Filters.eq("platform", 10), Filters.eq("platform", 11)
					,Filters.eq("platform", 14), Filters.eq("platform", 15));
			
			return orBson;
		} else if (platform == 4) {// 4-Web-网页端
//			int[] web = new int[] { 4, 5, 6, 7, 12, 13, 14, 15 };
			Bson orBson = Filters.or(Filters.eq("platform", 4), Filters.eq("platform", 5), Filters.eq("platform", 6)
					,Filters.eq("platform", 7), Filters.eq("platform", 12), Filters.eq("platform", 13)
					,Filters.eq("platform", 14), Filters.eq("platform", 15));
			
			return orBson;
		} else if (platform == 8) {// 8-Pad-Pad端
//			int[] pad = new int[] { 8, 9, 10, 11, 12, 13, 14, 15 };
			Bson orBson = Filters.or(Filters.eq("platform", 8), Filters.eq("platform", 9), Filters.eq("platform", 10)
					,Filters.eq("platform", 11), Filters.eq("platform", 12), Filters.eq("platform", 13)
					,Filters.eq("platform", 14), Filters.eq("platform", 15));
			
			return orBson;
		}
		return null;
	}

}
