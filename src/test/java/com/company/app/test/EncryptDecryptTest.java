package com.company.app.test;

import java.io.FileInputStream;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.junit4.SpringRunner;

import com.company.app.util.EncryptDecrypt;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
@PropertySource({ 
	  "classpath:application.properties"
	})
public class EncryptDecryptTest {
    public EncryptDecryptTest() {
    }
    
    @Value("${spring.datasource.url}")
	String url;
	@Value("${spring.datasource.username}")
	String username;
	@Value("${spring.datasource.password}")
	String password;
	@Value("${spring.datasource.driver-class-name}")
	String driver;
    
    @Test
    public void encryptDecryptTest() throws Exception {
        /*
    	 * First, create (or ask some other component for) the adequate encryptor for
    	  * decrypting the values in our .properties file.
    	  */

    	 StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    	 encryptor.setPassword(password); // could be got from web, env variable...   	 
    	 StandardPBEStringEncryptor encryptorx = new StandardPBEStringEncryptor();

         encryptor.setAlgorithm("PBEWithMD5AndDES");
         encryptor.setPassword("jasypt");
         encryptor.setKeyObtentionIterations(2);
         
         String encryptedPassword = encryptor.encrypt("dlw)#84deD");
         String dec = encryptor.decrypt("7QjWim9n9O6DszbwnPkQLJcTtloQbEJz");
         
         System.out.println(encryptedPassword);
         System.out.println(dec);
    	 
    }
 
}