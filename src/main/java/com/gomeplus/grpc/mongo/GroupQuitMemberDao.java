package com.gomeplus.grpc.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gomeplus.grpc.model.GroupQuitMember;
import com.gomeplus.grpc.protobuf.GomeplusRpcServices.ReqBatchGroupIdInfo;
import com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMemberCollection;
import com.gomeplus.grpc.utils.BeanTransUtils;
import com.gomeplus.grpc.utils.GomeImBeanUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;

/**
 * 退出群成员数据库操作类
 */
public class GroupQuitMemberDao extends BaseDao {
	private static final Logger log = LoggerFactory.getLogger(GroupQuitMemberDao.class);
	private final static String collName = "t_group_quit_member";
	
	/**
	 * 保存退出群成员信息
	 */
	public void save(String appId,GroupQuitMember member) {
		try {
			String dbName = getDatabaseName(appId);
			MongoCollection<Document> coll = this.getCollection(dbName, collName);
			Bson filter = Filters.and(Filters.eq("groupId", member.getGroupId()),
					Filters.eq("userId", member.getUserId()));
			long nowTime = System.currentTimeMillis();
			member.setCreateTime(nowTime);
			member.setUpdateTime(nowTime);
			Document doc = BeanTransUtils.bean2Document(member);
			Document update = new Document("$set", doc);
			FindOneAndUpdateOptions options = new FindOneAndUpdateOptions(); 
			coll.findOneAndUpdate(filter, update, options.upsert(true));
			log.info("GroupQuitMemberDao save success");
		} catch (Exception e) {
			log.error("GroupQuitMemberDao fail exception",e);
		}
	}

	/**
	 * 批量保存退出群成员信息
	 * @param groupQuitMembers
	 */
	public boolean saveGroupQuitMembers(String appId,List<GroupQuitMember> groupQuitMembers) {
		String dbName = getDatabaseName(appId);
		boolean success = false;
		List<Document> documentList = BeanTransUtils.bean2Document2(groupQuitMembers);
		success = this.insertBatch(dbName, collName, documentList);
		return success;
	}
	
	/**
	 * 根据退出群组id和用户id 删除记录
	 */
	public boolean delQuitMember(String appId,String groupId, long userId) {
		String dbName = getDatabaseName(appId);
		BasicDBObject del = new BasicDBObject();
		del.put("groupId", groupId);
		del.put("userId", userId);
		return delete(dbName, collName, del);
	}
	
