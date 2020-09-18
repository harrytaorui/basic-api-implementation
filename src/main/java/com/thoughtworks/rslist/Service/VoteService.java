package com.thoughtworks.rslist.Service;

import com.thoughtworks.rslist.Entity.VoteEntity;
import com.thoughtworks.rslist.dto.VoteRecord;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

	public VoteRecord convertEntityToVote(VoteEntity voteEntity) {
		return VoteRecord.builder().userId(voteEntity.getUser().getId())
				.voteNum(voteEntity.getVoteNum())
				.voteTime(voteEntity.getVoteTime()).build();
	}
}
