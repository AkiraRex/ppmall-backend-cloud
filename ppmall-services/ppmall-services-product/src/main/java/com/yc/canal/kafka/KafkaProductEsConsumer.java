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

@Component
public class KafkaProductEsConsumer extends MessageHandler {
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@KafkaListener(topics = "example")
	public void listen(Message message) throws Exception {
		processMessage(message);
	}

	@Override
	public void insert(Header header, List<Column> afterColumns) {
		ProductMap productMap = new ProductMap();
		for (Column column : afterColumns) {
			productMap.put(column.getName(), column.getValue());
		}
		
		IndexQuery query = new IndexQueryBuilder().withObject(productMap).build();
		elasticsearchTemplate.index(query);
	}

	@Override
	public void update(Header header, List<Column> afterColumns) {
		
		UpdateQuery query = new UpdateQueryBuilder().build();
		elasticsearchTemplate.update(query);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Header header, List<Column> beforeColumns) {
		// TODO Auto-generated method stub
		System.out.println("--------------DELETE---------------");
		System.out.println(beforeColumns);
		System.out.println("--------------DELETE---------------");
	}
}
