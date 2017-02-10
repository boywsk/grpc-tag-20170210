package com.gomeplus.grpc.router;

import com.alibaba.fastjson.JSON;
import com.gomeplus.grpc.global.RpcGlobal;
import com.gomeplus.grpc.router.report.ClientMsg;
import com.gomeplus.grpc.router.report.ReqRpcReportMsg;
import com.gomeplus.grpc.utils.JedisClusterClient;
import com.gomeplus.grpc.utils.LoadClassUtil;
import com.gomeplus.grpc.utils.UdpUtil;
import com.gomeplus.grpc.utils.ZKClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangshikai on 2016/11/14.
 */
public class ReportDispatcher {
    private static Logger LOG = LoggerFactory.getLogger(ReportDispatcher.class);
    public static ConcurrentSkipListSet<String> DISPATCHER_SERVER_LIST = new ConcurrentSkipListSet<>();

    public static ReportDispatcher INSTANCE = new ReportDispatcher();

    private static Set<String> RPC_CMDS = new HashSet<>();

    private ReportDispatcher() {
    }

    private void initRpcCmd(){
        try {
            Set<Class<?>> clazzSet = LoadClassUtil.getClasses(RpcGlobal.RPC_PACKAGE);
            for (Class clz : clazzSet) {
                if(clz.getSimpleName().endsWith("Grpc")){
                    RPC_CMDS.add(clz.getSimpleName());
                }
            }
        } catch (Exception e) {
            LOG.error("error:{}",e);
        }
    }

    /**
     * 向调服服务器汇报当前服务的服务信息
     * @param zkIpPort
     * @param zkPath
     * @param port
     */
    public void initDispatcherReport(final String zkIpPort, final String zkPath, final int port) {
        ZKClient.getInstance().getDispatcherZKPath(zkIpPort, zkPath, DISPATCHER_SERVER_LIST);
        initRpcCmd();
        //定期拉取rpc服务列表
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] ipPortArr = getIpPort(DISPATCHER_SERVER_LIST);
                    ClientMsg clientMsg = createReqRpcReportMsg(RpcGlobal.REQUEST_TYPE.RPC_REPORT.value, port);
                    UdpUtil.SendMsg(ipPortArr[0], Integer.valueOf(ipPortArr[1]), JSON.toJSONString(clientMsg));
                    LOG.info("RPC-REPORT-SUCCESS,DISPATCHER_SERVER_LIST:{}",DISPATCHER_SERVER_LIST.toString());
                } catch (Exception e) {
                    LOG.error("schedule report error:{}",e);
                }
            }
        }, 0, RpcGlobal.TWO_MINUTES, TimeUnit.MILLISECONDS);
    }

    private static String[] getIpPort(ConcurrentSkipListSet<String> DISPATCHER_SERVER_LIST) {
        long index = System.nanoTime() % DISPATCHER_SERVER_LIST.size();
        String ipPort = (String) DISPATCHER_SERVER_LIST.toArray()[(int) index];
        return ipPort.split(":");
    }


    //汇报服务资源消息
    public static ClientMsg createReqRpcReportMsg(int requestType, int port) {
        ClientMsg clientMsg = new ClientMsg();
        clientMsg.setRequestType(requestType);
        ReqRpcReportMsg reqRpcReportMsg = new ReqRpcReportMsg();
        String address = null;
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOG.error("error:{}",e);
        }
        String rpcIpPort = address + ":" + port;
        reqRpcReportMsg.setIpPort(rpcIpPort);
        reqRpcReportMsg.setType(RpcGlobal.SERVER_TYPE.RPC.value);
        reqRpcReportMsg.setCmd(RPC_CMDS);
        double weight = RpcGlobal.RPC_WEIGHT; //默认值
        try {
            String redisWeight = JedisClusterClient.INSTANCE.getJedisCluster().get(rpcIpPort + RpcGlobal.RPC_WEIGHT_REDIS_KEY);
            weight = (redisWeight == null ? RpcGlobal.RPC_WEIGHT : Double.parseDouble(redisWeight));
            LOG.info("server:{},realWeight:{},redisWeight:{}",rpcIpPort,weight,redisWeight);
        } catch (Exception e) {
            LOG.error("error:{}", e);
        }
        reqRpcReportMsg.setWeight(weight);
        clientMsg.setReqRpcReportMsg(reqRpcReportMsg);
        return clientMsg;
    }

}
