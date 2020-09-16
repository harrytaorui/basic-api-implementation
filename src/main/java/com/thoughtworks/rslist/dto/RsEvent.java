package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
	@NotEmpty
	private String eventName;
	@NotEmpty
	private String keyword;

	@NotNull
	@Valid
	private User user;

	@JsonIgnore
	public User getUser() {
		return user;
	}

	@JsonProperty
	public void setUser(User user) {
		this.user = user;
	}

}
