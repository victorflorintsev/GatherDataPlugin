import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;

public class CaretSystem {
    Project project;
    public CaretSystem(Project input_project) {
        project = input_project;
    }

    public String getTerms(AnActionEvent event) {
        final Editor editor = event.getRequiredData(LangDataKeys.EDITOR);
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();
        final int start = selectionModel.getSelectionStart();
        int lineNumber = document.getLineNumber(start);
        String line = getLine(document, lineNumber);

        return line;
    }

    private String getLine(Document document, int lineNumber) {
        document.getText();
        int startOffset = document.getLineStartOffset(lineNumber);
        int endOffset   = document.getLineEndOffset(lineNumber);
        // System.out.println("start: " + startOffset);
        // System.out.println("end: " + endOffset);
        TextRange tr = new TextRange(startOffset, endOffset);
        // System.out.println(document.getText(tr));

        // not this does not get rid of tabs yet
        return document.getText(tr);
    }

    // this is inefficient, ToDo: fix the fact that the cascade below gets called twice during runtime
    public int getLineNumber(AnActionEvent event) {
        final Editor editor = event.getRequiredData(LangDataKeys.EDITOR);
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();
        final int start = selectionModel.getSelectionStart();
        int lineNumber = document.getLineNumber(start);
        return lineNumber;
    }
}
