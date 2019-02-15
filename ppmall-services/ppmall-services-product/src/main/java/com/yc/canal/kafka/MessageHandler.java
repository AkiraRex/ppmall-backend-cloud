package com.yc.canal.kafka;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;

public abstract class MessageHandler {
	protected boolean debug = true;// 开启debug，会把每条消息的详情打印

	protected static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
	protected static final String SEP = SystemUtils.LINE_SEPARATOR;
	protected static String rowFormat;
	protected static String transactionFormat;
	protected static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	protected int exceptionStrategy = 1;
	protected int retryTimes = 3;
	protected int waitingTime = 100;// 当binlog没有数据时，主线程等待的时间，单位ms,大于0
	
	protected static String TABLE_NAME = "ppmall_product";
	protected static String SCHEMA_NAME = "ppmall-cloud";

	static {
		StringBuilder sb = new StringBuilder();
	

		sb = new StringBuilder();
		sb.append(SEP)
			.append("+++++++++++++Row+++++++++++++>>>")
			.append("binlog[{}:{}] , name[{},{}] , eventType : {} , executeTime : {} , delay : {}ms")
			.append(SEP);
		rowFormat = sb.toString();

		sb = new StringBuilder();
		sb.append(SEP)
			.append("===========Transaction {} : {}=======>>>")
			.append("binlog[{}:{}] , executeTime : {} , delay : {}ms")
			.append(SEP);
		transactionFormat = sb.toString();
	}

	public abstract void insert(CanalEntry.Header header, List<CanalEntry.Column> afterColumns, Acknowledgment acknowledgment);

	public abstract void update(CanalEntry.Header header, List<CanalEntry.Column> afterColumns, Acknowledgment acknowledgment);

	public abstract void delete(CanalEntry.Header header, List<CanalEntry.Column> beforeColumns, Acknowledgment acknowledgment);

	protected void processMessage(Message message, Acknowledgment acknowledgment) {
		//long batchId = message.getId();
		// 遍历每条消息
		for (CanalEntry.Entry entry : message.getEntries()) {
			session(entry, acknowledgment);// no exception
		}
		// ack all the time。
		// connector.ack(batchId);
	}

	private void session(CanalEntry.Entry entry, Acknowledgment acknowledgment) {
		CanalEntry.EntryType entryType = entry.getEntryType();
		int times = 0;
		boolean success = false;
		while (!success) {
			try {
				switch (entryType) {
				case TRANSACTIONBEGIN:
					transactionBegin(entry);
					break;
				case TRANSACTIONEND:
					transactionEnd(entry);
					break;
				case ROWDATA:
					rowData(entry, acknowledgment);
					break;
				default:
					break;
				}
				success = true;
			} catch (Exception e) {
				times++;
				logger.error("parse event has an error ,times: + " + times + ", data:" + entry.toString(), e);
			}

		}
	}

	private void rowData(CanalEntry.Entry entry, Acknowledgment acknowledgment) throws Exception {
		CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
		CanalEntry.EventType eventType = rowChange.getEventType();
		CanalEntry.Header header = entry.getHeader();
		long executeTime = header.getExecuteTime();
		long delayTime = new Date().getTime() - executeTime;
		String sql = rowChange.getSql();

		try {
			if (!isDML(eventType) || rowChange.getIsDdl()) {
				processDDL(header, eventType, sql);
				return;
			}
			// 处理DML数据
			if (header.getSchemaName().equals(SCHEMA_NAME) && header.getTableName().equals(TABLE_NAME)) {
				processDML(header, eventType, rowChange, sql, acknowledgment);
			}
			
		} catch (Exception e) {
			logger.error("process event error ,", e);
			logger.error(rowFormat,
					new Object[] { header.getLogfileName(), String.valueOf(header.getLogfileOffset()),
							header.getSchemaName(), header.getTableName(), eventType, String.valueOf(executeTime),
							String.valueOf(delayTime) });
			throw e;// 重新抛出
		}
	}

