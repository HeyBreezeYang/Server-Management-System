package com.xicloud.apigate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OssMybatisAppTest {

	@Autowired 
	private SqlSessionTemplate ossSqlSessionTemplate;
	
	@Test
	public void contextLoads() {
		ossSqlSessionTemplate.selectList("selectSystemUsers");
	}
}
