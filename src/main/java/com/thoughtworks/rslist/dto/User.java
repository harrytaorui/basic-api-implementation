package com.thoughtworks.rslist.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

	@NotEmpty
	@Size(max=8)
	@JsonProperty("user_name")
	private String userName;

	@NotNull
	@Range(min=18,max=100)
	@JsonProperty("user_age")
	private Integer age;

	@NotEmpty
	@JsonProperty("user_gender")
	private String gender;

	@Email
	@JsonProperty("user_email")
	private String email;

	@NotEmpty
	@Pattern(regexp = "^1\\d{10}$")
	@JsonProperty("user_phone")
	private String phone;

	@Builder.Default
	private int votes = 10;

	public User(String userName, Integer age, String gender, String email, String phone) {
		this.userName = userName;
		this.age = age;
		this.gender = gender;
		this.email = email;
		this.phone = phone;
	}
}
