package com.rcc.test.base;

/**
 * Validator校验，封装不符合项提示信息
 * @author rencc
 * @date 2019年2月21日
 */
public class FieldErrorItem {
	/** 字段 */
	private  String field;

	/** 错误信息 */
	private  String message;
	public FieldErrorItem(){
		
	}
	public FieldErrorItem(String field, String message ){
		this.field=field;
		this.message=message;
	}
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}




}
