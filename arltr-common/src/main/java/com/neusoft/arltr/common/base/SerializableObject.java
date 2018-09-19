/**
 * 
 */
package com.neusoft.arltr.common.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * bean串行基类
 * 
 *
 *
 */
public class SerializableObject {

	@Override
	public String toString() {
		
		String objString = "";
		
		if (this != null) {
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			try {
				objString = objectMapper.writeValueAsString(this);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		
		return objString;
	}
}
