package com.gomeplus.grpc.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gomeplus.grpc.global.Constant;
import com.gomeplus.grpc.model.GroupCollection;
import com.gomeplus.grpc.model.GroupNotice;
import com.gomeplus.grpc.utils.BeanTransUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;

public class GroupCollectionDao extends BaseDao{
	private final static Logger logger = LoggerFactory.getLogger(GroupCollectionDao.class);
	private final static String collName = "t_group_collection_";
	
	private final static int FRAGMENT_SIZE=16;//分表大小
	
	
	/**
	 *@Description: 插入或更新群收藏
	 *@param appId
	 *@param groupId
	 *@param userId
	 *@return
	 */
	public boolean updateOrInsertGroupCollection(String appId,String groupId,long userId,int isDel,long collectionTime){
		logger.info("updateOrInsertGroupCollection appId:{},groupId:{},userId:{}",appId,groupId,userId);
		String dbName = getDatabaseName(appId);
		BasicDBObject filter = new BasicDBObject();
		filter.put("groupId", groupId);
		filter.put("userId", userId);
		Document doc = new Document();
		doc.put("isDel", isDel);
		if (collectionTime>0) {
			doc.put("updateTime", collectionTime);
		}else {
			long nowTime = System.currentTimeMillis();
			doc.put("updateTime", nowTime);
		}
		UpdateOptions isUpdateOrInsert=new UpdateOptions();
		isUpdateOrInsert.upsert(true);
		return update(dbName, getFragmentTableName(userId), filter, doc,isUpdateOrInsert);
	}
	
	
	/**
	 *@Description: 根据用户ID得到收藏列表
	 *@param appId
	 *@param groupIdList
	 *@return
	 */
	public List<GroupCollection> getCollectGroupsByUserId(String appId,long userId){
		String dbName = getDatabaseName(appId);
		MongoCursor<Document> cursor = null;
		try {
    		MongoCollection<Document> coll = this.getCollection(dbName, getFragmentTableName(userId));
    		BasicDBObject filter = new BasicDBObject();
    		filter.put("userId", userId);
    		filter.put("isDel", Constant.GROUP_COLLECTION_ISDEL.COLLECTION_DEL_NO.value);
    		logger.info("getCollectGroupsByUserId appId:{},userId:{}",appId,userId);
    		cursor = coll.find(filter).iterator();
    		if (cursor==null) {
    			logger.info("getCollectGroupsByUserId cursor is null,appId:{},userId:{}",appId,userId);
    			return null;
			}
    		List<GroupCollection> groupCollectionList=new ArrayList<GroupCollection>();
    		while(cursor.hasNext()) {
    			Document item = cursor.next();
    			GroupCollection groupCollection= (GroupCollection) BeanTransUtils.document2Bean(item, GroupCollection.class);
    			groupCollectionList.add(groupCollection);
    		}
    		logger.info("getCollectGroupsByUserId success appId:{},userId:{}",appId,userId);
    		return groupCollectionList;
    	} catch (Exception e) {
    		logger.info("getCollectGroupsByUserId error appId:{},userId:{},e:{}",appId,userId,e);
    	} finally {
    		cursorClose(cursor);
    	}
    	return null;
	}
	/**
	 *@Description:得到真正的表名
	 *@param dbName
	 *@param collName
	 *@param userId
	 *@return
	 */
	private String getFragmentTableName(long userId) {
		return collName+(userId%FRAGMENT_SIZE);
	}
	
	
	
}
