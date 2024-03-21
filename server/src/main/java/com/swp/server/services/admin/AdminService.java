package com.swp.server.services.admin;

import com.swp.server.dto.*;
import org.springframework.http.ResponseEntity;

public interface AdminService {
	public ResponseEntity<?> viewListAccounts(ViewListAccountDTO viewListAccountDTO, Integer page);

	public ResponseEntity<?> createAccount(CreateAccountDTO createAccountDTO);

	public ResponseEntity<?> blockAccount(BlockAccountDTO blockAccountDTO);

	public ResponseEntity<?> searchAccount(SearchAccountDTO searchAccountDTO);

	ResponseEntity<?> filterAccount(FilterAccountDTO filterAccountDTO);

	public ResponseEntity<?> countByRole();

}
