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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * AbstractDataFetcher is an abstraction that should communicate with university web services and format temporary
 * timetable in the memory to deliver it to database with the help of DataManager.
 * <p/>
 * Also, it handles exclusions for parsing, non-numerical groups (encoded by the name of the major, not number) and
 * delivering groups lists and department lists.
 * <p/>
 *
 * @author Sevak Avetisyan,Vlad Slepukhin
 * @since 1.0
 */
public interface AbstractDataFetcher {
    /**
     * Parses schedule URL to find the tokens (tags) and names of departments. In our case, these tags are just part
     * of the url in the page.
     *
     * @return K-V of name-tag for departments
     * @see org.tt.core.fetch.SSUDataFetcher
     * @since 1.0
     */
    Map<String, String> getDepartments();

    /**
     * Parses department pages to get the list of group names.
     *
     * @param department token (tag) of the department, which we get in getDepartments()
     * @return List of names.
     * @see org.tt.core.fetch.SSUDataFetcher
     * @since 1.0
     */
    List<String> getGroups(String department);

    /**
     * Parse the resulting url of group to create a temporary, huge and complicated table from SSU website.
     *
     * @param url the resulting (by getting groups and departments, checking non-numerical thing) url.
     * @return String[][] statical representation (statical array 8*6) of HTML code.
     * @throws IOException
     * @see org.tt.core.fetch.SSUDataFetcher
     * @since 1.0
     */
    String[][] getTT(URL url) throws IOException;

    /**
     * Formats url for ssu schedule website with conversion of non-numerical groups.
     *
     * @param departmentTag    the tag of department, where group exists (one token of link for SSU)
     * @param groupDisplayName the displayed name. Usually the same as the token in the URL, but some groups needed
     *                         to be converted.
     * @return Ready to parse URL.
     * @see org.tt.core.fetch.SSUDataFetcher
     * @since 1.1
     */
    URL formatURL(String departmentTag, String groupDisplayName) throws MalformedURLException;

    /**
     * Accessor. Needed for testing purposes.
     *
     * @return Filled exclusion list.
     * @since 1.0
     */
    String[] getExclusions();

}
