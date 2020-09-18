package com.thoughtworks.rslist.Entity;


import com.thoughtworks.rslist.dto.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
	private String voteTime;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
}
