package com.swp.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.swp.server.entities.ChatEntity;
import com.swp.server.entities.MessageEntity;

@Repository
public interface MessageDAO extends JpaRepository<MessageEntity, Integer> {

	@Query("SELECT msg FROM Message"
			+ " msg WHERE chat_id = ?1")
	List<MessageEntity> findAllByChat(long chat_id);

}
