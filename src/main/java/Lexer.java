import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Hashtable;

public class Lexer {
    public static int line = 1; //contador de linhas
    private char ch = ' '; //caractere lido do arquivo
    private FileReader file;
    private Hashtable<String, Object> words = new Hashtable<>();

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
}
