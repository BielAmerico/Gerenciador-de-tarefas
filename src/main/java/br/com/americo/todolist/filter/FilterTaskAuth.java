package br.com.americo.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import br.com.americo.todolist.user.UserModel;
import br.com.americo.todolist.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

	@Autowired
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String servletPath = request.getServletPath();
		
		System.out.println("PATH" + servletPath);
		if (servletPath.startsWith("/tasks/")) {

			var authorization = request.getHeader("Authorization");
			
			var authEncoded = authorization.substring("Basic".length()).trim();

			byte[] authDecode = Base64.getDecoder().decode(authEncoded);

			var authString = new String(authDecode);

			System.out.println("Authorization");
			System.out.println(authString);

			
			String[] credentials = authString.split(":");
			var username = credentials[0];
			var password = credentials[1];
			System.out.println("Authorization");
			System.out.println(username);
			System.out.println(password);

			authorization.substring("Basic".length()).trim();

			//validar usuário
			var user = this.userRepository.findByUsername(username);

			if (user == null) {
				response.sendError(401);
			} else {
				// validar senha
				var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

				if (passwordVerify.verified) {
					//segue viagem
					
					request.setAttribute("idUser", user.getId());
					filterChain.doFilter(request, response);

				} else {
					response.sendError(401);
				}
			}

		} else {
			filterChain.doFilter(request, response);
		}
	}
}


