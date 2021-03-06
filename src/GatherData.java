import com.intellij.ide.errorTreeView.ErrorTreeElementKind;
import com.intellij.ide.errorTreeView.ErrorViewStructure;
import com.intellij.ide.errorTreeView.NewErrorTreeViewPanel;
import com.intellij.ide.errorTreeView.impl.ErrorViewTextExporter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnAction.TransparentUpdate;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.content.MessageView;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


/*
This class defines the action that happens when the right click menu button is clicked.
Action classes are the basis for how IntelliJ plugin development works, and each
action extends the AnAction Interface.
 */



public class GatherData extends AnAction implements TransparentUpdate, ApplicationComponent  {
    private final boolean HAS_TYPE = true;

    int numTypes = 4;
    boolean typeArrayInstantiated = false;
    String[] typeArray = new String[numTypes];

    enum IDEType {
        INTELLIJ, PYCHARM
    }

    // edit these booleans to only scan for specific types of messages from the current message system
    private boolean lookForErrors  = true;
    private boolean lookForWarning = false;
    private boolean lookForInfo    = false;
    private boolean lookForNote    = false;
    private boolean lookForGeneric = false;

    private Project project;
    private String projectName = "";


    public void initComponent() {
        System.out.println("Project opened (GatherData)");

        // scheduled task:


        // This is for short init code, if you need to execute something
        // that would take a while, look at the MyPreloadingActivity class
        // which allows longer code to be run in the background in a separate
        // thread. On init, a process is scheduled to run every second
    }

    public void disposeComponent() {
        System.out.println("Project closed (GatherData)");
        // called when the component closes (when project closes)
    }

         // when gather data is clicked on right click menu in editor...
         // also when gather data is called automatically every 5 seconds in MyToolWindowFactory
    @Override
    public void actionPerformed(AnActionEvent event) {
        if (!typeArrayInstantiated) {
            try {
                URL file = this.getClass().getResource("TypeInfo.txt");
                Scanner s = new Scanner(new File(file.getFile()));

                for (int i = 0; i < numTypes; i++) {
                    typeArray[i] = s.nextLine().replace("[","\\[").replace("]","\\]");
                }
                typeArrayInstantiated = true;
                s.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        // extract Project from AnActionEvent instance
        project = event.getRequiredData(LangDataKeys.PROJECT);
        projectName = project.getName();

        // Process Errors
        ErrorSystem errorSystem = getErrorSystem(project); // uses ErrorSystem class to deal with errors
        // the output text of the error module is located in errorSystem.getText
        // System.out.println(errorSystem.getText()); // This the current Error/Warning system text

        CaretSystem caretSystem = new CaretSystem(event);

        // int lineNumber = caretSystem.getLineNumber(event); // default 0

        int range = 2; // will search the errors around the above line number

        SearchSystem searchSystem = new SearchSystem();

        searchSystem.generateQuery(caretSystem, errorSystem, event, typeArray, numTypes);

        searchSystem.updateWebsiteList(); // updates the JList on MyToolWindowFactory

        // searchSystem.addTerms(errorSystem.getTerms(caretSystem, range));
        // searchSystem.addTerms(caretSystem.getTerms(event));


        // final Editor editor = event.getRequiredData(LangDataKeys.EDITOR);
        // final Document document = editor.getDocument();
        // System.out.println( /**/ document.getText() /**/ ); // how you get a hold of the whole text

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
//        Messages.showMessageDialog(project, "Description", "Information", Messages.getInformationIcon());
//
//        StringBuilder sourceRootsList = new StringBuilder();
//        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
//        for (VirtualFile file : vFiles) {
//            sourceRootsList.append(file.getUrl()).append("\n");
//        }
//
//        Messages.showInfoMessage("Source roots for the " + projectName + " plugin:\n" + sourceRootsList, "Project Properties");

    }

    private ErrorSystem getErrorSystem(Project project) {
        String output = "";
        IDEType ideType;

        Set<ErrorTreeElementKind> setOfTypesToFind;
        ErrorViewStructure EVS;
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
            System.out.println("No errors found: might be python?");
            ideType = IDEType.PYCHARM;
        }

        ErrorSystem errorSystem = new ErrorSystem(output, ideType);
        return errorSystem;
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

