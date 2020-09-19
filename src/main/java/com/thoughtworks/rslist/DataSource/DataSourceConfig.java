package com.thoughtworks.rslist.DataSource;

import com.thoughtworks.rslist.Repository.RsEventRepository;
import com.thoughtworks.rslist.Service.RsEventService;
import com.thoughtworks.rslist.Service.UserService;
import com.thoughtworks.rslist.Service.VoteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

	@Bean
	public RsEventService RsEventService() {
		return new RsEventService();
	}

	@Bean
	public UserService UserService() {
		return new UserService();
	}

	@Bean
	public VoteService VoteService() {
		return new VoteService();
	}


}
