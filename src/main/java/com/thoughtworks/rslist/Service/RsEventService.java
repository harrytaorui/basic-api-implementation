package com.thoughtworks.rslist.Service;

import com.thoughtworks.rslist.Entity.RsEventEntity;
import com.thoughtworks.rslist.Entity.UserEntity;
import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.beans.factory.annotation.Autowired;

public class RsEventService {


	public RsEventEntity convertEventToEntity(RsEvent rsEvent) {
		RsEventEntity rsEventEntity = RsEventEntity.builder()
				.eventName(rsEvent.getEventName())
				.keyword(rsEvent.getKeyword())
				.user(UserEntity.builder().id(rsEvent.getUserId()).build())
				.votes(rsEvent.getVotes())
				.build();
		return rsEventEntity;
	}

	public RsEvent convertEntityToEvent(RsEventEntity rsEventEntity) {
		RsEvent rsEvent = RsEvent.builder()
				.eventName(rsEventEntity.getEventName())
				.keyword(rsEventEntity.getKeyword())
				.userId(rsEventEntity.getUser().getId())
				.votes(rsEventEntity.getVotes())
				.id(rsEventEntity.getId())
				.build();
		return rsEvent;
	}
}
