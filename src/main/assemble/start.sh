#服务启动命令如:./start.sh 8899   注:8899为要开启的服务端口

# CLASS_PATH 为服务部署路径,根据实际部署路径调整修改为对应路径

DIR=`dirname $0`
DIR=`(cd "${DIR}/.."; pwd)`
CLASS_PATH="$DIR"

# 端口参数
port=$1

if [ ! -n "$1" ] ;then
	port=8899
	echo "default port is: $port"
fi

JAVA_OPTS="-Xms512m -Xmx1024m -Xmn512m"

echo "start gomeplus-grpc port is: $port"

# 加载环境变量
source /etc/profile
java  $JAVA_OPTS  -Djava.ext.dirs=$CLASS_PATH com.gomeplus.grpc.server.GrpcServer $port >/dev/null 2>&1 &