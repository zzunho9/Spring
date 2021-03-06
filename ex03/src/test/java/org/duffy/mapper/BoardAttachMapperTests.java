package org.duffy.mapper;

import org.duffy.domain.BoardAttachVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardAttachMapperTests {

	@Setter(onMethod_ = {@Autowired})
	private BoardAttachMapper mapper;
	
	@Test
	public void testInsert() {
		BoardAttachVO vo = new BoardAttachVO();
		vo.setBno(5545L);
		vo.setFileName("test2");
		vo.setFilePath("/test");
//		vo.setFileType(true);
		vo.setUuid("test2");
		
		log.info(vo);
		mapper.insert(vo);
	}
}
