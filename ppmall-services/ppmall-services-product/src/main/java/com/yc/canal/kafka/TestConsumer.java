package com.yc.canal.kafka;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Header;
import com.alibaba.otter.canal.protocol.Message;

@Component
public class TestConsumer extends MessageHandler {
	@KafkaListener(topics = "example")
	public void listen(Message message) throws Exception {
		processMessage(message);

		// System.out.printf(" value = %s \n", message);
	}

	@Override
	public void insert(Header header, List<Column> afterColumns) {
		System.out.println("--------------INSERT---------------");
		System.out.println(afterColumns);
		System.out.println("--------------INSERT---------------");
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Header header, List<Column> afterColumns) {
		// TODO Auto-generated method stub
		System.out.println(afterColumns);
		for (Column column : afterColumns) {
//			column.getName()
		}

	}

	@Override
	public void delete(Header header, List<Column> beforeColumns) {
		// TODO Auto-generated method stub
		System.out.println("--------------DELETE---------------");
		System.out.println(beforeColumns);
		System.out.println("--------------DELETE---------------");
	}
}
