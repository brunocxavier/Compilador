package com.cefet.compilador;

import com.cefet.compilador.enums.ParserState;
import com.cefet.compilador.enums.Type;
import com.cefet.compilador.exception.InvalidTokenException;
import com.cefet.compilador.exception.InvalidTypeException;
import com.cefet.compilador.exception.RepeatedDeclarationException;
import com.cefet.compilador.exception.VarNotDeclaredException;

import java.util.ArrayList;
import java.util.List;

import static com.cefet.compilador.Tag.*;
import static com.cefet.compilador.enums.ParserState.*;

public class Parser {
    private Token tok;
    private final Lexer lexer;
    private final TableOfSymbols actualTable = new TableOfSymbols();
    private List<Word> declarations = new ArrayList<>();
    private ParserState state = INITIAL;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void run() throws Exception {
        tok = lexer.scan();
        program();
    }

    private void program() throws Exception {
        if (tok.getTag() == PRG) {
            eat(PRG);
            eat(ID);
            state = OTHER;
            fillTableOfSymbols(Type.TYPE_VOID);//adiciona o nome do programa na tabela de simbolos
            eat(BEG);
            decl_list();
            stmt_list();
            eat(END);
            eat('.');
        } else {
            error();
        }
    }

    private void decl_list() throws Exception {
        if (tok.getTag() == ID) {
            state = DECLARATION;
            decl();
            eat(';');
            decl_list_prime();
            state = OTHER;
        } else {
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
        if (tok.getTag() == ID) {
            ident_list();
            eat(IS);
            Type varType = type();
            fillTableOfSymbols(varType);
        } else {
            error();
        }
    }

    private void ident_list() throws Exception {
        if (tok.getTag() == ID) {
            eat(ID);
            ident_list_prime();
        } else {
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

    private Type type() throws Exception {
        switch (tok.getTag()) {
            case INT_TYPE:
                eat(INT_TYPE);
                return Type.TYPE_INT;
            case FLOAT_TYPE:
                eat(FLOAT_TYPE);
                return Type.TYPE_FLOAT;
            case CHAR_TYPE:
                eat(CHAR_TYPE);
                return Type.TYPE_CHAR;
            default:
                error();
                return Type.TYPE_ERROR;
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
        if (tok.getTag() == ID) {
            Type type1 = eat(ID);
            eat('=');
            Type type2 = simple_expr();
            checkAssignType(type1, type2);
        } else {
            error();
        }
    }

    private void if_stmt() throws Exception {
        if (tok.getTag() == IF) {
            eat(IF);
            condition();
            eat(THEN);
            stmt_list();
            if_stmt_prime();
        } else {
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
            case '(':
                expression();
                break;
            default:
                error();
        }
    }

    private void repeat_stmt() throws Exception {
        if (tok.getTag() == REPEAT) {
            eat(REPEAT);
            stmt_list();
            stmt_suffix();
        } else {
            error();
        }
    }

    private void stmt_suffix() throws Exception {
        if (tok.getTag() == UNTIL) {
            eat(UNTIL);
            condition();
        } else {
            error();
        }
    }

    private void while_stmt() throws Exception {
        if (tok.getTag() == WHILE) {
            stmt_prefix();
            stmt_list();
            eat(END);
        } else {
            error();
        }
    }

    private void stmt_prefix() throws Exception {
        if (tok.getTag() == WHILE) {
            eat(WHILE);
            condition();
            eat(DO);
        } else {
            error();
        }
    }

    private void read_stmt() throws Exception {
        if (tok.getTag() == READ) {
            eat(READ);
            eat('(');
            eat(ID);
            eat(')');
        } else {
            error();
        }
    }

    private void write_stmt() throws Exception {
        if (tok.getTag() == WRITE) {
            eat(WRITE);
            eat('(');
            writable();
            eat(')');
        } else {
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

    private Type expression() throws Exception {
        switch (tok.getTag()) {
            case ID:
            case INT:
            case FLOAT:
            case CHAR:
            case '!':
            case '-':
            case '(':
                Type type1 = simple_expr();
                Type type2 = expression_prime();
                return checkType(type1, type2);
            default:
                error();
                return Type.TYPE_ERROR;
        }
    }

    private Type expression_prime() throws Exception {
        Type type;
        switch (tok.getTag()) {
            case EQ:
            case '>':
            case GE:
            case '<':
            case LE:
            case NE:
                relop();
                type = simple_expr();
                return type;
            case ')':
            case THEN:
            case DO:
            case END:
            case ELSE:
            case UNTIL:
            case ';':
                return Type.TYPE_VOID;
            default:
                error();
                return Type.TYPE_ERROR;
        }
    }

    private Type simple_expr() throws Exception {
        switch (tok.getTag()) {
            case ID:
            case INT:
            case FLOAT:
            case CHAR:
            case '!':
            case '-':
            case '(':
               Type type1 = term();
               Type type2 = simple_expr_prime();
               return checkType(type1, type2);
            default:
                error();
                return Type.TYPE_ERROR;
        }
    }

    private Type simple_expr_prime() throws Exception {
        Type type;
        switch (tok.getTag()) {
            case '+':
            case '-':
            case OR:
                addop();
                type = simple_expr();
                return type;
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
                return Type.TYPE_VOID;
            default:
                error();
                return Type.TYPE_ERROR;
        }
    }

    private Type term() throws Exception {
        switch (tok.getTag()) {
            case ID:
            case INT:
            case FLOAT:
            case CHAR:
            case '!':
            case '-':
            case '(':
                Type type1 = factor_a();
                Type type2 = term_prime();
                return checkOpType(type1, type2);
            default:
                error();
                return Type.TYPE_ERROR;
        }
    }

    private Type term_prime() throws Exception {
        Type type;
        switch (tok.getTag()) {
            case '*':
            case AND:
                mulop();
                type = term();
                return type;
            case '/':
                mulop();
                term();
                return Type.TYPE_FLOAT;
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
                return Type.TYPE_VOID;
            default:
                error();
        }
        return Type.TYPE_ERROR;
    }

    private Type factor_a() throws Exception {
        Type type;
        switch (tok.getTag()) {
            case ID:
            case INT:
            case FLOAT:
            case CHAR:
            case '(':
                type = factor();
                return type;
            case '!':
                eat('!');
                type = factor();
                return type;
            case '-':
                eat('-');
                type = factor();
                return checkTypeIsNumber(type);
            default:
                error();
        }
        return Type.TYPE_ERROR;
    }

    private Type factor() throws Exception {
        Type type;
        switch (tok.getTag()) {
            case ID:
                type = eat(ID);
                return type;
            case '(':
                eat('(');
                type = expression();
                eat(')');
                return type;
            case INT:
            case FLOAT:
            case CHAR:
                type = constant();
                return type;
            default:
                error();
                return Type.TYPE_ERROR;
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

    private Type constant() throws Exception {
        switch (tok.getTag()) {
            case INT:
                eat(INT);
                return Type.TYPE_INT;
            case FLOAT:
                eat(FLOAT);
                return Type.TYPE_FLOAT;
            case CHAR:
                eat(CHAR);
                return Type.TYPE_CHAR;
            default:
                error();
                return Type.TYPE_ERROR;
        }
    }

    private void advance() throws Exception {
        this.tok = this.lexer.scan();
    }

    private Type eat(int t) throws Exception {
        if (tok.getTag() == t) {
            if (t == ID) {
                return eatId();
            } else {
                advance();
                return Type.TYPE_VOID;
            }
        } else {
            error();
            return Type.TYPE_ERROR;
        }
    }

    private Type eatId() throws Exception {
        Type type;
        if (state.equals(OTHER)) {
            type = checkVarIsDeclared();
        } else {
            checkVarAlreadyOnTableAndAddToNewVarsList();
            type = Type.TYPE_VOID;
        }
        advance();
        return type;
    }

    private void checkVarAlreadyOnTableAndAddToNewVarsList() throws RepeatedDeclarationException {
        if (tok instanceof Word) {
            Word word = (Word) tok;
            if (!actualTable.exists(word.getLexeme())) {
                declarations.add(word);
            } else {
                throw new RepeatedDeclarationException("Duplicate variable declaration on line " + this.lexer.getLine() + " var: " + word.getLexeme());
            }
        }
    }

    private Type checkVarIsDeclared() throws VarNotDeclaredException {
        Type type = Type.TYPE_ERROR;
        if (tok instanceof Word) {
            Word word = (Word) tok;
            if (!actualTable.exists(word.getLexeme())) {
                throw new VarNotDeclaredException(word.getLexeme() + " is not declared and is used on line " + this.lexer.getLine());
            }
            type = actualTable.getSymbol(word.getLexeme()).getType();
        }
        return type;
    }

    private void fillTableOfSymbols(Type type) {
        for (Word word : declarations) {
            actualTable.add(new Symbol(word.getLexeme(), 0, "", type));
        }
        declarations = new ArrayList<>();
    }

    private void error() throws InvalidTokenException {
        throw new InvalidTokenException("invalid token " + tok + " on line " + this.lexer.getLine());
    }

    private void typeError() throws InvalidTypeException {
        throw new InvalidTypeException("invalid type on line " + this.lexer.getLine());
    }

    private Type checkType(Type type1, Type type2) {
        if (type1.equals(type2)) {
            return type1;
        }
        if (type1.equals(Type.TYPE_VOID)) {
            return type2;
        }
        if (type2.equals(Type.TYPE_VOID)) {
            return type1;
        }
        if ((type1.equals(Type.TYPE_INT) && type2.equals(Type.TYPE_FLOAT)) || (type2.equals(Type.TYPE_INT) && type1.equals(Type.TYPE_FLOAT))) {
            return Type.TYPE_FLOAT;
        }
        return Type.TYPE_ERROR;
    }

    private Type checkTypeIsNumber(Type type) {
        return (type.equals(Type.TYPE_INT) || type.equals(Type.TYPE_FLOAT)) ? type : Type.TYPE_ERROR;
    }

    private void checkAssignType(Type type1, Type type2) throws InvalidTypeException {
        if (Type.TYPE_FLOAT.equals(type1)) {
             if (!Type.TYPE_FLOAT.equals(type2) && !Type.TYPE_INT.equals(type2)) {
                   typeError();
             }
        } else if (!type1.equals(type2)) {
            typeError();
        }
    }

    private Type checkOpType(Type type1, Type type2) {
        if (type1.equals(Type.TYPE_INT) && type2.equals(Type.TYPE_INT)) {
            return Type.TYPE_INT;
        }
        if (type1.equals(Type.TYPE_FLOAT) && type2.equals(Type.TYPE_INT)) {
            return Type.TYPE_FLOAT;
        }
        if (type1.equals(Type.TYPE_INT) && type2.equals(Type.TYPE_FLOAT)) {
            return Type.TYPE_FLOAT;
        }
        if (type1.equals(Type.TYPE_FLOAT) && type2.equals(Type.TYPE_FLOAT)) {
            return Type.TYPE_FLOAT;
        }
        if (type1.equals(Type.TYPE_VOID)) {
            return type2;
        }
        if (type2.equals(Type.TYPE_VOID)) {
            return type1;
        }
        return Type.TYPE_ERROR;
    }

}
