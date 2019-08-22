package de.naju.adebar.app.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * The {@link UserDetailsService} for our {@link UserAccount} implementation. Completely straight
 * forward.
 *
 * @author Rico Bergmann
 *
 */
@Service
public class UserAccountService implements UserDetailsService {
	private UserAccountRepository userRepository;

	@Autowired
	public UserAccountService(UserAccountRepository userRepo) {
		Assert.notNull(userRepo, "User account repository should not be null");
		this.userRepository = userRepo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.
	 * String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) {
		return userRepository.findByUsername(Username.of(username))
				.orElseThrow(() -> new UsernameNotFoundException("For username " + username));
	}
}
