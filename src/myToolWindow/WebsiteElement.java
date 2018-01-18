package myToolWindow;

public class WebsiteElement {
    String output = "";
    String title = "";
    String website = "";

    WebsiteElement(String title, String website) {
        this.title = title;
        this.website = website;
        output = "<html>'" + title + "'<br>" + website + "</html>";
    }

    @Override
    public String toString() {
        return output;
    }

    //         listModel.addElement("<html>'Syntax Error: Missing Semi Colon'<br>dreamincode.net</html>");
}
