package com.gomeplus.grpc.router.report;

import java.io.Serializable;

/**
 * Created by wangshikai on 2016/7/18.
 */
public class ClientMsg implements Serializable {
    private int requestType;  // 请求类型: 1 : 上报服务器资源地址  2:拉取匹配资源服务地址

    private ReqRpcReportMsg reqRpcReportMsg;

    private ReqRpcServersMsg reqRpcServersMsg;

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public ReqRpcReportMsg getReqRpcReportMsg() {
        return reqRpcReportMsg;
    }

    public void setReqRpcReportMsg(ReqRpcReportMsg reqRpcReportMsg) {
        this.reqRpcReportMsg = reqRpcReportMsg;
    }

    public ReqRpcServersMsg getReqRpcServersMsg() {
        return reqRpcServersMsg;
    }

    public void setReqRpcServersMsg(ReqRpcServersMsg reqRpcServersMsg) {
        this.reqRpcServersMsg = reqRpcServersMsg;
    }
}
