import com.intellij.ide.errorTreeView.ErrorTreeElementKind;
import com.intellij.ide.errorTreeView.ErrorViewStructure;
import com.intellij.ide.errorTreeView.NewErrorTreeViewPanel;
import com.intellij.ide.errorTreeView.impl.ErrorViewTextExporter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.content.MessageView;
import myToolWindow.MyToolWindowFactory;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;


/*
This class defines the action that happens when the right click menu button is clicked.
Action classes are the basis for how IntelliJ plugin development works, and each
action extends the AnAction Interface.
 */



public class GatherData extends AnAction implements ApplicationComponent {
    enum IDEType {
        INTELLIJ, PYCHARM
    }

    // edit these booleans to only scan for specific types of messages from the current message system
    private boolean lookForErrors  = true;
    private boolean lookForWarning = false;
    private boolean lookForInfo    = false;
    private boolean lookForNote    = false;
    private boolean lookForGeneric = false;

    public void initComponent() {
        System.out.println("Project opened (GatherData)");
        // This is for short init code, if you need to execute something
        // that would take a while, look at the MyPreloadingActivity class
        // which allows longer code to be run in the background in a separate
        // thread.
    }

    public void disposeComponent() {
        System.out.println("Project closed (GatherData)");
        // called when the component closes (when project closes)
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        System.out.println("action was performed");
        // when gather data is clicked on right click menu in editor...

        // now, extract Project from AnActionEvent instance
        final Project project = event.getRequiredData(LangDataKeys.PROJECT);
        String projectName = project.getName();

        Set<ErrorTreeElementKind> setOfTypesToFind;
        ErrorViewStructure EVS;
        String output = "";
        IDEType ideType;

        try {
            setOfTypesToFind = getErrorTreeElementKindSet(lookForErrors, lookForWarning, lookForInfo, lookForNote, lookForGeneric);
            EVS = getErrorViewStructure(project);
            if (EVS.hasMessages(setOfTypesToFind)) {
                ErrorViewTextExporter EVTE = new ErrorViewTextExporter(EVS);
                output = EVTE.getReportText();
            } else {
                // No Messages in Error View Structure
                System.out.println("No messages found on right click.");
            }
            ideType = IDEType.INTELLIJ;
        } catch (Exception e) {
            System.out.println("This is python");
            ideType = IDEType.PYCHARM;
        }


        //
        System.out.println(output); // This the current Error/Warning system text
        //

        ErrorSystem errorSystem = new ErrorSystem(output, ideType);
        String url = errorSystem.search(); // does an internet search of the first error
        MyToolWindowFactory.URL = url; // sets the URL of the help button

        final Editor editor = event.getRequiredData(LangDataKeys.EDITOR);
        final Document document = editor.getDocument();

        System.out.println( /**/ document.getText() /**/ ); // how you get a hold of the whole text

        // -- below are notes on how to get specific lines and caret position and stuff --
//        final SelectionModel selectionModel = editor.getSelectionModel();
//        final int start = selectionModel.getSelectionStart();
//        final int end = selectionModel.getSelectionEnd();
//
//        final int startLine = document.getLineNumber(start);
//        final int endLine = document.getLineNumber(end) + 1;
//        selectionModel.removeSelection();
//        final EditorFragmentComponent fragment = EditorFragmentComponent.createEditorFragmentComponent(editor, startLine, endLine, false, false);

        // how to make a dialog window show up
        // /notes ------------------------------------------------------------------------

        // make a dialog box pop up.
        Messages.showMessageDialog(project, "Description", "Information", Messages.getInformationIcon());

        StringBuilder sourceRootsList = new StringBuilder();
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        for (VirtualFile file : vFiles) {
            sourceRootsList.append(file.getUrl()).append("\n");
        }

        Messages.showInfoMessage("Source roots for the " + projectName + " plugin:\n" + sourceRootsList, "Project Properties");


        PsiClass psiClass = getPsiClassFromContent(event);


        //below will print HTML data, save it to a file and open in browser to compare
        //System.out.println(doc.html());

        //If google search results HTML change the <h3 class="r" to <h3 class="r1"
        //we need to change below accordingly



        // Action must be registered in plugin.xml
    }

    @NotNull
    private Set<ErrorTreeElementKind> getErrorTreeElementKindSet(boolean lookForErrors, boolean lookForWarning, boolean lookForInfo, boolean lookForNote, boolean lookForGeneric) {
        Set<ErrorTreeElementKind> setOfTypesToFind = new HashSet<ErrorTreeElementKind>();
        if (lookForErrors) setOfTypesToFind.add(ErrorTreeElementKind.ERROR);
        if (lookForWarning) setOfTypesToFind.add(ErrorTreeElementKind.WARNING);
        if (lookForInfo) setOfTypesToFind.add(ErrorTreeElementKind.INFO);
        if (lookForNote) setOfTypesToFind.add(ErrorTreeElementKind.NOTE);
        if (lookForGeneric) setOfTypesToFind.add(ErrorTreeElementKind.GENERIC);
        return setOfTypesToFind;
    }

    private ErrorViewStructure getErrorViewStructure(Project project) {
        MessageView messageView = MessageView.SERVICE.getInstance(project);
        ContentManager contentManager = messageView.getContentManager();
        Content[] contents = contentManager.getContents();
        NewErrorTreeViewPanel NETVP = (NewErrorTreeViewPanel) contents[0].getComponent();
        return NETVP.getErrorViewStructure();
    }

    public static void info(Logger logger, String msg) {
            logger.info(msg);
    }

    private PsiClass getPsiClassFromContent(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            System.out.println("Psi File Returned NULL");
            return null;
        }

        // find offset for what is at the caret cursor
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);
        PsiClass psiClass = PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);

        if (psiClass == null) {
            return null;
        }
        // up to here is what gets the dropdown option to show up
        return psiClass;
    }
    @NotNull
    public String getComponentName() {
        return "GatherDataPlugin";
    }
}

