package com.mishra.consumers;

import java.util.HashSet;
import java.util.Set;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Logger {

	private static final String MQ_HOST_NAME 		= "localhost";
	private static final String MQ_EXCHANGE_NAME	= "routing";
	private static final String MQ_EXCHANGE_TYPE	= "direct";
	
	public static void main(String[] args) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(MQ_HOST_NAME);
		
		try {
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			
			channel.exchangeDeclare(MQ_EXCHANGE_NAME, MQ_EXCHANGE_TYPE);
			String queueName = channel.queueDeclare().getQueue();
			
			Set<String> severity = new HashSet<String>();
			severity.add("info");
			severity.add("warning");
			severity.add("error");
			
			for(String level:severity) {
				channel.queueBind(queueName, MQ_EXCHANGE_NAME, level ); //level is routing key
			}
			
			DeliverCallback deliverCallBack = (channelTag, delivery) -> {
				String msg = new String(delivery.getBody(),"UTF-8");
				System.out.println(msg);
			};
			System.out.println("Consuming Logs..");
			channel.basicConsume(queueName, true, deliverCallBack, consumerTag->{});
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

	}

}
