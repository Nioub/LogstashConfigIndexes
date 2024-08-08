package com.logstash;

import java.util.Deque;
import java.util.LinkedList;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Vocabulary;
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
import com.logstash.json.ArrayValues;
import com.logstash.json.HashValues;
import com.logstash.json.Plugin;
import com.logstash.json.PluginAttribute;
import com.logstash.json.PluginOrCondition;
import com.logstash.json.Stage;
import com.logstash.json.Stages;

public class JsonListener implements LogStashConfigListener {
	
	public Stages configStages = null;
	
	private final Vocabulary vocabulary;
	
	private Stage currentStage = null;
	private Deque<PluginOrCondition> pluginOrConditionStack = new LinkedList<>();
	private Deque<PluginAttribute> pluginAttributeStack = new LinkedList<>();
	private Deque<ArrayValues> arrayValuesStack = new LinkedList<>();
	private Deque<HashValues> hashValuesStack = new LinkedList<>();
	
	public JsonListener(final Vocabulary vocabulary) {
		this.vocabulary = vocabulary;
	}
	
	@Override
	public void visitTerminal(TerminalNode node) {}

	@Override
	public void visitErrorNode(ErrorNode node) {}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {}

	@Override
	public void enterConfig(ConfigContext ctx) {
		configStages = new Stages();
	}

	@Override
	public void exitConfig(ConfigContext ctx) {}

	@Override
	public void enterStage_declaration(Stage_declarationContext ctx) {
		final String currentStageName = ctx.getStart().getText();
		currentStage = new Stage();
		configStages.put(currentStageName, currentStage);
	}

	@Override
	public void exitStage_declaration(Stage_declarationContext ctx) {
		currentStage = null;
	}

	@Override
	public void enterStage_definition(Stage_definitionContext ctx) {}

	@Override
	public void exitStage_definition(Stage_definitionContext ctx) {}

	@Override
	public void enterPlugin_declaration(Plugin_declarationContext ctx) {
		final String currentPluginName = ctx.getStart().getText();
		final Plugin currentPlugin = new Plugin(currentPluginName);
		if (pluginOrConditionStack.isEmpty()) {
			currentStage.add(currentPlugin);
		}
		pluginOrConditionStack.push(currentPlugin);
	}

	@Override
	public void exitPlugin_declaration(Plugin_declarationContext ctx) {
		pluginOrConditionStack.pop();
	}

	@Override
	public void enterPlugin_definition(Plugin_definitionContext ctx) {}

	@Override
	public void exitPlugin_definition(Plugin_definitionContext ctx) {}

	@Override
	public void enterPlugin_attribute(Plugin_attributeContext ctx) {
		final String currentPluginAttributeName = ctx.getStart().getText();
		final PluginAttribute currentPluginAttribute = new PluginAttribute(currentPluginAttributeName);
		((Plugin) pluginOrConditionStack.peek()).pluginAttributes.add(currentPluginAttribute);
		pluginAttributeStack.push(currentPluginAttribute);
	}

	@Override
	public void exitPlugin_attribute(Plugin_attributeContext ctx) {
		pluginAttributeStack.pop();
	}

	@Override
	public void enterPlugin_attribute_value(Plugin_attribute_valueContext ctx) {
		final String tokenText = ctx.getStart().getText();
		final int tokenType = ctx.start.getType();
		final String symbolicName = vocabulary.getSymbolicName(tokenType);
		
		if ("IDENTIFIER".equals(symbolicName)) {
			pluginAttributeStack.peek().value = tokenText;
		} else if ("STRING".equals(symbolicName)) {
			String insideText = tokenText.substring(1, tokenText.length() - 1);
			if (tokenText.charAt(0) == '\'') {
				insideText = insideText.replaceAll("\\'", "'");
			} else {
				insideText = insideText.replaceAll("\\\"", "\"");
			}
			pluginAttributeStack.peek().value = insideText;
		} else if ("DECIMAL".equals(symbolicName)) {
			pluginAttributeStack.peek().value = Long.parseLong(tokenText);
		} else if ("LBRACKET".equals(symbolicName)) {
			final ArrayValues arrayValues = new ArrayValues();
			pluginAttributeStack.peek().value = arrayValues;
			arrayValuesStack.push(arrayValues);
		} else if ("LBRACE".equals(symbolicName)) {
			final HashValues hashValues = new HashValues();
			pluginAttributeStack.peek().value = hashValues;
			hashValuesStack.push(hashValues);
		} else {
			// Handle plugin_declaration here?
			System.out.println(symbolicName);
		}
	}

	@Override
	public void exitPlugin_attribute_value(Plugin_attribute_valueContext ctx) {
		final int tokenType = ctx.start.getType();
		final String symbolicName = vocabulary.getSymbolicName(tokenType);
		if ("RBRACKET".equals(symbolicName)) {
			arrayValuesStack.pop();
		} else if ("RBRACE".equals(symbolicName)) {
			hashValuesStack.pop();
		}
	}

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
	public void enterArray_element(Array_elementContext ctx) {
		final String tokenText = ctx.getStart().getText();
		final int tokenType = ctx.start.getType();
		final String symbolicName = vocabulary.getSymbolicName(tokenType);
		
		if ("IDENTIFIER".equals(symbolicName)) {
			arrayValuesStack.peek().add(tokenText);
		} else if ("STRING".equals(symbolicName)) {
			String insideText = tokenText.substring(1, tokenText.length() - 1);
			if (tokenText.charAt(0) == '\'') {
				insideText = insideText.replaceAll("\\'", "'");
			} else {
				insideText = insideText.replaceAll("\\\"", "\"");
			}
			arrayValuesStack.peek().add(insideText);
		} else if ("DECIMAL".equals(symbolicName)) {
			arrayValuesStack.peek().add(Long.parseLong(tokenText));
		} else if ("LBRACKET".equals(symbolicName)) {
			final ArrayValues arrayValues = new ArrayValues();
			arrayValuesStack.peek().add(arrayValues);
			arrayValuesStack.push(arrayValues);
		} else if ("LBRACE".equals(symbolicName)) {
			final HashValues hashValues = new HashValues();
			arrayValuesStack.peek().add(hashValues);
			hashValuesStack.push(hashValues);
		}
	}

	@Override
	public void exitArray_element(Array_elementContext ctx) {
		final int tokenType = ctx.start.getType();
		final String symbolicName = vocabulary.getSymbolicName(tokenType);
		if ("LBRACKET".equals(symbolicName)) {
			arrayValuesStack.pop();
		} else if ("LBRACE".equals(symbolicName)) {
			hashValuesStack.pop();
		}
		arrayValuesStack.pop();
	}

	@Override
	public void enterHash(HashContext ctx) {}

	@Override
	public void exitHash(HashContext ctx) {}

	@Override
	public void enterHash_element(Hash_elementContext ctx) {}

	@Override
	public void exitHash_element(Hash_elementContext ctx) {}

}
