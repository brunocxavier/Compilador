package com.cefet.compilador;

import static com.cefet.compilador.Tag.*;

public class Parser {
    private Token tok;
    private Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void run() throws Exception {
        tok = lexer.scan();
        program();
    }

    private void program() throws Exception {
        switch (tok.getTag()) {
            case PRG:
                eat(PRG);
                eat(ID);
                eat(BEG);
                decl_list();
                stmt_list();
                eat(END);
                eat('.');
                break;
            default:
                error();
        }
    }

    private void decl_list() throws Exception {
        switch (tok.getTag()) {
            case ID:
                decl();
                eat(';');
                decl_list_prime();
                break;
            default:
                error();
        }
    }

    private void decl_list_prime() throws Exception {
        switch (tok.getTag()) {
            case ID:
                decl_list();
                break;
            case IF:
            case REPEAT:
            case WHILE:
            case READ:
            case WRITE:
                break;
            default:
                error();
        }
    }

    private void decl() throws Exception {
        switch (tok.getTag()) {
            case ID:
                ident_list();
                eat(IS);
                type();
                break;
            default:
                error();
        }
    }

    private void ident_list() throws Exception {
        switch (tok.getTag()) {
            case ID:
                eat(ID);
                ident_list_prime();
                break;
            default:
                error();
        }
    }

    private void ident_list_prime() throws Exception {
        switch (tok.getTag()) {
            case ',':
                eat(',');
                eat(ID);
                ident_list_prime();
                break;
            case IS:
                break;
            default:
                error();
        }
    }

    private void type() throws Exception {
        switch (tok.getTag()) {
            case INT_TYPE:
                eat(INT_TYPE);
                break;
            case FLOAT_TYPE:
                eat(FLOAT_TYPE);
                break;
            case CHAR_TYPE:
                eat(CHAR_TYPE);
                break;
            default:
                error();
        }
    }

    private void stmt_list() throws Exception {
        switch (tok.getTag()) {
            case ID:
            case IF:
            case REPEAT:
            case WHILE:
            case READ:
            case WRITE:
                stmt();
                stmt_list_prime();
                break;
            default:
                error();
        }
    }

    private void stmt_list_prime() throws Exception {
        switch (tok.getTag()) {
            case ';':
                eat(';');
                stmt();
                stmt_list_prime();
                break;
            case END:
            case ELSE:
            case UNTIL:
                break;
            default:
                error();
        }
    }

    private void stmt() throws  Exception {
        switch (tok.getTag()) {
            case ID:
                assign_stmt();
                break;
            case IF:
                if_stmt();
                break;
            case WHILE:
                while_stmt();
                break;
            case REPEAT:
                repeat_stmt();
                break;
            case READ:
                read_stmt();
                break;
            case WRITE:
                write_stmt();
                break;
            default:
                error();
        }
    }

    private void assign_stmt() throws Exception {
        switch (tok.getTag()) {
            case ID:
                eat(ID);
                eat('=');
                simple_expr();
                break;
            default:
                error();
        }
    }

    private void if_stmt() throws Exception {
        switch (tok.getTag()) {
            case IF:
                eat(IF);
                condition();
                eat(THEN);
                stmt_list();
                if_stmt_prime();
                break;
            default:
                error();
        }
    }

    private void if_stmt_prime() throws Exception {
        switch (tok.getTag()) {
            case END:
                eat(END);
                break;
            case ELSE:
                eat(ELSE);
                stmt_list();
                eat(END);
                break;
            default:
                error();
        }
    }

    private void condition() throws Exception {
        switch (tok.getTag()) {
            case ID:
            case INT:
            case FLOAT:
            case CHAR:
            case '!':
            case '-':
                expression();
                break;
            default:
                error();
        }
    }

    private void repeat_stmt() throws Exception {
        switch (tok.getTag()) {
            case REPEAT:
                eat(REPEAT);
                stmt_list();
                stmt_suffix();
                break;
            default:
                error();
        }
    }

    private void stmt_suffix() throws Exception {
        switch (tok.getTag()) {
            case UNTIL:
                eat(UNTIL);
                condition();
                break;
            default:
                error();
        }
    }

    private void while_stmt() throws Exception {
        switch (tok.getTag()) {
            case WHILE:
                stmt_prefix();
                stmt_list();
                eat(END);
                break;
            default:
                error();
        }
    }

    private void stmt_prefix() throws Exception {
        switch (tok.getTag()) {
            case WHILE:
                eat(WHILE);
                condition();
                eat(DO);
                break;
            default:
                error();
        }
    }

