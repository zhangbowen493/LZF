package com.wanda.pay.util;

import org.json.JSONArray;
import org.json.JSONObject;

public class Result {
	
	public Result(String coder, String msg) {
		super();
		this.coder = coder;
		this.msg = msg;
	}
	public String coder ;
	public String msg ;
	public String token;
	public JSONObject jsonBodyObject;
	public JSONArray jsonBodyArray;
	
}
