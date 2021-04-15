package com.teamteach.commons.connectors.rabbit.core;



import java.util.function.Consumer;

public interface IMessagingPort {

     <T extends AbstractDocumentMsg> boolean registerGeneralResponseListener(String channelname,
                                                                             Class<T> clazz,
                                                                             Consumer<? extends AbstractDocumentMsg> consumer);
     // Sends the message to default exchange
     boolean  sendMessage(Object data);
     // Sends the message to specified exchange with routing key
     boolean  sendMessage(String exchangename, String routingkey , Object data);
}
