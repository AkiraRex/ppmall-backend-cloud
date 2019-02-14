package com.yc.canal.kafka;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Header;
import com.alibaba.otter.canal.protocol.Message;
import com.yc.elasticsearch.entry.ProductMap;
import org.springframework.kafka.support.Acknowledgment;

@Component
public class KafkaProductEsConsumer extends MessageHandler {
	
   
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	
	@KafkaListener(topics = "ppmall_product")
	public void listen(Message message, Acknowledgment ack) throws Exception {
		System.out.println("--------------listen---------------");
		processMessage(message, ack);
	}
	

	@Override
	public void insert(Header header, List<Column> afterColumns, Acknowledgment acknowledgment) {
		ProductMap productMap = new ProductMap();
		for (Column column : afterColumns) {
			productMap.put(column.getName(), column.getValue());
		}
		
		IndexQuery query = new IndexQueryBuilder().withObject(productMap).build();
		elasticsearchTemplate.index(query);
		acknowledgment.acknowledge();
	}

	@Override
	public void update(Header header, List<Column> afterColumns, Acknowledgment acknowledgment) {
		
		System.out.println("--------------UPDATE---------------");
		UpdateQuery query = new UpdateQueryBuilder().withId("s").build();
		System.out.println(header);
		System.out.println("--------------UPDATE---------------");
//		elasticsearchTemplate.update(query);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Header header, List<Column> beforeColumns, Acknowledgment acknowledgment) {
		// TODO Auto-generated method stub
		System.out.println("--------------DELETE---------------");
		elasticsearchTemplate.delete(ProductMap.class, "");
		System.out.println("--------------DELETE---------------");
	}
}