	/**
	 * 删除多个成员
	 * @return 是否删除成功
	 */
	public boolean delQuitMembers(String appId,String groupId, List<Long> members) {
		if (CollectionUtils.isEmpty(members)) {
			return false;
		}
		String dbName = getDatabaseName(appId);
		BasicDBObject[] deleteObjArr = new BasicDBObject[members.size()];
		for (int i = 0; i < members.size(); i++) {
			long memberId = members.get(i);
			BasicDBObject del = new BasicDBObject();
			del.put("groupId", groupId);
			del.put("userId", memberId);
			deleteObjArr[i] = del;
		}
		BasicDBObject delObject = new BasicDBObject().append(QueryOperators.OR, deleteObjArr);
		return deleteAll(dbName, collName, delObject);
	}
	
	
	/**
	 * 根据用户userId,groupId获取
	 * @param userId
	 * @return
	 */
	public GroupQuitMember getGroupQuitMember(String appId,String groupId,long userId) {
		String dbName = getDatabaseName(appId);
		GroupQuitMember groupMember=null;
		MongoCursor<Document> cursor = null;
		try {
			BasicDBObject filter = new BasicDBObject();
			filter.put("userId", userId);
			filter.put("groupId", groupId);
			cursor = find(dbName, collName, filter);
			while (cursor.hasNext()) {
				Document item = cursor.next();
				groupMember = (GroupQuitMember) BeanTransUtils.document2Bean(item, GroupQuitMember.class);
			}
			log.info("listGroupQuitMember success");
		} catch (Exception e) {
			log.error("listGroupQuitMember fail exception:", e);
		}finally{
			cursorClose(cursor);
		}
		return groupMember;
	}
	/**
	 * 根据groupId获取
	 * 
	 * @param userId
	 * @return
	 */
	public List<GroupQuitMember> listGroupQuitMemberByGroupId(String appId,String groupId, long time) {
		String dbName = getDatabaseName(appId);
		List<GroupQuitMember> list = new ArrayList<GroupQuitMember>();
		MongoCollection<Document> coll = this.getCollection(dbName, collName);
		BasicDBObject where = new BasicDBObject();
		where.put("groupId", groupId);
		where.put("createTime", new BasicDBObject("$gte", time));
		MongoCursor<Document> cursor = null;
		try {
			cursor = coll.find(where).iterator();
			if (cursor == null) {
				return list;
			}
			while (cursor.hasNext()) {
				Document item = cursor.next();
				GroupQuitMember been = (GroupQuitMember) BeanTransUtils
						.document2Bean(item, GroupQuitMember.class);
				list.add(been);
			}
			log.info("listGroupQuitMemberByGroupId success!");
			return list;
		} catch (Exception e) {
			log.info("listGroupQuitMemberByGroupId fail exception:",e);
			return null;
		}finally{
			cursorClose(cursor);
		}
	}
	
	
	/**
	 * 批量（增量）拉取退出成员集合
	 * @param appId
	 * @param reqBatchGroupIdInfos
	 * @return
	 */
	public Map<String,GroupQuitMemberCollection> getBatchIncreasePullGroupId2GroupQuitMemberMap(String appId,List<ReqBatchGroupIdInfo> reqBatchGroupIdInfos){
		if (CollectionUtils.isEmpty(reqBatchGroupIdInfos)) {
    		log.info("GroupMemberDao.getBatchIncreasePullGroupId2GroupMemberMap is empty");
    		return null;
    	}
    	
    	String dbName = getDatabaseName(appId);
    	Map<String, GroupQuitMemberCollection> group2GroupMemebrMap = new HashMap<String, GroupQuitMemberCollection>();
    	MongoCollection<Document> coll = this.getCollection(dbName, collName);
    	
    	BasicDBObject[] grouIdOrObjArr = new BasicDBObject[reqBatchGroupIdInfos.size()];
    	//根据=groupId,>=lastPushTimestamp, =status
    	int i=0;
    	for (ReqBatchGroupIdInfo groupIdInfo : reqBatchGroupIdInfos) {
    		BasicDBObject glsBson = new BasicDBObject();
    		glsBson.put("groupId", groupIdInfo.getGroupId());
    		glsBson.put("createTime", new BasicDBObject("$gte", groupIdInfo.getLastPullTimestamp()));
    		grouIdOrObjArr[i++]=glsBson;
		}
    	BasicDBObject orObject = new BasicDBObject().append(QueryOperators.OR, grouIdOrObjArr);
    	MongoCursor<Document> cursor = null;
    	try {
    		cursor = coll.find(orObject).iterator();
    		if (cursor == null) {
    			return group2GroupMemebrMap;
    		}
    		Map<String, List<GroupQuitMember>> tempMap=new HashMap<String, List<GroupQuitMember>>();
    		while (cursor.hasNext()) {
    			Document item = cursor.next();
    			GroupQuitMember dbGroupQuitMember = (GroupQuitMember) BeanTransUtils.document2Bean(item, GroupQuitMember.class);
    			if (dbGroupQuitMember==null) {
    				continue;
    			}
    			String groupId = dbGroupQuitMember.getGroupId();
    			List<GroupQuitMember> groupQuitMemberList = tempMap.get(groupId);
    			if (CollectionUtils.isEmpty(groupQuitMemberList)) {
					groupQuitMemberList=new ArrayList<GroupQuitMember>();
					groupQuitMemberList.add(dbGroupQuitMember);
					tempMap.put(groupId, groupQuitMemberList);
				}else {
					groupQuitMemberList.add(dbGroupQuitMember);
				}
    		}
    		if (MapUtils.isEmpty(tempMap)) {
    			log.info("GroupQuitMemberDao.getBatchIncreasePullGroupId2GroupQuitMemberMap tempMap is empty");
				return null;
			}
    		for (String groupId : tempMap.keySet()) {
    			List<GroupQuitMember> groupQuitMemberList = tempMap.get(groupId);
				if (CollectionUtils.isEmpty(groupQuitMemberList)) {
					continue;
				}
				GroupQuitMemberCollection.Builder builder = GroupQuitMemberCollection.newBuilder();
				for (GroupQuitMember groupQuitMember : groupQuitMemberList) {
					com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMember pbGroupQuitMember = GomeImBeanUtils.convertPBQuitMemberFromDBQuitMember(groupQuitMember);
					if (pbGroupQuitMember==null) {
						continue;
					}
					if (pbGroupQuitMember==com.gomeplus.grpc.protobuf.GroupQuitMemberServices.GroupQuitMember.getDefaultInstance()) {
						continue;
					}
					builder.addGroupQuitMembers(pbGroupQuitMember);
				}
				group2GroupMemebrMap.put(groupId, builder.build());
			}
    		
    		return group2GroupMemebrMap;
    	} catch (Exception e) {
    		log.info("getBatchIncreasePullGroupId2GroupQuitMemberMap fail exception",e);
    		return null;
    	}finally{
    		cursorClose(cursor);
    	}
	}
	
	 /**
     * 统计退出群成员数
     *
     * @param groupId
     * @return
     */
    public long countQuitGroupMember(String appId, String groupId,long time) {
        String dbName = getDatabaseName(appId);
        BasicDBObject filter = new BasicDBObject();
        filter.put("groupId", groupId);
        filter.put("createTime", new BasicDBObject("$gte", time));
        return getCount(dbName, collName, filter);
    }
	
}
