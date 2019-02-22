package com.rcc.test.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Collections;
import java.util.List;

/**
 * 全局接口返回结果对象
 * @author rencc
 * @date 2019年2月21日
 */
@JsonInclude(Include.NON_NULL)
public class RestResult<T> {
	private String errCode = "0";
	private String errDesc;
	private Object items;

	public static RestResult getInstance() {
		return new RestResult("0", "");
	}

	public static RestResult errorInstance() {
		return new RestResult("9999", "");
	}

	public RestResult() {
	}

	public RestResult(String errCode, String errDesc) {
		this.errCode = errCode;
		this.errDesc = errDesc;
	}
	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrDesc() {
		return errDesc;
	}

	public void setErrDesc(String errDesc) {
		this.errDesc = errDesc;
	}

	public Object getItems() {
		return items;
	}

	public void setItems(Object items) {
		this.items = items;
	}
	@Override
	public String toString() {
		return "BaseResult [errCode=" + this.errCode + ", errDesc=" + this.errDesc + ", items=" + this.items + "]";
	}
}