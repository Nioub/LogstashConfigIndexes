package com.logstash;

import java.util.HashSet;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.logstash.LogStashConfigParser.ArrayContext;
import com.logstash.LogStashConfigParser.Array_elementContext;
import com.logstash.LogStashConfigParser.Compare_expressionContext;
import com.logstash.LogStashConfigParser.ConfigContext;
import com.logstash.LogStashConfigParser.FieldrefContext;
import com.logstash.LogStashConfigParser.Fieldref_elementContext;
import com.logstash.LogStashConfigParser.HashContext;
import com.logstash.LogStashConfigParser.Hash_elementContext;
import com.logstash.LogStashConfigParser.In_expressionContext;
import com.logstash.LogStashConfigParser.Logical_expressionContext;
import com.logstash.LogStashConfigParser.Match_expressionContext;
import com.logstash.LogStashConfigParser.Negative_expressionContext;
import com.logstash.LogStashConfigParser.Plugin_attributeContext;
import com.logstash.LogStashConfigParser.Plugin_attribute_valueContext;
import com.logstash.LogStashConfigParser.Plugin_declarationContext;
import com.logstash.LogStashConfigParser.Plugin_definitionContext;
import com.logstash.LogStashConfigParser.RvalueContext;
import com.logstash.LogStashConfigParser.Stage_conditionContext;
import com.logstash.LogStashConfigParser.Stage_declarationContext;
import com.logstash.LogStashConfigParser.Stage_definitionContext;

public class ESListener implements LogStashConfigListener {
	
	private static final String SECTION_INPUT = "input";
	private static final String SECTION_FILTER = "filter";
	private static final String SECTION_OUTPUT = "output";
	private static final String PLUGIN_ELASTICSEARCH = "elasticsearch";
	private static final String PLUGIN_ATTRIBUTE_INDEX = "index";

	public Set<String> inputIndexes = new HashSet<>();
	public Set<String> filterIndexes = new HashSet<>();
	public Set<String> outputIndexes = new HashSet<>();
	
	private String currentSection = null;
	private String currentPlugin = null;
	private String currentPluginAttribute = null;

	@Override
	public void visitTerminal(TerminalNode node) {}

	@Override
	public void visitErrorNode(ErrorNode node) {}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {}

	@Override
	public void enterConfig(ConfigContext ctx) {}

	@Override
	public void exitConfig(ConfigContext ctx) {}

	@Override
	public void enterStage_declaration(Stage_declarationContext ctx) {
		currentSection = ctx.getStart().getText();
		//System.out.println(String.format("Entering Stage_declaration : %s", currentSection));
	}

	@Override
	public void exitStage_declaration(Stage_declarationContext ctx) {
		//System.out.println(String.format("Exiting  Stage_declaration : %s", currentSection));
		currentSection = null;
	}

	@Override
	public void enterStage_definition(Stage_definitionContext ctx) {}

	@Override
	public void exitStage_definition(Stage_definitionContext ctx) {}

	@Override
	public void enterPlugin_declaration(Plugin_declarationContext ctx) {
		currentPlugin = ctx.getStart().getText();
		//if (ELASTICSEARCH_PLUGIN.equals(currentPlugin)) {
		//	System.out.println(String.format("Entering plugin %s", ELASTICSEARCH_PLUGIN));
		//}
	}

	@Override
	public void exitPlugin_declaration(Plugin_declarationContext ctx) {
		currentPluginAttribute = ctx.getStart().getText();
		//if (ELASTICSEARCH_PLUGIN.equals(currentPluginAttribute)) {
		//	System.out.println(String.format("Exiting  plugin %s", ELASTICSEARCH_PLUGIN));
		//}
	}

	@Override
	public void enterPlugin_definition(Plugin_definitionContext ctx) {}

	@Override
	public void exitPlugin_definition(Plugin_definitionContext ctx) {}

	@Override
	public void enterPlugin_attribute(Plugin_attributeContext ctx) {
		currentPluginAttribute = ctx.getStart().getText();
		//if (INDEX_ATTRIBUTE.equals(currentPluginAttribute) && ELASTICSEARCH_PLUGIN.equals(currentPlugin)) {
		//	System.out.println(String.format("Entering plugin %s / attribute %s", ELASTICSEARCH_PLUGIN, INDEX_ATTRIBUTE));
		//}
	}

	@Override
	public void exitPlugin_attribute(Plugin_attributeContext ctx) {
		currentPluginAttribute = ctx.getStart().getText();
		//if (INDEX_ATTRIBUTE.equals(currentPluginAttribute) && ELASTICSEARCH_PLUGIN.equals(currentPlugin)) {
		//	System.out.println(String.format("Entering plugin %s / attribute %s", ELASTICSEARCH_PLUGIN, INDEX_ATTRIBUTE));
		//}
	}

	@Override
	public void enterPlugin_attribute_value(Plugin_attribute_valueContext ctx) {
		if (PLUGIN_ATTRIBUTE_INDEX.equals(currentPluginAttribute) && PLUGIN_ELASTICSEARCH.equals(currentPlugin)) {
			final String host = ctx.getStart().getText();
			//System.out.println(String.format("Adding index : %s",host));
			if (SECTION_INPUT.equals(currentSection)) {
				inputIndexes.add(host);
			} else if (SECTION_FILTER.equals(currentSection)) {
				filterIndexes.add(host);
			} else if (SECTION_OUTPUT.equals(currentSection)) {
				outputIndexes.add(host);
			}
		}
	}

	@Override
	public void exitPlugin_attribute_value(Plugin_attribute_valueContext ctx) {}

	@Override
	public void enterStage_condition(Stage_conditionContext ctx) {}

	@Override
	public void exitStage_condition(Stage_conditionContext ctx) {}

	@Override
	public void enterLogical_expression(Logical_expressionContext ctx) {}

	@Override
	public void exitLogical_expression(Logical_expressionContext ctx) {}

	@Override
	public void enterNegative_expression(Negative_expressionContext ctx) {}

	@Override
	public void exitNegative_expression(Negative_expressionContext ctx) {}

	@Override
	public void enterCompare_expression(Compare_expressionContext ctx) {}

	@Override
	public void exitCompare_expression(Compare_expressionContext ctx) {}

	@Override
	public void enterIn_expression(In_expressionContext ctx) {}

	@Override
	public void exitIn_expression(In_expressionContext ctx) {}

	@Override
	public void enterMatch_expression(Match_expressionContext ctx) {}

	@Override
	public void exitMatch_expression(Match_expressionContext ctx) {}

	@Override
	public void enterRvalue(RvalueContext ctx) {}

	@Override
	public void exitRvalue(RvalueContext ctx) {}

	@Override
	public void enterFieldref(FieldrefContext ctx) {}

	@Override
	public void exitFieldref(FieldrefContext ctx) {}

	@Override
	public void enterFieldref_element(Fieldref_elementContext ctx) {}

	@Override
	public void exitFieldref_element(Fieldref_elementContext ctx) {}

	@Override
	public void enterArray(ArrayContext ctx) {}

	@Override
	public void exitArray(ArrayContext ctx) {}

	@Override
	public void enterArray_element(Array_elementContext ctx) {}

	@Override
	public void exitArray_element(Array_elementContext ctx) {}

	@Override
	public void enterHash(HashContext ctx) {}

	@Override
	public void exitHash(HashContext ctx) {}

	@Override
	public void enterHash_element(Hash_elementContext ctx) {}

	@Override
	public void exitHash_element(Hash_elementContext ctx) {}

}
