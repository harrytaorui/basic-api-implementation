package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
	private User user;

}
