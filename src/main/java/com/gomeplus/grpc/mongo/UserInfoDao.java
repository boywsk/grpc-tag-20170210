package com.gomeplus.grpc.mongo;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;

import com.gomeplus.grpc.model.UserInfo;
import com.gomeplus.grpc.utils.BeanTransUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;

/**
 * 数据库用户信息
 */
public class UserInfoDao extends BaseDao {
    private final static String collName = "t_user_info";

    /**
     * 更新用户token信息
     * @param userInfo
     */
    public boolean updateUserInfo(String appId,UserInfo userInfo) {
        boolean success = false;
        String dbName = getDatabaseName(appId);
        BasicDBObject filter = new BasicDBObject();
        filter.put("uid", userInfo.getUid());
        Document doc = new Document();
        if(StringUtils.isNotEmpty(userInfo.getToken())){
            doc.put("token", userInfo.getToken());
        }
        if(userInfo.getTokenExpires() > 0){
            doc.put("tokenExpires", userInfo.getTokenExpires());
        }
        if(userInfo.getUpdateTime() > 0){
            doc.put("updateTime", userInfo.getUpdateTime());
        }
        success = this.update(dbName, collName, filter, doc);
        return success;
    }

    public UserInfo getUserInfoByImUserId(String appId,long uid){
        MongoCursor<Document> cursor = null;
        String dbName = getDatabaseName(appId);
        UserInfo userInfo = null;
        try {
            BasicDBObject filter = new BasicDBObject();
            filter.put("uid", uid);
            cursor = find(dbName, collName, filter);
            if (cursor.hasNext()) {
                Document item = cursor.next();
                userInfo = (UserInfo) BeanTransUtils.document2Bean(item, UserInfo.class);
            }
        } catch (Exception e) {
            log.error("UserInfoDao getUserInfoByImUserId error:", e);
        } finally {
            cursorClose(cursor);
        }
        return userInfo;
    }

    /**
     * 清空用户 apnsToken
     * @param appId
     * @param uid
     * @return
     */
    public boolean clearApnsToken(String appId,long uid) {
        String dbName = getDatabaseName(appId);
        BasicDBObject filter = new BasicDBObject();
        filter.put("uid", uid);
        Document doc = new Document();
        doc.put("apnsToken", "");
        return this.update(dbName, collName, filter, doc);
    }
}
