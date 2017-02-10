package com.gomeplus.grpc.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gomeplus.grpc.global.Constant;
import com.gomeplus.grpc.model.Group;
import com.gomeplus.grpc.utils.BeanTransUtils;
import com.gomeplus.grpc.utils.GomeImBeanUtils;
import com.gomeplus.grpc.utils.JedisClusterClient;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;


/**
 * 群组数据库操作层
 */
public class GroupDao extends BaseDao {
	private final static Logger log = LoggerFactory.getLogger(GroupDao.class);
	private final static String collName = "t_group";

	/**
	 * 保存群组信息
	 * 
	 * @param group
	 */
	public boolean save(String appId,Group group) {
		String dbName = getDatabaseName(appId);
		Document doc = BeanTransUtils.bean2Document(group);
		return insert(dbName, collName, doc);
	}

	/**
	 * 修改群组信息
	 * 
	 * @param group
	 */
	public void update(String appId,Group group) {
		String dbName = getDatabaseName(appId);
		MongoCollection<Document> coll = this.getCollection(dbName, collName);
		Document doc = new Document();
		String groupName  = group.getGroupName();
		
		if(StringUtils.isNotBlank(groupName)) {
			doc.put("groupName", groupName);
		}
		String groupDesc = group.getGroupDesc();
		if(StringUtils.isNotBlank(groupDesc)) {
			doc.put("groupDesc", groupDesc);
		}
		String avatar = group.getAvatar();
		if(StringUtils.isNotBlank(avatar)) {
			doc.put("avatar", avatar);
		}
		String qRcode = group.getqRcode();
		if(StringUtils.isNotBlank(qRcode)) {
			doc.put("qRcode", qRcode);
		}
		int capacity = group.getCapacity();
		if(capacity > 0) {
			doc.put("capacity", capacity);
		}
		int isAudit = group.getIsAudit();
		if(isAudit >= 0) {
			doc.put("isAudit", isAudit);
		}
		int isDele = group.getIsDele();
		if(isDele >= 0) {
			doc.put("isDele", isDele);
		}
		long userId = group.getUserId();
		if (userId>=0) {
			doc.put("userId", userId);
		}
		String subject = group.getSubject();
		if (StringUtils.isNotBlank(subject)) {
			doc.put("subject", subject);
		}
		String extraInfo = group.getExtraInfo();
		if (StringUtils.isBlank(extraInfo)) {
			extraInfo=StringUtils.EMPTY;
		}
		doc.put("extraInfo", extraInfo);
		
		doc.put("updateTime", System.currentTimeMillis());
		Bson filter =Filters.eq("groupId", group.getGroupId());
		try {
			coll.updateOne(filter, new Document("$set", doc));
			log.info("update group success!");
		} catch (Exception e) {
			log.error("update group fail exception",e);
		}
	}
	
	

	/**
	 * 根据groupId 获得group数据
	 * @param groupId
	 *
	 */
	public Group getGroup(String appId, String groupId) {
		String dbName = getDatabaseName(appId);
		MongoCursor<Document> cursor = null;
		Group group = null;
		try {
			BasicDBObject filter = new BasicDBObject();
			filter.put("groupId", groupId);
			filter.put("isDele", Constant.GROUP_DEL.E_GROUP_DEL_NOT.value);
			cursor = find(dbName, collName, filter);
			if (cursor.hasNext()) {
				Document item = cursor.next();
				group = (Group) BeanTransUtils.document2Bean(item, Group.class);
			}
			log.info("GroupDao getGroupById success!");
		} catch (Exception e) {
			log.error("GroupDao getGroupById fail exception:", e);
		} finally {
			cursorClose(cursor);
		}
		return group;
	}
	/**
	 * 根据groupId 获得group数据 包含删除的群
	 * @param groupId
	 *
	 */
	public Group getGroupContainDel(String appId, String groupId) {
		String dbName = getDatabaseName(appId);
		MongoCursor<Document> cursor = null;
		Group group = null;
		try {
			BasicDBObject filter = new BasicDBObject();
			filter.put("groupId", groupId);
			cursor = find(dbName, collName, filter);
			if (cursor.hasNext()) {
				Document item = cursor.next();
				group = (Group) BeanTransUtils.document2Bean(item, Group.class);
			}
			log.info("getGroupContainDel getGroupById success!");
		} catch (Exception e) {
			log.error("getGroupContainDel getGroupById fail exception:", e);
		} finally {
			cursorClose(cursor);
		}
		return group;
	}

