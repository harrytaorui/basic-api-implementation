package com.thoughtworks.rslist.Service;

import com.thoughtworks.rslist.Entity.RsEventEntity;
import com.thoughtworks.rslist.Entity.UserEntity;
import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RsEventService {

	@Autowired
	UserService userService;

	public RsEventEntity convertEventToEntity(RsEvent rsEvent) {
		RsEventEntity rsEventEntity = RsEventEntity.builder()
				.eventName(rsEvent.getEventName())
				.keyword(rsEvent.getKeyword())
				.user(UserEntity.builder().id(rsEvent.getUserId()).build())
				.build();
		return rsEventEntity;
	}
}
