package com.gh;

import com.gh.bean.alert.AlertData;
import com.gh.bean.alert.AlertDataInfo;
import com.gh.bean.alert.KeyValue;
import com.gh.bean.appcapability.AppMySql;
import com.gh.bean.appcapability.AppMySqlData;
import com.gh.bean.containercapability.Container;
import com.gh.bean.containercapability.ContainerFileSystem;
import com.gh.bean.logfile.*;
import com.gh.utils.DateUtil;
import com.gh.utils.HttpUtil;
import com.gh.utils.JsonUtil;
import org.codehaus.jackson.JsonNode;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UtilTest {

	@Test
	public void test1(){
		String json = "{" +
				"    \"type\":\"log_file_container\"," +
				"    \"data\": {" +
				"        \"container_uuid\": \"abc123\"," +
				"        \"environment_id\": \"def456\"," +
				"        \"time_stamp\": \"2016-11-15 17:00:00.134\"," +
				"        \"log_info\":{" +
				"            \"log_time\":\"2016-11-20 10:10:10.134\"," +
				"            \"source\":57," +
				"            \"message\":\"bash: vi: command not found\"" +
				"        }" +
				"    }" +
				"}";

		LogFileContainer bean = JsonUtil.getBean(json, LogFileContainer.class);
		System.out.println(bean.getData().getLog_info().getLog_time());
		bean.getData().getLog_info().setLog_time("2016-11-20T09:00:10.134Z");
		String s = JsonUtil.formatJson(bean);
		System.out.println(s);
		LogFileContainer bean1 = JsonUtil.getBean(s, LogFileContainer.class);
		System.out.println(bean1.getData().getLog_info().getLog_time());
		System.out.println(bean1.getData().toString());
	}


	@Test
	public void test2(){
		String json = "{" +
				"    \"type\": \"log_file_nginx\"," +
				"    \"data\": {" +
				"        \"container_uuid\": \"abc123\"," +
				"        \"environment_id\": \"def456\"," +
				"        \"app_file\":\"nginx_file1\"," +
				"        \"time_stamp\": \"2016-11-15 17:00:00.134\"," +
				"        \"log_info\":{" +
				"            \"log_time\":\"2016-11-20 10:10:10.134\"," +
				"            \"remote\":\"172.16.6.59\"," +
				"            \"host\":\"-\"," +
				"            \"user\": \"-\"," +
				"            \"method\":\"GET\"," +
				"            \"path\":\"/\"," +
				"            \"code\":\"200\"," +
				"            \"size\":\"396\"," +
				"            \"referer\": \"-\"," +
				"            \"agent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36\"" +
				"        }" +
				"    }" +
				"}";

		/*LogFileNginx bean = JsonUtil.getBean(json, LogFileNginx.class);
		System.out.println(bean.getData().getLog_info().getLog_time());
		System.out.println(bean.getData().getLog_info().getSize());
		bean.getData().getLog_info().setLog_time("2016-11-20T12:00:10.134Z");
		String s = JsonUtil.formatJson(bean);
		System.out.println(s);
		LogFileNginx bean1 = JsonUtil.getBean(s, LogFileNginx.class);
		System.out.println(bean1.getData().getLog_info().getLog_time());*/
		System.out.println(json.length());
	}

	@Test
	public void test3(){
		String json = "{" +
				"    \"type\": \"log_file_mysql\"," +
				"    \"data\": {" +
				"        \"container_uuid\": \"abc123\"," +
				"        \"environment_id\": \"def456\"," +
				"        \"container_name\": \"def456\"," +
				"        \"namespace\": \"def456\"," +
				"        \"app_file\":\"mysql_file1\"," +
				"        \"timestamp\": \"2016-11-15 17:00:00.134\"," +
				"        \"log_info\":{" +
				"                \"log_time\":\"2016-11-20 10:10:10.134\"," +
				"                \"warn_type\": \"Note\"," +
				"                \"message\":\"InnoDB: Creating shared tablespace for temporary tables\"" +
				"        }" +
				"    }" +
				"}";

		LogFileMySql bean = JsonUtil.getBean(json, LogFileMySql.class);
		System.out.println(bean.getData().getLog_info().getLog_time());
		bean.getData().getLog_info().setLog_time("2016-11-20T00:00:10.134Z");
		String s = JsonUtil.formatJson(bean);
		System.out.println(s);
		LogFileMySql bean1 = JsonUtil.getBean(s, LogFileMySql.class);
		System.out.println(bean1.getData().getLog_info().getLog_time());
	}

	@Test
	public void test5(){
		String json = "{" +
				"    \"type\":\"mysql\"," +
				"    \"data\":{" +
				"        \"container_uuid\":\"abc123\"," +
				"        \"environment_id\":\"def456\"," +
				"        \"stats\":[{" +
				" 			  \"timestamp\": \"2016-11-15 17:00:00.134\"," +
				"            \"used_connection\": \"10\",  "+
				"            \"query_per_second\": \"11\",      " +
				"            \"insert_per_second\": \"11\",      "+
				"            \"update_per_second\": \"11\",      "+
				"            \"delete_per_second\": \"11\",      " +
				"            \"commands_total\": \"10\",             " +
				"            \"handlers_total\": \"10\",             " +
				"            \"connection_errors_total\": \"10\",    " +
				"            \"buffer_pool_pages\": \"1\",            " +
				"            \"thread_connected\" : \"12\",       " +
				"            \"max_connections\" : \"152\",         " +
				"            \"query_response_time_seconds\": \"10\"," +
				"            \"read_query_response_time_seconds\": \"10\"," +
				"            \"write_query_response_time_seconds\": \"10\"," +
				"            \"queries_inside_innodb\": \"10\"," +
				"            \"queries_in_queue\": \"10\"," +
				"            \"read_views_open_inside_innodb\": \"10\"," +
				"            \"table_statistics_rows_read_total\": \"1\"," +
				"            \"table_statistics_rows_changed_total\": \"1\"," +
				"            \"table_statistics_rows_changed_x_indexes_total\": \"1\"," +
				"            \"sql_lock_waits_total\": \"1\"," +
				"            \"external_lock_waits_total\": \"1\"," +
				"            \"sql_lock_waits_seconds_total\": \"1000\"," +
				"            \"external_lock_waits_seconds_total\": \"1000\"," +
				"            \"table_io_waits_total\": \"1\"," +
				"            \"table_io_waits_seconds_total\": \"1000\" "+
				"        }]" +
				"    }" +
				"}";


		/*AppMySql bean = JsonUtil.getBean(json, AppMySql.class);
		System.out.println(bean.getData().getStats().get(0).getTimestamp());
		String s = JsonUtil.formatJson(bean);
		System.out.println(s);

		AppMySqlData bean2 = JsonUtil.getBean(JsonUtil.getJsonNode(json).get("data").toString(), AppMySqlData.class);
		System.out.println(bean2.getContainer_uuid());*/
		System.out.println("length:"+json.length());

	}

	@Test
	public void test4() throws Exception {
		String date = "2016-11-20 05:10:10.134";
	}

	@Test
	public void testParse(){
		String s = "{name={type=text, fields={keyword={type=keyword, ignore_above=256}}}}";
		JsonNode jsonNode = JsonUtil.getJsonNode(s.replaceAll("=",":").toString());
		System.out.print(jsonNode.getFieldNames().toString());
	}

	@Test
	public void test6() throws Exception {
//		HttpUtil.get();
		//{"alert_data":[{"status":"200","alert_type":"M","alert_dim":"A","app_type":"mysql",
		// "msg":"","environment_id":"1234","container_uuid":"c18f34df-538f-4c11-aeaa-d4646f671fb9",
		// "container_name":"","namespace":"","start_time":"2016-12-12T08:15:58.080062284Z","end_time":"2016-12-12T08:16:56.079252226Z",
		// "data":[{"p_key":"connection","p_value":"0.006622516556291391"}]}]}
		AlertData ad = new AlertData();
		AlertDataInfo warn = new AlertDataInfo();
		warn.setStatus("200");
		warn.setAlert_type("M");
		warn.setAlert_dim("A");
		warn.setApp_type("mysql");
		warn.setEnvironment_id("1234");
		warn.setContainer_uuid("c18f34df-538f-4c11-aeaa-d4646f671fb9");
		warn.setStart_time("2016-12-12T08:15:58.080062284Z");
		warn.setEnd_time("2016-12-12T08:16:56.079252226Z");
		ArrayList<KeyValue> list = new ArrayList<KeyValue>();
		KeyValue kv = new KeyValue();
		kv.setP_key("connection");
		kv.setP_value("0.006622516556291391");
		list.add(kv);
		warn.setData(list);

		AlertDataInfo [] aa = new AlertDataInfo[1];
		aa[0] = warn;
//		aa[1] = warn;
		ad.setAlert_data(aa);
		String s = JsonUtil.formatJson(ad);
		System.out.println(s);
		new HttpUtil().Post(s);

	}

	@Test
	public void test7() throws Exception {
//		HttpUtil.Post("hello");
//		System.out.println(HttpUtil.Get("http://223.202.32.56:8077/alert/v1/rule"));

//		System.out.println(DateUtil.df2.format(date));

//		Double d = new Double("6.813934482758621E-5");
//		System.out.println(new BigDecimal(d).toString());
	}

	@Test
	public void test10() {

		String s = "environment_id##_type##";
		String[] split = s.split("#");
		System.out.println(split.length);
		System.out.println("uuid:"+split[1]);
		System.out.println("type:"+split[2]);
		if (split.length > 3) System.out.println(split[3]);


	}

	@Test
	public void test12() throws Exception {
		String s = "2016-12-25T15:31:02.99995484365Z";
		String s2 = "2016-09-06T00:19:57.221479844+09:00";
		System.out.println(DateUtil.getYYYYMMdd(s));
	}

	@Test
	public void test8() throws Exception {
		String s = "2016-02-15T20:46:19.682119938Z";
		String s2 = "2016-12-29T20:19:57.221479844+00:00";

//		DateTimeFormatter isoDateTime = DateTimeFormatter.ISO_DATE_TIME;
//		ZonedDateTime zdt = ZonedDateTime.parse(s, isoDateTime);
		ZonedDateTime zdt = ZonedDateTime.parse(s);

		/*System.out.println(zdt.getYear() + "-" + zdt.getMonthValue() + "-" +
				zdt.getDayOfMonth() + " " + zdt.getHour() + ":" + zdt.getMinute() + ":" + zdt.getSecond() +
				" " + zdt.getZone().getId()+"\n"+zdt.toOffsetDateTime()+"\n"+zdt.toLocalDate()
				+"\n ss:"+zdt.getOffset().getTotalSeconds()
				+"\n time:"+zdt.toLocalDateTime()
				+"\n id:"+zdt.getOffset().getId()
				);*/

		System.out.println(zdt+"\n id:"+zdt.getOffset().getId()+" s:"+zdt.getOffset().getTotalSeconds());

		ZonedDateTime now1 = ZonedDateTime.now();
		System.out.println("local:"+now1);
		System.out.println("local:"+now1.getOffset().getId()+" s:"+now1.getOffset().getTotalSeconds());

		ZonedDateTime zonedDateTime1 = zdt.withZoneSameLocal(now1.getZone());
		ZonedDateTime zonedDateTime = zonedDateTime1.plusSeconds(now1.getOffset().getTotalSeconds() - zdt.getOffset().getTotalSeconds());
		System.out.println("zonedDateTime:"+zonedDateTime);

		System.out.println(zonedDateTime.getYear() + "-" + zonedDateTime.getMonthValue() + "-" + zonedDateTime.getDayOfMonth());

//		Duration duration = Duration.between(zdt,now1);

		/*Duration sixHours = Duration.between(
				ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+08:00")),
				ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+02:00")));*/

//		System.out.println("between:"+duration.getSeconds());

//		ZonedDateTime zonedDateTime = zdt.plusSeconds(duration.getSeconds());
//		System.out.println("plus:"+zonedDateTime+"\n"+zonedDateTime);

//		System.out.println(DateUtil.df_utc_1.parse("2016-12-09T01:10:56.147096822Z").getTime());
//		System.out.println(DateUtil.df.format(new Date(1481620545930l)));
		/*System.out.println(DateUtil.baseUTCToGeneral(s));
		System.out.println(DateUtil.formatToUTC_0(DateUtil.baseUTCToGeneral(s)));

		System.out.println(DateUtil.df2.format(DateUtil.df_utc_base3.parse(s)));

//		RFC822TimeZone
		ISO8601DateFormat iso = new ISO8601DateFormat();*/
//		iso.parse()
//		DateTime dateTime = DateTime.parseRfc3339(s2);
//		System.out.println(DateUtil.df2.format(dateTime.getValue()));
//
//		System.out.println(dateTime.toStringRfc3339());

/*		val f = java.time.format.DateTimeFormatter.ISO_DATE_TIME
		val zdt = java.time.ZonedDateTime.parse("2016-12-09T18:35:51.876140848+00:00", f)

		println(zdt.getYear.toString + "-" + zdt.getMonthValue.toString + "-" +
				zdt.getDayOfMonth.toString + " " + zdt.getHour + ":" + zdt.getMinute + ":" + zdt.getSecond +
				" " + zdt.getZone.getId)*/
	}

	@Test
	public void test9(){
		String json = "{" +
				"    \"type\":\"monitor_container\"," +
				"    \"data\":[{               " +
				"        \"timestamp\":\"2016-11-24T03:37:54.044466478Z\",  " +
				"        \"container_uuid\" :\"b8c45f54-bf17-4a38-979f-94f5ba0cf8ab\"," +
				"        \"environment_id\":\"123456\",     " +
				"        \"stats\":[{                                         " +
				"            \"timestamp\":\"2016-11-14T07:14:33.209935174Z\",  " +
				"            \"container_cpu_usage_seconds_total\":21402856922400," +
				"            \"container_cpu_user_seconds_total\":21402856922400," +
				"            \"container_cpu_system_seconds_total\":21402856922400," +
				"            \"container_memory_usage_bytes\":2140," +
				"            \"container_memory_limit_bytes\":2140," +
				"            \"container_memory_cache\":0," +
				"            \"container_memory_rss\":0," +
				"            \"container_memory_swap\":0," +
				"            \"container_network_receive_bytes_total\":21402856922400," +
				"            \"container_network_receive_packets_total\":21402856922400," +
				"            \"container_network_receive_packets_dropped_total\":21402856922400," +
				"            \"container_network_receive_errors_total\":21402856922400," +
				"            \"container_network_transmit_bytes_total\":21402856922400," +
				"            \"container_network_transmit_packets_total\":21402856922400," +
				"            \"container_network_transmit_packets_dropped_total\":21402856922400," +
				"            \"container_network_transmit_errors_total\":21402856922400," +
				"            \"container_filesystem\":[   " +
				"                {" +
				"                    \"container_filesystem_name\" :\"string\",     " +
				"                    \"container_filesystem_type\":\"vfs\", " +
				"                    \"container_filesystem_capacity\":31562334208, " +
				"                    \"container_filesystem_usage\":4681728" +
				"                }" +
				"            ]," +
				"            \"container_diskio_service_bytes_async\":83230720," +
				"            \"container_diskio_service_bytes_read\":66936832," +
				"            \"container_diskio_service_bytes_sync\":66338816," +
				"            \"container_diskio_service_bytes_total\":149569536," +
				"            \"container_diskio_service_bytes_write\":82632704," +
				"            \"container_tasks_state_nr_sleeping\":0," +
				"            \"container_tasks_state_nr_running\":0," +
				"            \"container_tasks_state_nr_stopped\":0," +
				"            \"container_tasks_state_nr_uninterruptible\":0," +
				"            \"container_tasks_state_nr_io_wait\":0" +
				"        }]" +
				"    }]" +
				"}";


		/*Container bean = JsonUtil.getBean(json, Container.class);
		System.out.println(bean.getData()[0].getStats()[0].getTimestamp());
		String s = JsonUtil.formatJson(bean);
		System.out.println(s);*/
		System.out.println(json.length());  // 10M = 4772条

	}

	@Test
	public void test11(){
		String ss ="{\"type\":\"container\",\"data\":[{\"timestamp\":\"2016-12-24T07:31:22.157331125Z\",\"container_uuid\":\"069659d9-7bd0-4a66-bc4e-1236c1d72d6d\",\"environment_id\":\"21\",\"container_name\":\"network-services-metadata-1\",\"namespace\":\"network-services\",\"stats\":[{\"timestamp\":\"2016-12-24T07:31:22.08216706Z\",\"container_cpu_usage_seconds_total\":58544194529,\"container_cpu_user_seconds_total\":57110000000,\"container_cpu_system_seconds_total\":2560000000,\"container_memory_usage_bytes\":35966976,\"container_memory_limit_bytes\":18446744073709551615,\"container_memory_cache\":6479872,\"container_memory_rss\":29487104,\"container_memory_swap\":0,\"container_network_receive_bytes_total\":38046512,\"container_network_receive_packets_total\":108268,\"container_network_receive_packets_dropped_total\":0,\"container_network_receive_errors_total\":0,\"container_network_transmit_bytes_total\":196386083,\"container_network_transmit_packets_total\":132145,\"container_network_transmit_packets_dropped_total\":0,\"container_network_transmit_errors_total\":0,\"container_filesystem\":[{\"container_filesystem_name\":\"/dev/disk/by-uuid/34b4dbb4-ac4c-4998-be97-bf192941c651\",\"container_filesystem_type\":\"vfs\",\"container_filesystem_capacity\":42127835136,\"container_filesystem_usage\":811008}],\"container_diskio_service_bytes_async\":167813120,\"container_diskio_service_bytes_read\":8167424,\"container_diskio_service_bytes_sync\":0,\"container_diskio_service_bytes_total\":167813120,\"container_diskio_service_bytes_write\":159645696,\"container_tasks_state_nr_sleeping\":0,\"container_tasks_state_nr_running\":0,\"container_tasks_state_nr_stopped\":0,\"container_tasks_state_nr_uninterruptible\":0,\"container_tasks_state_nr_io_wait\":0}]},{\"timestamp\":\"2016-12-24T07:31:22.157334082Z\",\"container_uuid\":\"5f16a407-a856-4887-9629-78a3ea683db1\",\"environment_id\":\"21\",\"container_name\":\"beancounter-agent-agent-1\",\"namespace\":\"beancounter-agent\",\"stats\":[{\"timestamp\":\"2016-12-24T07:31:22.068118509Z\",\"container_cpu_usage_seconds_total\":1520075905,\"container_cpu_user_seconds_total\":1110000000,\"container_cpu_system_seconds_total\":260000000,\"container_memory_usage_bytes\":8806400,\"container_memory_limit_bytes\":18446744073709551615,\"container_memory_cache\":589824,\"container_memory_rss\":8216576,\"container_memory_swap\":0,\"container_network_receive_bytes_total\":620109950,\"container_network_receive_packets_total\":1294372,\"container_network_receive_packets_dropped_total\":0,\"container_network_receive_errors_total\":0,\"container_network_transmit_bytes_total\":198932290,\"container_network_transmit_packets_total\":1286735,\"container_network_transmit_packets_dropped_total\":0,\"container_network_transmit_errors_total\":0,\"container_filesystem\":[{\"container_filesystem_name\":\"/dev/disk/by-uuid/34b4dbb4-ac4c-4998-be97-bf192941c651\",\"container_filesystem_type\":\"vfs\",\"container_filesystem_capacity\":42127835136,\"container_filesystem_usage\":45056}],\"container_diskio_service_bytes_async\":565248,\"container_diskio_service_bytes_read\":565248,\"container_diskio_service_bytes_sync\":0,\"container_diskio_service_bytes_total\":565248,\"container_diskio_service_bytes_write\":0,\"container_tasks_state_nr_sleeping\":0,\"container_tasks_state_nr_running\":0,\"container_tasks_state_nr_stopped\":0,\"container_tasks_state_nr_uninterruptible\":0,\"container_tasks_state_nr_io_wait\":0}]},{\"timestamp\":\"2016-12-24T07:31:22.157335668Z\",\"container_uuid\":\"d5f13113-36cf-418b-86ec-88896620f03f\",\"environment_id\":\"21\",\"container_name\":\"healthcheck-healthcheck-1\",\"namespace\":\"healthcheck\",\"stats\":[{\"timestamp\":\"2016-12-24T07:31:21.675004351Z\",\"container_cpu_usage_seconds_total\":2691444244,\"container_cpu_user_seconds_total\":1140000000,\"container_cpu_system_seconds_total\":720000000,\"container_memory_usage_bytes\":19218432,\"container_memory_limit_bytes\":18446744073709551615,\"container_memory_cache\":9990144,\"container_memory_rss\":9228288,\"container_memory_swap\":0,\"container_network_receive_bytes_total\":9323071,\"container_network_receive_packets_total\":12954,\"container_network_receive_packets_dropped_total\":0,\"container_network_receive_errors_total\":0,\"container_network_transmit_bytes_total\":1092248,\"container_network_transmit_packets_total\":13147,\"container_network_transmit_packets_dropped_total\":0,\"container_network_transmit_errors_total\":0,\"container_filesystem\":[{\"container_filesystem_name\":\"/dev/disk/by-uuid/34b4dbb4-ac4c-4998-be97-bf192941c651\",\"container_filesystem_type\":\"vfs\",\"container_filesystem_capacity\":42127835136,\"container_filesystem_usage\":143360}],\"container_diskio_service_bytes_async\":14946304,\"container_diskio_service_bytes_read\":14438400,\"container_diskio_service_bytes_sync\":0,\"container_diskio_service_bytes_total\":14946304,\"container_diskio_service_bytes_write\":507904,\"container_tasks_state_nr_sleeping\":0,\"container_tasks_state_nr_running\":0,\"container_tasks_state_nr_stopped\":0,\"container_tasks_state_nr_uninterruptible\":0,\"container_tasks_state_nr_io_wait\":0}]},{\"timestamp\":\"2016-12-24T07:31:22.157337194Z\",\"container_uuid\":\"d1974c9c-654e-40a8-8b8c-622fa106a285\",\"environment_id\":\"21\",\"container_name\":\"hna-log-mon-container-log-1\",\"namespace\":\"hna-log-mon\",\"stats\":[{\"timestamp\":\"2016-12-24T07:31:21.797676174Z\",\"container_cpu_usage_seconds_total\":124189182422,\"container_cpu_user_seconds_total\":108130000000,\"container_cpu_system_seconds_total\":14920000000,\"container_memory_usage_bytes\":42127360,\"container_memory_limit_bytes\":18446744073709551615,\"container_memory_cache\":53248,\"container_memory_rss\":42074112,\"container_memory_swap\":0,\"container_network_receive_bytes_total\":620098264,\"container_network_receive_packets_total\":1294220,\"container_network_receive_packets_dropped_total\":0,\"container_network_receive_errors_total\":0,\"container_network_transmit_bytes_total\":198908695,\"container_network_transmit_packets_total\":1286527,\"container_network_transmit_packets_dropped_total\":0,\"container_network_transmit_errors_total\":0,\"container_filesystem\":[{\"container_filesystem_name\":\"/dev/disk/by-uuid/34b4dbb4-ac4c-4998-be97-bf192941c651\",\"container_filesystem_type\":\"vfs\",\"container_filesystem_capacity\":42127835136,\"container_filesystem_usage\":49152}],\"container_diskio_service_bytes_async\":0,\"container_diskio_service_bytes_read\":0,\"container_diskio_service_bytes_sync\":0,\"container_diskio_service_bytes_total\":0,\"container_diskio_service_bytes_write\":0,\"container_tasks_state_nr_sleeping\":0,\"container_tasks_state_nr_running\":0,\"container_tasks_state_nr_stopped\":0,\"container_tasks_state_nr_uninterruptible\":0,\"container_tasks_state_nr_io_wait\":0}]},{\"timestamp\":\"2016-12-24T07:31:22.157338845Z\",\"container_uuid\":\"19ab3fae-32e2-4625-b947-000e8cb43c02\",\"environment_id\":\"21\",\"container_name\":\"testlog-mysql09-1\",\"namespace\":\"testlog\",\"stats\":[{\"timestamp\":\"2016-12-24T07:31:22.044786963Z\",\"container_cpu_usage_seconds_total\":28495720923,\"container_cpu_user_seconds_total\":20920000000,\"container_cpu_system_seconds_total\":12670000000,\"container_memory_usage_bytes\":386002944,\"container_memory_limit_bytes\":18446744073709551615,\"container_memory_cache\":142163968,\"container_memory_rss\":243822592,\"container_memory_swap\":0,\"container_network_receive_bytes_total\":677378,\"container_network_receive_packets_total\":4594,\"container_network_receive_packets_dropped_total\":0,\"container_network_receive_errors_total\":0,\"container_network_transmit_bytes_total\":3611787,\"container_network_transmit_packets_total\":7969,\"container_network_transmit_packets_dropped_total\":0,\"container_network_transmit_errors_total\":0,\"container_filesystem\":[{\"container_filesystem_name\":\"/dev/disk/by-uuid/34b4dbb4-ac4c-4998-be97-bf192941c651\",\"container_filesystem_type\":\"vfs\",\"container_filesystem_capacity\":42127835136,\"container_filesystem_usage\":286720}],\"container_diskio_service_bytes_async\":11792384,\"container_diskio_service_bytes_read\":9199616,\"container_diskio_service_bytes_sync\":289255424,\"container_diskio_service_bytes_total\":301047808,\"container_diskio_service_bytes_write\":291848192,\"container_tasks_state_nr_sleeping\":0,\"container_tasks_state_nr_running\":0,\"container_tasks_state_nr_stopped\":0,\"container_tasks_state_nr_uninterruptible\":0,\"container_tasks_state_nr_io_wait\":0}]},{\"timestamp\":\"2016-12-24T07:31:22.157340507Z\",\"container_uuid\":\"c144423b-672a-4ccc-8445-ba9565b56187\",\"environment_id\":\"21\",\"container_name\":\"hna-log-mon-container-monitor-1\",\"namespace\":\"hna-log-mon\",\"stats\":[{\"timestamp\":\"2016-12-24T07:31:21.410065939Z\",\"container_cpu_usage_seconds_total\":49829275011,\"container_cpu_user_seconds_total\":33120000000,\"container_cpu_system_seconds_total\":13150000000,\"container_memory_usage_bytes\":41725952,\"container_memory_limit_bytes\":18446744073709551615,\"container_memory_cache\":745472,\"container_memory_rss\":40980480,\"container_memory_swap\":0,\"container_network_receive_bytes_total\":1152599,\"container_network_receive_packets_total\":14671,\"container_network_receive_packets_dropped_total\":0,\"container_network_receive_errors_total\":0,\"container_network_transmit_bytes_total\":53530913,\"container_network_transmit_packets_total\":24448,\"container_network_transmit_packets_dropped_total\":0,\"container_network_transmit_errors_total\":0,\"container_filesystem\":[{\"container_filesystem_name\":\"/dev/disk/by-uuid/34b4dbb4-ac4c-4998-be97-bf192941c651\",\"container_filesystem_type\":\"vfs\",\"container_filesystem_capacity\":42127835136,\"container_filesystem_usage\":77824}],\"container_diskio_service_bytes_async\":659456,\"container_diskio_service_bytes_read\":659456,\"container_diskio_service_bytes_sync\":0,\"container_diskio_service_bytes_total\":659456,\"container_diskio_service_bytes_write\":0,\"container_tasks_state_nr_sleeping\":0,\"container_tasks_state_nr_running\":0,\"container_tasks_state_nr_stopped\":0,\"container_tasks_state_nr_uninterruptible\":0,\"container_tasks_state_nr_io_wait\":0}]},{\"timestamp\":\"2016-12-24T07:31:22.157341782Z\",\"container_uuid\":\"8d62ba90-61eb-48ab-804c-e8d8dc8f248b\",\"environment_id\":\"21\",\"container_name\":\"testlog-nginx07-1\",\"namespace\":\"testlog\",\"stats\":[{\"timestamp\":\"2016-12-24T07:31:21.279625426Z\",\"container_cpu_usage_seconds_total\":13793494233,\"container_cpu_user_seconds_total\":7820000000,\"container_cpu_system_seconds_total\":3530000000,\"container_memory_usage_bytes\":51974144,\"container_memory_limit_bytes\":18446744073709551615,\"container_memory_cache\":1122304,\"container_memory_rss\":50851840,\"container_memory_swap\":0,\"container_network_receive_bytes_total\":12100614,\"container_network_receive_packets_total\":62789,\"container_network_receive_packets_dropped_total\":0,\"container_network_receive_errors_total\":0,\"container_network_transmit_bytes_total\":10225428,\"container_network_transmit_packets_total\":90925,\"container_network_transmit_packets_dropped_total\":0,\"container_network_transmit_errors_total\":0,\"container_filesystem\":[{\"container_filesystem_name\":\"/dev/disk/by-uuid/34b4dbb4-ac4c-4998-be97-bf192941c651\",\"container_filesystem_type\":\"vfs\",\"container_filesystem_capacity\":42127835136,\"container_filesystem_usage\":659456}],\"container_diskio_service_bytes_async\":442368,\"container_diskio_service_bytes_read\":442368,\"container_diskio_service_bytes_sync\":622592,\"container_diskio_service_bytes_total\":1064960,\"container_diskio_service_bytes_write\":622592,\"container_tasks_state_nr_sleeping\":0,\"container_tasks_state_nr_running\":0,\"container_tasks_state_nr_stopped\":0,\"container_tasks_state_nr_uninterruptible\":0,\"container_tasks_state_nr_io_wait\":0}]},{\"timestamp\":\"2016-12-24T07:31:22.15734268Z\",\"container_uuid\":\"b4ac2f58-aac9-4624-aac5-7c3844725986\",\"environment_id\":\"21\",\"container_name\":\"kafka-kafka-1\",\"namespace\":\"kafka\",\"stats\":[{\"timestamp\":\"2016-12-24T07:31:21.677895979Z\",\"container_cpu_usage_seconds_total\":12368473795,\"container_cpu_user_seconds_total\":8150000000,\"container_cpu_system_seconds_total\":1790000000,\"container_memory_usage_bytes\":94720000,\"container_memory_limit_bytes\":18446744073709551615,\"container_memory_cache\":503808,\"container_memory_rss\":94216192,\"container_memory_swap\":0,\"container_network_receive_bytes_total\":9612546,\"container_network_receive_packets_total\":78271,\"container_network_receive_packets_dropped_total\":0,\"container_network_receive_errors_total\":0,\"container_network_transmit_bytes_total\":5888174,\"container_network_transmit_packets_total\":54183,\"container_network_transmit_packets_dropped_total\":0,\"container_network_transmit_errors_total\":0,\"container_filesystem\":[{\"container_filesystem_name\":\"/dev/disk/by-uuid/34b4dbb4-ac4c-4998-be97-bf192941c651\",\"container_filesystem_type\":\"vfs\",\"container_filesystem_capacity\":42127835136,\"container_filesystem_usage\":102400}],\"container_diskio_service_bytes_async\":409600,\"container_diskio_service_bytes_read\":405504,\"container_diskio_service_bytes_sync\":0,\"container_diskio_service_bytes_total\":409600,\"container_diskio_service_bytes_write\":4096,\"container_tasks_state_nr_sleeping\":0,\"container_tasks_state_nr_running\":0,\"container_tasks_state_nr_stopped\":0,\"container_tasks_state_nr_uninterruptible\":0,\"container_tasks_state_nr_io_wait\":0}]}]}";
		System.out.println(ss.length()+"--byte:"+ss.getBytes().length);
	}
	
}
