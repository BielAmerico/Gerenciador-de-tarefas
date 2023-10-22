package br.com.americo.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public ResponseEntity<Object> saveUser(UserModel userModel) {

		var user = this.userRepository.findByUsername(userModel.getUsername());

		if (user != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
		}

		String passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

		userModel.setPassword(passwordHashred);

		var userCreated = this.userRepository.save(userModel);
		return ResponseEntity.status(HttpStatus.OK).body(userCreated);

	}
}
