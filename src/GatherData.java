import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

/*
This class defines the action that happens when the sidebar button is clicked.
Action classes are the basis for how IntelliJ plugin development work, and each
action extends the AnAction Interface.
 */

public class GatherData extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        /* Here is where the main code lies for what happens on a click.
        The AnActionEvent class gives you all you need about the project. */
        Project project = event.getProject();
        PsiClass psiClass = getPsiClassFromContent(event);
        System.out.println("CLICK");
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Run");
        System.out.println();
        // ^ create a debug point here and check out the information, see if you can pinpoint some data to read.
        // don't worry if you can't understand any of the variables, there are utility classes we need to find
        // that can extract the information for us.

        // After we've made the Action we need to register the GatherData class in plugin.xml in resources
    }
    private PsiClass getPsiClassFromContent(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }

        // find offset for what is at the caret cursor
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);
        // it would help to know what a PSI tree is,
        // it's essentially a parser tree of what's
        // in IntelliJ, has some other cool features
        // that might be useful to know.
        PsiClass psiClass = PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
        // learn to use PsiTreeUtil, in the words of the webinar "very very useful for IJ plugin development"
        if (psiClass == null) {
            return null;
        }
        // up to here is what gets the dropdown option to show up
        return psiClass;
    }
}

//project.getMessageBus().connect(/some disposable/).subscribe(ExecutionManager.EXECUTION_TOPIC, new ExecutionListener() {
//@Override
//public void processStarted(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler) {
//        //here you can add listener to existing 'live' output console
//
//        handler.addProcessListener(new ProcessAdapter() {
//@Override
//public void onTextAvailable(ProcessEvent event, Key outputType) {
//        ProcessHandler processHandler = event.getProcessHandler();
//        if (outputType != ProcessOutputTypes.STDERR) return;
//            String text = event.getText();
//        if (text != null && text.toLowerCase().contains("error")) {
//            new Notification("error-message", processHandler.toString(), text, NotificationType.ERROR).notify(project);
//        }
//}
//        });
//        }
//        });
