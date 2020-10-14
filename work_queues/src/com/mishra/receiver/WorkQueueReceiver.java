/**
 * Multiple instances can be started of receiver, messages will be distributed among them in Round Robin fashion.
 * On average every consumer will get the same number of messages.
 * 
 * */

package com.mishra.receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class WorkQueueReceiver {

	private final static String HW_QNAME 		= "message";
	private final static String HW_HOST_NAME 	= "localhost";
	/**
	 * 1. If Auto_Ack is true, message will be marked for deletion immediately after sending to consumer, 
	 * 		if that consumer dies in between processing a message then we'll lose all the messages that were dispatched
	 * 		to that consumer but not yet processed along with the message that it was processing.
	 * 2. If AutoAck is false, An acknowledgement is sent back by the consumer to tell RabbitMQ 
	 * 		that a particular message has been received, processed and that RabbitMQ is free to delete it. 
	 * */
	private final static boolean HW_AUTO_ACK    = true;
	
	public static void main(String[] args) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HW_HOST_NAME);
		
		try {
			Connection conn =  factory.newConnection();
			Channel channel = conn.createChannel();
			channel.queueDeclare(HW_QNAME,false, false, false, null);
			System.out.println("Waiting For Message");
			
			DeliverCallback deliverCallBack = (consumerTag, delivery)->{
				String msg = new String(delivery.getBody(),"UTF-8");
				System.out.println("Received Msg: "+msg);
				try {
					processMsg(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				finally {
					System.out.println("Processing Done..");
				}
			};
			
			channel.basicConsume(HW_QNAME, HW_AUTO_ACK,deliverCallBack,consumerTag->{});
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

	}

	private static void processMsg(String task) throws InterruptedException {
		for(char ch : task.toCharArray()) {
			if(ch == '.')
				Thread.sleep(1000);
		}
		
	}

}
