package ch.fuchsgroup.migeltoken.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.fuchsgroup.migeltoken.models.User;
import ch.fuchsgroup.migeltoken.models.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class LoginController {
	UserService userService;
	PasswordEncoder passwordEncoder;
	@Autowired
	public LoginController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	@PostMapping("user")
	public ResponseEntity<String>login(@RequestParam("user") String username, @RequestParam("password") String pwd) {
		User user = userService.getUser(username);
		if(user == null || !passwordEncoder.matches(pwd, user.getPassword())) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		String token = getJWTToken(user);		
		return new ResponseEntity<>(token, HttpStatus.OK);
		
	}

	private String getJWTToken(User user) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(user.getRoles());
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(user.getUsername())
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				//.setExpiration(new Date(System.currentTimeMillis() + 600000))
				
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes())
				.compact();

		return "Bearer " + token;
	}
}
