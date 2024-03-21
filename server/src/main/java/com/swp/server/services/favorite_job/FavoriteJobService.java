package com.swp.server.services.favorite_job;

import com.swp.server.dto.AccountDTO;
import com.swp.server.dto.Favorite_JobDTO;
import org.springframework.http.ResponseEntity;

public interface FavoriteJobService {
	ResponseEntity<?> favoriteJob(Favorite_JobDTO favorite_jobDTO);

	ResponseEntity<?> viewFavoriteJob(int accountId, Integer page);

	ResponseEntity<?> deleteFavoriteJob(int favoriteJobId);
}