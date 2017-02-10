package com.gomeplus.grpc.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gomeplus.grpc.model.GroupMemberMark;
import com.gomeplus.grpc.protobuf.GomeplusRpcServices.ReqBatchGroupIdInfo;
import com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMarkCollection;
import com.gomeplus.grpc.utils.BeanTransUtils;
import com.gomeplus.grpc.utils.GomeImBeanUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

/**
 * 群组成员备注
 */
public class GroupMemberMarkDao extends BaseDao {
    private static final Logger log = LoggerFactory.getLogger(GroupMemberMarkDao.class);
    private final static String collName = "t_group_memberMark";

    /**
     * 添加成员备注
     * @param appId
     * @param memberMark
     */
    public boolean saveMemberMark(String appId,GroupMemberMark memberMark){
        String dbName = getDatabaseName(appId);
        Document doc = BeanTransUtils.bean2Document(memberMark);
        return insert(dbName, collName, doc);
    }

    public boolean updateMemberMark(String appId,String groupId,long userId,long markedUserId,String mark){
        String dbName = getDatabaseName(appId);
        BasicDBObject filter = new BasicDBObject();
        filter.append("groupId", groupId).append("userId",userId).append("markedUserId",markedUserId);
        Document doc = new Document();
        doc.put("mark", mark);
        long nowTime = System.currentTimeMillis();
        doc.put("createTime", nowTime);
        doc.put("updateTime",nowTime);
        return update(dbName, collName, filter,doc);
    }

    /**
     * 删除所有用户对该用户的备注
     * @param appId
     * @param groupId
     * @param userIdOrMarkUserId
     */
    public boolean delMemberMark(String appId,String groupId,long userIdOrMarkUserId){
        String dbName = getDatabaseName(appId);
        BasicDBObject markUserIdDelBson = new BasicDBObject();
        markUserIdDelBson.put("groupId", groupId);
        markUserIdDelBson.put("markedUserId",userIdOrMarkUserId);
        BasicDBObject userIdDelBson = new BasicDBObject();
        userIdDelBson.put("groupId", groupId);
        userIdDelBson.put("userId",userIdOrMarkUserId);
        
        BasicDBObject delBson=new BasicDBObject(QueryOperators.OR,new BasicDBObject[]{markUserIdDelBson,userIdDelBson});
        return deleteAll(dbName, collName, delBson);
    }
    /**
     * 删除所有用户对该用户的备注
     * @param appId
     * @param groupId
     * @param userId
     */
    public boolean delMemberMarkBatch(String appId,String groupId,List<Long> usrIdOrMarkUserIdList){
    	String dbName = getDatabaseName(appId);
		BasicDBObject[] deleteObjArr = new BasicDBObject[usrIdOrMarkUserIdList.size()];
		for (int i = 0; i <usrIdOrMarkUserIdList.size(); i++) {
			long memberId = usrIdOrMarkUserIdList.get(i);
	        BasicDBObject markUserIdDelBson = new BasicDBObject();
	        markUserIdDelBson.put("groupId", groupId);
	        markUserIdDelBson.put("markedUserId",memberId);
	        BasicDBObject userIdDelBson = new BasicDBObject();
	        userIdDelBson.put("groupId", groupId);
	        userIdDelBson.put("userId",memberId);
	        
	        BasicDBObject delBson=new BasicDBObject(QueryOperators.OR,new BasicDBObject[]{markUserIdDelBson,userIdDelBson});
			deleteObjArr[i] = delBson;
		}
		BasicDBObject delObject = new BasicDBObject().append(QueryOperators.OR, deleteObjArr);
		return deleteAll(dbName, collName, delObject);
    }

    /**
     * 删除群组相关的所有备注(解散群用)
     * @param appId
     * @param groupId
     */
    public boolean delAllMemberMark(String appId,String groupId){
        String dbName = getDatabaseName(appId);
        BasicDBObject del = new BasicDBObject();
        del.put("groupId", groupId);
        return delete(dbName, collName, del);
    }

    /**
     * 获取某人所有的成员备注信息
     * @param appId
     * @param groupId
     * @param userId
     * @return
     */
    public List<GroupMemberMark> getMemberMarks(String appId,String groupId,long userId){
        List<GroupMemberMark> list = new ArrayList<>();
        String dbName = getDatabaseName(appId);
        BasicDBObject filter = new BasicDBObject();
        filter.put("groupId",groupId);
        filter.put("userId", userId);
        MongoCursor<Document> cursor=null;
        try {
        	cursor = find(dbName, collName, filter);
        	while (cursor.hasNext()) {
        		Document item = cursor.next();
        		GroupMemberMark been = (GroupMemberMark) BeanTransUtils.document2Bean(item, GroupMemberMark.class);
        		list.add(been);
        	}
        	log.info("getMemberMarks success!");
        	return list;
		} catch (Exception e) {
			log.error("getMemberMarks fail exception",e);
			return null;
		}finally{
			cursorClose(cursor);
		}
    }

