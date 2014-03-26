/**
 *Copyright 2014 Plain Solutions
 *
 *Authors:
 *    Sevak Avetisyan <sevak.avet@gmail.com>
 *    Vlad Slepukhin <slp.vld@gmail.com>
 *
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package org.ssutt.core.fetch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SSUDataFetcher implements DataFetcher {
    private static final Logger logger = LogManager.getLogger(SSUDataFetcher.class.getName());
    private List<String> exclusions = new ArrayList<>();

    private Map<String, String> nonNumericalGroups;

    private String globalScheduleURL = "";

    public SSUDataFetcher(String url, String... exclusions) {
        globalScheduleURL = url;
        nonNumericalGroups = new HashMap<>();

        for (String s : exclusions) {
            this.exclusions.add(s);
        }
        logger.info("SSU DataFetcher Initialized correctly.");

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
            e.printStackTrace();
            System.exit(2);
        }

        Elements links = doc.select("a[href]");
        //parsing exceptions
        for (Element link : links) {
            if (link.attr("href").startsWith("/schedule/")) {
                String[] test = link.attr("abs:href").split("/"); //test[4] - the last token like knt,mm,ff

                if (!exclusions.contains(test[4]))
                    result.put(link.ownText(), test[4]);
            }
        }

        return result;
    }

    @Override
    public List<String> getGroups(String department) {
        List<String> result = new ArrayList<>();

        String url = globalScheduleURL + '/' + department;

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(4);
        }

        Elements links = doc.select("a[href]");
        for (Element link : links) {
            if (link.attr("href").startsWith("/schedule/" + department + "/do/")) {
                String[] esc = link.attr("abs:href").split("/");

                result.add(link.ownText());
                if (numExclusion(link.ownText()))
                    nonNumericalGroups.put(link.ownText(), esc[esc.length - 1]);
            }

        }
        return result;
    }

    @Override
    public String[][] getTT(URL url) throws IOException {
        //see comment later. we need table with empty cells, not empty table
        String[][] table = createEmptyTable();


        Document doc = Jsoup.parse(url, 5000);
        Elements tr = doc.getElementsByTag("tr");

        for (int i = 1; i < 9; ++i) {
            Elements data = null;
            try {
                data = tr.get(i).getElementsByTag("td");
            } catch (IndexOutOfBoundsException ex) {
                //we have some fucking links in SSU structure that don't take us to timetable
                //DEAL WITH it
                return table; //should be empty or so
            }
            for (int j = 0; j < data.size(); j++) {
                table[i - 1][j] = data.get(j).text();
            }
        }

        return table;
    }

    private String[][] createEmptyTable() {
        String[][] result = new String[8][6];

        for (int i = 0; i < 8; ++i)
            for (int j = 0; j < 6; ++j)
                result[i][j] = "";

        return result;
    }

    private boolean numExclusion(String test) {
        String regex = "[0-9]+";

        return (!(test.matches(regex)));
    }

    public Map<String, String> getNonNumericalGroups() {
        return nonNumericalGroups;
    }
}

