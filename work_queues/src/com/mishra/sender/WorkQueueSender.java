package com.mishra.sender;

import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class WorkQueueSender {
	private final static String HW_QNAME  		= "message";
	private final static String HW_HOST_NAME 	= "localhost";
	
	public static void main(String[] args) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HW_HOST_NAME);
		try(Connection conn = factory.newConnection();
				Channel channel = conn.createChannel()){
			channel.queueDeclare(HW_QNAME, false, false, false, null);
			
			System.out.println("Write Your msg use . for seconds, hit 0 + Enter to terminate");
			Scanner sc = new Scanner(System.in);
			String msg;
			while(true) {
				msg = sc.next();
				if(msg == "0") {
					break;
				}
				channel.basicPublish("",HW_QNAME , null, msg.getBytes());
				System.out.println("Sent msg: "+msg);
			}
			sc.close();
			
		}
		catch(Exception ex) {
			System.out.println("Exception Occourred : "+ex.getMessage());
		}

	}

}
