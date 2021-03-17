package net.study.resume.service;

import net.study.resume.entity.Profile;
import net.study.resume.repository.storage.ProfileRepository;
import net.study.resume.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private ProfileRepository profileRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Profile profile = profileRepository.findByUidOrEmailOrPhone(username, username, username)
				.orElseThrow(()->new UsernameNotFoundException("Invalid username or password."));

		return  UserPrincipal.create(profile);

	}
}
