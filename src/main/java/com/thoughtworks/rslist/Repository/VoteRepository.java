package com.thoughtworks.rslist.Repository;


import com.thoughtworks.rslist.Entity.VoteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends CrudRepository<VoteEntity, Integer> {

	List<VoteEntity> findAll();

	@Query(nativeQuery = true, value = "SELECT * FROM vote_record WHERE user_id = ?1 AND event_id = ?2")
	List<VoteEntity> getRecordByUserIdAndEventId(int userId, int eventId, Pageable pageable);

	List<VoteEntity> findAllByUserIdAndEventId(int userId, int eventId, Pageable pageable);

	List<VoteEntity> findAllByVoteTimeBetween(LocalDateTime start, LocalDateTime end);


}
