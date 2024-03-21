package com.swp.server.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.swp.server.dto.BlockAccDTO;
import com.swp.server.dto.ChatMessage;
import com.swp.server.dto.ChatMessageDTO;
import com.swp.server.dto.ChatReturn;
import com.swp.server.dto.MessageReturn;
import com.swp.server.entities.*;
import com.swp.server.repository.ChatDAO;
import com.swp.server.repository.MessageDAO;

@Controller
public class WebSocketController {
	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private ChatDAO chatDAO;

	@Autowired
	private MessageDAO messageDAO;

	@MessageMapping("/chat/{chatId}")
	@SendTo("/topic/{chatId}")
	public void chat(@DestinationVariable String chatId, ChatMessageDTO message) {

		MessageEntity messageReturn = new MessageEntity();

		messageReturn.setChat_id(Long.parseLong(chatId));
		messageReturn.setSender(message.getUser());
		messageReturn.setT_stamp(generateTimeStamp());
		messageReturn.setContent(message.getMessage());

		MessageEntity messageEntity = messageDAO.save(messageReturn);

		System.out.print("Chat id::" + chatId);

		List<MessageEntity> messageEntities = messageDAO.findAllByChat(Long.parseLong(chatId));

		this.template.convertAndSend("/topic/" + chatId, messageEntities);

	}

	@MessageMapping("/blockAccount")
	@SendTo("/toBlockAccount")
	public void chat(BlockAccDTO blockAccDTO) {
		System.out.print(blockAccDTO.toString());

		this.template.convertAndSend("/toBlockAccount", blockAccDTO);

	}

	@GetMapping("/getMessages/getAllChat")
	public ResponseEntity<?> getAllChat() {

		List<ChatEntity> chatEntities = chatDAO.findAll();
		List<ChatReturn> chatReturns = new ArrayList<ChatReturn>();

		if (chatEntities.size() == 0) {
			List<ChatReturn> list = new ArrayList<ChatReturn>();
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}
		for (ChatEntity entity : chatEntities) {
			ChatReturn chatReturn = new ChatReturn();
			chatReturn.setId(entity.getChat_id());
			chatReturn.setName(entity.getName());
			chatReturns.add(chatReturn);
		}

		return ResponseEntity.ok().body(chatReturns);

	}

	@GetMapping("/getMessages/{chatId}")
	public ResponseEntity<?> getMessages(@PathVariable String chatId) {
		Optional<ChatEntity> ce = chatDAO.findByChatId(Long.parseLong(chatId));
		List<MessageReturn> messageReturns = new ArrayList<MessageReturn>();

		if (ce.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found room chat !!!");
		}

		List<MessageEntity> listEntities = messageDAO.findAllByChat(ce.get().getChat_id());

		if (listEntities.size() == 0) {
			List<MessageReturn> list = new ArrayList<MessageReturn>();
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}

		for (MessageEntity entity : listEntities) {
			MessageReturn messageReturn = new MessageReturn();
			messageReturn.setMessageId(entity.getMs_id());
			messageReturn.setChatId(entity.getChat_id());
			messageReturn.setContent(entity.getContent());
			messageReturn.setSender(entity.getSender());
			messageReturn.setTimestamp(entity.getT_stamp());
			messageReturns.add(messageReturn);
		}

		return ResponseEntity.ok().body(messageReturns);

	}

	@GetMapping("/createOrGetChat/{email}")
	public ResponseEntity<Long> createAndOrGetChat(@PathVariable String email) {
		ChatEntity ce = chatDAO.findByName(email);

		if (ce != null) {
			return ResponseEntity.ok(ce.getChat_id());
		} else {
			ChatEntity newChat = new ChatEntity(email);
			chatDAO.save(newChat);
			return ResponseEntity.status(HttpStatus.CREATED).body(newChat.getChat_id());
		}
	}

	private String generateTimeStamp() {
		Instant instant = Instant.now();
		ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");

		String timeStamp = formatter.format(zonedDateTime);
		System.out.println("Generated Timestamp: " + timeStamp);

		return timeStamp;
	}

}
