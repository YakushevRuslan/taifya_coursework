package local.linux.tfy_curs.model;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import local.linux.tfy_curs.util.LexemeParser;

import java.util.*;

public class Token {
    public Token(){}

    public enum TokenType {
        BEGIN, END, INTEGER, IDENTIFIER, NUMBER, COMMA, SEMICOLON, REAL, DOUBLE, TRUE, FALSE, POINT,
        EQUAL, IF, ELSE, PLUS, MINUS, MULT, DIV, LESS, EQUALS, COLON, MORE, ASSIGNMENT,THEN, VAR, ENTER
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private TokenType type;
    private String value;


    public Token(TokenType type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return String.format("%s, %s ", type, value);
    }


    static TokenType[] Delimiters = new TokenType[]{
            TokenType.PLUS, TokenType.MINUS, TokenType.MULT,
            TokenType.DIV, TokenType.EQUAL, TokenType.MORE,
            TokenType.LESS, TokenType.COMMA
    };


    public static boolean isDelimiter(Token token) {
        return Arrays.asList(Delimiters).contains(token.type);
    }


    public static Map<String, TokenType> SpecialWords = new HashMap<String, TokenType>() {{
        put("begin", TokenType.BEGIN);
        put("integer", TokenType.INTEGER);
        put("if", TokenType.IF);
        put("else", TokenType.ELSE);
        put("==", TokenType.EQUALS);
        put("real", TokenType.REAL);
        put("double", TokenType.DOUBLE);
        put(":=",TokenType.ASSIGNMENT);
        put("var", TokenType.VAR);
        put("then", TokenType.THEN);
        put("end", TokenType.END);
        put("true", TokenType.TRUE);
        put("false", TokenType.FALSE);
    }};


    public static boolean isSpecialWord(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        return SpecialWords.containsKey(word);
    }


    public static Map<Character, TokenType> SpecialSymbols = new HashMap<Character, TokenType>() {{
        put(';', TokenType.SEMICOLON);
        put(',', TokenType.COMMA);
        put('.', TokenType.POINT);
        put('+', TokenType.PLUS);
        put('=', TokenType.EQUAL);
        put('>', TokenType.MORE);
        put('-', TokenType.MINUS);
        put('*', TokenType.MULT);
        put('/', TokenType.DIV);
        put('<', TokenType.LESS);
        put(':', TokenType.COLON);
        put('#', TokenType.ENTER);
    }};

    public List<Token>  createTokens(TextArea textAreaTokens, LexemeParser lexemeParser) {
        List<Token> tokens = new ArrayList<>();
        textAreaTokens.clear();
        String lexem = "";
        String type = "";

        for (int i = 0; i < lexemeParser.getLexemes().size(); i++) {

            lexem = lexemeParser.getLexemes().get(i).split(" ")[0];
            type = lexemeParser.getLexemes().get(i).split(" ")[1];

            if (type.equals("I")) {       //Identificator
                if (Token.isSpecialWord(lexem)) {
                    Token token = new Token(Token.SpecialWords.get(lexem));
                    tokens.add(token);
                    textAreaTokens.appendText(token.getType().toString());
                    textAreaTokens.appendText("\n");
                    textAreaTokens.appendText("\n");
                } else {
                    Token token = new Token(Token.TokenType.IDENTIFIER);
                    token.setValue(lexem);
                    tokens.add(token);
                    textAreaTokens.appendText(token.toString());
                    textAreaTokens.appendText("\n");
                    textAreaTokens.appendText("\n");

                }
            } else if (type.equals("L")) {      //Literal
                Token token = new Token(Token.TokenType.NUMBER);
                token.setValue(lexem);
                tokens.add(token);
                textAreaTokens.appendText(token.toString());
                textAreaTokens.appendText("\n");
                textAreaTokens.appendText("\n");

            } else if (type.equals("D")) { // delimiter
                if(lexem.length() > 1 && lexem.charAt(0) == ':' && lexem.charAt(1) == '='){
                    Token token = new Token(Token.TokenType.ASSIGNMENT);
                    token.setValue(lexem);
                    tokens.add(token);
                    textAreaTokens.appendText(token.toString());
                    textAreaTokens.appendText("\n");
                    textAreaTokens.appendText("\n");
                }else {
                    Token token = new Token(Token.SpecialSymbols.get(lexem.charAt(0)));
                    token.setValue(lexem);
                    tokens.add(token);
                    textAreaTokens.appendText(token.toString());
                    textAreaTokens.appendText("\n");
                    textAreaTokens.appendText("\n");
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Предупреждение");
                alert.setHeaderText(String.format("Ошибка. При формировании токена для %s произошла ошибка.", type));
                alert.showAndWait();
            }
        }
        return  tokens;
    }

     public static boolean isSpecialSymbol(char ch) {
        return SpecialSymbols.containsKey(ch);
    }

}
