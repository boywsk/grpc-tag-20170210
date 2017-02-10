package com.gomeplus.grpc.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gomeplus.grpc.global.Global;
import com.gomeplus.grpc.message.GroupMsgModel;
import com.gomeplus.grpc.utils.BeanTransUtils;
import com.gomeplus.grpc.utils.GomeImStringUtils;
import com.gomeplus.grpc.utils.JedisClusterClient;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import redis.clients.jedis.JedisCluster;

/**
 * 数据库消息相关操作
 */
public class MsgDao extends BaseDao {
	private static final Logger log = LoggerFactory.getLogger(MsgDao.class);
	
	private final static String dbName = "db_msg_";
	private final static String collName = "t_group_msg_";

	/**
	 * 分页获取聊天消息
	 *
	 * @param groupId
	 * @param seqId
	 * @param size
	 * @return
	 */
	public List<GroupMsgModel> listGroupMsg(int appId, long uid, String groupId, long seqId, int size,long time) {
		log.info("[listGroupMsg] appId=[{}],uid=[{}],groupId=[{}],seqId=[{}],pageSize=[{}]",
				appId, uid, groupId, seqId, size);
		List<GroupMsgModel> list = new ArrayList<GroupMsgModel>();
		if (size<=0) {
			return list;
		}
		MongoCursor<Document> cursor = null;
		try {
			String[] names = getDBAndTableName(appId, uid);
			log.info("[listGroupMsg] dbName=[{}],tableName=[{}]", names[0], names[1]);
			MongoCollection<Document> coll = this.getCollection(names[0], names[1]);
			Bson where =null;
			if (time<=0) {
				where = Filters.and(Filters.eq("groupId", groupId), Filters.lte("msgSeqId", seqId),Filters.eq("toUid", uid));
			}else{
				where = Filters.and(Filters.eq("groupId", groupId), Filters.lte("msgSeqId", seqId),Filters.eq("toUid", uid),Filters.gte("sendTime", time));
			}
			
			Bson sort = new BasicDBObject("msgSeqId", -1);
			cursor = coll.find(where).sort(sort).limit(size).iterator();
			while (cursor.hasNext()) {
				Document doc = cursor.tryNext();
				GroupMsgModel msg = (GroupMsgModel) BeanTransUtils.document2Bean(doc, GroupMsgModel.class);
				list.add(msg);
			}
			log.info("listGroupMsg success!");
			return list;
		} catch (Exception e) {
			log.error("listGroupMsg fail exception",e);
			return null;
		}finally{
			cursorClose(cursor);
		}
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
     * 从redis中获取成员initSeq
     * @param appId
     * @param groupId
     * @param uid
     * @return
     */
    public long getInitSeqByGroupIdAndUid(int appId, String groupId, long uid) {
        long initSeq = 0;
        log.info("[listMembersFromRdis] appId=[{}],groupId=[{}],uid=[{}]", appId, groupId, uid);
        JedisCluster cluster = JedisClusterClient.INSTANCE.getJedisCluster();
        String key = appId + "_" + groupId + "_initSeqId";
        String value = cluster.hget(key, "" + uid);
        if(value != null) {
            return Long.valueOf(value);
        }
        return initSeq;
    }
    
    /**
	 * 获取消息已读人员列表
	 *
	 * @param groupId
	 * @param seqId
	 * @param size
	 * @return
	 */
	public GroupMsgModel getMarkReadMsg(int appId, long uid, String groupId, String msgId) {
		log.info("[getMarkReadMsg] appId=[{}],groupId=[{}],uid=[{}],msgId=[{}]", appId, groupId, uid, msgId);
		String[] names = getDBAndTableName(appId, uid);
		log.info("[getMarkReadMsg] dbName=[{}],tableName=[{}]", names[0], names[1]);
		MongoCollection<Document> coll = this.getCollection(names[0], names[1]);
		Bson filter = Filters.and(Filters.eq("groupId", groupId), Filters.eq("msgId", msgId));
		Document doc = coll.find(filter).first();
		if(doc != null) {
			GroupMsgModel msg = (GroupMsgModel) BeanTransUtils.document2Bean(doc, GroupMsgModel.class);
			return msg;
		}
		log.error("[getMarkReadMsg] appId=[{}],groupId=[{}],uid=[{}],msgId=[{}],根据groupId,msgId未查询到记录！", appId, groupId, uid, msgId);
		return null;
	}
	
}
