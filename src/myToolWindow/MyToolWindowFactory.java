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
import java.util.Random;

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
  private static MySelectionListener listListener;


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
    public static void addWebsite(String title, String website) {
      int max_length = 5;
      int length = listModel.getSize();

      title = title.substring(0, Math.min(title.length(), 40)); // shorten the string if smaller than 40 characters

        boolean doesContain = false;
        for (int i = 0; i < length; i++) {
            if (title.equals(listModel.get(i).title)) doesContain = true;
        }

        if (!doesContain) {
            if (max_length > length) {
                listModel.addElement(new WebsiteElement(title, website));
            } else {
                // select a random one to remove
                int random_index = new Random().nextInt(max_length);
                int selected_index = listListener.getIndex();
                if (random_index == selected_index) {
                    random_index = (random_index + 1) % max_length;
                } // unless the random one is the selected one, in which case
                // you increment it by one, modulo length to get next logical element.
                // this only works because only one element can be selected.
                listModel.removeElementAt(random_index);
                listModel.addElement(new WebsiteElement(title, website));
            }
        }
      // listListener.index;
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
            MyToolWindowFactory.off = false;
            //AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay // bad thread, doesn't work
            EdtExecutorService.getScheduledExecutorInstance().scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
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
            }, 5, 15 , SECONDS);

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
