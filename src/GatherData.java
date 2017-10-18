import com.intellij.codeInsight.hint.EditorFragmentComponent;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.ide.errorTreeView.ErrorTreeElement;
import com.intellij.ide.errorTreeView.ErrorTreeElementKind;
import com.intellij.ide.errorTreeView.ErrorViewStructure;
import com.intellij.ide.errorTreeView.NewErrorTreeViewPanel;
import com.intellij.ide.errorTreeView.impl.ErrorViewTextExporter;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.content.MessageView;
import com.sun.glass.ui.Application;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/*
This class defines the action that happens when the right click menu button is clicked.
Action classes are the basis for how IntelliJ plugin development works, and each
action extends the AnAction Interface.
 */

public class GatherData extends AnAction implements ApplicationComponent {

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
        // when gather data is clicked on right click menu in editor...

        // now, extract Project from AnActionEvent instance
        final Project project = event.getRequiredData(LangDataKeys.PROJECT);
        String projectName = project.getName();

        // edit these booleans to only scan for specific types of messages from the current message system
        boolean lookForErrors =  true;
        boolean lookForWarning = false;
        boolean lookForInfo =    false;
        boolean lookForNote =    false;
        boolean lookForGeneric = false;
        Set<ErrorTreeElementKind> setOfTypesToFind = getErrorTreeElementKindSet(lookForErrors, lookForWarning, lookForInfo, lookForNote, lookForGeneric);

        ErrorViewStructure EVS = getErrorViewStructure(project);
        String output = "";
        if (EVS.hasMessages(setOfTypesToFind)) {
            ErrorViewTextExporter EVTE = new ErrorViewTextExporter(EVS);
            output = EVTE.getReportText();
        } else {
            // No Messages in Error View Structure
            System.out.println("No messages found on right click.");
        }

        //
        System.out.println(output); // This the current Error/Warning system text
        //


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
    @NotNull
    public String getComponentName() {
        return "GatherDataPlugin";
    }
}

