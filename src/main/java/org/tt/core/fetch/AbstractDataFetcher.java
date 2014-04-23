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

import org.tt.core.fetch.lexx.entity.Department;
import org.tt.core.fetch.lexx.entity.Group;
import org.tt.core.fetch.lexx.entity.Lesson;

import java.io.IOException;
import java.util.List;

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
     * Parses fetched data to find the tokens (tags) and names of departments. In our case, these tags are just part
     * of the url in the page.
     *
     * @return List of departments
     * @see org.tt.core.fetch.lexx.LexxDataFetcher
     * @since 1.3
     */
    List<Department> getDepartments();

    /**
     * Parses department pages to get the list of group names.
     *
     * @param department department name
     * @return List of group names.
     * @see org.tt.core.fetch.lexx.LexxDataFetcher
     * @since 1.3
     */
    List<Group> getGroups(String department);

    /**
     * Parse the resulting url of group to create a temporary, huge and complicated table from SSU website.
     *
     * @param department department name
     * @param group group name
     * @return List of List of Lessons
     * @throws IOException
     * @see org.tt.core.fetch.lexx.LexxDataFetcher
     * @since 1.0
     */
    List<List<Lesson>> getTT(String department, String group) throws IOException;
}
