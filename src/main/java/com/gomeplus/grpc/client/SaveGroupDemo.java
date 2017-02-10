package com.gomeplus.grpc.client;


/**
 * Created by wangshikai on 2016/10/17.
 */
public class SaveGroupDemo {

//    public static void main(String[] args) {
//        SaveGroupServiceGrpc.SaveGroupServiceBlockingStub serviceBlockingStub = SaveGroupServiceGrpc.newBlockingStub(LoadBalanceRouter.INSTANCE.getRouterChannel());
//        GomeplusRpcServices.RequestSaveGroup requestSaveGroup = GomeplusRpcServices.RequestSaveGroup.newBuilder().setAppId("123").setGroup(GomeplusRpcServices.Group.newBuilder()
//                .setGroupId("groupId").setAvatar("avatar").setCapacity(100).setGroupName("groupName").setGroupDesc("群组").setIsAudit(1).build()).build();
//        GomeplusRpcServices.ResponseSaveGroup responseSaveGroup = serviceBlockingStub.saveGroupService(requestSaveGroup);
//        System.out.println("返回结果:"+responseSaveGroup.getResult());
//
//    }
}
