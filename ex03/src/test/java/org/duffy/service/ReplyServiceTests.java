package org.duffy.service;

import org.duffy.domain.ReplyVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyServiceTests {

	@Setter(onMethod_ = @Autowired)
	private ReplyService replyService;
	
	@Test
	public void testRegister() {
		ReplyVO reply = new ReplyVO();
		
		reply.setBno(5582L);
		reply.setReply("TEST2");
		reply.setReplyer("TESTER2");
		
		replyService.register(reply);
	}
}
