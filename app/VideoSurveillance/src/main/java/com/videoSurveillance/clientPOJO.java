package com.videoSurveillance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class clientPOJO {
	HttpServletRequest request;
	HttpServletResponse response;
	
	public clientPOJO(HttpServletRequest req, HttpServletResponse resp) {
		this.request = req;
		this.response = resp;
	}
}
