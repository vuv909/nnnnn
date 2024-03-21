package com.swp.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.swp.server.entities.ChatEntity;

@Repository
public interface ChatDAO extends JpaRepository<ChatEntity, Integer> {

	@Query("SELECT chat FROM Chat chat WHERE chat.chat_id = ?1")
	Optional<ChatEntity> findByChatId(long id);

	@Query("SELECT chat FROM Chat chat WHERE chat.name = ?1")
	List<ChatEntity> findByPartecipant(String user);

	@Query("SELECT chat FROM Chat chat WHERE chat.name = ?1")
	ChatEntity findByName(String chat);

}
