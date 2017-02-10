package com.gomeplus.grpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangshikai on 2016/10/8.
 */
public class ClientGetUserDemo {
    private static Logger LOG = LoggerFactory.getLogger(ClientGetUserDemo.class);

    
    public static void main(String[] args) {
    }
//        try {
//            final GomeplusRpcServices.RequestUser request = GomeplusRpcServices.RequestUser.newBuilder()
//                    .setAppId("TEST_APP_ID").setImUserId(665).build(); 
//            //----------------异步调用--------------------
//            GetUserServiceGrpc.GetUserServiceStub asyncGetUserService = GetUserServiceGrpc.newStub(LoadBalanceRouter.INSTANCE.getRouterChannel());
//            asyncGetUserService.getUser(request, new StreamObserver<GomeplusRpcServices.ResponseUser>() {
//                @Override
//                public void onNext(GomeplusRpcServices.ResponseUser responseUser) {
//                    if (!responseUser.equals(GomeplusRpcServices.ResponseUser.getDefaultInstance())) {
//                        LOG.info("异步调用:uid:{},token:{}", responseUser.getUid(), responseUser.getToken());
//                    } else {
//                        LOG.info("用户不存在");
//                    }
//                }
//
//                @Override
//                public void onError(Throwable throwable) {
//                    //TODO
//                }
//
//                @Override
//                public void onCompleted() {
//                    //TODO
//                }
//            });
//
//            //---------------------同步调用------------------------------------
//            for (int i = 0; i < 100; i++) {
//                Thread.sleep(500);
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final GetUserServiceGrpc.GetUserServiceBlockingStub getUserService = GetUserServiceGrpc.newBlockingStub(LoadBalanceRouter.INSTANCE.getRouterChannel());
//                        long a = System.currentTimeMillis();
//                        GomeplusRpcServices.ResponseUser response = getUserService.getUser(request);
//                        long b = System.currentTimeMillis();
//                        System.out.println(" client 查询时间差:" + (b - a));
//                        if (!response.equals(GomeplusRpcServices.ResponseUser.getDefaultInstance())) {
//                            LOG.info("uid:{},token:{}", response.getUid(), response.getToken());
//                        } else {
//                            LOG.info("用户不存在");
//                        }
//                    }
//                });
//                t.start();
//            }
//
//            ListenableFuture<GomeplusRpcServices.ResponseUser> listenableFuture = GetUserServiceGrpc.newFutureStub(LoadBalanceRouter.INSTANCE.getRouterChannel()).getUser(request);
//            GomeplusRpcServices.ResponseUser user = listenableFuture.get();
//            if (!user.equals(GomeplusRpcServices.ResponseUser.getDefaultInstance())) {
//                LOG.info("Future uid:{},token:{}", user.getUid(), user.getToken());
//            } else {
//                LOG.info("Future 用户不存在");
//            }
//
//
//        } catch (Exception e) {
//            LOG.error("error:{}", e);
//        }
//    }

}
