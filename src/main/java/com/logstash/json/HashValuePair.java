package com.logstash.json;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HashValuePair {

	@NonNull
	public final String key;
	
	public Object value;
}
