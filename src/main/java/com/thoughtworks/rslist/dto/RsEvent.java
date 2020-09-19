package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsEvent {

	public interface withOutUser{}
	public interface withUser extends withOutUser{}

	@NotEmpty
	@JsonView(withOutUser.class)
	private String eventName;

	@NotEmpty
	@JsonView(withOutUser.class)
	private String keyword;

	@NotNull
	private User user;

	private Integer votes;

	@NotNull
	private Integer userId;

	private Integer id;

	@JsonView(withUser.class)
	public Integer getUserId() {
		return userId;
	}

	@JsonProperty
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@JsonProperty
	public User getUser() {
		return user;
	}

	@JsonIgnore
	public void setUser(User user) {
		this.user = user;
	}

	public String toJsonWithUser() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writerWithView(withUser.class)
				.writeValueAsString(this);
	}
}
