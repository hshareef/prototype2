package com.prototype.response;

import java.util.List;
import com.prototype.messages.Message;

public class BaseResponse {
	
	private Integer code;
	private List<Message> messages;
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
}
