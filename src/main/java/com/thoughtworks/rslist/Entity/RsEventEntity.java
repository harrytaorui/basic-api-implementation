package com.thoughtworks.rslist.Entity;

import com.thoughtworks.rslist.dto.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rs_event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventEntity {

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "name")
	private String eventName;
	private String keyword;
	private Integer votes;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
}
