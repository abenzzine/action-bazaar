/**
 *  ChatStatusMessageHandler.java
 *  EJB 3 in Action
 *  Book: http://manning.com/panda2/
 *  Code: http://code.google.com/p/action-bazaar/
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.actionbazaar.chat.bulletin;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * This bulletin service distributes messages to all connected clients.
 * @author Ryan Cuprak
 */
@ServerEndpoint("/admin/bulletin")
public class BulletinService implements MessageListener {
    
    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger("Bulletin");
    
    /**
     * JMS Context
     */
    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    private JMSContext context;
    
    /**
     * Bulletin Messages
     */
    @Resource(mappedName="jms/BulletinMessages")
    private Topic topic;
    
    /**
     * Producer
     */
    private JMSProducer producer;
    
    /**
     * Invoked when a client connects to the server
     * @param session 
     */
    @OnOpen
    public void onOpen(Session session) {
        logger.log(Level.INFO,"Connection has been established.");
        JMSConsumer consumer = context.createConsumer(topic);
        consumer.setMessageListener(null);
        producer = context.createProducer();
        producer.send(topic,"Hello World!");
        
    }
    
    /**
     * Processes an incoming message from the client
     * @param message
     * @param session 
     */
    @OnMessage
    public void processMessage(String message, Session session) {
        System.out.println("Processing incoming message." + message);
    }

    /**
     * Invoked whenever a message is published to the bulletin
     * @param message - to be sent out to clients
     */
    @Override
    public void onMessage(Message message) {
        logger.log(Level.INFO, "Got a message!{0}", message);
    }
}
