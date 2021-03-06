package org.duffy.controller;

import org.duffy.domain.BoardVO;
import org.duffy.domain.Criteria;
import org.duffy.domain.PageDTO;
import org.duffy.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
public class BoardController {
	
	@Setter(onMethod_ = @Autowired)
	private BoardService boardService;

	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		
		PageDTO page = new PageDTO(cri, boardService.getTotal(cri));
		
		log.info("/list cri: "+cri);
		if(page.isPrev()) {
			log.info("true");
		}

		model.addAttribute("post", boardService.getList(cri));
		model.addAttribute("page", page);
	}
	
	@GetMapping("/post/{bno}")
	public String post(@PathVariable("bno")Long bno, Model model) {
		
		log.info("/post?bno"+bno);
		
		model.addAttribute("post", boardService.read(bno));
		
		return "/board/post";
	}
	
	@PostMapping("/register")
	public String register(BoardVO board, RedirectAttributes attributes) {
		
		log.info("/register POST");
		
		boardService.register(board);
		
		attributes.addFlashAttribute("result", board.getBno());
		
		return "redirect:/board/list";
	}
	
	@GetMapping("/register")
	public void register() {
		
		log.info("register GET");
	}
	
	@PostMapping("/delete")
	public String remove(Long bno, RedirectAttributes attributes) {
		
		log.info("/delete?bno"+bno);
		
		attributes.addFlashAttribute("result", boardService.delete(bno));
		
		return "redirect:/board/list";
	}
	
	@GetMapping("/modify")
	public void modify(Long bno, Model model) {
		
		log.info("modify GET");
		
		model.addAttribute("post", boardService.read(bno));
	}
	
	
	@PostMapping("/modify")
	public String modify(BoardVO board, RedirectAttributes attributes) {
		Long bno = board.getBno();
		
		log.info("/modify?bno"+bno);
		
		attributes.addFlashAttribute("result", boardService.modify(board));
		
		return "redirect:/board/post?bno="+board.getBno();
	}
	
}
