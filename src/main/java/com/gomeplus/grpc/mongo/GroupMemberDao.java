package com.gomeplus.grpc.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gomeplus.grpc.global.Constant;
import com.gomeplus.grpc.global.Global;
import com.gomeplus.grpc.model.GroupInfo;
import com.gomeplus.grpc.model.GroupMember;
import com.gomeplus.grpc.protobuf.GomeplusRpcServices.ReqBatchGroupIdInfo;
import com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMemberCollection;
import com.gomeplus.grpc.utils.BeanTransUtils;
import com.gomeplus.grpc.utils.GomeImBeanUtils;
import com.gomeplus.grpc.utils.JedisClusterClient;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import redis.clients.jedis.JedisCluster;

/**
 * 群成员数据库操作类
 */
public class GroupMemberDao extends BaseDao {
    private final static Logger log = LoggerFactory.getLogger(GroupMemberDao.class);
    private final static String collName = "t_group_member";
//    private final static int DEFAULT_GROUP_PAGE_SIZE=50;

    /**
     * 保存群成员信息
     *
     * @return 是否插入成功
     */
    public boolean save(String appId, GroupMember groupMember, long groupCreaterUid,boolean isSaveToRedis) {
        //加入到redis
    	try {
    		List<Long> list = new ArrayList<>();
    		list.add(groupMember.getUserId());
    		if (isSaveToRedis) {
    			addGroupMembersUid2Redis(appId, groupMember.getGroupId(), list, groupCreaterUid);
    		}
    		//加入到mongodb
    		String dbName = getDatabaseName(appId);
    		Document doc = BeanTransUtils.bean2Document(groupMember);
    		boolean insert = insert(dbName, collName, doc);
    		log.info("save GroupMember success,success:{}",insert);
    		return insert;
		} catch (Exception e) {
			log.error("saveGroupMember error exception",e);
			return false;
		}
        
    }
	
    /**
     * 修改成员状态
     *
     * @param groupId
     * @param userId
     * @param status
     * @return
     */
    public boolean updateNickName(String appId, String groupId, long userId, String nickName) {
    	String dbName = getDatabaseName(appId);
    	BasicDBObject filter = new BasicDBObject();
    	filter.put("groupId", groupId);
    	filter.put("userId", userId);
    	
    	Document newdoc = new Document();
    	newdoc.put("nickName", nickName);
    	newdoc.put("updateTime", System.currentTimeMillis());
    	
    	return update(dbName, collName, filter, newdoc);
    }
    /**
     * 修改群昵称
     *
     * @param groupId
     * @param userId
     * @param status
     * @return
     */
    public boolean updateGroupNickname(String appId, String groupId, long userId, String groupNickname) {
        String dbName = getDatabaseName(appId);
        BasicDBObject filter = new BasicDBObject();
        filter.put("groupId", groupId);
        filter.put("userId", userId);

        Document newdoc = new Document();
        newdoc.put("groupNickName", groupNickname);
        newdoc.put("updateTime", System.currentTimeMillis());

        return update(dbName, collName, filter, newdoc);
    }

    /**
     * 修改identity
     *
     * @param groupId
     * @param userId
     * @param identity
     * @return
     */
    public boolean updateIdentity(String appId, String groupId, long userId, int identity) {
        String dbName = getDatabaseName(appId);
        BasicDBObject filter = new BasicDBObject();
        filter.put("groupId", groupId);
        filter.put("userId", userId);

        Document newdoc = new Document();
        if (identity >= 0) {
            newdoc.put("identity", identity);
        }
        newdoc.put("updateTime", System.currentTimeMillis());

        return update(dbName, collName, filter, newdoc);
    }

    /**
     * 修改屏蔽群消息状态
     *
     * @param groupId
     * @param userId
     * @param isShield
     * @return
     */
    public boolean updateShield(String appId, String groupId, long userId, int isShield) {
        String dbName = getDatabaseName(appId);
        BasicDBObject filter = new BasicDBObject();
        filter.put("groupId", groupId);
        filter.put("userId", userId);

        Document newdoc = new Document();
        if (isShield >= 0) {
        	//TODO 同时维护两个字段不需要，需要删除一个 liuzhenhuan 20170109
            newdoc.put("isShield", isShield);
            newdoc.put("isMsgBlocked", isShield);
        }
        newdoc.put("updateTime", System.currentTimeMillis());

        return update(dbName, collName, filter, newdoc);
    }
    /**
     * 修改群收藏
     *
     * @param groupId
     * @param userId
     * @param isCollection
     * @return
     */
    public boolean updateGroupCollection(String appId, String groupId, long userId, int isCollection) {
    	
    	String dbName = getDatabaseName(appId);
    	BasicDBObject filter = new BasicDBObject();
    	filter.put("groupId", groupId);
    	filter.put("userId", userId);
    	
    	Document newdoc = new Document();
    	if (isCollection >= 0) {
    		newdoc.put("isCollectionGroup", isCollection);
    	}
    	long nowTime = System.currentTimeMillis();
    	newdoc.put("groupCollectTime", nowTime);
    	newdoc.put("updateTime",nowTime);
    	
    	return update(dbName, collName, filter, newdoc);
    }

    /**
     * 修改群置顶状态
     *
     * @param groupId
     * @param userId
     * @param isTop
     * @return
     */
    @Deprecated
    public boolean updateIsTop(String appId, String groupId, long userId, int isTop) {
        String dbName = getDatabaseName(appId);
        BasicDBObject filter = new BasicDBObject();
        filter.put("groupId", groupId);
        filter.put("userId", userId);
        Document newdoc = new Document();
        if (isTop >= 0) {
            newdoc.put("isTop", isTop);
        }
        newdoc.put("updateTime", System.currentTimeMillis());

        return update(dbName, collName, filter, newdoc);
    }
    
    /**
     * 修改群置顶状态
     *
     * @param groupId
     * @param userId
     * @param isTop
     * @return
     */
    public boolean updateStickie(String appId, String groupId, long userId,int stickie) {
    	String dbName = getDatabaseName(appId);
    	BasicDBObject filter = new BasicDBObject();
    	filter.put("groupId", groupId);
    	filter.put("userId", userId);
    	Document newdoc = new Document();
    	long nowTime = System.currentTimeMillis();
		if (stickie==Constant.GROUP_STICKIE.E_GROUP_STICKIE_NOT.value) {
			newdoc.put("stickies", 0);//置顶时间
		}else {
			newdoc.put("stickies", nowTime);
		}
    	newdoc.put("updateTime", nowTime);
    	
    	return update(dbName, collName, filter, newdoc);
    }
    
