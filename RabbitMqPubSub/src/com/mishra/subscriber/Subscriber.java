package com.mishra.subscriber;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Subscriber {
	
	private static final String MQ_EXCHANGE_NAME	= "logs";
	private static final String MQ_HOST_NAME 		= "localhost"; 
	
	public static void main(String[] args) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(MQ_HOST_NAME);
		
		try{
			Connection conn = factory.newConnection();
			Channel channel = conn.createChannel();
			
			/**
			 * We want to get all log messages, not just a subset of them. We're also interested only in currently flowing messages 
			 * not in the old ones.	To solve that we need two things.
			 * Firstly, whenever we connect to Rabbit we need a fresh, empty queue. To do this we could create a queue with a random name, 
			 * 		or we can let the server choose a random queue name for the queue.
			 * Secondly, once we disconnect the consumer the queue should be automatically deleted.
			 * */
			
			channel.exchangeDeclare(MQ_EXCHANGE_NAME, "fanout");
						
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, MQ_EXCHANGE_NAME, "");
			
			System.out.println("Msg Queue Created: "+queueName +" Waiting For Message..");
			
			DeliverCallback deliverCallBack = (consumerTag, delivery)->{
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" Msg Received: "+message);
			};
			
			channel.basicConsume(queueName, true, deliverCallBack, consumerTag->{});
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

	}

}
