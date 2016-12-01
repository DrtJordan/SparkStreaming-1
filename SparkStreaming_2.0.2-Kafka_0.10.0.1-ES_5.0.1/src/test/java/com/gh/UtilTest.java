package com.gh;

import com.gh.bean.AlertDataInfo;
import com.gh.bean.appcapability.AppMySql;
import com.gh.bean.appcapability.AppMySqlData;
import com.gh.bean.logfile.*;
import com.gh.utils.DateUtil;
import com.gh.utils.JsonUtil;
import org.junit.Test;

import java.util.*;

public class UtilTest {

	@Test
	public void test1(){
		String json = "{" +
				"    \"type\": \"log_file_container\"," +
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

		LogFileNginx bean = JsonUtil.getBean(json, LogFileNginx.class);
		System.out.println(bean.getData().getLog_info().getLog_time());
		System.out.println(bean.getData().getLog_info().getSize());
		bean.getData().getLog_info().setLog_time("2016-11-20T12:00:10.134Z");
		String s = JsonUtil.formatJson(bean);
		System.out.println(s);
		LogFileNginx bean1 = JsonUtil.getBean(s, LogFileNginx.class);
		System.out.println(bean1.getData().getLog_info().getLog_time());
	}

	@Test
	public void test3(){
		String json = "{" +
				"    \"type\": \"log_file_mysql\"," +
				"    \"data\": {" +
				"        \"container_uuid\": \"abc123\"," +
				"        \"environment_id\": \"def456\"," +
				"        \"app_file\":\"mysql_file1\"," +
				"        \"time_stamp\": \"2016-11-15 17:00:00.134\"," +
				"        \"log_info\":{" +
				"                \"log_time\":\"2016-11-20 10:10:10.134\"," +
				"                \"warn_type\": \"Note\"," +
				"                \"message\":\"InnoDB: Creating shared tablespace for temporary tables\"" +
				"        }" +
				"    }" +
				"}";

		LogFileApp bean = JsonUtil.getBean(json, LogFileApp.class);
		System.out.println(bean.getData().getLog_info().getLog_time());
		bean.getData().getLog_info().setLog_time("2016-11-20T00:00:10.134Z");
		String s = JsonUtil.formatJson(bean);
		System.out.println(s);
		LogFileApp bean1 = JsonUtil.getBean(s, LogFileApp.class);
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


		AppMySql bean = JsonUtil.getBean(json, AppMySql.class);
		System.out.println(bean.getData().getStats().get(0).getTimestamp());
		String s = JsonUtil.formatJson(bean);
		System.out.println(s);

		AppMySqlData bean2 = JsonUtil.getBean(JsonUtil.getJsonNode(json).get("data").toString(), AppMySqlData.class);
		System.out.println(bean2.getContainer_uuid());

	}

	@Test
	public void test4() throws Exception {
		String date = "2016-11-20 05:10:10.134";
		System.out.println(DateUtil.formatToUTC_0(date));
		System.out.println(DateUtil.formatToUTC_8(DateUtil.formatToUTC_0(date)));
	}

	@Test
	public void test6() throws Exception {
//		HttpUtil.get();

		AlertDataInfo warn = new AlertDataInfo();
		warn.setStatus("success");
		warn.setAlert_type("M");
		warn.setAlert_dim("C");
		warn.setApp_type("mysql");
		warn.setEnvironment_id("rancher");
		warn.setContainer_uuid("dafasdf-123141");
		warn.setStart_time("2016-11-20 10:10:10.134");
		warn.setEnd_time("2016-11-20 10:10:10.134");
		Map<String,Object> datas = new HashMap<String, Object>();
		datas.put("p_key","cpu");
		datas.put("p_value",81);
		warn.setData(datas);

		String s = JsonUtil.formatJson(warn);
		System.out.println(s);

	}

	@Test
	public void test7() throws Exception {

	}
	
}
