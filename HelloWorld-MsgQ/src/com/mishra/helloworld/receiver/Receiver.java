package com.mishra.helloworld.receiver;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Receiver {

	private final static String HW_QNAME 		= "message";
	private final static String HW_HOST_NAME 	= "localhost";
	private final static boolean HW_AUTO_ACK    = true;
	
	public static void main(String[] args) {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HW_HOST_NAME);
		
		Connection conn;
		try {
			conn = factory.newConnection();
			Channel channel = conn.createChannel();
			channel.queueDeclare(HW_QNAME,false, false, false, null);
			System.out.println("Waiting For Message");
			
			DeliverCallback deliverCallBack = (consumerTag,delivery)->{
				String message  	= new String(delivery.getBody(), "UTF-8");

				System.out.println("Recieved Msg : "+ message);
			};
			
			channel.basicConsume(HW_QNAME, HW_AUTO_ACK, deliverCallBack, consumerTag ->{});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

}
