package local.linux.tfy_curs.util;

import local.linux.tfy_curs.model.Token;

import java.util.*;

import java.util.List;

public class SyntaxParser {
    private List<Token> tokens;
    private Token token;
    private int numberToken = -1;
    private boolean check = true;
    private String result = "";
    private ArrayList<String> errors = new ArrayList<>();

    public String getResult() {
        return result;
    }

    public List<String> getErrors() {
        return errors;
    }

    public SyntaxParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private void next() {
        if (numberToken + 1 < tokens.size()) {
            numberToken++;
            token = tokens.get(numberToken);
        }
    }

    private void reportError(String expected) {
        if (token.getValue() != null)
            errors.add("Ожидалось: " + expected + "\nПолучено: " + token.getValue());
        else
            errors.add("Ожидалось: " + expected + "\nПолучено: " + token.getType());
        check = false; // Устанавливаем check в false, если есть ошибка
    }


    public boolean run() {
        next();
        program();
        return check;
    }

    private void program() {
        if (token.getType() == Token.TokenType.VAR) {
            next();
            listDescription();
            if (token.getType() == Token.TokenType.BEGIN) {
                next();
                listOperation();
                if (token.getType() == Token.TokenType.END) {
                    next();
                    if (token.getType() != Token.TokenType.POINT) {
                        reportError("POINT");
                    }
                } else {
                    reportError("END");
                }
            } else {
                reportError("BEGIN");
            }
        } else {
            reportError("VAR");
        }
    }

    // Список описаний переменных
    private void listDescription() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            description();
            if (token.getType() == Token.TokenType.SEMICOLON) {
                next();
                altDescription();
            } else {
                reportError("SEMICOLON");
            }
        } else {
            reportError("IDENTIFIER");
        }
    }

    // Описание переменной
    private void description() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            listVariable();
            if (token.getType() == Token.TokenType.COLON) {
                next();
                type();
            } else {
                reportError("COLON");
            }
        } else {
            reportError("IDENTIFIER");
        }
    }

    // Список переменных
    private void listVariable() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            next();
            altListVariable();
        } else {
            reportError("IDENTIFIER");
        }
    }

    // Альтернативный список переменных
    private void altListVariable() {
        if (token.getType() == Token.TokenType.COMMA) {
            next();
            listVariable();
        } else if (token.getType() != Token.TokenType.COLON) {
            reportError("COLON or COMMA");
        }
    }

    // Альтернативное описание
    private void altDescription() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            listDescription();
        } else if (token.getType() != Token.TokenType.BEGIN) {
            reportError("BEGIN");
        }
    }

    // Тип переменной
    private void type() {
        if (token.getType() == Token.TokenType.INTEGER || token.getType() == Token.TokenType.REAL || token.getType() == Token.TokenType.DOUBLE) {
            next();
        } else {
            reportError("INTEGER or REAL or DOUBLE");
        }
    }

    // Список операций
    private void listOperation() {
        while (token.getType() == Token.TokenType.IDENTIFIER || token.getType() == Token.TokenType.IF) {
            operation();
            if (token.getType() == Token.TokenType.SEMICOLON) {
                next();
            } else if (token.getType() != Token.TokenType.END && token.getType() != Token.TokenType.ELSE) {
                reportError("SEMICOLON or END or ELSE");
                return;
            }
        }
    }

    // Операция
    private void operation() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            appropriation();
        } else if (token.getType() == Token.TokenType.IF) {
            comparison();
        } else {
            reportError("IDENTIFIER or IF");
        }
    }

    // Присваивание
    private void appropriation() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            next();
            if (token.getType() == Token.TokenType.ASSIGNMENT) {
                next();
                operand();
            } else {
                reportError("ASSIGNMENT");
            }
        } else {
            reportError("IDENTIFIER");
        }
    }

    // Условный оператор
    private void comparison() {
        if (token.getType() == Token.TokenType.IF) {
            next();
            logicalExpression();
            if (token.getType() == Token.TokenType.THEN) {
                next();
                if (token.getType() != Token.TokenType.ELSE && token.getType() != Token.TokenType.END) {
                    blockOperation();
                }
                altBlockOperation();
            } else {
                reportError("THEN");
            }
        } else {
            reportError("IF");
        }
    }

    // Альтернативная обработка блока
    private void altBlockOperation() {
        if (token.getType() == Token.TokenType.ELSE) {
            next();
            blockOperation();
        }
    }

    // Блок операций
    private void blockOperation() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            appropriation();
        } else if (token.getType() == Token.TokenType.BEGIN) {
            next();
            listOperation();
            if (token.getType() == Token.TokenType.END) {
                next();
            } else {
                reportError("END");
            }

        }
        else
            reportError("IDENTIFIER or BEGIN");
    }

    // Операнд
    private void operand() {
        if (token.getType() == Token.TokenType.IDENTIFIER || token.getType() == Token.TokenType.NUMBER) {
            next();
        } else {
            reportError("IDENTIFIER or NUMBER");
        }
    }

    // Логическое выражение
    private void logicalExpression() {
        operand();
        compare();
        operand();
    }

    // Сравнение
    private void compare() {
        if (token.getType() == Token.TokenType.MORE || token.getType() == Token.TokenType.LESS) {
            next();
        } else {
            reportError("MORE or LESS");
        }
    }
}

