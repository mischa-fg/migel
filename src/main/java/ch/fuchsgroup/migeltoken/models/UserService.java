package ch.fuchsgroup.migeltoken.models;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	UserRepository userRepository;
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User getUser(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if(!user.isPresent()) {
			return null;
		}
		return user.get();
	}
	
	public void save(User user) {
		userRepository.save(user);
	}
	
}