    /**
     * 批量更新群成员状态信息
     *
     * @return 是否更新成功
     */
	public boolean updateBacthStatus(String appId,String groupId,List<Long> userIdList,long groupCreaterUid,int status) {
		
		String dbName = getDatabaseName(appId);
		BasicDBObject[] deleteObjArr = new BasicDBObject[userIdList.size()];
		List<Long> addUserIds=new ArrayList<Long>();
		List<Long> delUserIds=new ArrayList<Long>();
		for (int i=0;i<userIdList.size();i++) {
			BasicDBObject updateObj = new BasicDBObject();
			long userId = userIdList.get(i);
			updateObj.put("groupId", groupId);
			updateObj.put("userId",userId);
			deleteObjArr[i] = updateObj;
			if (status==Constant.GROUP_STATUS.E_GROUP_STATUS_OK.value) {
				addUserIds.add(userId);
			}else if (status==Constant.GROUP_STATUS.E_GROUP_STATUS_DEL.value) {
				delUserIds.add(userId);
			}
		}
		if (!CollectionUtils.isEmpty(addUserIds)) {
			addGroupMembersUid2Redis(appId,groupId, addUserIds, groupCreaterUid);
		}
		if (!CollectionUtils.isEmpty(delUserIds)) {
			delGroupMembersUid2Redis(appId, groupId, delUserIds);
		}
		BasicDBObject filter = new BasicDBObject().append(QueryOperators.OR, deleteObjArr);
		BasicDBObject update = new BasicDBObject();
		long nowTime = System.currentTimeMillis();
		update.put("updateTime", nowTime);
		update.put("status", status);
		if (status==Constant.GROUP_STATUS.E_GROUP_STATUS_OK.value
				||status==Constant.GROUP_STATUS.E_GROUP_STATUS_DEL.value) {//用户成功加入群或退出群(joinTime为退出群时间)时 
			update.put("joinTime", nowTime);
		}
		return updateAll(dbName, collName, filter, update);
	}

    /**
     * 批量保存群成员信息
     *
     * @return 是否插入成功
     */
    public boolean save(String appId, List<GroupMember> groupMembers, long groupCreaterUid,boolean isSaveToRedis) {
        List<Long> list = new ArrayList<>();
        String groupId = "";
        //加入到mongodb
        String dbName = getDatabaseName(appId);
        List<Document> docs = new ArrayList<Document>();
        for (int i = 0; i < groupMembers.size(); i++) {
            GroupMember groupMember = groupMembers.get(i);
            Document doc = BeanTransUtils.bean2Document(groupMember);
            docs.add(doc);
            list.add(groupMember.getUserId());
            if (groupId.isEmpty()) {
                groupId = groupMember.getGroupId();
            }
        }
        //加入到redis
        if (isSaveToRedis) {
        	addGroupMembersUid2Redis(appId, groupId, list, groupCreaterUid);
		}
        return insertBatch(dbName, collName, docs);
    }
    

    /**
     * 根据 群组id删除群组所有成员
     *
     * @return 是否删除成功
     */
    public boolean delGroupAllMember(String appId, String groupId) {
        //从redis移除
        delAllGroupMember2Redis(appId, groupId);
        //从mongodb移除
        String dbName = getDatabaseName(appId);
        BasicDBObject del = new BasicDBObject();
        del.put("groupId", groupId);
        return delete(dbName, collName, del);
    }
    /**
     * 更新groupId中的所有成员状态为删除状态
     *
     * @return 是否删除成功
     */
    public boolean updateDelStatusByGroupId(String appId, String groupId) {
    	//从redis移除
    	delAllGroupMember2Redis(appId, groupId);
    	//从mongodb移除
    	String dbName = getDatabaseName(appId);
    	BasicDBObject del = new BasicDBObject();
    	del.put("groupId", groupId);
    	BasicDBObject update = new BasicDBObject();
    	long nowTime = System.currentTimeMillis();
		update.put("updateTime", nowTime);
		update.put("joinTime", nowTime);
    	update.put("status", Constant.GROUP_STATUS.E_GROUP_STATUS_DEL.value);
    	return updateAll(dbName, collName, del,update);
    }

    /**
     * 根据 群组id 和 用户id 删除记录
     *
     * @return 是否删除成功
     */
    public boolean delGroupMember(String appId, String groupId, long userId) {
        //从redis移除
        List<Long> list = new ArrayList<>();
        list.add(userId);
        delGroupMembersUid2Redis(appId, groupId, list);
        //从mongodb移除
        String dbName = getDatabaseName(appId);
        BasicDBObject del = new BasicDBObject();
        del.put("groupId", groupId);
        del.put("userId", userId);
        return delete(dbName, collName, del);
    }

    /**
     * 删除多个成员
     *
     * @return 是否删除成功
     */
    public boolean delGroupMembers(String appId, String groupId, List<Long> members) {
        List<Long> list = new ArrayList<>();
        //从mongodb移除
        String dbName = getDatabaseName(appId);
        BasicDBObject[] deleteObjArr = new BasicDBObject[members.size()];
        for (int i = 0; i < members.size(); i++) {
            long memberId = members.get(i);
            list.add(memberId);
            BasicDBObject del = new BasicDBObject();
            del.put("groupId", groupId);
            del.put("userId", memberId);
            deleteObjArr[i] = del;
        }
        //从redis移除
        delGroupMembersUid2Redis(appId, groupId, list);
        BasicDBObject delObject = new BasicDBObject().append(QueryOperators.OR, deleteObjArr);
        return deleteAll(dbName, collName, delObject);
    }

