import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<Token> tokens = new ArrayList<>();
        try {
            Lexer lexer = new Lexer("test2.txt");
            for (;;) {
                Token token = lexer.scan();
                if (token.tag == 65535) {
                    break;
                }
                tokens.add(token);
            }

            System.out.println("Tokens");
            for (Token token : tokens) {
                System.out.println(token);
            }
            System.out.println("//////////////");

            System.out.println("Tabela de simbolos");
            for (Word w:lexer.getWords().values()) {
                System.out.println(w);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
