package org.duffy.service;

import java.util.List;

import org.duffy.domain.BoardVO;
import org.duffy.domain.Criteria;

public interface BoardService {

	public void register(BoardVO board);
	
	public List<BoardVO> getListAll(Criteria cri);
	
	public BoardVO getList(Long bno);
	
	public boolean remove(Long bno);
	
	public boolean modify(BoardVO board);

	public int getTotal(Criteria cri);

}
