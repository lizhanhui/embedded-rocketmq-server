package com.ndpmedia.rocketmq.test;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IntegrationTest {

    @BeforeClass
    public static void beforeClass() {
        Launcher.main(null);

        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSendMessage() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("PG_QuickStart");
        producer.start();

        Message message = new Message("T_QuickStart", "Hello".getBytes());
        SendResult sendResult = producer.send(message);
        Assert.assertNotNull(sendResult);
        producer.shutdown();
    }

    @Test
    public void testConsumeMessage() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("CG_QuickStart");
        final AtomicInteger count = new AtomicInteger(0);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("T_QuickStart", null);
        consumer.setMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                count.addAndGet(msgs.size());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });


        consumer.start();
        Thread.sleep(30 * 1000);
        Assert.assertTrue(count.get() > 0);
        consumer.shutdown();
    }


}
