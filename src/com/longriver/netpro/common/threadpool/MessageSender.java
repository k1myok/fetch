package com.longriver.netpro.common.threadpool;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQTopicPublisher;
import org.apache.activemq.ActiveMQTopicSession;
import org.apache.activemq.command.ActiveMQTopic;
 

public class MessageSender {
     
    private String url;
    private String user;
    private String password;
    private final String QUEUE;
 
    public MessageSender(String queue, String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.QUEUE = queue;
    }
 
    public void sendJsonFetch(String jsonFethch) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
        
        TopicConnection connection = null;  
        ActiveMQTopicSession session = null;  
        ActiveMQTopicPublisher publisher = null;  
        ActiveMQTopic topic = null;  
 
        try {
            connection = connectionFactory.createTopicConnection();
            System.out.println("MessageSender=url="+url);
            System.out.println("MessageSender=QUEUE="+QUEUE);
            System.out.println("MessageSender=connection="+connection);
            connection.start();
 
             session = (ActiveMQTopicSession) connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
 
                topic = (ActiveMQTopic) session.createTopic(QUEUE);  
                publisher = (ActiveMQTopicPublisher) session.createPublisher(topic);  
                publisher.setDeliveryMode(DeliveryMode.PERSISTENT);  
                connection.start();  
                
                TextMessage messageText = session.createTextMessage();  
                messageText.setText(jsonFethch);  
                publisher.publish(messageText);  
                
                session.close();
                publisher.close();
                connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        } 
    }
 
    public String getUrl() {
        return url;
    }
 
    public void setUrl(String url) {
        this.url = url;
    }
 
    public String getUser() {
        return user;
    }
 
    public void setUser(String user) {
        this.user = user;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
}