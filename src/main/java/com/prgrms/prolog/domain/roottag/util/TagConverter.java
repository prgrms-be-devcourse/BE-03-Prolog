package com.prgrms.prolog.domain.roottag.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class TagConverter {

	private static final String TAG_EXPRESSION = "#";

	private TagConverter() {
	}

	public static Set<String> convertFrom(String tagNames) {
		if (tagNames == null || tagNames.isBlank()) {
			return Collections.emptySet();
		}

		return Arrays.stream(tagNames.split(TAG_EXPRESSION))
			.filter(tagName -> !tagName.isBlank())
			.collect(Collectors.toSet());
	}
}
