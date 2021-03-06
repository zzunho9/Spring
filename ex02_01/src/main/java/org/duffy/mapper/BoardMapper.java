package org.duffy.mapper;

import java.util.List;
import java.util.Map;

import org.duffy.domain.BoardVO;
import org.duffy.domain.Criteria;

public interface BoardMapper {
	public List<BoardVO> getList();
	public List<BoardVO> getListWithPaging(Criteria cri);
	public void insert(BoardVO baord);
	public void insertSelectKey(BoardVO board);
	public BoardVO read(Long bno);
	public int delete(Long bno);
	public int update(BoardVO board);
	public int getTotalCount(Criteria cri); 
	public List<BoardVO> searchTest(Map<String, Map<String, String>> map);
}
