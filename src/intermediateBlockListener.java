import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.TerminalNode;
public class intermediateBlockListener extends JavaParserBaseListener{
    TokenStreamRewriter rewriter;

    public intermediateBlockListener(TokenStreamRewriter rewriter) {
        this.rewriter = rewriter;

    }

    public void enterStatement(JavaParser.StatementContext ctx) {
        if(ctx.FOR() != null  || ctx.WHILE() != null || ctx.IF() != null)
        {
            int statementIndex = 0;
            if(!ctx.statement(statementIndex).getStart().getText().equals("{"))
            {
                rewriter.insertBefore(ctx.statement(statementIndex).getStart(), "{\n\t    ");
                rewriter.insertAfter(ctx.statement(statementIndex).getStop(), "\n\t}\n");
            }
        }

        if(ctx.ELSE() != null)
        {
            int statementIndex = 1;
            if(!ctx.statement(statementIndex).getStart().getText().equals("if"))
            {
                if(!ctx.statement(statementIndex).getStart().getText().equals("{"))
                {
                    rewriter.insertBefore(ctx.statement(statementIndex).getStart(), "{\n\t    ");
                    rewriter.insertAfter(ctx.statement(statementIndex).getStop(), "\n\t}\n");
                }
            }
        }
    }
}