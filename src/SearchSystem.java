import myToolWindow.MyToolWindowFactory;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;

public class SearchSystem {
    private String toSearch = " ";
    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";

    public void addTerms(String terms) {
        if (!terms.equals("")) toSearch += " " + terms;
    }
    public void updateHelpButton() {
        String searchTerm = toSearch + " site:stackoverflow.com";
        int num = 1;
        String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm+"&num="+num;
        //without proper User-Agent, we will get 403 error
        String url = "http://www.google.com"; // default output

        try {
            org.jsoup.nodes.Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
            Elements results = doc.select("h3.r > a");
//            for (Element result : results) {
//                String linkHref = result.attr("href");
//                if (url.equals("http://www.google.com")) {
//                    url = linkHref.substring(7, linkHref.indexOf("&"));
//                }
//                String linkText = result.text();
//                System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(6, linkHref.indexOf("&")));
//            }
            String linkHref = results.first().attr("href");
            url = linkHref.substring(7, linkHref.indexOf("&"));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        MyToolWindowFactory.URL = url;
    }

    public void updateWebsiteList(JList websiteList) {
        String searchTerm = toSearch + " site:stackoverflow.com";
        int num = 5; // number of entries
        String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm+"&num="+num;
        //without proper User-Agent, we will get 403 error
        String url = "http://www.google.com"; // default output

        try {
            org.jsoup.nodes.Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
            Elements results = doc.select("h3.r > a");
//            for (Element result : results) {
//                String linkHref = result.attr("href");
//                if (url.equals("http://www.google.com")) {
//                    url = linkHref.substring(7, linkHref.indexOf("&"));
//                }
//                String linkText = result.text();
//                System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(6, linkHref.indexOf("&")));
//            }
            String linkHref = results.first().attr("href");
            url = linkHref.substring(7, linkHref.indexOf("&"));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        MyToolWindowFactory.URL = url;
    }
}
