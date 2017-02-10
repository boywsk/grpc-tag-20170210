package com.gomeplus.grpc.client;

public class GroupServiceDemo {
	public static void main(String[] args) {
		
//		GroupServiceGrpc.GroupServiceBlockingStub serviceBlockingStub = SaveGroupServiceGrpc
//				.newBlockingStub(LoadBalanceRouter.INSTANCE.getRouterChannel());
//		GomeplusRpcServices.RequestSaveGroup requestSaveGroup = GomeplusRpcServices.RequestSaveGroup
//				.newBuilder()
//				.setAppId("123")
//				.setGroup(
//						GomeplusRpcServices.Group.newBuilder()
//								.setGroupId("groupId").setAvatar("avatar")
//								.setCapacity(100).setGroupName("groupName")
//								.setGroupDesc("群组").setIsAudit(1).build())
//				.build();
//		GomeplusRpcServices.ResponseSaveGroup responseSaveGroup = serviceBlockingStub
//				.saveGroupService(requestSaveGroup);
//		System.out.println("返回结果:" + responseSaveGroup.getResult());
/*
		GroupServiceGrpc.GroupServiceBlockingStub serviceBlockingStub=GroupServiceGrpc
				.newBlockingStub(LoadBalanceRouter.INSTANCE.getRouterChannel());
		RequestGroup requestGroup=GomeplusRpcServices.RequestGroup.newBuilder().setAppId("123").setGroup(GomeplusRpcServices.Group.newBuilder().setGroupId("1123").build()).build();
		RespnoseResult saveGroup = serviceBlockingStub.saveGroup(requestGroup);
		boolean success = saveGroup.getSuccess();
		System.out.println("返回结果："+success);*/
		
	
	}
}
