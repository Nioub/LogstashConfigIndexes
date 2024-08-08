package com.logstash.json;

import java.util.ArrayList;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Plugin extends PluginOrCondition {

	@NonNull
	public final String name;
	
	public final ArrayList<PluginAttribute> pluginAttributes = new ArrayList<PluginAttribute>();
	
}
