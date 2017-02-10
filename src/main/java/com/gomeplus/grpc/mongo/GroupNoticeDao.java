package com.gomeplus.grpc.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gomeplus.grpc.model.GroupNotice;
import com.gomeplus.grpc.utils.BeanTransUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;

public class GroupNoticeDao extends BaseDao{
	private final static Logger log = LoggerFactory.getLogger(GroupNoticeDao.class);
	
	private final static String collName = "t_group_notice";
	
	public boolean saveGroupNotice(String appId,GroupNotice groupNotice){
		String dbName = getDatabaseName(appId);
		Document doc = BeanTransUtils.bean2Document(groupNotice);
		return insert(dbName, collName, doc);
	}
	
	public boolean updateGroupNotice(String appId,String groupId,long userId,String noticeContent){
		String dbName = getDatabaseName(appId);
		BasicDBObject filter = new BasicDBObject();
		filter.put("groupId", groupId);
		filter.put("userId", userId);
		
		Document doc = new Document();
		if (noticeContent!=null) {
			doc.put("noticeContent", noticeContent);
		}
		long nowTime = System.currentTimeMillis();
		doc.put("createTime", nowTime);
		doc.put("updateTime", nowTime);
		return update(dbName, collName, filter, doc);
	}
	
	public boolean updateOrInsertGroupNotice(String appId,String groupId,long userId,String noticeContent){
		String dbName = getDatabaseName(appId);
		BasicDBObject filter = new BasicDBObject();
		filter.put("groupId", groupId);
		filter.put("userId", userId);
		
		Document doc = new Document();
		if (noticeContent!=null) {
			doc.put("noticeContent", noticeContent);
		}
		long nowTime = System.currentTimeMillis();
		doc.put("createTime", nowTime);
		doc.put("updateTime", nowTime);
		UpdateOptions isUpdateOrInsert=new UpdateOptions();
		isUpdateOrInsert.upsert(true);
		return update(dbName, collName, filter, doc,isUpdateOrInsert);
	}
	
	public GroupNotice getGroupNotice(String appId,String groupId){
		String dbName = getDatabaseName(appId);
        MongoCursor<Document> cursor = null;
        try {
            BasicDBObject filter = new BasicDBObject();
            filter.put("groupId", groupId);
            cursor = find(dbName, collName, filter);
            if(cursor.hasNext()) {
                Document item = cursor.next();
                GroupNotice been = (GroupNotice) BeanTransUtils.document2Bean(item, GroupNotice.class);
                log.info("GroupNoticeDao getGroupNotice success!");
                return been;
            }
        } catch (Exception e) {
            log.error("GroupNoticeDao getGroupNotice error exception:", e);
        } finally {
            cursorClose(cursor);
        }
        return null;
	}
	
	
	public List<GroupNotice> getGroupNoticeByGroupIds(String appId,List<String> groupIdList){
		String dbName = getDatabaseName(appId);
		MongoCursor<Document> cursor = null;
		try {
    		MongoCollection<Document> coll = this.getCollection(dbName, collName);
    		BasicDBObject[] findObjArr = new BasicDBObject[groupIdList.size()];
    		for (int i=0;i<groupIdList.size();i++) {
    			BasicDBObject whereObj = new BasicDBObject();
    			String groupId = groupIdList.get(i);
    			whereObj.put("groupId", groupId);
    			findObjArr[i] = whereObj;
    		}
    		BasicDBObject filter = new BasicDBObject().append(QueryOperators.OR, findObjArr);
    		cursor = coll.find(filter).iterator();
    		List<GroupNotice> groupNoticeList=new ArrayList<GroupNotice>();
    		while(cursor.hasNext()) {
    			Document item = cursor.next();
    			GroupNotice groupNotice= (GroupNotice) BeanTransUtils.document2Bean(item, GroupNotice.class);
    			
    			groupNoticeList.add(groupNotice);
    		}
    		log.info("GroupMember getGroupNoticeByGroupIds success!");
    		return groupNoticeList;
    	} catch (Exception e) {
    		log.error("GroupMember getGroupNoticeByGroupIds error exception:", e);
    	} finally {
    		cursorClose(cursor);
    	}
    	return null;
	}
	
	public Map<String,GroupNotice> getGroupId2NoticeMap(String appId,List<String> groupIdList){
		String dbName = getDatabaseName(appId);
		MongoCursor<Document> cursor = null;
		try {
    		MongoCollection<Document> coll = this.getCollection(dbName, collName);
    		BasicDBObject[] findObjArr = new BasicDBObject[groupIdList.size()];
    		for (int i=0;i<groupIdList.size();i++) {
    			BasicDBObject whereObj = new BasicDBObject();
    			String groupId = groupIdList.get(i);
    			whereObj.put("groupId", groupId);
    			findObjArr[i] = whereObj;
    		}
    		BasicDBObject filter = new BasicDBObject().append(QueryOperators.OR, findObjArr);
    		cursor = coll.find(filter).iterator();
    		Map<String,GroupNotice> groupNoticeMap=new HashMap<String,GroupNotice>();
    		
    		while(cursor.hasNext()) {
    			Document item = cursor.next();
    			GroupNotice groupNotice= (GroupNotice) BeanTransUtils.document2Bean(item, GroupNotice.class);
    			if (groupNotice==null) {
					continue;
				}
    			groupNoticeMap.put(groupNotice.getGroupId(), groupNotice);
    		}
    		log.info("getGroup2NoticeMap success!");
    		return groupNoticeMap;
    	} catch (Exception e) {
    		log.error("getGroup2NoticeMap error exception:", e);
    	} finally {
    		cursorClose(cursor);
    	}
    	return null;
	}
	
	
	
	
}
