package com.prgrms.prolog.global.log;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class CustomHttpServletRequestFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {

		if (request.getContentType() != null && request.getContentType().equals("multipart/form-data")) {
			chain.doFilter(request, response);
			return;
		}

		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		CustomHttpServletRequestWrapper customRequest = new CustomHttpServletRequestWrapper(httpServletRequest);
		chain.doFilter(customRequest, response);
	}
}