    /**
     * 获取某人对群组某成员的备注信息
     * @param appId
     * @param groupId
     * @param userId
     * @return
     */
    public GroupMemberMark getMemberMark(String appId,String groupId,long userId,long markedUserId){
        GroupMemberMark groupMemberMark = null;
        String dbName = getDatabaseName(appId);
        BasicDBObject filter = new BasicDBObject();
        filter.put("groupId",groupId);
        filter.put("userId", userId);
        filter.put("markedUserId",markedUserId);
        MongoCursor<Document> cursor =null;
        try {
        	cursor = find(dbName, collName, filter);
        	while (cursor.hasNext()) {
        		Document item = cursor.next();
        		groupMemberMark = (GroupMemberMark) BeanTransUtils.document2Bean(item, GroupMemberMark.class);
        		break;
        	}
        	log.info("getMemberMark success!");
        	return groupMemberMark;
		} catch (Exception e) {
			log.error("getMemberMark fail exception",e);
			return null;
		}finally{
			cursorClose(cursor);
		}
    }
    
    /**
     * 批量保存群成员信息
     *
     * @return 是否插入成功
     */
    public boolean save(String appId, List<GroupMemberMark> groupMemberMarkList) {
        List<Long> list = new ArrayList<>();
        //加入到mongodb
        String dbName = getDatabaseName(appId);
        List<Document> docs = new ArrayList<Document>();
        for (int i = 0; i < groupMemberMarkList.size(); i++) {
            GroupMemberMark groupMember = groupMemberMarkList.get(i);
            Document doc = BeanTransUtils.bean2Document(groupMember);
            docs.add(doc);
        }
        //加入到redis
        return insertBatch(dbName, collName, docs);
    }
    
    /**
     * 根据查询某用户对其他人在群里的备注
     * @param appId
     * @param userId
     * @param reqBatchGroupIdInfos
     * @return
     */
    public Map<String, GroupMemberMarkCollection> getBatchPullGroupId2GroupMembeMarkMap(String appId,long userId,List<ReqBatchGroupIdInfo> reqBatchGroupIdInfos){
    	if (CollectionUtils.isEmpty(reqBatchGroupIdInfos)) {
    		log.info("GroupMemberDao.getBatchPullGroupId2GroupMembeMarkMap is empty");
    		return null;
    	}
    	
    	String dbName = getDatabaseName(appId);
    	Map<String, GroupMemberMarkCollection> group2GroupMemebrMap = new HashMap<String, GroupMemberMarkCollection>();
    	MongoCollection<Document> coll = this.getCollection(dbName, collName);
    	
    	BasicDBObject[] grouIdOrObjArr = new BasicDBObject[reqBatchGroupIdInfos.size()];
    	//根据=groupId,>=lastPushTimestamp, =status
    	int i=0;
    	for (ReqBatchGroupIdInfo groupIdInfo : reqBatchGroupIdInfos) {
    		BasicDBObject glsBson = new BasicDBObject();
    		glsBson.put("groupId", groupIdInfo.getGroupId());
    		glsBson.put("markedUserId", new BasicDBObject("$in", groupIdInfo.getUserIdsList()));
    		glsBson.put("userId", userId);
    		grouIdOrObjArr[i++]=glsBson;
		}
    	BasicDBObject orObject = new BasicDBObject().append(QueryOperators.OR, grouIdOrObjArr);
    	MongoCursor<Document> cursor = null;
    	try {
    		cursor = coll.find(orObject).iterator();
    		if (cursor == null) {
    			return group2GroupMemebrMap;
    		}
    		Map<String, List<GroupMemberMark>> tempMap=new HashMap<String, List<GroupMemberMark>>();
    		while (cursor.hasNext()) {
    			Document item = cursor.next();
    			GroupMemberMark dbGroupMemberMark = (GroupMemberMark) BeanTransUtils.document2Bean(item, GroupMemberMark.class);
    			if (dbGroupMemberMark==null) {
    				continue;
    			}
    			String groupId = dbGroupMemberMark.getGroupId();
    			List<GroupMemberMark> groupMemberMarkList = tempMap.get(groupId);
    			if (CollectionUtils.isEmpty(groupMemberMarkList)) {
					groupMemberMarkList=new ArrayList<GroupMemberMark>();
					groupMemberMarkList.add(dbGroupMemberMark);
					tempMap.put(groupId, groupMemberMarkList);
				}else {
					groupMemberMarkList.add(dbGroupMemberMark);
				}
    		}
    		if (MapUtils.isEmpty(tempMap)) {
    			log.info("GroupMemberMarkDao.getBatchIncreasePullGroupId2GroupMemberMap tempMap is empty");
				return null;
			}
    		for (String groupId : tempMap.keySet()) {
    			List<GroupMemberMark> groupMemberMarkList = tempMap.get(groupId);
				if (CollectionUtils.isEmpty(groupMemberMarkList)) {
					continue;
				}
				GroupMemberMarkCollection.Builder builder = GroupMemberMarkCollection.newBuilder();
				for (GroupMemberMark groupMember : groupMemberMarkList) {
					com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMark pbGroupMemberMark = GomeImBeanUtils.convertPBGroupMemberMarkFromDBGroupMemberMark(groupMember);
					if (pbGroupMemberMark==null) {
						continue;
					}
					if (pbGroupMemberMark==com.gomeplus.grpc.protobuf.GroupMemberMarkServices.GroupMemberMark.getDefaultInstance()) {
						continue;
					}
					builder.addGroupMemberMarks(pbGroupMemberMark);
				}
				group2GroupMemebrMap.put(groupId, builder.build());
			}
    		
    		return group2GroupMemebrMap;
    	} catch (Exception e) {
    		log.info("getBatchPullGroupId2GroupMembeMarkMap fail exception",e);
    		return null;
    	}finally{
    		cursorClose(cursor);
    	}
    	
    }
    

}
