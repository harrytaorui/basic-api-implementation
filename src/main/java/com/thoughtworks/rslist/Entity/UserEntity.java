package com.thoughtworks.rslist.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "name")
	private String userName;
	private Integer age;
	private String gender;
	private String email;
	private String phone;
	private Integer votes;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<RsEventEntity> rsEvents;
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<VoteEntity> voteLists;
}
