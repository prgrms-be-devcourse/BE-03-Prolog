package com.prgrms.prolog.global.common;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import lombok.Getter;

@Getter
public class PageCustomResponse<T> {

	private List<T> stock;

	private PageableCustom pageableCustom;

	public PageCustomResponse(List<T> stock, Pageable pageable, long totalElement) {
		this.stock = stock;
		this.pageableCustom = new PageableCustom(new PageImpl<>(stock, pageable, totalElement));
	}
}
