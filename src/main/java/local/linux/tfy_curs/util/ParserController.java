package local.linux.tfy_curs.util;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import local.linux.tfy_curs.ParserApplication;
import local.linux.tfy_curs.model.Token;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class ParserController {
    private ParserApplication parserApplication;
    LexemeParser lexemeParser = new LexemeParser();
    Token token = new Token();
    @FXML
    TextArea textAreaSourceCode;
    @FXML
    TextArea textAreaResult;
    @FXML
    TextArea textAreaLexems;
    @FXML
    TextArea textAreaTokens;

    @FXML
    protected void onStartButtonClick() {
        textAreaTokens.clear();
        textAreaLexems.clear();
        lexemeParser.lexemeAnalysis(textAreaSourceCode, textAreaLexems);
        List<Token> tokens = token.createTokens(textAreaTokens,lexemeParser);
        runParser(tokens);
        tokens.clear();
        lexemeParser.clearLexemes();
    }

    @FXML
    protected void onDownloadButtonClick() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(new Stage());
        Path filePath = file.toPath();
        String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        textAreaSourceCode.setText(content);

    }

    public  void runParser(List<Token> tokens){
        textAreaResult.clear();
        SyntaxParser parser = new SyntaxParser(tokens);
        Boolean bool = parser.run();
        if (bool){
            textAreaResult.appendText("The code matches the grammar");
        }else {
            textAreaResult.appendText("The code no matches the grammar\n");
            textAreaResult.appendText(parser.getErrors().get(0));
        }
    }
}
