package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
	@NotEmpty
	private String eventName;
	@NotEmpty
	private String keyword;
	@NotEmpty
	private User user;

}
