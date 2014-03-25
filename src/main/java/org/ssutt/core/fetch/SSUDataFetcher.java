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

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class SSUDataFetcher implements DataFetcher {
    private static final Logger logger = LogManager.getLogger(SSUDataFetcher.class.getName());
    private List<String> exclusions = new ArrayList<>();
    private static  String globalScheduleURL = "";

    /**
     * We perfer using singletons, so this constructor creates an object and passes all the useful info
     * to class
     * @param String url - SSU or any website with timetables in apropriate form
     * @param String... exclusions - if you need to exclude some data with the same HTML tags (like colleges in our case)
     * @return SSUDataFetcher - an instance
     */
    public SSUDataFetcher(String url, String... exclusions){
        globalScheduleURL = url;
        for(String s: exclusions) {
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

    @Override
    public Map<String, String> getGroups(String department) {
        Map<String, String> result = new HashMap<>();

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

                result.put(link.ownText(),  esc[esc.length - 1]);
            }

        }
        return result;
    }
}
