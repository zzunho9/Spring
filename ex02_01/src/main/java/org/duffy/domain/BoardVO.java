package org.duffy.domain;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
public class BoardVO {
	private Long bno;
	private String title, writer, context;
	private Date regDate, updateDate;
}
