/*
 * Copyright 2014 Plain Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tt.core.fetch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * SSUDataFetcher is an implementation of {@link AbstractDataFetcher} with restrictions and markup parsing for
 * <a href=sgu.ru/schedule>SSU Timetables Website</a>.
 *
 * @author Sevak Avetisyan,Vlad Slepukhin
 * @since 1.0
 */
public class SSUDataFetcher implements AbstractDataFetcher {
    /**
     * Exclusion lists to avoid, while parsing departments
     * We drop all the colleges (part of SSU) as they have different times of classes
     */
    private List<String> exclusions = Arrays.asList("kgl", "cre", "el");

    /**
     * Another restriction to handle unescaped sequences for groups, which are named not only with numbers or by their
     * majors.
     */
    private Map<String, String> nonNumericalGroups = Collections.emptyMap();

    /**
     * The URL, which should be parsed to get departments.
     */
    private String globalScheduleURL = "http://www.sgu.ru/schedule";

    /**
     * Constructs an instance with link to website to parse and optional exclusions in it (here, just tokens,
     * see getDepartments)
     */
    public SSUDataFetcher() {
        nonNumericalGroups = new HashMap<>();
    }

    /**
     * Parses schedule URL to find the tokens (tags) and names of departments. In our case, these tags are just part
     * of the url in the page.
     *
     * @return K-V of name-tag for departments
     * @since 1.0
     */
    @Override
    public Map<String, String> getDepartments() {
        Map<String, String> result = new HashMap<>();
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

    /**
     * Parses department pages to get the list of groupnames.
     *
     * @param department token (tag) of the department, which we get in getDepartments()
     * @return List of names.
     * @since 1.0
     */
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

    /**
     * Parse the resulting url of group to create a temporary, huge and complicated table from SSU website.
     *
     * @param url the resulting (by getting groups and departments, checking non-numerical thing) url.
     * @return String[][] statical representation (statical array 8*6) of HTML code.
     * @throws IOException
     * @since 1.0
     */
    @Override
    public String[][] getTT(URL url) throws IOException {
        System.out.println("Parsing: " + url.toString());
        //see comment later. we need table with empty cells, not empty table
        String[][] table = createEmptyTable();

        Document doc = Jsoup.parse(url, 5000);
        Elements tr = doc.getElementsByTag("tr");

        for (int i = 1; i < 9; ++i) {
            Elements data;
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

        System.out.println("Parsed: " + url.toString());
        return table;
    }

    /**
     * Formats url for ssu schedule website with conversion of non-numerical groups.
     *
     * @param departmentTag    the tag of department, where group exists (one token of link for SSU)
     * @param groupDisplayName the displayed name. Usually the same as the token in the URL, but some groups needed
     *                         to be converted.
     * @return Ready to parse URL.
     * @since 1.1
     */
    @Override
    public URL formatURL(String departmentTag, String groupDisplayName) throws MalformedURLException {
        String groupAddress =
                (nonNumericalGroups.containsKey(groupDisplayName)) ?
                        nonNumericalGroups.get(groupDisplayName) : groupDisplayName;

        String url = String.format("%s/%s/%s/%s", globalScheduleURL, departmentTag, "do", groupAddress);

        return URI.create(url).toURL();

    }

    /**
     * Accessor. Needed for testing purposes.
     *
     * @return Filled exclusion list.
     * @since 1.0
     */
    public String[] getExclusions() {
        return exclusions.toArray(new String[exclusions.size()]);
    }

    /**
     * Create an empty temporary table, if we find needed tags on the page.
     *
     * @return Empty table with size 8*6.
     * @since 1.0
     */
    private String[][] createEmptyTable() {
        String[][] result = new String[8][6];

        for (int i = 0; i < 8; ++i)
            for (int j = 0; j < 6; ++j)
                result[i][j] = "";

        return result;
    }

    /**
     * Checks if group name is represented in non-numerical format: major name, some dots and parentheses.
     *
     * @param test name to test
     * @return <code>true</code> if is an exclusion, else <code>false</code>
     * @since 1.0
     */
    private boolean numExclusion(String test) {
        String regex = "[0-9]+";

        return (!(test.matches(regex)));
    }

}

