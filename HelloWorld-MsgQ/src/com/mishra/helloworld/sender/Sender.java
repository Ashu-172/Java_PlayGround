package com.mishra.helloworld.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class Sender {
	private final static String HW_QNAME  		= "message";
	private final static String HW_HOST_NAME 	= "localhost";
	

	public static void main(String[] args) {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HW_HOST_NAME);
		
		try(Connection conn = factory.newConnection();
				Channel channel = conn.createChannel()) {
			channel.queueDeclare(HW_QNAME, false, false, false, null);
			String msg = "Hello World";
			channel.basicPublish("",HW_QNAME, null, msg.getBytes());
			System.out.println("Sent Msg: "+msg);
			
		}
		catch(Exception ex) {
			System.out.println("Exception Occourred : "+ex.getMessage());
		}
		

	}

}
