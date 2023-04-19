import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.File;
import java.io.FileWriter;

public class blockListener extends JavaParserBaseListener {
    int i;
    TokenStreamRewriter rewriter;
    int c=0;
    boolean exp=false;
    public blockListener(TokenStreamRewriter rewriter) {
        this.rewriter = rewriter;
        this.i = 0;
    }



    @Override
    public void enterCompilationUnit(JavaParser.CompilationUnitContext ctx) {
        //add the necessary imports
        rewriter.insertBefore(ctx.getStart(), "import java.io.File;" + "\n");
        rewriter.insertBefore(ctx.getStart(), "import java.io.FileWriter;" + "\n");
        rewriter.insertBefore(ctx.getStart(), "import java.io.IOException;" + "\n");
    }

    @Override public void enterClassBody(JavaParser.ClassBodyContext ctx) {
        //add two functions to call each time i enter a block or an expression
        //check() is to write to visitexpr file that have all the visited expressions
        rewriter.insertAfter(ctx.getStart(),"public static boolean check(int numexp) {\n" +
                "\t"  +   " try{FileWriter myWriter = new FileWriter(\"visitexpr.txt\",true);\n" +
                "\t"  +"  myWriter.write(\"exp\"+numexp+\"is visited\\n\");\n" +
                "\t"  +" myWriter.close();\n}" +
                "\t"  +" catch(Exception e){}\n"+
                "\t"  +" return false;\n" +
                "\t"  +" }\n");
      // visit is to write to output.txt that have all the visited blocks
        rewriter.insertAfter(ctx.getStart(),"public static void visit(int blocknum) {\n" +

                "\t"  +   " try{FileWriter w = new FileWriter(\"output.txt\",true);\n" +
                "\t"  +" w.write(\"block \"+blocknum+\" is Visited\\n\");\n" +
                "\t"  +" w.close();\n}" +
                "\t"  +" catch(Exception e){}\n"+
                "\t"  +" }\n");

    }

    @Override public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        // create the two files only once when entering main so that you don't overwrite them if you used another method
        // also to delete the content of the files if they already exist
        if (ctx.identifier().getText().equals("main")){

            rewriter.insertBefore(ctx.methodBody().getStart(), "throws IOException ");
            rewriter.insertAfter(ctx.methodBody().getStart(), "\n" + "\t" + "File output = new File(\"output.txt\");" + "\n");
            rewriter.insertAfter(ctx.methodBody().getStart(), "\n" + "\t" + "if (output.exists()) {\n" +
                    "    output.delete();\n" +
                    "}" + "\n");
            rewriter.insertAfter(ctx.methodBody().getStart(), "\t" + "output.createNewFile();" + "\n");
            rewriter.insertAfter(ctx.methodBody().getStart(), "\n" + "\t" + "File visitor = new File(\"visitexpr.txt\");" + "\n");
            rewriter.insertAfter(ctx.methodBody().getStart(), "\n" + "\t" + "if (visitor.exists()) {\n" +
                    "    visitor.delete();\n" +
                    "}" + "\n");
            rewriter.insertAfter(ctx.methodBody().getStart(), "\t" + "visitor.createNewFile();" + "\n");
        }
    }
    @Override
    public void enterBlock(JavaParser.BlockContext ctx) {
        //prints a call to function in the code
        i++;
        rewriter.insertAfter(ctx.getStart(),"visit("+i+");"+"\n");

    }



    @Override public void visitTerminal(TerminalNode node) {
        //check if the terminals are OR or AND
        if(node.getText().equals("||")||node.getText().equals("&&")){exp=true;}
    }


    @Override
    public void enterParExpression(JavaParser.ParExpressionContext ctx) {exp=true;}

    @Override
    public void enterExpression(JavaParser.ExpressionContext ctx) {

        if(exp&&ctx.AND()==null&&ctx.OR()==null){//exp true and does not contain and ,or

            c++;

            rewriter.insertBefore(ctx.getStart(),"(check("+c+")||");// write number of expr visited

            rewriter.insertAfter(ctx.getStop(),")");

            exp=false;

            if(ctx.getText().charAt(0)=='(')exp=true;
        }
    }

}
