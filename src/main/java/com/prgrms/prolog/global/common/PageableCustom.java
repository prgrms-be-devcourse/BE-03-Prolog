package com.prgrms.prolog.global.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageableCustom {
	private boolean first;
	private boolean last;
	private boolean hasNext;
	private int totalPages;
	private long totalElements;
	private int page;
	private int size;
	private Sort sort;

	public PageableCustom(Page page) {
		this.first = page.isFirst();
		this.last = page.isLast();
		this.hasNext = page.hasNext();
		this.totalPages = page.getTotalPages();
		this.totalElements = page.getTotalElements();
		this.page = page.getNumber() + 1;
		this.size = page.getSize();
		this.sort = page.getSort();
	}
}
