import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.awt.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputFile = "Example.java";
        FileInputStream inputStream = new FileInputStream(inputFile);
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        ParseTree tree = parser.compilationUnit();
        ParseTreeWalker walker = new ParseTreeWalker();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        //intermediateBlockListener handles the expressions such as if/while/for that use a single line code
        // with no braces
        walker.walk(new intermediateBlockListener(rewriter), tree);
        //the output of the modified functions is then written to the file intermediate.java
        File inter= new File("intermediate.java");
        inter.createNewFile();
        FileWriter writer1 = new FileWriter("intermediate.java");
        writer1.write(rewriter.getText());
        writer1.close();
        /////////////////////////////////////////////////////////////////////////////////////
        inputFile = "intermediate.java";
        inputStream = new FileInputStream(inputFile);
        input = new ANTLRInputStream(inputStream);
        lexer = new JavaLexer(input);
        tokens = new CommonTokenStream(lexer);
        parser = new JavaParser(tokens);
        tree = parser.compilationUnit();
        walker = new ParseTreeWalker();

        rewriter = new TokenStreamRewriter(tokens);
        //convert intermediate.java to output.java that writes to a text file which block was visited
        walker.walk(new blockListener(rewriter), tree);
        File output = new File("output.java");
        output.createNewFile();
        FileWriter writer2 = new FileWriter("output.java");
        writer2.write(rewriter.getText());
        writer2.close();
        ////////////////////////////////////////////////////////////////////////////////////////

        //run the output file to produce the text file
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("java output.java");
        try {
            pr.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //-------------------------------------------------------------------------------------------------------------
            //html file part
          // in html we take the intermediate code as our input since it doesn't have the blocks visited part not
        //any extra code

        inputFile = "intermediate.java";
        inputStream = new FileInputStream(inputFile);
        input = new ANTLRInputStream(inputStream);
        lexer = new JavaLexer(input);
        tokens = new CommonTokenStream(lexer);
        parser = new JavaParser(tokens);
        tree = parser.compilationUnit();
        walker = new ParseTreeWalker();
        rewriter = new TokenStreamRewriter(tokens);
        //swap > < with "&lt and &gt" in html file
        for (int i = 0; i < tokens.getTokens().size(); i++) {
            Token token = tokens.getTokens().get(i);
            if (token.getText().equals( "<")) {
                rewriter.replace(token, "&lt;");
            } else if (token.getText().equals(">")) {
                rewriter.replace(token, "&gt;");
            }    else if (token.getText().equals(">=")) {
                rewriter.replace(token, "&gt;=");
            }
            else if (token.getText().equals("<=")) {
                rewriter.replace(token, "&lt;=");
        }}
        walker.walk(new htmListener(rewriter), tree);
        File html = new File("finalOutput.html");
        html.createNewFile();
        FileWriter writer3 = new FileWriter("finalOutput.html");
        writer3.write(rewriter.getText());
        writer3.close();
        File htmlFile = new File("finalOutput.html");
        // open the html file automatically
        Desktop.getDesktop().browse(htmlFile.toURI());



    }
    
}
