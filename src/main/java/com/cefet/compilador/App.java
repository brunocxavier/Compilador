package com.cefet.compilador;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Passe o caminho do programa como parametro");
            return;
        }
        try {
            Lexer lexer = new Lexer(args[0]);
            TableOfSymbols actualTable = new TableOfSymbols();
            int level = 0;

            System.out.print("Tokens");
            for (int i = 0;; i++) {
                Token token = lexer.scan();
                if (token.tag == Tag.EOF) {
                    break;
                } else if (token instanceof Word) {
                    /*TODO Se for um novo escopo deve se criar uma nova tabela de simbolos pra esse nivel
                    *  se for o fim de um escopo deve mudar a tabela atual para o pai da atual*/
                    if (token.tag == Tag.ID) {
                        Word word = (Word) token;
                        if (!actualTable.exists(word.getLexeme())) {
                            actualTable.add(new Symbol(word.getLexeme(), level, ""));
                        }
                    }
                }
                if (i % 10 == 0){//Teste de linha para melhorar a visibilidade do codigo e facilitar o print
                    System.out.println();
                }
                System.out.print(token + " ");
            }

            System.out.println();
            System.out.println("Tabela de simbolos");
            for (Symbol symbol : actualTable.getTable()) {
                System.out.println(symbol);
            }
            System.out.println();
            System.out.println("Sucesso");
        } catch (Exception e) {
            System.out.println();
            e.printStackTrace();
        }

    }
}
