package com.logstash;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logstash.json.Stages;

public class ExportJsonTest {

	@Test
	public void testBasic() throws IOException {
		final String basic_conf_filepath = "./src/test/resource/basic.conf";
		final String basic_res_filepath = "./src/test/resource/basic.json";
		
		CharStream input = CharStreams.fromFileName(basic_conf_filepath);
        final LogStashConfigLexer lexer = new LogStashConfigLexer(input);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final LogStashConfigParser parser = new LogStashConfigParser(tokens);
        final JsonListener listener = new JsonListener(parser.getVocabulary());
        parser.addParseListener(listener);
        parser.config();
        
        final Stages stages = listener.configStages;
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper
        		.writerWithDefaultPrettyPrinter()
        		.writeValueAsString(stages);
        
        CharStream res_stream = CharStreams.fromFileName(basic_res_filepath);
        assertEquals(res_stream.toString(), jsonString);
	}
}
