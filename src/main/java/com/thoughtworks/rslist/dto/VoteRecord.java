package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteRecord {

	private Integer voteNum;
	private Integer userId;
	private String voteTime;
}
