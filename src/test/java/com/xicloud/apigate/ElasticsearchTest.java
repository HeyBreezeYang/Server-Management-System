package com.xicloud.apigate;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.system.started.elasticsearch.ElasticsearchUtils;
import com.vlandc.oss.common.JsonHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchTest {

	@Test
	public void contextLoads() {
		List<Map<String, Object>> list = ElasticsearchUtils.searchListData("filebeat-*", "*", 0, "", "", false, "", "oss-server.i1cloud");
	    for (Map<String, Object> item : list) {
	    	
	        System.out.println(JsonHelper.toJson(item));
	    }
	}

}
