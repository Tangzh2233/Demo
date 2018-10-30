package com.edu.controller.filter;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 过滤<br>
 * 当值为流的时候，不转换为json<br>
 */
public class LogJsonFilter implements PropertyPreFilter {

	private final Set<String> excludes = new HashSet<>();

	public LogJsonFilter(String... names) {
		excludes.addAll(Arrays.asList(names));
	}

	@Override
	public boolean apply(JSONSerializer serializer, Object object, String name) {
		if (object instanceof HttpServletResponse) {// 做类型过滤
			return false;
		}

		if (object instanceof byte[]) {// 做类型过滤
			return false;
		}

		if (object instanceof HttpServletRequest) {// 做类型过滤
			return false;
		}

		if (object instanceof InputStream) {// 做类型过滤
			return false;
		}

		if (object instanceof OutputStream) {// 做类型过滤
			return false;
		}
		// 根据属性名称做过滤
		if (excludes.contains(name)) {
			return false;
		}

		return true;
	}
}
