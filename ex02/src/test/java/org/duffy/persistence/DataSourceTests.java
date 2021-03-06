package org.duffy.persistence;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.duffy.persistence.DataSourceTests;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j 
public class DataSourceTests {
	@Autowired
	private DataSource dataSource;
	@Autowired
	private SqlSessionFactory sqlSelssionFactory;
	
	@Test
	public void testConnection() {
		try(Connection conn = dataSource.getConnection()){
			
			log.info("==============================");
			log.info(conn);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testConnection02() {
		
		try(SqlSession session =  sqlSelssionFactory.openSession();
			Connection conn = session.getConnection()){
			
			log.info(session);
			log.info(conn);
			
		}catch(Exception e) {
			e.printStackTrace(); 
		}
	}
}

