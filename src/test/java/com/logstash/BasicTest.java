package com.logstash;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

public class BasicTest {

	@Test
	public void testBasic() throws IOException {
		final String basic_conf_filepath = "./src/test/resource/basic.conf";
		
        CharStream input = CharStreams.fromFileName(basic_conf_filepath);
        LogStashConfigLexer lexer = new LogStashConfigLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LogStashConfigParser parser = new LogStashConfigParser(tokens);
        //parser.setTrace(true);
        ParseTree tree = parser.config();
        System.out.println(tree.toStringTree(parser));
	}
	
}