    /**
     * 获取群内 所有已通过的成员关系
     *
     * @param groupId
     * @return
     */
    public List<GroupMember> listGroupMembers(String appId, String groupId) {
        String dbName = getDatabaseName(appId);
        List<GroupMember> list = new ArrayList<GroupMember>();
        MongoCollection<Document> coll = this.getCollection(dbName, collName);
        BasicDBObject where = new BasicDBObject();
        where.put("groupId", groupId);
        where.put("status", Constant.GROUP_STATUS.E_GROUP_STATUS_OK.value);
        MongoCursor<Document> cursor = null;
        try {
        	cursor = coll.find(where).iterator();
        	if (cursor == null) {
        		return list;
        	}
        	while (cursor.hasNext()) {
        		Document item = cursor.next();
        		GroupMember been = (GroupMember) BeanTransUtils.document2Bean(item, GroupMember.class);
        		list.add(been);
        	}
        	log.info("listGroupMembers success!");
        	return list;
		} catch (Exception e) {
			log.error("listGroupMembers fail exception!",e);
			return null;
		}finally{
			cursorClose(cursor);
		}
    }

    /**
     * 分页获取群内 所有成员关系
     *
     * @param groupId
     * @param time
     */
    public List<GroupMember> listGroupMembers(String appId, String groupId, long time, int status,int page, int size) {
        String dbName = getDatabaseName(appId);
        List<GroupMember> list = new ArrayList<GroupMember>();
        MongoCollection<Document> coll = this.getCollection(dbName, collName);
        BasicDBObject where = new BasicDBObject();
        where.put("groupId", groupId);
        if (status >= 0) {
            where.put("status", status);
        }
        where.put("joinTime", new BasicDBObject("$gte", time));
        Bson sort = new BasicDBObject("joinTime", 1);
        MongoCursor<Document> cursor = null;
        try {
        	if (page<=0) {
        		cursor = coll.find(where).sort(sort).iterator();
        	}else{
        		cursor = coll.find(where).sort(sort).skip((page - 1) * size).limit(size).iterator();
        	}
        	if (cursor == null) {
        		return list;
        	}
        	while (cursor.hasNext()) {
        		Document item = cursor.next();
        		GroupMember been = (GroupMember) BeanTransUtils.document2Bean(item, GroupMember.class);
        		list.add(been);
        	}
        	log.info("listGroupMembers success!");
        	return list;
		} catch (Exception e) {
			log.info("listGroupMembers fail exception",e);
			return null;
		}finally{
			cursorClose(cursor);
		}
    }
    /**
     * 根据groupId集合批量获取群成员信息
     *
     * @param groupId
     * @param time
     */
    public List<GroupMember> listGroupMembersByGroupIds(String appId, List<com.gomeplus.grpc.protobuf.GomeplusRpcServices.ReqBatchGroupIdInfo> groupIdInfos) {
    	log.info("GroupMemberDao.listGroupMembersByGroupIds appId:{},groupIdInfos:{}",appId,groupIdInfos);
    	if (CollectionUtils.isEmpty(groupIdInfos)) {
    		log.info("GroupMemberDai.listGroupMembersByGroupIds is empty");
    		return null;
    	}
    	
    	String dbName = getDatabaseName(appId);
    	List<GroupMember> groupMemberlist = new ArrayList<GroupMember>();
    	MongoCollection<Document> coll = this.getCollection(dbName, collName);
    	
    	BasicDBObject[] grouIdOrObjArr = new BasicDBObject[groupIdInfos.size()];
    	//根据=groupId,>=lastPushTimestamp, =status
    	int i=0;
    	for (com.gomeplus.grpc.protobuf.GomeplusRpcServices.ReqBatchGroupIdInfo groupIdInfo : groupIdInfos) {
    		BasicDBObject glsBson = new BasicDBObject();
    		glsBson.put("groupId", groupIdInfo.getGroupId());
    		glsBson.put("joinTime", new BasicDBObject("$gte", groupIdInfo.getLastPullTimestamp()));
    		glsBson.put("status", groupIdInfo.getStatus());
    		grouIdOrObjArr[i++]=glsBson;
		}
    	BasicDBObject orObject = new BasicDBObject().append(QueryOperators.OR, grouIdOrObjArr);
    	MongoCursor<Document> cursor = null;
    	try {
    		cursor = coll.find(orObject).iterator();
    		if (cursor == null) {
    			return groupMemberlist;
    		}
    		while (cursor.hasNext()) {
    			Document item = cursor.next();
    			GroupMember been = (GroupMember) BeanTransUtils.document2Bean(item, GroupMember.class);
    			groupMemberlist.add(been);
    		}
    		log.info("listGroupMembersByGroupIds success");
    		return groupMemberlist;
    	} catch (Exception e) {
    		log.info("listGroupMembersByGroupIds fail exception",e);
    		return null;
    	}finally{
    		cursorClose(cursor);
    	}
    }
    /**
     * 得到最先加入群的普通成员
     *
     * @param groupId
     * @param time
     */
    public GroupMember getEarliestAddGroupMember(String appId,String groupId) {
    	String dbName = getDatabaseName(appId);
        MongoCollection<Document> coll = this.getCollection(dbName, collName);
        BasicDBObject where = new BasicDBObject();
        where.put("groupId", groupId);
        where.put("status", Constant.GROUP_STATUS.E_GROUP_STATUS_OK.value);
        where.put("identity", Constant.GROUP_MEMBER_IDENTITY.E_MEMBER_ORDINARY.value);
        Bson sort = new BasicDBObject("joinTime", 1);
        MongoCursor<Document> cursor = null;
        try {
        	cursor = coll.find(where).sort(sort).limit(1).iterator();
        	if (cursor == null) {
        		return null;
        	}
        	if (cursor.hasNext()) {
        		Document item = cursor.next();
        		GroupMember groupMember = (GroupMember) BeanTransUtils.document2Bean(item, GroupMember.class);
        		return groupMember;
        	}
        	log.info("listGroupMembers success!");
		} catch (Exception e) {
			log.info("listGroupMembers fail exception",e);
		}finally{
			cursorClose(cursor);
		}
        return null;
    }
    
 
    /**
     * 获取指定的群成员关系
     *
     * @param groupId
     * @param userId
     */
    public GroupMember getGroupMemberByUid(String appId, String groupId, long userId) {
        String dbName = getDatabaseName(appId);
        MongoCursor<Document> cursor = null;
        try {
            BasicDBObject filter = new BasicDBObject();
            filter.put("groupId", groupId);
            filter.put("userId", userId);
            filter.put("status", Constant.GROUP_STATUS.E_GROUP_STATUS_OK.value);
          //TODO 只查询status=0 与status=1 的群成员，本应只查询status=1的，但是OA-没有status=0,但是在创建单聊的时候之前的status=0，为了兼容处理 
//            BasicDBObject[] grouIdOrObjArr = new BasicDBObject[2];
//        	BasicDBObject glsBson = new BasicDBObject();
//        	glsBson.put("status",Constant.GROUP_STATUS.E_GROUP_STATUS_OK.value);
//        	BasicDBObject glsBson2 = new BasicDBObject();
//        	glsBson2.put("status",Constant.GROUP_STATUS.E_GROUP_STATUS_NOT.value);
//        	grouIdOrObjArr[0]=glsBson;
//        	grouIdOrObjArr[1]=glsBson2;
//        	filter.append(QueryOperators.OR, grouIdOrObjArr);
            
            cursor = find(dbName, collName, filter);
            if(cursor.hasNext()) {
                Document item = cursor.next();
                GroupMember been = (GroupMember) BeanTransUtils.document2Bean(item, GroupMember.class);
                return been;
            }
            log.info("GroupMemberDao getGroupUsers success!");
        } catch (Exception e) {
            log.error("GroupMemberDao getGroupUsers:", e);
        } finally {
            cursorClose(cursor);
        }

        return null;
    }
    /**
     * 获取指定的群成员关系
     *
     * @param groupId
     * @param userId
     */
    public GroupMember getGroupMemberByUidContainStatus(String appId, String groupId, long userId) {
    	String dbName = getDatabaseName(appId);
    	MongoCursor<Document> cursor = null;
    	try {
    		BasicDBObject filter = new BasicDBObject();
    		filter.put("groupId", groupId);
    		filter.put("userId", userId);
    		//TODO 只查询status=0 与status=1 的群成员，本应只查询status=1的，但是OA-没有status=0,但是在创建单聊的时候之前的status=0，为了兼容处理 
//            BasicDBObject[] grouIdOrObjArr = new BasicDBObject[2];
//        	BasicDBObject glsBson = new BasicDBObject();
//        	glsBson.put("status",Constant.GROUP_STATUS.E_GROUP_STATUS_OK.value);
//        	BasicDBObject glsBson2 = new BasicDBObject();
//        	glsBson2.put("status",Constant.GROUP_STATUS.E_GROUP_STATUS_NOT.value);
//        	grouIdOrObjArr[0]=glsBson;
//        	grouIdOrObjArr[1]=glsBson2;
//        	filter.append(QueryOperators.OR, grouIdOrObjArr);
    		
    		cursor = find(dbName, collName, filter);
    		if(cursor.hasNext()) {
    			Document item = cursor.next();
    			GroupMember been = (GroupMember) BeanTransUtils.document2Bean(item, GroupMember.class);
    			return been;
    		}
    		log.info("GroupMemberDao getGroupUsers success!");
    	} catch (Exception e) {
    		log.error("GroupMemberDao getGroupUsers:", e);
    	} finally {
    		cursorClose(cursor);
    	}
    	
    	return null;
    }
    /**
     * 获取指定的群成员关系
     *
     * @param groupId
     */
    public List<GroupMember> getGroupMemberByUidList(String appId, String groupId, List<Long> idList,int status) {
    	if (CollectionUtils.isEmpty(idList)) {
			return null;
		}
    	String dbName = getDatabaseName(appId);
    	MongoCursor<Document> cursor = null;
    	MongoCollection<Document> coll = this.getCollection(dbName, collName);
    	try {
    		BasicDBObject[] findObjArr = new BasicDBObject[idList.size()];
    		for (int i = 0; i < idList.size(); i++) {
    			BasicDBObject updateObj = new BasicDBObject();
    			long userId = idList.get(i);
    			updateObj.put("groupId", groupId);
    			updateObj.put("userId",userId);
    			findObjArr[i] = updateObj;
			}
    		BasicDBObject filter = new BasicDBObject().append(QueryOperators.OR, findObjArr);
    		cursor = coll.find(filter).iterator();
    		List<GroupMember> groupMemberList=new ArrayList<GroupMember>();
    		while(cursor.hasNext()) {
    			Document item = cursor.next();
    			GroupMember  groupMember= (GroupMember) BeanTransUtils.document2Bean(item, GroupMember.class);
    			if (status<0||status==groupMember.getStatus()) {
    				groupMemberList.add(groupMember);
				}
    		}
    		log.info("GroupMemberDao getGroupUsers success!");
    		return groupMemberList;
    	} catch (Exception e) {
    		log.error("GroupMemberDao getGroupUsers:", e);
    	} finally {
    		cursorClose(cursor);
    	}
    	
    	return null;
    }


