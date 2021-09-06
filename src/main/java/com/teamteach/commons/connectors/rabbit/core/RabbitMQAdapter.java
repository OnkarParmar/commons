package com.teamteach.commons.connectors.rabbit.core;

import com.teamteach.commons.utils.AppJsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Log4j2
@Component
@RequiredArgsConstructor
class RabbitMQAdapter implements IMessagingPort {

    static final String LISTENER_ID = "taihorabbitlistener";

    final RabbitTemplate rabbitTemplate;
    final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    boolean canbeStarted = false;
    boolean containerstarted = false;

    @Value("${exchange.signup}")
    String exchangename;

    @Value("${listen.queue.names:,}")
    Set<String> queuenames;

    Map<String,Consumer<?>>
                generalConsumers = new ConcurrentHashMap<>();

    Map<String,Class<?>> genMessageTypes = new ConcurrentHashMap<>();

    @Override
    public <T> boolean registerGeneralResponseListener(String channelname,
                                                                                   Class<T> clazz,
                                                                                   Consumer<T> consumer) {
        if (generalConsumers.containsKey(channelname))
            return false;
        if (!queuenames.contains(channelname))
            return false;
        generalConsumers.put(channelname, consumer);
        genMessageTypes.put(channelname, clazz);
        canbeStarted = true;
        startContainer();
        return true;
    }

    @Override
    public boolean sendMessage(Object data) {
        rabbitTemplate.convertAndSend(data);
        return true;
    }

    @Override
    public boolean sendMessage(Object data, String routingkey) {
        System.out.println(exchangename+" --> "+routingkey);
        System.out.println(data);
        rabbitTemplate.convertAndSend(exchangename, routingkey, data);
        return true;
    }

    @RabbitListener( id=LISTENER_ID, queues = "#{'${listen.queue.names}'.split(',')}", autoStartup = "false")
    @Transactional
    void receiveMessage(byte[] msgBytes , @Header(AmqpHeaders.CONSUMER_QUEUE) String queue) {
        String msgStr = new String(msgBytes);
        log.info("Received Message {} from Queue {}", msgStr , queue);
        try {
            sendGeneralMessageToSubscriber(queue, msgStr);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }

    private void sendGeneralMessageToSubscriber(String queue, String msgStr) {
        Class<?> clazz = this.genMessageTypes.getOrDefault(queue, null);
        var respMsg =
                AppJsonUtils.deserializeClass(msgStr, clazz);
        Consumer consumer
                = this.generalConsumers.getOrDefault(queue, null);
        consumer.accept(respMsg);
    }

    void startContainer() {
        if (!containerstarted)
            new ListenerWatchThread().start();
        return;
    }

    class ListenerWatchThread extends Thread {
        public void run() {
            while (true ) {
                try {
                    Thread.sleep(1000l);
                    var listener =
                            rabbitListenerEndpointRegistry.getListenerContainer(LISTENER_ID);
                    if (listener != null && canbeStarted) {
                        listener.start();
                        containerstarted = true;
                        break;
                    }
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}
