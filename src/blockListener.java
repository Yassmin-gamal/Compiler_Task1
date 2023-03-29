import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.IOException;

public class blockListener extends JavaParserBaseListener{
    int i;
    TokenStreamRewriter rewriter;
    public blockListener(TokenStreamRewriter rewriter){
        this.rewriter = rewriter;
        this.i=0;
    }
     @Override public void enterCompilationUnit(JavaParser.CompilationUnitContext ctx) {
        rewriter.insertBefore(ctx.getStart(),"import java.io.*; " + "\n");
        rewriter.insertBefore(ctx.getStart(),"import java.io.IOException; " + "\n");
        rewriter.insertBefore(ctx.getStart(),"import java.io.FileWriter; " + "\n");
        rewriter.insertBefore(ctx.getStart(),"import java.util.Scanner;   " + "\n");


    }

    @Override public void enterMethodBody(JavaParser.MethodBodyContext ctx) {
        rewriter.insertBefore(ctx.getStart(),"throws IOException ");
        rewriter.insertAfter(ctx.getStart(),"\n"+"\t"+"File output = new File(\"output.txt\");"+"\n");
        rewriter.insertAfter(ctx.getStart(),"\t"+"output.createNewFile();"+"\n");
        rewriter.insertAfter(ctx.getStart(),"\t"+"FileWriter w = new FileWriter(\"output.txt\");"+"\n");
        rewriter.insertBefore(ctx.getStop(),"w.close();"+"\n");

    }

    @Override public void enterBlock(JavaParser.BlockContext ctx) {
        this.i++;
        rewriter.insertAfter(ctx.getStart()," //block"+ this.i +"\n");

        rewriter.insertAfter(ctx.getStart(),"\t"+"w.write(\"block "+ this.i + " is visited\" +\"\\n\");"  +"\n");



    }
    @Override public void exitBlock(JavaParser.BlockContext ctx) {

    }

}
