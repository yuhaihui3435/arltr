package com.neusoft.arltr.user.controller.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.arltr.ArltrUserApplication;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.user.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = ArltrUserApplication.class)
public class UserControllerTest {

	@Autowired
    private TestRestTemplate restTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	@SuppressWarnings("unchecked")
	public void getUserOk() throws Exception {
		
//		User user = createTestUser();
//		
//		RespBody<User> resp =  restTemplate.getForObject("/user/" + user.getId(), RespBody.class);
//		
//		assertThat(resp.getCode()).isEqualTo(RespBody.SUCCESS);
//		
//		RespBody<User> r = objectMapper.readValue(objectMapper.writeValueAsString(resp), new TypeReference<RespBody<User>>(){});
//		
//		assertThat(r.getBody()).isNotNull();
//		assertThat(r.getBody().getId()).isEqualTo(user.getId());
//		
//		removeTestUser(user);
		
		System.out.println("test");
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getUserNg() throws Exception {
		
//		RespBody<User> resp =  restTemplate.getForObject("/user/123456", RespBody.class);
//		
//		assertThat(resp.getCode()).isEqualTo(RespBody.BUSINESS_ERROR);
	}
	
	private User createTestUser() {
		
		Date now = new Date();
		
		User user = new User();
		user.setEmployeeName("测试用户");
		user.setEmployeeNo("123456");
		user.setCreateAt(now);
		user.setUpdateAt(now);
		user.setUserName("test_user");
		
		userRepository.save(user);
		
		return user;
	}
	
	private void removeTestUser(User user) {
		
		userRepository.delete(user);
	}
}
