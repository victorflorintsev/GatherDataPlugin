import com.intellij.openapi.application.PreloadingActivity;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link PreloadingActivity} to demonstrate how a plugin could run some (possibly expensive)
 * activities on startup.
 *
 * PreLoading activities are run in a separate thread so as to not interfere with IntelliJ's startup.
 *
 */
public class MyPreloadingActivity extends PreloadingActivity {
    private static final Logger LOG = Logger.getInstance(MyPreloadingActivity.class);

    /**
     * This method is run in IntelliJ's pooled background thread. Only one PreloadingActivity is run at the same time.
     *
     * @param indicator
     * @see com.intellij.openapi.application.Preloader
     * @see PreloadingActivity
     */
    public void preload(@NotNull ProgressIndicator indicator) {
/*
    Uncomment the code below to see a demonstration of code being run in the background.
    You'll notice that the project is opened and you can navigate the IDE, however
    in a separate thread in the background, a really inefficient sorting algorithm is
    being run without a problem.
 */


//        AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Scheduled Task Running...");
//                AnAction action = ActionManager.getInstance().getAction("RightClickButton");
//                InputEvent inputEvent = ActionCommand.getInputEvent("RightClickButton");
//                ActionCallback callback = ActionManager.getInstance().tryToExecute(action, inputEvent, null, null, true);
//                System.out.println("finished attempt to run GatherData");
//            }
//        }, 15, 3L , SECONDS);



//        LOG.info("Preloading plugin-data");
//
//        final long startTime = System.currentTimeMillis();
//        System.out.println("started");
//        sortLinearly(200000);
//
//        final long endTime = System.currentTimeMillis();
//
//        long time = endTime - startTime;
//
//        System.out.println("Finished in " + time + " ms");
    }

    private void sortLinearly(int i) {
        // sorts i numbers
        // bubble sort to take up time, it's inefficient on purpose to demonstrate the separate thread
        int[] list = new int[i];
        int temp;
        for (int data: list) {
            data = (int)Math.random()*100;
        }
        for (int j = 0; j < i; j++) {
            for (int k = 0; k < i-1; k++) {
                if (list[k] > list[k+1]) {
                    temp = list[k];
                    list[k] = list[k+1];
                    list[k+1] = temp;
            }
        }
        }
    }
}