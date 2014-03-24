package org.ssutt.core.fetch;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by fau on 24/03/14.
 */
public class SSUDataFetcher implements DataFetcher {
    private static SSUDataFetcher ssudf;

    private static List<String> exclusions = new ArrayList<>();
    private static  String globalScheduleURL = "";

    /**
     * We perfer using singletons, so this constructor creates an object and passes all the useful info
     * to class
     * @param String url - SSU or any website with timetables in apropriate form
     * @param String... exclusions - if you need to exclude some data with the same HTML tags (like colleges in our case)
     * @return SSUDataFetcher - an instance
     */
    private SSUDataFetcher(String url, String... exclusions){
        globalScheduleURL = url;
        for(String s: exclusions) {
            this.exclusions.add(s);
        }

    }

    public static SSUDataFetcher getInstance(String url, String... exclusions) {
        if (ssudf == null) {
            ssudf = new SSUDataFetcher(url,exclusions);
        }
        return ssudf;
    }

    public String[] getExclusions() {
        return exclusions.toArray(new String[exclusions.size()]);
    }

    @Override
    public Map<String, String> getDepartments() {
        Map result = new HashMap<String, String>();
        Document doc = null;

        try {
            doc = Jsoup.connect(globalScheduleURL).get();
        } catch (IOException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(2);
        }

        Elements links = doc.select("a[href]");
        //parsing exceptions
        for (Element link : links) {
            if (link.attr("href").startsWith("/schedule/")) {
                String[] test = link.attr("abs:href").split("/"); //test[4] - the last token like knt,mm,ff

                if (!Arrays.asList(exclusions).contains(test[4])) {
                    result.put(link.ownText(),test[4]);
                }
            }
        }

        return result;
    }
}
