package com.neusoft.arltr.synonym.controller.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.arltr.ArltrSynonymApplication;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.synonym.Synonym;
import com.neusoft.arltr.synonym.repository.SynonymRepository;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = ArltrSynonymApplication.class)
public class SynonymControllerTest {
	@Autowired
    private TestRestTemplate restTemplate;
	@Autowired
	private SynonymRepository synonymRepository;
	ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * 测试获取详细信息方法
	 * @throws Exception
	 */
//	@Test
//	@SuppressWarnings("unchecked")
//	public void getOne() throws Exception {
//		Synonym data = createData(0);
//		RespBody<Synonym> resp = restTemplate.getForObject("/synonym/" + data.getId(), RespBody.class);
//		
//		assertThat(resp.getCode()).isEqualTo(RespBody.SUCCESS);
//		
//		RespBody<Synonym> r = objectMapper.readValue(objectMapper.writeValueAsString(resp), new TypeReference<RespBody<Synonym>>(){});
//		
//		assertThat(r.getBody()).isNotNull();
//		assertThat(r.getBody().getId()).isEqualTo(data.getId());
//		
//		removeTestSynonym(data);
//	}
	
	/**
	 * 测试新增和修改方法
	 * @throws Exception
	 */
//	@Test
//	@SuppressWarnings("unchecked")
//	public void save() throws Exception{
//		//测试Update加
//		Synonym data1 = createData(5);
//		
//		Synonym data = new Synonym();
//		Date now = new Date();
//		data.setWord("同义词测试");
//		data.setSynonymWord("222");
//		data.setCreateBy(222);
//		data.setCreateByName("test_user2");
//		data.setCreateAt(now);
//		data.setUpdateAt(now);
//		data.setUpdateBy(222);
//		data.setUpdateByName("test_user2");
//		//测试Update加
//		data.setId(data1.getId());
//		
//		RespBody<Synonym> resp = restTemplate.postForObject("/synonym/save", data, RespBody.class);
//
//		assertThat(resp.getCode()).isEqualTo(RespBody.SUCCESS);
//
//		RespBody<Synonym> r = objectMapper.readValue(objectMapper.writeValueAsString(resp), new TypeReference<RespBody<Synonym>>(){});
//
//		assertThat(r.getBody()).isNotNull();
//		Synonym temp = getInforByID(r.getBody().getId());
//		assertThat(temp).isNotNull();
//		assertThat(r.getBody().getWord()).isEqualTo(temp.getWord());
//
//		assertThat(temp.getWord()).isEqualTo(data.getWord());
//		
////		assertThat(resp.getCode()).isEqualTo(RespBody.BUSINESS_ERROR);
//
//
////		removeTestSynonym(data);
//	}
	
	/**
	 * 测试单一删除方法
	 * @throws Exception
	 */
//	@Test
//	@SuppressWarnings("unchecked")
//	public void remove() throws Exception {
//		Synonym data1 = createData(0);
//		RespBody<Synonym> resp = restTemplate.postForObject("/synonym/" + data1.getId()+"/remove",null, RespBody.class);
//		//删除成功
//		assertThat(resp.getCode()).isEqualTo(RespBody.SUCCESS);
//		
//		RespBody<Synonym> r = objectMapper.readValue(objectMapper.writeValueAsString(resp), new TypeReference<RespBody<Synonym>>() {});
//
//		Synonym deleInfor = getInforByID(data1.getId());
//		
//		assertThat(deleInfor).isNull();
//
//		//removeTestSynonym(data1);
//	}
	
	
	@Test
	@SuppressWarnings("unchecked")
	public void query() throws Exception {
//		Map<String,Object> param = new HashMap<String,Object>();
		
		Synonym s = new Synonym();
		
		Map<String,Object> param = objectMapper.readValue(objectMapper.writeValueAsString(s), HashMap.class);
		
		param.put("pageNumber", 1);
		param.put("pageSize", 10);
		param.put("order", "asc");
		param.put("sort", "createAt");
		
		RespBody<Synonym> resp = restTemplate.getForObject("/synonym/query", RespBody.class, param);
		assertThat(resp.getCode()).isEqualTo(RespBody.SUCCESS);
		
	}

	/**
	 * 新增一条同义词记录
	 * @return
	 */
	private Synonym createData(int flag){
		
		Date now = new Date();
		Synonym data = new Synonym();
		if(flag!=0){
			data.setWord("同义词测试"+flag);
			data.setSynonymWord("1111"+flag);
			data.setCreateBy(111);
			data.setCreateByName("test_user"+flag);
			data.setCreateAt(now);
			data.setUpdateAt(now);
			data.setUpdateBy(111);
			data.setUpdateByName("test_user"+flag);
		}else{
			data.setWord("同义词测试");
			data.setSynonymWord("1111");
			data.setCreateBy(111);
			data.setCreateByName("test_user");
			data.setCreateAt(now);
			data.setUpdateAt(now);
			data.setUpdateBy(111);
			data.setUpdateByName("test_user");
		}

		synonymRepository.save(data);
		return data;
	}
	private void removeTestSynonym(Synonym data) {
		synonymRepository.delete(data);
	}
	
	private Synonym getInforByID(Integer id){
		return synonymRepository.findOne(id);
	}
}
