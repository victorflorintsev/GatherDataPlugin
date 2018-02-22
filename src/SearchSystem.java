import com.intellij.openapi.actionSystem.AnActionEvent;
import myToolWindow.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SearchSystem {
    private String toSearch = " ";
    private String lastToSearch = " ";
    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";

    public void addTerms(String terms) {
        if (!terms.equals("")) toSearch += " " + terms;
    }

    public void updateWebsiteList() { // searches and adds a website to the JList in MyToolWindowFactory
    if (!toSearch.equals(lastToSearch)) {
        System.out.println("Searching: " + toSearch);
        String searchTerm = toSearch + " site:www.stackoverflow.com/ ";
        int num = 2; // number of entries at a time

        // if (searchTerm.equals(" ")) searchTerm = "";
        String searchURL = GOOGLE_SEARCH_URL + "?q=" + searchTerm + "&num=" + num;
        searchURL = searchURL.replaceAll(" ","%20");
        searchURL = searchURL.replaceAll("'","%27");


        try {
            org.jsoup.nodes.Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get(); //without proper User-Agent, we will get 403 error

            Elements results = doc.select("h3.r > a");

            for (Element result : results) {
                String linkHref = result.attr("href");
                String url = linkHref.substring(7, linkHref.indexOf("&"));
                MyToolWindowFactory.addWebsite(result.text(), url);
            }
//            for (Element result : results) {
//                String linkHref = result.attr("href");
//                if (url.equals("http://www.google.com")) {
//                    url = linkHref.substring(7, linkHref.indexOf("&"));
//                }
//                String linkText = result.text();
//                System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(6, linkHref.indexOf("&")));
//            }

        } catch (IOException e) {
            System.out.println(e.getMessage() + " : " + searchURL);

        }
        lastToSearch = toSearch;
    }


    }

    public void generateQuery(CaretSystem caretSystem, ErrorSystem errorSystem, AnActionEvent event, String[] typeArray, int numTypes) {
        toSearch = "Java ";
        //toSearch += addType(caretSystem, event);
        toSearch += errorSystem.getTerms(caretSystem,4);
        toSearch += caretSystem.getTerms(event);
        toSearch.toLowerCase();
        for (int i = 0; i < numTypes; i++) {
            toSearch = toSearch.replaceAll(typeArray[i].toLowerCase(),"");
        } // removes all type instances

    }

    private String addType(CaretSystem caretSystem, AnActionEvent event) {
        String test = caretSystem.getTerms(event);
        if (test.contains("int")) return "int ";

        if (test.contains("String")) return "String ";

        if (test.contains("double")) return "double ";

        if (test.contains("scanner") || test.contains("Scanner")) return "Scanner ";

        return "";
    }
}
