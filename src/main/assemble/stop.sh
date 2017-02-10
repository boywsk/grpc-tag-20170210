# 停止服务

pid=`ps -ef | grep "com.gomeplus.grpc.server.GrpcServer" | grep -v grep | awk '{print $2}'`

if [ -n "$pid" ]; then
  kill -9 $pid
fi

# 服务停止
echo 'GrpcServer 服务停止......'