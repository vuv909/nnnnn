package com.swp.server.controller;

import com.swp.server.dto.ChatMessageDTO;
import com.swp.server.dto.SendJobApplyNotificationDTO;
import com.swp.server.dto.ViewJobApplyNotificationDTO;
import com.swp.server.entities.JobApplyNotification;
import com.swp.server.entities.MessageEntity;
import com.swp.server.repository.JobApplyNotificationRepo;
import com.swp.server.services.job.JobService;
import com.swp.server.services.job_apply_notification.JobApplyNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/notify")
public class JobApplyNotificationController {
	@Autowired
	private JobApplyNotificationService jobApplyNotificationService;
	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private JobApplyNotificationRepo jobApplyNotificationRepo;

	@MessageMapping("/notify")
	@SendTo("/toClient")
	public void chat(SendJobApplyNotificationDTO notification) {

		sendJobApplyNotification(notification);

		List<ViewJobApplyNotificationDTO> jobApplyNotifications = viewJobApplyNotificationSocket();

		this.template.convertAndSend("/toClient", jobApplyNotifications);

	}

	public List<ViewJobApplyNotificationDTO> viewJobApplyNotificationSocket() {
		List<JobApplyNotification> viewJobApplyNotificationDTOList = jobApplyNotificationRepo.findAll();
		List<ViewJobApplyNotificationDTO> sendDTO = new ArrayList<>();
		int count = 0;
		if (!viewJobApplyNotificationDTOList.isEmpty()) {
			for (JobApplyNotification noti : viewJobApplyNotificationDTOList) {
				ViewJobApplyNotificationDTO jobNotification = new ViewJobApplyNotificationDTO();
				jobNotification.setContent(noti.getContent());
				Calendar calendar = Calendar.getInstance();
				java.util.Date currentDate = calendar.getTime();
				Timestamp timestamp = new Timestamp(currentDate.getTime());
				System.out.println("cur: " + timestamp);
				Timestamp timestampDB = new Timestamp(noti.getCreated_At().getTime());
				System.out.println("db: " + timestampDB);

				long duration = timestamp.getTime() - timestampDB.getTime();
				System.out.println("duration: " + duration);

				long seconds = duration / 1000;
				long minutes = seconds / 60;
				long hours = minutes / 60;
				long days = hours / 24;
				long years = days / 365;

				StringBuilder display = new StringBuilder();
				if (years > 0) {
					display.append(years).append(" year");
					if (years > 1) {
						display.append("s");
					}
					display.append(", ");
				}
				if (days > 0) {
					display.append(days % 365).append(" day");
					if (days % 365 > 1) {
						display.append("s");
					}
					display.append(", ");
				}
				if (hours > 0) {
					display.append(hours % 24).append(" hour");
					if (hours % 24 > 1) {
						display.append("s");
					}
					display.append(", ");
				}
				if (minutes > 0) {
					display.append(minutes % 60).append(" minute");
					if (minutes % 60 > 1) {
						display.append("s");
					}
					display.append(", ");
				}
				display.append(seconds % 60).append(" second");
				if (seconds % 60 > 1) {
					display.append("s ago");
				}
				jobNotification.setEmail(noti.getEmail());
				jobNotification.setRead(noti.isRead());
				jobNotification.setCreated_At(display.toString());
				sendDTO.add(jobNotification);
				JobApplyNotification savedNotification = noti;
				JobApplyNotification save = jobApplyNotificationRepo.save(savedNotification);

			}
			return sendDTO;
		} else {
			return sendDTO;
		}
	}

	public void sendJobApplyNotificationFunction(SendJobApplyNotificationDTO sendNotifyDTO) {
		System.out.print(sendNotifyDTO.toString());
		try {
			if (sendNotifyDTO.getContent().isEmpty()) {
				return;
			}
			Calendar calendar = Calendar.getInstance();
			java.util.Date currentDate = calendar.getTime();
			Timestamp timestamp = new Timestamp(currentDate.getTime());
			System.out.println(timestamp);

			JobApplyNotification newNotification = new JobApplyNotification();
			newNotification.setEmail(sendNotifyDTO.getEmail());
			newNotification.setContent(sendNotifyDTO.getContent());
			newNotification.setCreated_At(timestamp);
			newNotification.setRead(false);
			jobApplyNotificationRepo.save(newNotification);

		} catch (Exception e) {
			return;
		}
	}

	@PostMapping("/createNotification")
	public ResponseEntity<?> sendJobApplyNotification(@ModelAttribute SendJobApplyNotificationDTO sendNotifyDTO) {
		return jobApplyNotificationService.sendJobApplyNotification(sendNotifyDTO);
	}

	@GetMapping("/viewTotal/{email}")
	public ResponseEntity<?> viewTotalCount(@PathVariable String email) {
		return jobApplyNotificationService.viewTotalCount(email);
	}

	@PutMapping("/read/{email}")
	public ResponseEntity<?> readNoti(@PathVariable String email) {
		return jobApplyNotificationService.readNoti(email);
	}

	@GetMapping("/view/{email}")
	public ResponseEntity<?> viewJobApplyNotification(@PathVariable String email) {
		return jobApplyNotificationService.viewJobApplyNotificationByEmail(email);
	}

	@GetMapping("/view")
	public ResponseEntity<?> viewJobApplyNotification() {
		return jobApplyNotificationService.viewJobApplyNotification();
	}

}
