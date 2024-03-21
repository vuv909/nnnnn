package com.swp.server.services.job_apply_notification;

import com.swp.server.dto.SendJobApplyNotificationDTO;
import com.swp.server.dto.ViewJobApplyNotificationDTO;
import com.swp.server.entities.JobApplyNotification;
import com.swp.server.repository.JobApplyNotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class JobApplyNotificationServiceImpl implements JobApplyNotificationService {
	@Autowired
	private JobApplyNotificationRepo jobApplyNotificationRepo;

	@Override
	public ResponseEntity<?> sendJobApplyNotification(SendJobApplyNotificationDTO sendNotifyDTO) {
		System.out.print(sendNotifyDTO.toString());
		try {
			if (sendNotifyDTO.getContent().isEmpty()) {
				Map<String, Object> error = new HashMap<>();
				error.put("error", "Không được để trống trường nào nội dung !!!");
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
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
			Map<String, Object> error = new HashMap<>();
			error.put("success", "Thêm thành công !!!");
			return new ResponseEntity<>(error, HttpStatus.OK);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "INTERNAL SERVER ERROR !!!");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> viewJobApplyNotificationByEmail(String email) {
		List<JobApplyNotification> viewJobApplyNotificationDTOList = jobApplyNotificationRepo.findAllByEmail(email);
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
			return ResponseEntity.ok(sendDTO);
		} else {
			return ResponseEntity.ok(sendDTO);
		}
	}

	@Override
	public ResponseEntity<List<ViewJobApplyNotificationDTO>> viewJobApplyNotification() {
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
			return ResponseEntity.ok(sendDTO);
		} else {
			return ResponseEntity.ok(sendDTO);
		}
	}

	@Override
	public ResponseEntity<?> viewTotalCount(String email) {
		List<JobApplyNotification> viewJobApplyNotificationDTOList = jobApplyNotificationRepo.findAllByEmail(email);
		List<ViewJobApplyNotificationDTO> sendDTO = new ArrayList<>();
		int count = 0;
		if (!viewJobApplyNotificationDTOList.isEmpty()) {
			for (JobApplyNotification noti : viewJobApplyNotificationDTOList) {
				if (noti.isRead() == false) {
					count += 1;
				}
			}
			return ResponseEntity.ok(count);
		} else {
			return ResponseEntity.ok(sendDTO);
		}
	}

	@Override
	public ResponseEntity<?> readNoti(String email) {
		List<JobApplyNotification> viewJobApplyNotificationDTOList = jobApplyNotificationRepo.findAllByEmail(email);
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

				jobNotification.setRead(true);
				jobNotification.setCreated_At(display.toString());
				sendDTO.add(jobNotification);
				JobApplyNotification savedNotification = noti;
				savedNotification.setRead(true);
				JobApplyNotification save = jobApplyNotificationRepo.save(savedNotification);

			}
			return ResponseEntity.ok("Save successfull !!");
		} else {
			return ResponseEntity.ok("Save failed");
		}
	}

	private long calculateDurationInMilliseconds(long years, long days, long hours, long minutes, long seconds) {
		return (((((years * 365) + days) * 24 + hours) * 60 + minutes) * 60 + seconds) * 1000;
	}
}
