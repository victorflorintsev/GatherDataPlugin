import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Vector;


public class ErrorSystem {
    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search"; // delete this

    private String text = "";
    public Vector<VicError> errorVector = new Vector<VicError>();

    private GatherData.IDEType ideType;

    ErrorSystem(String text, GatherData.IDEType ideType) {
        this.text = text + "\n ";
        this.ideType = ideType;
        procText();
    }

    private void procText() {
        if (ideType == ideType.PYCHARM) {
            // ToDo
        }
        if (ideType == ideType.INTELLIJ) {
//        Information:java: Errors occurred while compiling module 'testProject'
//        Information:javac 1.8.0_144 was used to compile java sources
//        Information:11/15/2017 8:14 PM - Compilation completed with 1 error and 0 warnings in 3s 18ms
//        C:\Users\victo\IdeaProjects\testProject\src\PluginDemo.java
//        Error:Error:line (3)java: ';' expected
            for (int i = 0; i < text.length() - 6; i++) {
                if (text.substring(i, i + 6).equals("Error:")) {
                    int endIndex1 = text.indexOf('\n', i);
                    if (endIndex1 > 0) {
                        String snippet = text.substring(i, endIndex1);
                        int beginIndex = snippet.indexOf("(");
                        int endIndex = snippet.indexOf(")");
                        int in = Integer.parseInt(snippet.substring(beginIndex + 1, endIndex)); // to get line number
                        snippet = snippet.substring(endIndex + 1,snippet.length()-1);
                        errorVector.add(new VicError(in, snippet));

                        int move = i + snippet.length();
                        if (move < text.length()) i = move;
                    }
                }
            }
        }
    }

    public String search() {
        //Taking search term input from console
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please enter the search term.");
//        String searchTerm = scanner.nextLine();
//        System.out.println("Please enter the number of results. Example: 5 10 20");
//        int num = scanner.nextInt();
//        scanner.close();

        String searchTerm = "";
        if (!errorVector.isEmpty()) searchTerm = errorVector.elementAt(0).whatsWrong + " site:stackoverflow.com ";
        int num = 1;

        String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm+"&num="+num;
        //without proper User-Agent, we will get 403 error
        String url = "http://www.google.com"; // default output

        try {
            org.jsoup.nodes.Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
            Elements results = doc.select("h3.r > a");

            for (Element result : results) {
                String linkHref = result.attr("href");
                if (url.equals("http://www.google.com")) {
                    url = linkHref.substring(7, linkHref.indexOf("&"));
                }
                String linkText = result.text();
                System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(6, linkHref.indexOf("&")));
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return url;
    }

    public String getText() {
        return text;
    }

    public String getTerms(CaretSystem caretSystem, int range) {
        int lineNumber = caretSystem.getLineNumber();
        if (!errorVector.isEmpty()) {
            String out = "";
            for (VicError e:errorVector) {
                if (e.lineNumber > lineNumber - range && e.lineNumber < lineNumber + range) {
                    if (!out.contains(e.whatsWrong)) {
                        out += e.whatsWrong + " ";
                    }
                } // adds all in-range errors to out string if they contain unique information
            }
            return out;
        }
        return "";
    }

    public class VicError {
        public int lineNumber = 0;
        public String whatsWrong = "";
        VicError(int num, String string) { lineNumber = num; whatsWrong = string;}
    }

}
