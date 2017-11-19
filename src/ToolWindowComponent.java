import com.intellij.ProjectTopics;
import com.intellij.execution.impl.ConsoleInputListener;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;


/**
 * Created by chase on 5/18/17.
 */
public class ToolWindowComponent implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
    }

    @Override
    public void init(ToolWindow window) {
        Application application = ApplicationManager.getApplication();

        ApplicationListener c = new ApplicationListener() {

            @Override
            public boolean canExitApplication() {
                return false;
            }

            @Override
            public void applicationExiting() {

            }

            @Override
            public void beforeWriteActionStart(@NotNull Object o) {

            }

            @Override
            public void writeActionStarted(@NotNull Object o) {

            }

            @Override
            public void writeActionFinished(@NotNull Object o) {

            }

            @Override
            public void afterWriteActionFinished(@NotNull Object o) {

            }
        };
        application.addApplicationListener(c);
    }
}

