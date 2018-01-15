package myToolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.playback.commands.ActionCommand;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.concurrency.EdtExecutorService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.net.URL;
import java.util.Calendar;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by IntelliJ IDEA.
 * User: Alexey.Chursin
 * Date: Aug 25, 2010
 * Time: 2:09:00 PM
 */
public class MyToolWindowFactory implements ToolWindowFactory {
  public static String URL = "http://www.roof.com";

  public class HelpListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        //AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay // bad thread, doesn't work
        EdtExecutorService.getScheduledExecutorInstance().scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Scheduled Task Running...");
                    AnAction action = ActionManager.getInstance().getAction("RightClickButton");
                    InputEvent inputEvent = ActionCommand.getInputEvent("RightClickButton");
                    ActionCallback callback = ActionManager.getInstance().tryToExecute(action, inputEvent, null, null, true);
                    // System.out.println(callback.toString());
                    System.out.println("finished attempt to run GatherData");
                }
                catch (Exception e) {
                    System.out.println("exception in scheduled task!");
                    e.printStackTrace();

                    throw new RuntimeException(e);
                }
            }
        }, 10, 5L , SECONDS);

      try {
        String url = URL;
        Desktop.getDesktop().browse(new URL(url).toURI());
      } catch (Exception e2) {
        e2.printStackTrace(); }

    }
  }

  private JButton refreshToolWindowButton;
  private JButton hideToolWindowButton;
  private JLabel currentDate;
  private JLabel currentTime;
  private JPanel myToolWindowContent;
  private JButton helpButton;
  private ToolWindow myToolWindow;


  public MyToolWindowFactory() {
    hideToolWindowButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        myToolWindow.hide(null);
      }
    });
    refreshToolWindowButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        MyToolWindowFactory.this.currentDateTime();
      }
    });
  }

  // Create the tool window content.
  public void createToolWindowContent(Project project, ToolWindow toolWindow) {
    helpButton.addActionListener(new HelpListener());
    myToolWindow = toolWindow;
    this.currentDateTime();
    ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
    Content content = contentFactory.createContent(myToolWindowContent, "", false);
    toolWindow.getContentManager().addContent(content);


//    AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Scheduled Task Running...");
//                AnAction action = ActionManager.getInstance().getAction("RightClickButton");
//                InputEvent inputEvent = ActionCommand.getInputEvent("RightClickButton");
//                ActionCallback callback = ActionManager.getInstance().tryToExecute(action, inputEvent, null, null, true);
//                System.out.println("finished attempt to run GatherData");
//            }
//        }, 15, 3L , SECONDS);
  }

  public void currentDateTime() {
    // Get current date and time
    Calendar instance = Calendar.getInstance();
    currentDate.setText(String.valueOf(instance.get(Calendar.DAY_OF_MONTH)) + "/"
                        + String.valueOf(instance.get(Calendar.MONTH) + 1) + "/" +
                        String.valueOf(instance.get(Calendar.YEAR)));
    currentDate.setIcon(new ImageIcon(getClass().getResource("/myToolWindow/Calendar-icon.png")));
    int min = instance.get(Calendar.MINUTE);
    String strMin;
    if (min < 10) {
      strMin = "0" + String.valueOf(min);
    } else {
      strMin = String.valueOf(min);
    }
    currentTime.setText(instance.get(Calendar.HOUR_OF_DAY) + ":" + strMin);
    currentTime.setIcon(new ImageIcon(getClass().getResource("/myToolWindow/Time-icon.png")));
    // Get time zone
    long gmt_Offset = instance.get(Calendar.ZONE_OFFSET); // offset from GMT in milliseconds
    String str_gmt_Offset = String.valueOf(gmt_Offset / 3600000);
    str_gmt_Offset = (gmt_Offset > 0) ? "GMT + " + str_gmt_Offset : "GMT - " + str_gmt_Offset;

  }

}
