package odunlamizo.taskflow.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import odunlamizo.taskflow.auth.Application;
import odunlamizo.taskflow.auth.dto.UserDto;
import odunlamizo.taskflow.auth.model.EmailVerificationToken;
import odunlamizo.taskflow.auth.model.User;
import odunlamizo.taskflow.auth.repository.EmailVerificationTokenRepository;
import odunlamizo.taskflow.auth.repository.UserRepository;
import odunlamizo.taskflow.auth.util.Objects;

@AutoConfigureMockMvc
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = Application.class)
public class IntegrationTests {

	@Nested
	@Order(1)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	class AccountContollerIntegrationTest {

		@LocalServerPort
		private int port;

		@Autowired
		private MockMvc mockMvc;

		@Autowired
		private UserRepository userRepository;

		@Autowired
		private EmailVerificationTokenRepository verificationTokenRepository;

		@Test
		@Order(1)
		public void whenPostUser_createUser_createToken_thenStatus3xx() throws Exception {
			UserDto user = new UserDto();
			user.setEmail("john@taskflow.xxx");
			user.setName("John Kayode");
			user.setUsername("JKay_99");
			user.setPassword("i8BinKay");
			mockMvc.perform(
					post(String.format("http://localhost:%s/signup", port)).params(Objects.convertObjectToParams(user)))
					.andExpect(status().is3xxRedirection());
			List<User> users = userRepository.findAll();
			assertThat(users).extracting("email").contains("john@taskflow.xxx");
			List<EmailVerificationToken> tokens = verificationTokenRepository.findAll();
			assertThat(tokens).extracting("user", User.class)
					.haveExactly(1,
							new Condition<>(
									u -> u.getName().getFirstName().equals("John")
											&& u.getName().getLastName().equals("Kayode"),
									"Is John Kayode"));
		}

		@Test
		@Order(2)
		public void whenPostToken_setUserEnabled_thenStatus3xx() throws Exception {
			User user = userRepository.findByEmail("john@taskflow.xxx").get();
			EmailVerificationToken token = verificationTokenRepository.findByUser(user).get();
			mockMvc.perform(post(String.format("http://localhost:%s/verify", port)).param("token", token.getId()))
					.andExpect(status().is3xxRedirection());
			user = userRepository.findByEmail("john@taskflow.xxx").get();
			assertThat(user.isEnabled()).isTrue();
		}

	}

	@Nested
	@Order(2)
	class MailControllerIntegrationTest {

		@LocalServerPort
		private int port;

		@Autowired
		private MockMvc mockMvc;

		@Autowired
		private EmailVerificationTokenRepository verificationTokenRepository;

		@Test
		public void whenSendVerification_createToken_thenStatus3xx() throws Exception {
			mockMvc.perform(post(String.format("http://localhost:%s/mail/verification", port)).param("email",
					"john@taskflow.xxx"))
					.andExpect(status().is3xxRedirection());
			List<EmailVerificationToken> tokens = verificationTokenRepository.findAll();
			assertThat(tokens).extracting("user", User.class)
					.haveExactly(1,
							new Condition<>(
									u -> u.getName().getFirstName().equals("John")
											&& u.getName().getLastName().equals("Kayode"),
									"Is John Kayode"));
		}

	}

}
