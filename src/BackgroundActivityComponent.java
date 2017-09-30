import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;


public class BackgroundActivityComponent implements ApplicationComponent {
    private static final Logger LOG = Logger.getInstance(BackgroundActivityComponent.class);

    public void initComponent() {
        LOG.info("Initializing plugin data structures");
    }

    public void disposeComponent() {
        LOG.info("Disposing plugin data structures");
    }

    @NotNull
    public String getComponentName() {
        return "myApplicationComponent";
    }
}