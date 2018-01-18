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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.net.URL;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by IntelliJ IDEA.
 * User: Alexey.Chursin
 * Date: Aug 25, 2010
 * Time: 2:09:00 PM
 */
public class MyToolWindowFactory implements ToolWindowFactory {
  public static String URL = "http://www.roof.com";
    private static boolean off = false;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

  private JButton startButton;
  private JButton hideToolWindowButton;

  private JPanel myToolWindowContent;
  private JButton openButton;

  private ToolWindow myToolWindow;

  private JList websiteList;
  private static DefaultListModel<WebsiteElement> listModel;
  MySelectionListener listListener;


  public MyToolWindowFactory() {
      setUpWebsiteList();
      startButton.addActionListener(new StartListener());
      openButton.addActionListener(new OpenListener());
      hideToolWindowButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
            {
                myToolWindow.hide(null);
                off = true;
            }
        });
  }

    private void setUpWebsiteList() {
        listModel = new DefaultListModel();
        listModel.addElement(new WebsiteElement("Syntax Error: Missing Semi Colon", "http://www.dreamincode.net/forums/topic/13095-syntax-error-missing-semi-colon/"));
        listModel.addElement(new WebsiteElement("How to solve error: ';' expected in Java?", "https://stackoverflow.com/questions/35261567/how-to-solve-error-expected-in-java"));
        listModel.addElement(new WebsiteElement("Are semicolons required in Java?","https://stackoverflow.com/questions/22287337/are-semicolons-required-in-java"));
        //Create the list and put it in a scroll pane.
        websiteList.setModel(listModel);
        websiteList.setFixedCellHeight(120);
        websiteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        websiteList.setSelectedIndex(0);
        listListener = new MySelectionListener();
        websiteList.addListSelectionListener(listListener);

        websiteList.setVisibleRowCount(5);
        websiteList.updateUI();
    }

    // Create the tool window content.
  public void createToolWindowContent(Project project, ToolWindow toolWindow) {
    myToolWindow = toolWindow;
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

    public static DefaultListModel<WebsiteElement> getListModel() {
        return listModel;
    }

    public class StartListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay // bad thread, doesn't work
            EdtExecutorService.getScheduledExecutorInstance().scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    MyToolWindowFactory.off = false;
                    try {
                        System.out.println("Scheduled Task Running...");
                        AnAction action = ActionManager.getInstance().getAction("RightClickButton");
                        InputEvent inputEvent = ActionCommand.getInputEvent("RightClickButton");
                        ActionCallback callback = ActionManager.getInstance().tryToExecute(action, inputEvent, null, null, true);
                    }
                    catch (Exception e) {
                        System.out.println("exception in scheduled task!");
                        e.printStackTrace();

                        throw new RuntimeException(e);
                    }
                    if (MyToolWindowFactory.off) throw new RuntimeException(); // turn off scheduler when window is hidden
                }
            }, 1, 5 , SECONDS);

        }
    }

    private class OpenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                WebsiteElement element = listModel.elementAt(listListener.getIndex());
                String url = element.website;
                Desktop.getDesktop().browse(new URL(url).toURI());
            } catch (Exception e2) {
                e2.printStackTrace(); }
        }
    }

    private class MySelectionListener implements ListSelectionListener {
        private int index = 0;
        public int getIndex() {return index;}
        @Override
        public void valueChanged(ListSelectionEvent e) {
            index = websiteList.getSelectedIndex();
            // System.out.println("index = " + index);
        }
    }
}
