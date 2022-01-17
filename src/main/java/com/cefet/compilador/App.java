package com.cefet.compilador;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Passe o caminho do programa como parametro");
            return;
        }
        try {
            Lexer lexer = new Lexer(args[0]);
            Parser parser = new Parser(lexer);
            parser.run();
            System.out.println("Sucesso");
        } catch (Exception e) {
            System.out.println();
            e.printStackTrace();
        }

    }
}
