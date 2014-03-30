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
package org.ssutt.core.fetch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * TTDataFetcher is an abstraction that should communicate with university web services and format temporary
 * timetable in the memory to deliver it to database with the help of DataManager.
 * <p>
 * Also, it handles exclusions for parsing, non-numerical groups (encoded by the name of the major, not number) and
 * delivering groups lists and department lists.
 * <p>
 * @author Sevak Avetisyan,Vlad Slepukhin
 * @since 1.0
 */
public interface TTDataFetcher {
    Map<String, String> getDepartments();

    List<String> getGroups(String department);

    String[][] getTT(URL url) throws IOException;

    String[] getExclusions();

    URL formatURL(String departmentTag, String groupDisplayName) throws MalformedURLException;

}