	/**
	 * 设置群组的删除状态
	 * 
	 * @param groupId
	 *            群组id
	 * @param isDel
	 *            群主删除状态
	 * @return boolean 是否修改成功
	 */
	public boolean setGroupIsDel(String appId,String groupId, int isDel) {
		String dbName = getDatabaseName(appId);
		BasicDBObject filter = new BasicDBObject();
		filter.put("groupId", groupId);

		Document newdoc = new Document();
		newdoc.put("isDele", isDel);
		newdoc.put("updateTime", System.currentTimeMillis());

		return update(dbName, collName, filter, newdoc);
	}
	
	/**
	 * 根据groupId获取group
	 * @param appId
	 * @param groupId
	 * @return
	 */
	public Group getGroupById(int appId, String groupId) {
		log.info("[getGroupById] appId=[{}],groupId=[{}]", appId, groupId);
		MongoCollection<Document> coll = this.getAppCollection(appId, collName);
		
		Group group=null;
		MongoCursor<Document> cursor = null;
		try {
			BasicDBObject filter = new BasicDBObject();
    		filter.put("isDele", Constant.GROUP_DEL.E_GROUP_DEL_NOT.value);
    		filter.put("groupId", groupId);
    		cursor = coll.find(filter).iterator();
    		if (cursor==null) {
    			log.info("[getGroupById] find group is null appId=[{}],groupId=[{}]", appId, groupId);
				return null;
			}
    		if(cursor.hasNext()) {
    			Document item = cursor.next();
    			group= (Group) BeanTransUtils.document2Bean(item, Group.class);
    		}
			if(group != null) {
				JedisClusterClient util = JedisClusterClient.INSTANCE;
				String key = appId + "_groupSeqId" + "_" + groupId;
				long seqId = 0L;
				String value = util.getJedisCluster().get(key);
				if(value != null) {
					seqId = Long.valueOf(value);
				}
				group.setSeq(seqId);
			}
			log.info("getGroupById success!");
		} catch (Exception e) {
			log.error("getGroupById fail exception!",e);
		}

		return group;
	}
	
	/**
	 * 通过groupIdList 集合到群信息
	 * @param appId
	 * @param groupId
	 * @param groupIdList
	 * @return
	 */
	public List<Group> getGroupsByGroupIds(String appId,List<String> groupIdList){
		if (CollectionUtils.isEmpty(groupIdList)) {
			return null;
		}
    	String dbName = getDatabaseName(appId);
    	MongoCursor<Document> cursor = null;
    	try {
    		MongoCollection<Document> coll = this.getCollection(dbName, collName);
    		BasicDBObject[] findObjArr = new BasicDBObject[groupIdList.size()];
			for (int i=0;i<groupIdList.size();i++) {
				BasicDBObject whereObject = new BasicDBObject();
				String  groupId = groupIdList.get(i);
				whereObject.put("groupId", groupId);
				whereObject.put("isDele", Constant.GROUP_DEL.E_GROUP_DEL_NOT.value);
				findObjArr[i] = whereObject;
			}
			BasicDBObject filter = new BasicDBObject().append(QueryOperators.OR, findObjArr);
    		cursor = coll.find(filter).iterator();
    		if (cursor==null) {
    			log.info("getGroupsByGroupIds find groupds is null,appId:{}",appId);
				return null;
			}
    		List<Group> groupList=new ArrayList<Group>();
    		while(cursor.hasNext()) {
    			Document item = cursor.next();
    			Group  group= (Group) BeanTransUtils.document2Bean(item, Group.class);
    			groupList.add(group);
    		}
    		log.info("GroupMember getGroupUsers success!");
    		return groupList;
    	} catch (Exception e) {
    		log.error("GroupMember getGroupUsers error exception:", e);
    	} finally {
    		cursorClose(cursor);
    	}
    	return null;
	}
	