    /**
     * 根据用户Id，获取其关联所有的群 groupMember信息
     *
     * @param userId
     */
    public List<GroupMember> listMemberGroups(String appId, long userId) {
    	List<GroupMember> mongoGroupMembers = listMemberGroupsFromMongo(appId, userId);
    	if (CollectionUtils.isNotEmpty(mongoGroupMembers)) {
    		for (GroupMember groupMember : mongoGroupMembers) {
    			JedisCluster jedis = JedisClusterClient.INSTANCE.getJedisCluster();
    			String userGroupsKey = appId + "_" +userId + Global.USER_GROUPS_KEY;
    			jedis.sadd(userGroupsKey, groupMember.getGroupId());
			}
    		log.info("getMemberGroup from mongo,then store in redis,appId:{},userId:{},mongoGroupMembers size:{}",appId,userId,mongoGroupMembers.size());
    		return mongoGroupMembers;
    	}
    	log.info("GroupMemberDao listMemberGroups getResult is empty,,appId:{},userId:{}",appId,userId);
    	return null;
    }
    
    /**
     * 根据用户Id，获取其关联所有的群 groupIds
     *
     * @param userId
     */
    public List<String> listMemberGroupIds(String appId, long userId,int groupSize) {
    	List<String> redisGroupMembers = listMemberGroupIdsFromRedis(appId, userId);
    	//groupSize<0表示只查询redis中的数据,群聊不会有问题，主要是单聊会出问题,小于0 只查群聊不会有问题
    	if (groupSize<0) {
    		if (CollectionUtils.isNotEmpty(redisGroupMembers)) {
    			log.info("getMemberGroup from redis,appId:{},userId:{}",appId,userId);
    			return redisGroupMembers;
			}
		}
    	
    	if (CollectionUtils.isNotEmpty(redisGroupMembers)&&groupSize>0) {
	    	if (CollectionUtils.size(redisGroupMembers)>=groupSize) {//
	    		log.info("getMemberGroup from redis,appId:{},userId:{}",appId,userId);
				return redisGroupMembers;
			}
    	}
    	List<GroupMember> mongoGroupMembers = listMemberGroupsFromMongo(appId, userId);
    	
    	if (CollectionUtils.isNotEmpty(mongoGroupMembers)) {
    		List<String> resultGroupIdList=new ArrayList<String>();
    		JedisCluster jedis = JedisClusterClient.INSTANCE.getJedisCluster();
    		for (GroupMember groupMember : mongoGroupMembers) {
    			String userGroupsKey = appId + "_" +userId+ Global.USER_GROUPS_KEY;
    			String groupId = groupMember.getGroupId();
    			jedis.sadd(userGroupsKey, groupId);
    			if (resultGroupIdList.contains(groupId)) {
    				continue;
				}
    			resultGroupIdList.add(groupId);
			}
    		log.info("getMemberGroup from mongo,then store in redis,appId:{},userId:{},mongoGroupMembers size:{}",appId,userId,mongoGroupMembers.size());
    		return resultGroupIdList;
    	}
    	log.info("GroupMemberDao listMemberGroups getResult is empty,,appId:{},userId:{}",appId,userId);
    	return null;
    }
    
    
    /**
     * 根据用户Id，获取Redis其关联所有的群groupId ,GroupMember 中只有GroupId
     *
     * @param userId
     */
    private List<String> listMemberGroupIdsFromRedis(String appId, long userId) {
    	try {
    		JedisCluster jedis = JedisClusterClient.INSTANCE.getJedisCluster();
    		String userGroupsKey = appId + "_" + userId + Global.USER_GROUPS_KEY;
    		Set<String> groupIds = jedis.smembers(userGroupsKey);
    		if (CollectionUtils.isEmpty(groupIds)) {
    			log.info("GroupMemberDao listMemberGroups groupIds is empty");
    			return null;
    		}
    		List<String> groupMemberIds = new ArrayList<String>();
    		for(String groupId:groupIds){
    			groupMemberIds.add(groupId);
    		}
    		log.info("GroupMemberDao listMemberGroupIds success!");
    		return groupMemberIds;
    	} catch (Exception e) {
    		log.error("GroupMemberDao listMemberGroupIds:", e);
    		return null;
    	}
    }
    
