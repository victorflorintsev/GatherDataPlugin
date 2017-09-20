import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

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
        System.out.println("CLICK");

        // After we've made the Action we need to register the GatherData class in plugin.xml
    }
}
