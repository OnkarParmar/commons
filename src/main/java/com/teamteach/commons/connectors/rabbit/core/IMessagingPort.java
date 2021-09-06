package com.teamteach.commons.connectors.rabbit.core;



import java.util.function.Consumer;

public interface IMessagingPort {

     <T> boolean registerGeneralResponseListener(String channelname,
                                                                             Class<T> clazz,
                                                                             Consumer<T> consumer);
     // Sends the message to default exchange
     boolean  sendMessage(Object data);
     // Sends the message to specified exchange with routing key
     boolean  sendMessage(Object data, String routingkey);
}