    /**
     *根据用户Id，获取Mongo中其关联所有的群
     *
     * @param userId
     */
    private List<GroupMember> listMemberGroupsFromMongo(String appId, long userId) {
        String dbName = getDatabaseName(appId);
        MongoCursor<Document> cursor = null;
        List<GroupMember> groupMembers = null;
        try {
            groupMembers = new ArrayList<GroupMember>();
            BasicDBObject filter = new BasicDBObject();
            filter.put("userId", userId);
            //TODO 只查询status=0 与status=1 的群成员，本应只查询status=1的，但是OA-没有status=0,但是在创建单聊的时候之前的status=0，为了兼容处理 
            BasicDBObject[] grouIdOrObjArr = new BasicDBObject[2];
        	BasicDBObject glsBson = new BasicDBObject();
        	glsBson.put("status",Constant.GROUP_STATUS.E_GROUP_STATUS_OK.value);
        	BasicDBObject glsBson2 = new BasicDBObject();
        	glsBson2.put("status",Constant.GROUP_STATUS.E_GROUP_STATUS_NOT.value);
        	grouIdOrObjArr[0]=glsBson;
        	grouIdOrObjArr[1]=glsBson2;
        	filter.append(QueryOperators.OR, grouIdOrObjArr);
            
            cursor = find(dbName, collName, filter);
            if (cursor==null) {
				return null;
			}
            while (cursor.hasNext()) {
                Document item = cursor.next();
                GroupMember been = (GroupMember) BeanTransUtils.document2Bean(item, GroupMember.class);
                groupMembers.add(been);
            }
            log.info("GroupMemberDao listMemberGroups success!");
        } catch (Exception e) {
            log.error("GroupMemberDao listMemberGroups:", e.toString());
        }finally{
        	cursorClose(cursor);
        }
        return groupMembers;
    }

    /**
     * 统计群成员数
     *
     * @param groupId
     * @return
     */
    public long countGroupMember(String appId, String groupId,long time,int status) {
        String dbName = getDatabaseName(appId);
        BasicDBObject filter = new BasicDBObject();
        filter.put("groupId", groupId);
        if (status>=0) {
        	filter.put("status",status);
		}
//        if (status<0) {
//        	BasicDBObject[] grouIdOrObjArr = new BasicDBObject[3];
//        	BasicDBObject glsBson = new BasicDBObject();
//        	glsBson.put("status",Constant.GROUP_STATUS.E_GROUP_STATUS_OK.value);
//        	BasicDBObject glsBson2 = new BasicDBObject();
//        	glsBson2.put("status",Constant.GROUP_STATUS.E_GROUP_STATUS_DEL.value);
//        	//TODO status=0的不需要查询，但是为了兼容处理，现在需要查询
//        	BasicDBObject glsBson3 = new BasicDBObject();
//        	glsBson2.put("status",Constant.GROUP_STATUS.E_GROUP_STATUS_NOT.value);
//        	grouIdOrObjArr[0]=glsBson;
//        	grouIdOrObjArr[1]=glsBson2;
//        	grouIdOrObjArr[2]=glsBson3;
//        	filter.append(QueryOperators.OR, grouIdOrObjArr);
//		}else {
//			filter.put("status",status);
//			
//		}
//		filter.put("status", Constant.GROUP_STATUS.E_GROUP_STATUS_OK.value);
		filter.put("joinTime", new BasicDBObject("$gte", time));
        return getCount(dbName, collName, filter);
    }
    /**
     * 统计群成员数批量
     *
     * @param groupId
     * @return
     */
    public Map<String, Long> countGroupMemberByGroupIds(String appId, List<String> groupIds) {
    	Map<String, Long> map=new HashMap<String, Long>();
    	JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
    	for (String groupId : groupIds) {
    		String idsKey = appId + "_" + groupId + Global.GROUP_MEMBERS_SUFFIX;
			Long count= jedisCluster.hlen(idsKey);
			if (count==null) {
				map.put(groupId, 0L);
			}else {
				map.put(groupId, count);
			}
		}
    	return map;
    }
    /**
     * 统计群成员数批量
     *
     * @param groupId
     * @return
     */
    public long countGroupMemberFromRedis(String appId, String groupId) {
    	JedisCluster jedisCluster = JedisClusterClient.INSTANCE.getJedisCluster();
		String idsKey = appId + "_" + groupId + Global.GROUP_MEMBERS_SUFFIX;
		Long count= jedisCluster.hlen(idsKey);
		return count==null?0L:count;
    		
    }
    
