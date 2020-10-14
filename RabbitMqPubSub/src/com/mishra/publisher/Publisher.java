package com.mishra.publisher;


import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publisher {

	private static final String MQ_EXCHANGE_NAME	= "logs";
	private static final String MQ_HOST_NAME 		= "localhost"; 
	
	public static void main(String[] args) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(MQ_HOST_NAME);
		
		try(Connection conn = factory.newConnection();
				Channel channel = conn.createChannel()){
			/**
			 * fanout exchange BroadCast the message to all the queues it knows.
			 * */
			channel.exchangeDeclare(MQ_EXCHANGE_NAME, "fanout"); 
			
			System.out.println("Write Your msg use . for seconds, hit 0 + Enter to terminate");
			Scanner sc = new Scanner(System.in);
			String msg;
			while(true) {
				msg = sc.next();
				if(msg.equals("quit")) {
					break;
				}
			
				channel.basicPublish(MQ_EXCHANGE_NAME, "", null, msg.getBytes());
				System.out.println("Message Sent : "+msg);
			}
			sc.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

}
