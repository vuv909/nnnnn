package com.swp.server.services.job_apply_notification;

import com.swp.server.dto.JobDTO;
import com.swp.server.dto.SendJobApplyNotificationDTO;
import org.springframework.http.ResponseEntity;

public interface JobApplyNotificationService {
	public ResponseEntity<?> sendJobApplyNotification(SendJobApplyNotificationDTO sendNotifyDTO);

	public ResponseEntity<?> viewJobApplyNotification();

	public ResponseEntity<?> viewTotalCount(String email);

	public ResponseEntity<?> readNoti(String email);

	public ResponseEntity<?> viewJobApplyNotificationByEmail(String email);
}
