package com.gomeplus.grpc.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;

/**
 * Created by wangshikai on 2016/7/19.
 */
public class ZKClient {

    private static Logger LOG = LoggerFactory.getLogger(ZKClient.class);

    private static CuratorFramework CLIENT;

    private static CountDownLatch LATCH = new CountDownLatch(1);

    private static ConcurrentSkipListSet<Boolean> CONTAINS_SET = new ConcurrentSkipListSet<>();

    private ZKClient() {
    }

    private static ZKClient INSTANCE = new ZKClient();

    public static ZKClient getInstance() {
        return INSTANCE;
    }

    public CuratorFramework init(String zkIpPort, String rootPath, int port) {
        initZkClient(zkIpPort, LATCH);
        try {
            LATCH.await();

            //创建根节点
            createRootNode(rootPath);

            //创建子节点
            String childPath = "";
            try {
                InetAddress address = InetAddress.getLocalHost();
                childPath = rootPath + "/" + address.getHostAddress() + ":" + port;

                createChildNode(rootPath, childPath);

                LOG.info("创建临时子节点 childPath:{}", childPath);
            } catch (UnknownHostException e) {
                LOG.error("获取本机地址失败,检查......");
            }
        } catch (InterruptedException e) {
            LOG.error("error:{}", e);
        }

        return CLIENT;
    }

    public static void initZkClient(String zkIpPort, final CountDownLatch latch) {
        CLIENT = CuratorFrameworkFactory
                .builder()
                .connectString(zkIpPort)
                .connectionTimeoutMs(1000)
                .sessionTimeoutMs(1000)
                //.namespace(namespace)
                .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 2000))
                .build();
        ConnectionStateListener listener = new ConnectionStateListener() {
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                if (newState == ConnectionState.CONNECTED) {
                    LOG.info("ZK连接成功");
                    latch.countDown();
                }
            }
        };
        CLIENT.getConnectionStateListenable().addListener(listener);
        CLIENT.start();
    }


    public static void createRootNode(String rootPath) {
        try {
            Stat state = CLIENT.checkExists().forPath(rootPath);
            if (state == null) {
                CLIENT.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(rootPath);
            } else {
                LOG.info("根节点已经存在,ROOT_PATH:{}", rootPath);
            }
        } catch (Exception e) {
            LOG.error("error:{}", e);
        }
    }

    public static void createChildNode(final String rootPath, final String path) {
        try {
            CLIENT.create().withMode(CreateMode.EPHEMERAL).forPath(path);
            CLIENT.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                @Override
                public void stateChanged(CuratorFramework client, ConnectionState state) {
                    try {
                        Stat nodeStat = CLIENT.checkExists().forPath(path);
                        if (nodeStat == null) {
                            //createChildNode(path);
                            CLIENT.create().withMode(CreateMode.EPHEMERAL).forPath(path);
                            LOG.info("ZK 连接状态变化,重新注册临时子节点......");
                        } else {
                            LOG.info("ZK 子节点已经存在:{}", path);
                            List<String> pathList = CLIENT.getChildren().forPath(rootPath);
                            for (String p : pathList) {
                                LOG.info("子节点路径:{},子节点长度：{}", p, pathList.size());
                            }
                        }
                    } catch (Exception e) {
                        LOG.error("error:{}", e);
                    }
                }
            });
        } catch (Exception e) {
            LOG.error("error:{}", e);
        }
    }


    public static void getChildrenPath(final String rootPath, final ConcurrentSkipListSet<String> serverList) {
        try {
            final Watcher watcher = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                        LOG.info("-----------------------ZK子节点发生变化------------------");
                        try {
                            List<String> childrenPaths = CLIENT.getChildren().forPath(rootPath);
                            serverList.addAll(childrenPaths);
                            getChildrenPath(rootPath, serverList);
                        } catch (Exception e) {
                            LOG.error("获取ZKerror error exception", e);
                        }
                    }
                    LOG.info("监听变化-----------------------发生变化");
                }
            };

            List<String> childrenPaths = CLIENT.getChildren().usingWatcher(watcher).forPath(rootPath);
            serverList.addAll(childrenPaths);
            if (!CONTAINS_SET.contains(true)) {
                CONTAINS_SET.add(true);
                CLIENT.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                    @Override
                    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                        try {
                            CLIENT.getChildren().usingWatcher(watcher).forPath(rootPath);
                        } catch (Exception e) {
                            LOG.error("ZKClient getChildrenPath error exception", e);
                        }
                    }
                });
            }
        } catch (Exception e) {
            LOG.error("error:{}", e);
        }
    }


    public void getDispatcherZKPath(final String ipPort, final String zkPath, final ConcurrentSkipListSet<String> serverList) {
        try {
            if (CLIENT == null || CLIENT.getState() != CuratorFrameworkState.STARTED) {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                initZkClient(ipPort, countDownLatch);
                countDownLatch.await();
            }
            getChildrenPath(zkPath, serverList);
        } catch (Exception e) {
            LOG.error("ZKClient getZKPath error exception", e);
        }
    }

    public static void main(String[] args) {
        String IP_PORT = "10.125.3.31:2181"; // 开发环境zk地址
        String ROOT_PATH = "/im-dispatcher"; // zk 调度服务根节点

        initZkClient(IP_PORT, LATCH);
        try {
            LATCH.await();
        } catch (Exception e) {
            LOG.error("ZKClient main error exception", e);
        }
    }

}
