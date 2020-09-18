package com.thoughtworks.rslist.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "vote_record")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteEntity {

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "vote_number")
	private Integer voteNum;
	@Column(name = "time")
	private LocalDateTime voteTime;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
	@ManyToOne
	@JoinColumn(name = "event_id")
	private RsEventEntity event;
}
