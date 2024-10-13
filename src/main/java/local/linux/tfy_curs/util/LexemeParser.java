package local.linux.tfy_curs.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LexemeParser {
    List<String> lexemes = new ArrayList<>();

    public List<String> getLexemes(){
        return lexemes;
    }

    public void clearLexemes(){
        lexemes.clear();
    }

    public void lexemeAnalysis(TextArea textAreaSourceCode, TextArea textAreaLexems) {
        char[] data = textAreaSourceCode.getText().toCharArray();
        for (int i = 0; i < data.length; i++) {
            if (isLetters(data[i])) {
                StringBuilder letter = new StringBuilder();
                while (isLetters(data[i])) {
                    letter.append(data[i]);
                    i++;
                }

                textAreaLexems.appendText(String.format("%s - Идентификатор \n", letter));
                lexemes.add(String.format("%s I", letter));
                textAreaLexems.appendText("\n");

                if (letter.length() > 8) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Предупреждение");
                    alert.setHeaderText("Ошибка. Максимальная длина идентификатора превышает 8 символов.");
                    alert.showAndWait();
                }
            }

            if (isNumbers(data[i])) {
                StringBuilder number = new StringBuilder();
                while (isNumbers(data[i])) {
                    number.append(data[i]);
                    i++;
                }

                textAreaLexems.appendText(String.format("%s - Литерал \n", number));
                lexemes.add(String.format("%s L", number));
                textAreaLexems.appendText("\n");
            }

            if (isSeparator(data[i])) {
                if (data[i] == '\n')
                    continue;
                if (i < data.length + 1) {
                    if (data[i] == ':' && data[i + 1] == '=') {
                        textAreaLexems.appendText(String.format("%s%s - Разделитель \n", data[i], data[i + 1]));
                        lexemes.add(String.format("%s%s D", data[i], data[i + 1]));
                        textAreaLexems.appendText("\n");
                        i++;
                    } else {
                        textAreaLexems.appendText(String.format("%s - Разделитель \n", data[i]));
                        lexemes.add(String.format("%s D", data[i]));
                        textAreaLexems.appendText("\n");

                    }
                }

            } else {
                if (data[i] == ' ')
                    continue;
                char tmp = data[i];
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Предупреждение");
                alert.setHeaderText("Ошибка. Не удается распознать символ : " + tmp);
                alert.showAndWait();
            }
        }
    }
    private char[] separators = {',', '+', '-', '=', '/', '(', ')', '\n', ':', ';', '.', '>', '<'};

    public boolean isLetters(char symbol) {
        String letters = "" + symbol;
        return Pattern.matches("[a-zA-Z]+$", letters);
    }

    public boolean isNumbers(char symbol) {
        String letters = "" + symbol;
        return Pattern.matches("[0-9]+$", letters);
    }

    public boolean isSeparator(char symbol) {
        for (char sep : separators) {
            if (symbol == sep) {
                return true;
            }
        }
        return false;
    }
}
