package com.swp.server.services.jwt;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.swp.server.entities.Account;
import com.swp.server.repository.AccountRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AccountRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// WRITE LOGIC TO GET USER FROM DB

		Optional<Account> optionalUser = userRepo.findFirstByEmail(email);

		if (optionalUser.isEmpty())
			throw new UsernameNotFoundException("User not found ", null);

		return new org.springframework.security.core.userdetails.User(optionalUser.get().getEmail(),
				optionalUser.get().getPassword(), new ArrayList<>());
	}

}
