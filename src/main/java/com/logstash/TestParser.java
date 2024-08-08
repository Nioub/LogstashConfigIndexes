package com.logstash;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public final class TestParser {

    public static void main(String[] args) throws Exception {
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
        //parser.setTrace(true);
        final ParseTree tree = parser.config();
        System.out.println(tree.toStringTree(parser));
    }
}
