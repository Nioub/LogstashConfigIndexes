package com.logstash.json;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PluginAttribute {
	
	@NonNull
	public final String name;
	// Can be : plugin_declaration | IDENTIFIER | STRING | DECIMAL | array | hash
	public Object value = null;

}
