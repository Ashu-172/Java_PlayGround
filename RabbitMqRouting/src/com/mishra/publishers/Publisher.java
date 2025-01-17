package com.mishra.publishers;

import java.util.HashSet;
import java.util.Set;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publisher {

	private static final String MQ_HOST_NAME 		= "localhost";
	private static final String MQ_EXCHANGE_TYPE 	= "direct";
	private static final String MQ_EXCHANGE_NAME	= "routing";
	
	public static void main(String[] args) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(MQ_HOST_NAME);
		
		try(Connection connection = factory.newConnection();
				Channel channel = connection.createChannel();){
			channel.exchangeDeclare(MQ_EXCHANGE_NAME, MQ_EXCHANGE_TYPE);
			System.out.println("Publishing logs..");
			
			Set<String> severity = new HashSet<String>();
			severity.add("info");
			severity.add("warning");
			severity.add("error");
			
			String msg;
			for(int i=0; i<10;i++) {
				for(String level:severity) {
					 msg= level+" : This is log msg "+i;
					channel.basicPublish(MQ_EXCHANGE_NAME, level, null, msg.getBytes()); //level is the routingKey using which consumer will bind queues to exchange
					System.out.println("Log: '"+msg+"'");
				}
			}
			System.out.println("Logs Published..");
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

	}

}
