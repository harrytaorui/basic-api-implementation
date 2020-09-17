package com.thoughtworks.rslist.Repository;


import com.thoughtworks.rslist.Entity.RsEventEntity;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventEntity, Integer> {

	List<RsEventEntity> findAll();
	
}