    /**
     * 获取用户群组消息最大seqId
     * @param appId
     * @param uid
     * @return
     */
    public Map<String, String> listMemberMaxSeq(int appId, long uid) {
    	try {
    		JedisCluster cluster = JedisClusterClient.INSTANCE.getJedisCluster();
    		String readKey = appId + "_" + uid + Global.GROUP_INITSEQ_INCR_SUFFIX;
    		Map<String, String> map = cluster.hgetAll(readKey);
    		return map;
		} catch (Exception e) {
			log.error("GroupMemberDao listMemberMaxSeq error exception",e);
			return null;
		}
    }
    
    /**
     * 获取成员所有群组readSeqId,initSeqId
     * @param appId
     * @param uid
     * @return
     */
    public List<GroupMember> listMemberSeq(int appId, long uid, byte clientId,int groupSize) {
    	try {
    		log.info("[listMemberSeq] appId=[{}],uid=[{}],clientId=[{}]", appId, uid, clientId);
    		List<String> memberGroupIds = listMemberGroupIds(appId+"", uid,groupSize);
    		if (CollectionUtils.isEmpty(memberGroupIds)) {
    			log.info("[listMemberSeq] memberGroupIds is empty appId=[{}],uid=[{}],clientId=[{}]", appId, uid, clientId);
				return null;
			}
    		
    		List<GroupMember> list = new ArrayList<GroupMember>();
    		JedisCluster jedis = JedisClusterClient.INSTANCE.getJedisCluster();
//    		String readKey = appId + "_" + uid + "_readSeqId";
    		String readKey = appId + "_" + uid + Global.GROUP_READ_SEQ_ID_INITSEQ_SUFFIX;
    		Map<String, String> readMap = jedis.hgetAll(readKey);
//    		String initKey = appId + "_" + uid + "_initSeqId";
    		String initKey = appId + "_" + uid + Global.GROUP_MEMBER_INITSEQ_SUFFIX;
    		Map<String, String> initMap = jedis.hgetAll(initKey);
    		Map<String, String> receiveMap = this.listMemberReceiveSeqId(appId, uid, clientId);
    		
    		
    		log.info("[listMemberSeq] readMap size=[{}],initMap size=[{}],receiveMap size=[{}]",
    				readMap.size(), initMap.size(), receiveMap.size());
    		
    		for(String groupId : memberGroupIds) {
    			GroupMember member=new GroupMember();
    			long initSeqId = 0L;
    			long receiveSeqId = 0L;
    			long readSeqId = 0L;
				String initValue = initMap.get(groupId);
    			if(StringUtils.isNotBlank(initValue)) {
    				initSeqId = NumberUtils.toLong(initValue);
    			}
    			String receiveValue = receiveMap.get(groupId);
    			if(StringUtils.isNotBlank(receiveValue)) {
    				receiveSeqId = NumberUtils.toLong(receiveValue);
    			}
    			String readvalue = readMap.get(groupId);
    			if((StringUtils.isNotBlank(readvalue))) {
    				readSeqId = NumberUtils.toLong(readvalue);
    			}
    			if(receiveSeqId <= 0) {
    				receiveSeqId = initSeqId;
    			}
    			String membersKey = appId + "_" + groupId + Global.GROUP_MEMBERS_SUFFIX;
    			String json = jedis.hget(membersKey, uid+"");
    			if (StringUtils.isNotBlank(json)) {
    				GroupInfo groupInfo = JSON.parseObject(json, GroupInfo.class);
    				member.setStickies(groupInfo.getStickies());
    				member.setIsMsgBlocked(groupInfo.getIsMsgBlock());
				}
    			
    			member.setGroupId(groupId);
    			member.setInitSeq(initSeqId);
    			member.setReadSeq(readSeqId);
    			member.setReceiveSeqId(receiveSeqId);
    			
    			list.add(member);
    		}
    		log.info("listMemberSeq success!");
    		return list;
		} catch (Exception e) {
			log.error("listMemberSeq fail exception!",e);
			return null;
		}
    }
    
    
    /**
     * 获取用户客户端接收到聊天消息最大seqId
     * @param appId
     * @param uid
     * @param clientId
     * @return
     */
    private Map<String, String> listMemberReceiveSeqId(int appId, long uid, byte clientId) {
        if(clientId >= 10 && clientId < 20) {
            clientId = 10;
        } else if(clientId >= 20 && clientId < 30) {
            clientId = 20;
        } else if(clientId >= 30 && clientId < 40) {
            clientId = 30;
        }else if (clientId >= 40 && clientId < 50){
        	clientId = 40;
        	
        }
        JedisCluster cluster = JedisClusterClient.INSTANCE.getJedisCluster();
//        String key = appId + "_" + uid + "_" + clientId + "_receiveSeqId";
        String key = appId + "_" + uid + "_" + clientId + Global.GROUP_RECEIVE_SEQ_ID_SUFFIX;
        Map<String, String> map = cluster.hgetAll(key);
        return map;
    }
    /**
     * redis中添加群组成员userId
     *
     * @param appId
     * @param groupId
     * @param userIds
     */
    private static void addGroupMembersUid2Redis(String appId, String groupId, List<Long> userIds, long groupCreaterUid) {
        JedisCluster jedis = JedisClusterClient.INSTANCE.getJedisCluster();
        String key = appId + "_" + groupId + Global.GROUP_MEMBERS_SUFFIX;
        //appId+"_"+uid+"_userGroups"
        log.info("addGroupMembersUid2Redis start,key:{},groupMemberIds:{},"
        		+ "appId:{},groupId:{},groupCreatedUid:{}", key, JSON.toJSON(userIds),appId,groupId,groupCreaterUid);
        try {
        	if (!CollectionUtils.isEmpty(userIds)) {
        		
        		for (Long uid : userIds) {
        			String groupMemberInitSeqKey = appId + "_" + uid + Global.GROUP_MEMBER_INITSEQ_SUFFIX;
        			jedis.hset(key, String.valueOf(uid), "");
        			//userId->[groupId,groupId](Set集合) 
        			String userGroupsKey = appId + "_" + uid + Global.USER_GROUPS_KEY;
        			jedis.sadd(userGroupsKey, groupId);
        			String memberIncSeqKey = appId + "_" + groupCreaterUid + Global.GROUP_INITSEQ_INCR_SUFFIX;
        			String incSeq = jedis.hget(memberIncSeqKey, groupId);
//        			log.info("incSeq: {},uid:{},groupCreaterUid:{},groupId:{}",incSeq,uid,groupCreaterUid,groupId);
        			long initSeq = 0;
        			
        			if(StringUtils.isNotBlank(incSeq)) {
        				initSeq = Long.parseLong(incSeq) - 1;//邀请加入时的
        			}
        			initSeq = initSeq<0?0:initSeq;
        			String newUserId = appId + "_" + uid + Global.GROUP_INITSEQ_INCR_SUFFIX;
//        			log.info("intSeq: {},uid:{},groupCreaterUid:{},groupId:{}",initSeq,uid,groupCreaterUid,groupId);
        			jedis.hset(groupMemberInitSeqKey, groupId,String.valueOf(initSeq));
        			jedis.hset(newUserId, groupId,String.valueOf(initSeq));
        			for (int i = 10; i <=40; i+=10) {//设置inceiveSeqId =inintSeq
        				int clientId=i;
        				String groupMemberReceiveSeqKey = appId+"_"+uid+"_"+clientId+Global.GROUP_RECEIVE_SEQ_ID_SUFFIX;; //接收到的消息
        				jedis.hset(groupMemberReceiveSeqKey,groupId, String.valueOf(initSeq));
        			}
        		}
        	}
        	log.info("addGroupMembersUid2Redis success,key:{},groupMember:{}", key, userIds.toString());
			
		} catch (Exception e) {
			log.error("addGroupMembersUid2Redis error",e);
			
		}
    }
    
