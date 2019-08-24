package com.company.app.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {
	
	
	@Test
	public void checkBCrypt() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String passwd = encoder.encode("admin"); 
		System.out.println(" ---> "+passwd);
	}

}
