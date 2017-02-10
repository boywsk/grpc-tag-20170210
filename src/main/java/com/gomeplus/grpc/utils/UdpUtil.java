package com.gomeplus.grpc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by wangshikai on 2016/11/9.
 */
public class UdpUtil {

    private static Logger LOG = LoggerFactory.getLogger(UdpUtil.class);

    /**
     *
     * @param ip
     * @param port
     * @param msg
     * @return
     */
    public static void SendMsg(String ip, int port, String msg) {
        try {
            if (msg == null) {
                LOG.error("msg:{} is empty!", msg);
                return;
            }
            InetAddress address = InetAddress.getByName(ip);
            //InetAddress address = InetAddress.getByName("10.125.72.89");  //服务器地址  10.125.3.61   10.69.16.92
            //int port = 8866;  //服务器的端口号  国美+ 8877   企业办公 8866
            //创建发送方的数据报信息
            DatagramPacket dataGramPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, port);

            DatagramSocket socket = new DatagramSocket();  //创建套接字
            socket.setSoTimeout(5000);
            socket.send(dataGramPacket);  //通过套接字发送数据

            socket.close();
        } catch (IOException e) {
            //e.printStackTrace();
            LOG.error("error:{}", e);
        }
    }

}