    private static List<String> convertStringList(List<Long> userIds) {
        List<String> list = new ArrayList<>();
        for (Long uid : userIds) {
            list.add(String.valueOf(uid));
        }
        return list;
    }

    /**
     * 移除redis中群组成员uid
     *
     * @param appId
     * @param groupId
     * @param userIds
     */
    private void delGroupMembersUid2Redis(String appId, String groupId, List<Long> userIds) {
        JedisCluster jedis = JedisClusterClient.INSTANCE.getJedisCluster();
        String key = appId + "_" + groupId + Global.GROUP_MEMBERS_SUFFIX;
        log.info("delGroupMembersUid2Redis start,key:{},groupMember:{}", key, userIds.toString());
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<String> list = convertStringList(userIds);
            String[] groupMembers = list.toArray(new String[list.size()]);
            jedis.hdel(key, groupMembers);
            //删除 ID->[groupId,groupId](Set)
//            deleteSeqId(appId, groupId, userIds);
            for (Long userId : userIds) {
            	deleteSeqId(appId, groupId, userId);
			}
        }
        log.info("delGroupMembersUid2Redis success,key:{},groupMember:{}", key, userIds.toString());
    }

	/**
	 *@Description:删除seqId
	 *@param appId
	 *@param groupId
	 *@param userIds
	 */
	private void deleteSeqId(String appId, String groupId,long userId) {
		JedisCluster jedis = JedisClusterClient.INSTANCE.getJedisCluster();
		// 删除seqId
		String userGroupsKey = appId + "_" + userId + Global.USER_GROUPS_KEY;
		jedis.srem(userGroupsKey, groupId);
		//TODO 群成员退出群时 删除maxSeqId，在离线拉取的时候会用到 用户检测没有的groupId,保留
		String maxSeq = appId + "_" + userId + Global.GROUP_INITSEQ_INCR_SUFFIX;
		jedis.hdel(maxSeq, groupId);
		String initKey = appId + "_" + userId + Global.GROUP_MEMBER_INITSEQ_SUFFIX;
		jedis.hdel(initKey, groupId);
		String readKey = appId + "_" + userId + Global.GROUP_READ_SEQ_ID_INITSEQ_SUFFIX;
		jedis.hdel(readKey, groupId);
		for (int i = 10; i <= 40; i += 10) {// 设置inceiveSeqId =inintSeq
			int clientId = i;
			String groupMemberReceiveSeqKey = appId + "_" + userId + "_" + clientId
					+ Global.GROUP_RECEIVE_SEQ_ID_SUFFIX;
			jedis.hdel(groupMemberReceiveSeqKey, groupId);
		}

	}
    /**
     * 移除redis中全部成员
     *
     * @param appId
     * @param groupId
     */
    private void delAllGroupMember2Redis(String appId, String groupId) {
        JedisCluster jedis = JedisClusterClient.INSTANCE.getJedisCluster();
        String key = appId + "_" + groupId + Global.GROUP_MEMBERS_SUFFIX;
        Map<String, String> userIdMap = jedis.hgetAll(key);
        if (MapUtils.isNotEmpty(userIdMap)) {
        	for(String userId:userIdMap.keySet()){
        		long uid = NumberUtils.toLong(userId);
        		deleteSeqId(appId, groupId, uid);
        	}
		}
        log.info("delAllGroupMember2Redis start,key:{}", key);
        jedis.del(key);
        log.info("delAllGroupMember2Redis success,key:{}", key);
    }
    
    /**
     * 根据groupId,lastPullTimstamp,status 批量获取群成员
     * @param appId
     * @param reqBatchGroupIdInfos
     * @return
     */
    public Map<String, GroupMemberCollection> getBatchIncreasePullGroupId2GroupMemberMap(String appId,List<ReqBatchGroupIdInfo> reqBatchGroupIdInfos){
    	if (CollectionUtils.isEmpty(reqBatchGroupIdInfos)) {
    		log.info("GroupMemberDao.getBatchIncreasePullGroupId2GroupMemberMap is empty");
    		return null;
    	}
    	
    	String dbName = getDatabaseName(appId);
    	Map<String, GroupMemberCollection> group2GroupMemebrMap = new HashMap<String, GroupMemberCollection>();
    	MongoCollection<Document> coll = this.getCollection(dbName, collName);
    	
    	BasicDBObject[] grouIdOrObjArr = new BasicDBObject[reqBatchGroupIdInfos.size()];
    	//根据=groupId,>=lastPushTimestamp, =status
    	int i=0;
    	for (ReqBatchGroupIdInfo groupIdInfo : reqBatchGroupIdInfos) {
    		BasicDBObject glsBson = new BasicDBObject();
    		glsBson.put("groupId", groupIdInfo.getGroupId());
    		glsBson.put("joinTime", new BasicDBObject("$gte", groupIdInfo.getLastPullTimestamp()));
//    		glsBson.put("status", Constant.GROUP_STATUS.E_GROUP_STATUS_OK.value);
    		grouIdOrObjArr[i++]=glsBson;
		}
    	BasicDBObject orObject = new BasicDBObject().append(QueryOperators.OR, grouIdOrObjArr);
    	MongoCursor<Document> cursor = null;
    	try {
    		cursor = coll.find(orObject).iterator();
    		if (cursor == null) {
    			return group2GroupMemebrMap;
    		}
    		Map<String, List<GroupMember>> tempMap=new HashMap<String, List<GroupMember>>();
    		while (cursor.hasNext()) {
    			Document item = cursor.next();
    			GroupMember dbGroupMember = (GroupMember) BeanTransUtils.document2Bean(item, GroupMember.class);
    			if (dbGroupMember==null) {
    				continue;
    			}
    			String groupId = dbGroupMember.getGroupId();
    			List<GroupMember> groupMemberList = tempMap.get(groupId);
    			if (CollectionUtils.isEmpty(groupMemberList)) {
					groupMemberList=new ArrayList<GroupMember>();
					groupMemberList.add(dbGroupMember);
					tempMap.put(groupId, groupMemberList);
				}else {
					groupMemberList.add(dbGroupMember);
				}
    		}
    		if (MapUtils.isEmpty(tempMap)) {
    			log.info("GroupMemberDao.getBatchIncreasePullGroupId2GroupMemberMap tempMap is empty");
				return null;
			}
    		for (String groupId : tempMap.keySet()) {
    			List<GroupMember> groupMemberList = tempMap.get(groupId);
				if (CollectionUtils.isEmpty(groupMemberList)) {
					continue;
				}
				GroupMemberCollection.Builder builder = GroupMemberCollection.newBuilder();
				for (GroupMember groupMember : groupMemberList) {
					com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember pbGroupMember = GomeImBeanUtils.convertPBGroupMemberFromDBGroupMember(groupMember);
					if (pbGroupMember==null) {
						continue;
					}
					if (pbGroupMember==com.gomeplus.grpc.protobuf.GroupMemberServices.GroupMember.getDefaultInstance()) {
						continue;
					}
					builder.addGroupMembers(pbGroupMember);
				}
				group2GroupMemebrMap.put(groupId, builder.build());
			}
    		
    		return group2GroupMemebrMap;
    	} catch (Exception e) {
    		log.info("listGroupMembersByGroupIds fail exception",e);
    		return null;
    	}finally{
    		cursorClose(cursor);
    	}
    	
    }
    
    /**
     *@Description: 得到所有收藏的群
     *@return
     */
    public List<GroupMember> getAllGroupCollections(String appId){
    	 String dbName = getDatabaseName(appId);
         MongoCursor<Document> cursor = null;
         List<GroupMember> groupMembers = null;
         try {
             groupMembers = new ArrayList<GroupMember>();
             BasicDBObject filter = new BasicDBObject();
             filter.put("isCollectionGroup", Constant.GROUP_COLLECTION.COLLECTION_YES.value);
             cursor = find(dbName, collName, filter);
             if (cursor==null) {
 				return null;
 			}
             while (cursor.hasNext()) {
                 Document item = cursor.next();
                 GroupMember been = (GroupMember) BeanTransUtils.document2Bean(item, GroupMember.class);
                 groupMembers.add(been);
             }
             log.info("GroupMemberDao listMemberGroups success!");
         } catch (Exception e) {
             log.error("GroupMemberDao listMemberGroups:", e.toString());
         }finally{
         	cursorClose(cursor);
         }
         return groupMembers;
    	
    }
    
	/**
	 * 根据groupId集合得到消息群（会话）
	 * @param appId
	 * @param uid
	 * @param groupIds
	 * @return
	 */
	public List<GroupMember> getGroupMembersByUidAndGroupIds(String appId,long userId,List<String> groupIds) {
		if (CollectionUtils.isEmpty(groupIds)) {
			log.error("getIsStickieByGroupIds groupIds is null");
			return null;
		}
    	String dbName = getDatabaseName(appId);
    	MongoCursor<Document> cursor = null;
    	MongoCollection<Document> coll = this.getCollection(dbName, collName);
    	try {
    		BasicDBObject[] deleteObjArr = new BasicDBObject[groupIds.size()];
    		for (int i = 0; i < groupIds.size(); i++) {
    			BasicDBObject whereObj = new BasicDBObject();
    			String groupId = groupIds.get(i);
    			whereObj.put("groupId", groupId);
    			whereObj.put("userId",userId);
    			deleteObjArr[i] = whereObj;
			}
    		BasicDBObject filter = new BasicDBObject().append(QueryOperators.OR, deleteObjArr);
    		cursor = coll.find(filter).iterator();
    		List<GroupMember> groupMemberList=new ArrayList<GroupMember>();
    		while(cursor.hasNext()) {
    			Document item = cursor.next();
    			GroupMember  groupMember= (GroupMember) BeanTransUtils.document2Bean(item, GroupMember.class);
    			groupMemberList.add(groupMember);
    		}
    		log.info("getGroupsByGroupIds success!");
    		return groupMemberList;
    	} catch (Exception e) {
    		log.error("getGroupsByGroupIds:", e);
    	} finally {
    		cursorClose(cursor);
    	}
    	return null;
	}
    
    
}