    private void read_stmt() throws Exception {
        switch (tok.getTag()) {
            case READ:
                eat(READ);
                eat('(');
                eat(ID);
                eat(')');
                break;
            default:
                error();
        }
    }

    private void write_stmt() throws Exception {
        switch (tok.getTag()) {
            case WRITE:
                eat(WRITE);
                eat('(');
                writable();
                eat(')');
                break;
            default:
                error();
        }
    }

    private void writable() throws Exception {
        switch (tok.getTag()) {
            case LITERAL:
                eat(LITERAL);
                break;
            case ID:
            case INT:
            case FLOAT:
            case CHAR:
            case '!':
            case '-':
            case '(':
                simple_expr();
                break;
            default:
                error();
        }
    }

    private void expression() throws Exception {
        switch (tok.getTag()) {
            case ID:
            case INT:
            case FLOAT:
            case CHAR:
            case '!':
            case '-':
            case '(':
                simple_expr();
                expression_prime();
                break;
            default:
                error();
        }
    }

    private void expression_prime() throws Exception {
        switch (tok.getTag()) {
            case EQ:
            case '>':
            case GE:
            case '<':
            case LE:
            case NE:
                relop();
                simple_expr();
                break;
            case ')':
            case THEN:
            case DO:
            case END:
            case ELSE:
            case UNTIL:
            case ';':
                break;
            default:
                error();
        }
    }

    private void simple_expr() throws Exception {
        switch (tok.getTag()) {
            case ID:
            case INT:
            case FLOAT:
            case CHAR:
            case '!':
            case '-':
            case '(':
               term();
               simple_expr_prime();
               break;
            default:
                error();
        }
    }

    private void simple_expr_prime() throws Exception {
        switch (tok.getTag()) {
            case '+':
            case '-':
            case OR:
                addop();
                simple_expr();
                break;
            case EQ:
            case '>':
            case GE:
            case '<':
            case LE:
            case NE:
            case ')':
            case THEN:
            case DO:
            case END:
            case ELSE:
            case UNTIL:
            case ';':
                break;
            default:
                error();
        }
    }

    private void term() throws Exception {
        switch (tok.getTag()) {
            case ID:
            case INT:
            case FLOAT:
            case CHAR:
            case '!':
            case '-':
            case '(':
                factor_a();
                term_prime();
                break;
            default:
                error();
        }
    }

    private void term_prime() throws Exception {
        switch (tok.getTag()) {
            case '*':
            case '/':
            case AND:
                mulop();
                term();
                break;
            case '+':
            case '-':
            case OR:
            case EQ:
            case '>':
            case GE:
            case '<':
            case LE:
            case NE:
            case ')':
            case THEN:
            case DO:
            case END:
            case ELSE:
            case UNTIL:
            case ';':
                break;
            default:
                error();
        }
    }

    private void factor_a() throws Exception {
        switch (tok.getTag()) {
            case ID:
            case INT:
            case FLOAT:
            case CHAR:
            case '(':
                factor();
                break;
            case '!':
                eat('!');
                factor();
                break;
            case '-':
                eat('-');
                factor();
                break;
            default:
                error();
        }
    }

    private void factor() throws Exception {
        switch (tok.getTag()) {
            case ID:
                eat(ID);
                break;
            case '(':
                eat('(');
                expression();
                eat(')');
                break;
            case INT:
            case FLOAT:
            case CHAR:
                constant();
                break;
            default:
                error();
        }
    }

    private void relop() throws Exception {
        switch (tok.getTag()) {
            case EQ:
            case '>':
            case GE:
            case '<':
            case LE:
            case NE:
                eat(tok.getTag());
                break;
            default:
                error();
        }
    }

    private void addop() throws Exception {
        switch (tok.getTag()) {
            case '+':
            case '-':
            case OR:
                eat(tok.getTag());
                break;
            default:
                error();
        }
    }

    private void mulop() throws Exception {
        switch (tok.getTag()) {
            case '*':
            case '/':
            case AND:
                eat(tok.getTag());
                break;
            default:
                error();
        }
    }

    private void constant() throws Exception {
        switch (tok.getTag()) {
            case INT:
            case FLOAT:
            case CHAR:
                eat(tok.getTag());
                break;
            default:
                error();
        }
    }

    private void advance() throws Exception {
        this.tok = this.lexer.scan();
    }

    private void eat(int t) throws Exception {
        if (tok.getTag() == t) {
            advance();
        } else {
            error();
        }
    }

    private void error() throws Exception {
        throw new Exception("token invalido na linha" + this.lexer.getLine());
    }
}
