import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer(args[0]);
            TableOfSymbols actualTable = new TableOfSymbols();
            int level = 0;

            System.out.println("Tokens");
            for (;;) {
                Token token = lexer.scan();
                if (token.tag == Tag.EOF) {
                    break;
                } else if (token instanceof Word) {
                    /*TODO Se o token for then deve se criar uma nova tabela de simbolos pra esse nivel
                    *  se for end deve se excluir a tabela atual*/
                    if (token.tag == Tag.ID) {//TODO passar pro analixador sintatico
                        Word word = (Word) token;
                        if (!actualTable.exists(word.getLexeme())) {
                            actualTable.add(new Item(word.getLexeme(), level, ""));
                        }
                    }
                }
                System.out.println(token);
            }

            System.out.println("//////////////");
            System.out.println("Tabela de simbolos");
            System.out.println(actualTable.getTable());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
