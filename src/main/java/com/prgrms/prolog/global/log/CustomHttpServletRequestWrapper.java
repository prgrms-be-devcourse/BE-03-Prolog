package com.prgrms.prolog.global.log;

import static java.nio.charset.StandardCharsets.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final Map<String, String[]> paramMap = new HashMap<>();
	private final byte[] cachedBody;

	public CustomHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		paramMap.putAll(request.getParameterMap());
		ServletInputStream inputStream = request.getInputStream();
		cachedBody = inputStream.readAllBytes();
		inputStream.close();
	}

	@Override
	public ServletInputStream getInputStream() {
		final ByteArrayInputStream cachedArray = new ByteArrayInputStream(cachedBody);
		return new ServletInputStream() {

			@Override
			public boolean isFinished() {
				return cachedArray.available() == 0;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {
				throw new RuntimeException("Not Implemented");
			}

			@Override
			public int read() {
				return cachedArray.read();
			}
		};
	}

	@Override
	public BufferedReader getReader() {
		return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.cachedBody), UTF_8));
	}

	public String getCachedBody() throws JSONException {
		if (paramMap.isEmpty() && cachedBody.length != 0) {
			return new JSONObject(getReader().lines().collect(Collectors.joining())).toString();
		}

		JSONObject jsonObject = new JSONObject();
		for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
			jsonObject.accumulate(entry.getKey(), String.join("", entry.getValue()));
		}
		return jsonObject.toString();
	}
}
