import java.io.*;
import java.util.Scanner;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.TerminalNode;

public class htmListener extends JavaParserBaseListener {//color all green and not visited red
    int i;
    int c = 0, end = 0;
    boolean exp = false;
    TokenStreamRewriter rewriter;
    public htmListener(TokenStreamRewriter rewriter)  {
        this.rewriter = rewriter;
        this.i = 0;
    }

    @Override
    public void enterBlock(JavaParser.BlockContext ctx) {
        this.i++;
        boolean f=false;
        // Open the file
        File file = new File("output.txt");
        Scanner scanner = null;
        try {// read from file
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("block "+this.i+" is Visited")) {
                f = true;

            }

        }
        scanner.reset();
        scanner.close();

        System.out.println(f);
        if(!f){//if block not found in txt file color it red
            rewriter.insertBefore(ctx.getStart(),"<pre style=\"background-color:#C71F1F;\">");
            rewriter.insertAfter(ctx.getStop(),"</pre>\n");
        }
    }

    @Override
    public void enterCompilationUnit(JavaParser.CompilationUnitContext ctx) {//adding html tags and clor all body with green
        rewriter.insertBefore(ctx.getStart(),"<pre>\n");
        rewriter.insertBefore(ctx.getStart(),"<body style=\"background-color:#2B9217;\">");//color block green
        rewriter.insertBefore(ctx.getStart(),"</head>\n");
        rewriter.insertBefore(ctx.getStart(),"<head>\n");
        rewriter.insertBefore(ctx.getStart(),"<html>\n");
        rewriter.insertAfter(ctx.getStop(),"</pre>\n");
        rewriter.insertAfter(ctx.getStop(),"</body>\n");
        rewriter.insertAfter(ctx.getStop(),"</html>\n");
    }
    @Override
    public void exitCompilationUnit(JavaParser.CompilationUnitContext ctx) {
        rewriter.insertAfter(ctx.getStop(), "</pre>\n");
        rewriter.insertAfter(ctx.getStop(), "</body>\n");
        rewriter.insertAfter(ctx.getStop(), "</html>\n");
    }
    
    @Override
    public void visitTerminal(TerminalNode node) {
    if (node.getText().equals("||") || node.getText().equals("&&")) {
            exp = true;
        }
    }
    
    @Override
    public void enterParExpression(JavaParser.ParExpressionContext ctx) {
        exp = true;
        end = c + 1;

    }

    @Override
    public void exitParExpression(JavaParser.ParExpressionContext ctx) {
        boolean reach = true;
        boolean found = false;
        for (int i = end; i <= c; i++) {
            //System.out.println(i);
            String s ="exp"+i+"is visited";
            //System.out.println(s);
            try {
                File myObj = new File("visitexpr.txt");
                Scanner myReader = new Scanner(myObj);
                boolean current_exp = false;
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    if (data.equals(s)) {
                        current_exp = true;
                    }
                }
                if (!current_exp) reach = false;
                else found = true;
                myReader.close();
            } catch (FileNotFoundException e) {
            }
        }
        if (!reach && found) {
            rewriter.insertBefore(ctx.getStart(), "<span style=\"background-color:orange;\">");
            rewriter.insertAfter(ctx.getStop(), "</span>");
        }
    }
    
    @Override
    public void enterExpression(JavaParser.ExpressionContext ctx) {
        if (exp && ctx.AND() == null && ctx.OR() == null) {
            c++;
            exp = false;
        }
    }

}