	/**
	 * 通过groupIdList 集合到群信息
	 * @param appId
	 * @param groupId
	 * @param groupIdList
	 * @return
	 */
	public Map<String,com.gomeplus.grpc.protobuf.GroupServices.Group> getGroupId2GroupMap(String appId,long traceId,List<String> groupIdList){
		if (CollectionUtils.isEmpty(groupIdList)) {
			return null;
		}
		String dbName = getDatabaseName(appId);
		MongoCursor<Document> cursor = null;
		try {
			MongoCollection<Document> coll = this.getCollection(dbName, collName);
			BasicDBObject[] findObjArr = new BasicDBObject[groupIdList.size()];
			for (int i=0;i<groupIdList.size();i++) {
				BasicDBObject whereObject = new BasicDBObject();
				String  groupId = groupIdList.get(i);
				whereObject.put("groupId", groupId);
				whereObject.put("isDele", Constant.GROUP_DEL.E_GROUP_DEL_NOT.value);
				findObjArr[i] = whereObject;
			}
			BasicDBObject filter = new BasicDBObject().append(QueryOperators.OR, findObjArr);
			cursor = coll.find(filter).iterator();
			if (cursor==null) {
    			log.info("getGroupId2GroupMap find groupds is null,appId:{}",appId);
				return null;
			}
			Map<String,com.gomeplus.grpc.protobuf.GroupServices.Group> groupId2GroupMap=new HashMap<String,com.gomeplus.grpc.protobuf.GroupServices.Group>();
			
			while(cursor.hasNext()) {
				Document item = cursor.next();
				Group  group= (Group) BeanTransUtils.document2Bean(item, Group.class);
//				log.info("getGroupId2GroupMap group info appId:{},traceId:{},group:{}",appId,traceId,JSON.toJSON(group));
				groupId2GroupMap.put(group.getGroupId(),GomeImBeanUtils.convertPBGroupFromDBGroup(group));
			}
			log.info("GroupMember getGroupUsers success!");
			return groupId2GroupMap;
		} catch (Exception e) {
			log.error("GroupMember getGroupUsers error exception:", e);
		} finally {
			cursorClose(cursor);
		}
		return null;
	}
	
	/**
	 * 保存对话的group信息
	 * @param group
	 */
	public boolean saveOrUpdateGroupById(String appId,String groupId,Group group) {
		try{
			String dbName = getDatabaseName(appId);
			MongoCollection<Document> coll = this.getCollection(dbName, collName);
			Document doc = new Document();
			if(group.getType() > 0){
				doc.put("type", group.getType());
			}
			if(group.getSeq() >= 0){
				doc.put("seq",group.getSeq());
			}
			if(group.getIsDele() >= 0){
				doc.put("isDele",group.getIsDele());
			}
			if(group.getCreateTime() > 0){
				doc.put("createTime",group.getCreateTime());
			}
			if(group.getUpdateTime() > 0){
				doc.put("updateTime",group.getUpdateTime());
			}
//			if(StringUtils.isNotEmpty(group.getName())){
//				doc.put("name",group.getName());
//			}
			Bson filter = Filters.eq("groupId", groupId);
			coll.findOneAndUpdate(filter, new Document("$set", doc), new FindOneAndUpdateOptions().upsert(true));
			return true;
		}catch (Exception e){
			log.error("saveOrUpdateGroupId error:{},groupId:{}", e, group.getGroupId());
			return false;
		}
	}
	
}
















