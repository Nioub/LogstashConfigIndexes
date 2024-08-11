package com.logstash.json;

import java.util.ArrayList;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConditionChoice {
	//* For now, the condition stays as text only */
	public String conditionText = null;
	public ArrayList<PluginOrCondition> pluginsOrConditionsList = new ArrayList<>();
}
