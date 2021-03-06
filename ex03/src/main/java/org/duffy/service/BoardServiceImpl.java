package org.duffy.service;

import java.util.List;

import org.duffy.domain.BoardVO;
import org.duffy.domain.Criteria;
import org.duffy.mapper.BoardMapper;
import org.duffy.mapper.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@AllArgsConstructor
@NoArgsConstructor
public class BoardServiceImpl implements BoardService{

	@Setter(onMethod_ = @Autowired)
	private BoardMapper boardMapper;
	
	@Setter(onMethod_ = @Autowired)
	private ReplyMapper replyMapper;
	
	@Override
	public List<BoardVO> getListAll(Criteria cri) {
		
		log.info("get List........"+cri);
		return boardMapper.getListWithPaging(cri);
	}
	
	@Override
	public void register(BoardVO board) {
		
		log.info("register........."+board);
		
		boardMapper.insertSelectKey(board);
	}


	@Override
	public BoardVO getList(Long bno) {
		
		log.info("getList........"+bno);
		return boardMapper.read(bno);
	}

	@Transactional
	@Override
	public boolean remove(Long bno) {
		log.info("remove......."+bno);
		
		replyMapper.deleteByBno(bno);
		
		return boardMapper.delete(bno)==1;
	}

	@Override
	public boolean modify(BoardVO board) {
		
		log.info("modify......."+board);
		return boardMapper.update(board)==1;
	}

	@Override
	public int getTotal(Criteria cri) {
		
		log.info("get total count.......");
		return boardMapper.getTotalCount(cri);
	}


}
