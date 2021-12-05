import exceptions.CharWithSizeGraterThanOneException;
import exceptions.InvalidFloatException;
import exceptions.UnclosedCharException;
import exceptions.UnclosedLiteralException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
    public static int line = 1; //contador de linhas
    private char ch = ' '; //caractere lido do arquivo
    private FileReader file;
    public Hashtable<String, Word> words = new Hashtable<>();

    /* Método para inserir palavras reservadas na HashTable */
    private void reserve(Word w){
        words.put(w.getLexeme(), w); // lexema é a chave para entrada na HashTable
    }

    public Lexer(String fileName) throws FileNotFoundException{
        try{
            file = new FileReader (fileName);
        }
        catch(FileNotFoundException e){
            System.out.println("Arquivo não encontrado");
            throw e;
        }

        //Insere palavras reservadas na HashTable
        reserveWords();
    }

    private void reserveWords () {
        reserve(new Word ("if", Tag.IF));
        reserve(new Word ("program", Tag.PRG));
        reserve(new Word ("begin", Tag.BEG));
        reserve(new Word ("end", Tag.END));
        reserve(new Word ("int", Tag.INT));
        reserve(new Word("is", Tag.IS));
        reserve(new Word("char", Tag.CHAR));
        reserve(new Word("float", Tag.FLOAT));
        reserve(new Word("then", Tag.THEN));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("repeat", Tag.REPEAT));
        reserve(new Word("until", Tag.UNTIL));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("read", Tag.READ));
        reserve(new Word("write", Tag.WRITE));
    }

    /*Lê o próximo caractere do arquivo*/
    private void readch() throws IOException {
        ch = (char) file.read();
    }

    /* Lê o próximo caractere do arquivo e verifica se é igual a c*/
    private boolean readch(char c) throws IOException{
        readch();
        if (ch != c) return false;
        ch = ' ';
        return true;
    }


    public Token scan() throws Exception {
        //Desconsidera delimitadores na entrada
        for (; ; readch()) {
            if (ch != ' ' && ch != '\t' && ch != '\r' && ch != '\b') {
                if (ch == '\n') {
                    line++; //conta linhas
                } else {
                    break;
                }
            }
        }

        //Comentario {
        if (ch == '/' && readch('*')) {
            do {
                readch();
            } while (ch != '*' || !readch('/'));
        }

        switch (ch) {
            //Operadores
            case '&':
                if (readch('&')) {
                    return Word.and;
                } else {
                    return new Token('&');
                }
            case '|':
                if (readch('|')) {
                    return Word.or;
                } else {
                    return new Token('|');
                }
            case '=':
                if (readch('=')) {
                    return Word.eq;
                } else {
                    return new Token('=');
                }
            case '<':
                if (readch('=')) {
                    return Word.le;
                } else {
                    return new Token('<');
                }
            case '>':
                if (readch('=')) {
                    return Word.ge;
                } else {
                    return new Token('>');
                }
            case '!':
                if (readch('=')) {
                    return Word.ne;
                } else {
                    return new Token('!');
                }
        }

        //Números
        if (Character.isDigit(ch)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(ch, 10);
                readch();
            } while (Character.isDigit(ch));
            if (ch == '.') {
                StringBuilder stringValueBd = new StringBuilder("" + value);
                int size = 0;
                do {
                    stringValueBd.append(ch);
                    readch();
                    size ++;
                } while (Character.isDigit(ch));
                if (size < 2) {
                    throw new InvalidFloatException("Invalid float value at line " + line);
                }
                return new NumFloat(stringValueBd.toString());
            }
            return new NumInt(value);
        }

        //Identificadores
        if (Character.isLetter(ch)) {
            StringBuilder sb = new StringBuilder();
            do {
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch) || '_' == ch);
            String s = sb.toString();
            Word w = words.get(s);
            if (w != null) {
                return w;
            }
            w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }

        //literal
        if (ch == '{') {
            StringBuilder sb = new StringBuilder();
            do {
                if (ch == Tag.EOF) {
                    throw new UnclosedLiteralException("Unclosed literal at line " + line);
                }
                sb.append(ch);
                readch();
            } while (ch != '}');
            sb.append(ch);
            ch = ' ';
            String s = sb.toString();
            return new Word(s, Tag.LITERAL);
        }

        //char
        if (ch == '\'') {
            StringBuilder sb = new StringBuilder();
            int size = 0;
            do {
                if (ch == Tag.EOF) {
                    throw new UnclosedCharException("Unclosed char at line " + line);
                }
                if (size > 1) {
                    throw new CharWithSizeGraterThanOneException("Invalid char at line " + line);
                }
                sb.append(ch);
                readch();
                size ++;
            } while (ch != '\'');
            sb.append(ch);
            ch = ' ';
            String s = sb.toString();
            return new Word(s, Tag.CHAR);
        }

        Token t = new Token(ch);
        ch = ' ';
        return t;
    }
}
