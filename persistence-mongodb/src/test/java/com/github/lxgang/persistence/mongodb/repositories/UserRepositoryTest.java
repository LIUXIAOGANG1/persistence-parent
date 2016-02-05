package com.github.lxgang.persistence.mongodb.repositories;

import javax.annotation.Resource;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lxgang.persistence.beans.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class UserRepositoryTest {

	@Resource
	private UserRepository userRepository;
	
    @Before
    public void setUp() throws Exception {
        assertNotNull(userRepository);
        userRepository.deleteAll();
    }
    
    @Test
	public void save(){
		User user = new User();
		user.setId(1);
		user.setName("Test");
		user.setAge(19);
		user.setSex("男");
		
		User result = userRepository.save(user);

		assertNotNull(result);
		assertEquals("男", result.getSex());
	}
}
