package com.logstash;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logstash.json.Stages;

public class ExportJson {

	public static void main(String[] args) throws IOException {
        System.out.print("Parsing: ");

        String sourceName = null;
        Reader reader = null;
        if (args.length > 0) {
            System.out.println(args[0]);
            sourceName = args[0];
            reader = new BufferedReader(new FileReader(args[0]));
        } else {
            System.out.println("STDIN");
            sourceName = "(STDIN)";
            reader = new BufferedReader(new InputStreamReader(System.in));
        }

        final CharStream input = CharStreams.fromReader(reader, sourceName);
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
        System.out.println(String.format("JSON output :\n%s\n", jsonString));
    }
}