	/**
	 * 处理 dml 数据
	 *
	 * @param header
	 * @param eventType
	 * @param rowChange
	 * @param sql
	 */
	protected void processDML(CanalEntry.Header header, CanalEntry.EventType eventType, CanalEntry.RowChange rowChange,
			String sql, Acknowledgment acknowledgment) {
		for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
			switch (eventType) {
			case DELETE:
				delete(header, rowData.getBeforeColumnsList(), acknowledgment);
				break;
			case INSERT:
				insert(header, rowData.getAfterColumnsList(), acknowledgment);
				break;
			case UPDATE:
				update(header, rowData.getAfterColumnsList(), acknowledgment);
				break;
			default:
				whenOthers(header, sql);
			}
		}
	}

	protected void transactionBegin(CanalEntry.Entry entry) {
		if (!debug) {
			return;
		}
		try {
			CanalEntry.TransactionBegin begin = CanalEntry.TransactionBegin.parseFrom(entry.getStoreValue());
			// 打印事务头信息，执行的线程id，事务耗时
			CanalEntry.Header header = entry.getHeader();
			long executeTime = header.getExecuteTime();
			long delayTime = new Date().getTime() - executeTime;
			logger.info(transactionFormat,
					new Object[] { "begin", begin.getTransactionId(), header.getLogfileName(),
							String.valueOf(header.getLogfileOffset()), String.valueOf(header.getExecuteTime()),
							String.valueOf(delayTime) });
		} catch (Exception e) {
			logger.error("parse event has an error , data:" + entry.toString(), e);
		}
	}

	protected void transactionEnd(CanalEntry.Entry entry) {
		if (!debug) {
			return;
		}
		try {
			CanalEntry.TransactionEnd end = CanalEntry.TransactionEnd.parseFrom(entry.getStoreValue());
			// 打印事务提交信息，事务id
			CanalEntry.Header header = entry.getHeader();
			long executeTime = header.getExecuteTime();
			long delayTime = new Date().getTime() - executeTime;

			logger.info(transactionFormat,
					new Object[] { "end", end.getTransactionId(), header.getLogfileName(),
							String.valueOf(header.getLogfileOffset()), String.valueOf(header.getExecuteTime()),
							String.valueOf(delayTime) });
		} catch (Exception e) {
			logger.error("parse event has an error , data:" + entry.toString(), e);
		}
	}

	/**
	 * 判断事件类型为DML 数据
	 *
	 * @param eventType
	 * @return
	 */
	protected boolean isDML(CanalEntry.EventType eventType) {
		switch (eventType) {
		case INSERT:
		case UPDATE:
		case DELETE:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 处理 DDL数据
	 *
	 * @param header
	 * @param eventType
	 * @param sql
	 */

	protected void processDDL(CanalEntry.Header header, CanalEntry.EventType eventType, String sql) {
		if (!debug) {
			return;
		}
		String table = header.getSchemaName() + "." + header.getTableName();
		// 对于DDL，直接执行，因为没有行变更数据
		switch (eventType) {
		case CREATE:
			logger.warn("parse create table event, table: {}, sql: {}", table, sql);
			return;
		case ALTER:
			logger.warn("parse alter table event, table: {}, sql: {}", table, sql);
			return;
		case TRUNCATE:
			logger.warn("parse truncate table event, table: {}, sql: {}", table, sql);
			return;
		case ERASE:
		case QUERY:
			logger.warn("parse event : {}, sql: {} . ignored!", eventType.name(), sql);
			return;
		case RENAME:
			logger.warn("parse rename table event, table: {}, sql: {}", table, sql);
			return;
		case CINDEX:
			logger.warn("parse create index event, table: {}, sql: {}", table, sql);
			return;
		case DINDEX:
			logger.warn("parse delete index event, table: {}, sql: {}", table, sql);
			return;
		default:
			logger.warn("parse unknown event: {}, table: {}, sql: {}", eventType.name(), table, sql);
			break;
		}
	}
	
	/**
     * 强烈建议捕获异常，非上述已列出的其他操作，非核心
     * 除了“insert”、“update”、“delete”操作之外的，其他类型的操作.
     * 默认实现为“无操作”
     *
     * @param header 可以从header中获得schema、table的名称
     * @param sql
     */
    public void whenOthers(CanalEntry.Header header, String sql) {
        String schema = header.getSchemaName();
        String table = header.getTableName();
        logger.error("ignore event,schema: {},table: {},SQL: {}", schema, table, sql);
    }

}
