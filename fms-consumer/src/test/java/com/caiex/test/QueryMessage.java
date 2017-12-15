package com.caiex.test;

public class QueryMessage {
	
	/*static DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("operationLog");
	
	static {
		consumer.setInstanceName(UUID.randomUUID().toString().substring(0,6));
		consumer.setNamesrvAddr("192.168.1.219:9876 

");
		consumer.setMessageListener(new MessageListenerConcurrently() {
			
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
					ConsumeConcurrentlyContext context) {
				return null;
			}
		});
		try {
			consumer.subscribe("reply", "*");
//			consumer.subscribe("cx_order_topic", "*");
			consumer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws RemotingException, MQBrokerException, InterruptedException, MQClientException {
		
//		MessageExt messageExt = consumer.viewMessage("C0A801B000002A9F00000000112414C8");
		MessageExt messageExt = consumer.viewMessage("C0A801DB00002A9F0000000000031");
		System.out.println(messageExt.getTags());
		Object object = SerializeUtil.deserialize(messageExt.getBody());
//		System.out.println(((TicketInfo)object).getMap().get(1).getLocal_m());
		System.out.println(object);
		
		System.exit(0);
	}*/

}