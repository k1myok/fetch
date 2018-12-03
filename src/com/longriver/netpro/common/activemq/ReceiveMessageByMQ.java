package com.longriver.netpro.common.activemq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.JFileChooser;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.BlobMessage;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.common.MQResult.GuideUtil;
import com.longriver.netpro.util.GetProprities;

public class ReceiveMessageByMQ {
    static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    private static Logger logger = Logger
            .getLogger(ReceiveMessageByMQ.class);

    public static void main(String[] args) {
        String receiveType = "1";// 接收的类型 1发布一对一 ；2订阅一对多
        String isNotFile = "false";
        String ip = GetProprities.PaginationConfig.getProperty("mqip");// 接收ip
        String modeName = GetProprities.PaginationConfig.getProperty("returnname");// 模式名称
        if (modeName != null && !modeName.equals("")) {
            try {
                // logger.info("receivePictrueMQ==="+DateUtil.getCurrentTime());
                ReceiveMessageByMQ.receive(receiveType, ip, isNotFile,
                        modeName);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }else
        {
            logger.error("缺乏mq消息接收名称，消息不接收");
        }
    }
    
    public static void receivetask() throws JMSException
    {
        String ip = GetProprities.PaginationConfig.getProperty("mqip");// 接收ip
        String modeName = GetProprities.PaginationConfig.getProperty("guidevpsname");// 模式名称
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "tcp://" + ip + ":61616");

        // 创建 Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();
        // 创建 Session
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(modeName);
        // 创建 Consumer
        MessageConsumer consumer = session.createConsumer(destination);
        // 注册消息监听器，当消息到达时被触发并处理消息
        consumer.setMessageListener(new GuideUtil()); 
    }
   

    /**
     * 
     * 
     * Title void Description
     * 
     * @author jacun
     * @param modeName
     * @param ip
     * @param receiveType
     * @date 2017-4-11上午10:43:10
     * @throws JMSException
     */
    public static void receive(String receiveType, String ip, String isNotFile,
            String modeName) throws JMSException {
        System.out.println("开始接收2");
        // 获取 ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "tcp://" + ip + ":61616");

        // 创建 Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();
        // 创建 Session
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        // 创建 Destinatione
        // 判断是一对一还是一对多
        if ("2".equals(receiveType)) {
            // 一对多
            System.out.println("一对多接收数据3");
            receiveTopic(session, isNotFile, modeName);
        } else {
            // 一对一
            System.out.println("一对一接收数据3");
            receiveQueue(session, isNotFile, modeName);
        }

    }

    private static void receiveTopic(Session session, String isNotFile,
            String modeName) {
        try {
            final String isFile = isNotFile;
            Destination destination = session.createTopic(modeName);
            // 创建 Consumer
            MessageConsumer consumer = session.createConsumer(destination);
            // 注册消息监听器，当消息到达时被触发并处理消息
            consumer.setMessageListener(new MessageListener() {
                // 监听器中处理消息
                public void onMessage(Message message) {
                    logger.info("receivemessagefromMQ===");
                    if ("true".equals(isFile)) {
                        System.out.println("有文件接收数据4");
                        ReceiveMessageByMQ.receiveFile(message);
                    } else {
                        System.out.println("无文件接收数据4");
                        ReceiveMessageByMQ.receiveData(message);

                    }

                }

            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    private static void receiveQueue(Session session, String isNotFile,
            String modeName) {
        try {
            final String isFile = isNotFile;
            Destination destination = session.createQueue(modeName);
            // 创建 Consumer
            MessageConsumer consumer = session.createConsumer(destination);
            // 注册消息监听器，当消息到达时被触发并处理消息
            consumer.setMessageListener(new MessageListener() {
                // 监听器中处理消息

                public void onMessage(Message message) {
                    if ("true".equals(isFile)) {
                        System.out.println("有文件接收数据4");
                        ReceiveMessageByMQ.receiveFile(message);
                    } else {
                        System.out.println("无文件接收数据4");
                        ReceiveMessageByMQ.receiveData(message);

                    }

                }

            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    protected static void receiveData(Message message) {
        String sendType = null;
        String jsonData = null;
        String website = null;
        try {
            TextMessage msg = (TextMessage) message;
            sendType = msg.getStringProperty("sendType");
            jsonData = msg.getStringProperty("jsonData");
            website = msg.getStringProperty("website");
//            missiondetail missiondetail = (missiondetail) JsonHelper.JSONToObj(jsonData, missiondetail.class);
//            TaskProcessing.update(missiondetail);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    private static void receiveFile(Message message) {
        System.out.println("有文件接收成功5");
        try {
            System.out.println(message.getJMSDestination().toString());
        } catch (JMSException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String sendType = null;
        String jsonData = null;
        String website = null;
        if (message instanceof BlobMessage) {
            BlobMessage blobMessage = (BlobMessage) message;
            try {
                System.out.println("有文件接收成功5");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("请指定文件保存位置");
                fileChooser.setSelectedFile(new File("e:\\"));
                File file = fileChooser.getSelectedFile();
                OutputStream os = new FileOutputStream(file);
                InputStream inputStream = blobMessage.getInputStream();
                // 写文件，你也可以使用其他方式
                byte[] buff = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buff)) > 0) {
                    os.write(buff, 0, len);
                }
                os.close();

                sendType = blobMessage.getStringProperty("sendType");
                jsonData = blobMessage.getStringProperty("jsonData");
                website = blobMessage.getStringProperty("website");
                System.out.println(sendType);
                System.out.println(jsonData);
                System.out.println(website);
                // SavePicMessage.saveMessage(jsonData,website);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
