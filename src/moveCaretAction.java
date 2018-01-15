import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class moveCaretAction extends AnAction {
    int count = 0;
    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        System.out.println("carat moved: " + count++);
        // if we can't find a way to schedule an event or get around using event to find the cursor position we will use this
    }
}
