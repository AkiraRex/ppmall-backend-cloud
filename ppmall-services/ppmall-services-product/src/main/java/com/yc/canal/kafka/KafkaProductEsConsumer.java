package com.yc.canal.kafka;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Header;
import com.alibaba.otter.canal.protocol.Message;
import com.yc.elasticsearch.entity.ProductEntity;

@Component
public class KafkaProductEsConsumer extends MessageHandler {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	protected static final Logger logger = LoggerFactory.getLogger(KafkaProductEsConsumer.class);
	

	@KafkaListener(topics = "ppmall-cloud.ppmall_product")
	public void listen(Message message, Acknowledgment ack) throws Exception {
		processMessage(message, ack);
	}

	@Override
	public void insert(Header header, List<Column> afterColumns, Acknowledgment acknowledgment) {

		try {
			ProductEntity productMap = buildProductMap(afterColumns);
			IndexQuery query = new IndexQueryBuilder().withObject(productMap).build();
			elasticsearchTemplate.index(query);
			acknowledgment.acknowledge();
			
			logger.info("elasticsearch index: " + productMap + "suceess");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private ProductEntity buildProductMap(List<Column> afterColumns) {
		ProductEntity productMap = new ProductEntity();
		for (Column column : afterColumns) {
			productMap.put(column.getName(), column.getValue());
		}
		productMap.setId(productMap.get("id").toString());
		return productMap;
	}

	@Override
	public void update(Header header, List<Column> afterColumns, Acknowledgment acknowledgment) {

		try {
			ProductEntity productMap = buildProductMap(afterColumns);
			IndexQuery query = new IndexQueryBuilder().withObject(productMap).build();
			elasticsearchTemplate.index(query);
			acknowledgment.acknowledge();

			logger.info("elasticsearch index: " + productMap + "suceess");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public void delete(Header header, List<Column> beforeColumns, Acknowledgment acknowledgment) {
		// TODO Auto-generated method stub
		ProductEntity productMap = buildProductMap(beforeColumns);
		try {
			elasticsearchTemplate.delete(ProductEntity.class, productMap.getId());
			acknowledgment.acknowledge();
			
			logger.info("elasticsearch delete: " + productMap + "suceess");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
