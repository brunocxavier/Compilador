import java.io.IOException;

public class App {
    private final static int EOF = 65535;
    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer("test2.txt");
            TableSymbols table = new TableSymbols();
            int level = 0; //TODO aumentar o nivel quando entrar em um bloco e diminuir quando sair, deve ser feito pelo analisador sintatico

            System.out.println("Tokens");
            for (;;) {
                Token token = lexer.scan();
                if (token.tag == EOF) {
                    break;
                } else if (token instanceof Word && token.tag != Tag.LITERAL && token.tag != Tag.CHAR) { //TODO passar pro analixador sintatico
                    Word word = (Word) token;
                    if (!table.exists(word.getLexeme(), level)) {
                        table.add(new Item(word.getLexeme(), level, ""));
                    }
                }
                System.out.println(token);
            }

            System.out.println("//////////////");
            System.out.println("Tabela de simbolos");
            System.out.println(table.getTable());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
