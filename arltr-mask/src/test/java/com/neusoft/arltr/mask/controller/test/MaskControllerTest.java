package com.neusoft.arltr.mask.controller.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
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
import com.neusoft.arltr.ArltrMaskApplication;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.mask.MaskWord;
import com.neusoft.arltr.mask.repository.MaskRepository;

/** 
* 类功能描述:
* @author 作者: 
* @version 创建时间：2017年6月22日 下午2:10:00 
*  
*/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = ArltrMaskApplication.class)
public class MaskControllerTest {
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	private int id;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	MaskRepository maskRepository;
	
	MaskWord testEntity;
	
//	@Before
//	public void init() {
//		MaskWord mm = new MaskWord();
//		mm.setCreateAt(new Date());
//		mm.setCreateBy(1);
//		mm.setCreateByName("heihei");
//		mm.setUpdateAt(new Date());
//		mm.setUpdateBy(1);
//		mm.setUpdateByName("hiahia");
//		mm.setWord("asdfasdf");
//		
//		testEntity = maskRepository.save(mm);
//		
//		
//	}
//	@After
//	public void fina() {
//		maskRepository.delete(testEntity);
//	}
//	
//	
//	@Test
//	@SuppressWarnings("unchecked")
//	public void getAllTest() throws Exception{
//		RespBody<List<MaskWord>> resp =  restTemplate.getForObject("/mask/all", RespBody.class);
//		
//		assertThat(resp.getCode()).isEqualTo(RespBody.SUCCESS);
//		
//		RespBody<List<MaskWord>> r =  objectMapper.readValue(objectMapper.writeValueAsString(resp), new TypeReference<RespBody<List<MaskWord>>>(){});
//		assertThat(r.getBody()).isNotNull();
//		List<MaskWord> list = r.getBody();
//		assertThat(list.size()).isNotEqualTo(0);
//	}
//	
	@Test
	@SuppressWarnings("unchecked")
	public void saveTest() throws Exception{
//		for(int i = 0;i<15;i++){
//			MaskWord ss = mw();
//			ss.setWord("asdf"+i);
//		RespBody<MaskWord> resp =  restTemplate.postForObject("/mask/save", ss,  RespBody.class);
//		
//		assertThat(resp.getCode()).isEqualTo(RespBody.SUCCESS);
//		}
//		RespBody<MaskWord> r =  objectMapper.readValue(objectMapper.writeValueAsString(resp), new TypeReference<RespBody<MaskWord>>(){});
//		MaskWord l= r.getBody();
//		id = l.getId();
//		RespBody<List<MaskWord>> resp1 =  restTemplate.getForObject("/mask/all", RespBody.class);
//		RespBody<List<MaskWord>> r =  objectMapper.readValue(objectMapper.writeValueAsString(resp1), new TypeReference<RespBody<List<MaskWord>>>(){});
//		assertThat(r.getBody()).isNotNull();
//		List<MaskWord> list = r.getBody();
//		assertThat(list.size()).isEqualTo(1);
		System.out.println("test");
	}
//	
//	@Test
//	@SuppressWarnings("unchecked")
//	public void getOne() throws Exception{
//		RespBody<MaskWord> resp =  restTemplate.getForObject("/mask/"+testEntity.getId(), RespBody.class);
//		
//		assertThat(resp.getCode()).isEqualTo(RespBody.SUCCESS);
//		RespBody<MaskWord> r =  objectMapper.readValue(objectMapper.writeValueAsString(resp), new TypeReference<RespBody<MaskWord>>(){});
//		assertThat(r.getBody()).isNotNull();
//		assertThat(r.getBody().getId()).isEqualTo(testEntity.getId());
//	}
	
//	@Test
//	@SuppressWarnings("unchecked")
//	public void deleteTest() throws Exception{
//		RespBody<MaskWord> resp =  restTemplate.getForObject("/mask/1/remove", RespBody.class);
//		
//		assertThat(resp.getCode()).isEqualTo(RespBody.SUCCESS);
//		RespBody<MaskWord> resp1 =  restTemplate.getForObject("/mask/1", RespBody.class);
//		RespBody<MaskWord> r =  objectMapper.readValue(objectMapper.writeValueAsString(resp1), new TypeReference<RespBody<MaskWord>>(){});
//		assertThat(r.getBody()).isNull();
//	}
	
//	@Test
//	@SuppressWarnings("unchecked")
//	public void deleteAllTest() throws Exception{
//		RespBody<List<MaskWord>> resp1 =  restTemplate.getForObject("/mask/all", RespBody.class);
//		RespBody<List<MaskWord>> r =  objectMapper.readValue(objectMapper.writeValueAsString(resp1), new TypeReference<RespBody<List<MaskWord>>>(){});
//		List<MaskWord> list = r.getBody();
//		RespBody<MaskWord> resp =  restTemplate.postForObject("/mask/remove/multiple", list,  RespBody.class);
//		assertThat(resp.getCode()).isEqualTo(RespBody.SUCCESS);
//		RespBody<List<MaskWord>> resp2 =  restTemplate.getForObject("/mask/all", RespBody.class);
//		RespBody<List<MaskWord>> r2 =  objectMapper.readValue(objectMapper.writeValueAsString(resp2), new TypeReference<RespBody<List<MaskWord>>>(){});
//		assertThat(r2.getBody()).isNull();
//	}
	private MaskWord mw(){
		MaskWord mm = new MaskWord();
		mm.setCreateAt(new Date());
		mm.setCreateBy(1);
		mm.setCreateByName("heihei");
		mm.setUpdateAt(new Date());
		mm.setUpdateBy(1);
		mm.setUpdateByName("hiahia");
		mm.setWord("asdfasdasdf");
		return mm;
	}
	
}
