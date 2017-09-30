import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;


public class BackgroundActivityComponent implements ApplicationComponent {
    private static final Logger LOG = Logger.getInstance(BackgroundActivityComponent.class);

    public void initComponent() {
        LOG.info("Initializing plugin data structures");
        System.out.println("I'm alive! (BackgroundActivityComponent)");
    }

    public void disposeComponent() {
        LOG.info("Disposing plugin data structures");
        System.out.println("I'm dead! (BackgroundActivityComponent)");

    }

    @NotNull
    public String getComponentName() {
        return "BackgroundActivityComponent";
    }
}