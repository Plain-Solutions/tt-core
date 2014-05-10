/*
* Copyright 2014 Plain Solutions
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.tt.core.entity.datafetcher;

/**
 * Group is an entity to store fetched data about each group in the database and
 * return it to the users. Actually it is a wrapper around simple string to keep the same
 * design project-wide.
 *
 * @author Vlad Slepukhin
 * @since 2.0.0
 */
public class Group implements Comparable {
    /**
     * The name of the group.
     */
    String name;

    /**
     * Empty constructor.
     *
     * @since 2.0.0
     */
    public Group() {
    }

    /**
     * Configured constructor.
     *
     * @param name a {@link org.tt.core.entity.datafetcher.Group#name} of the group.
     * @since 2.0.0
     */
    public Group(String name) {
        this.name = name;
    }

    /**
     * Get the name.
     *
     * @return {@link org.tt.core.entity.datafetcher.Group#name}.
     * @since 2.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name A {@link org.tt.core.entity.datafetcher.Group#name}.
     * @since 2.0.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Used in update comparing.
     *
     * @param group A instance of this class.
     * @return 0 if <code>equals</code>.
     * @since 2.0.0
     */
    @Override
    public int compareTo(Object group) {
        return name.compareTo(((Group) group).getName());
    }

    /**
     * Used in fetching comparing.
     *
     * @param group A instance of this class.
     * @return <code>true</code> if equals, otherwise <code>false</code>.
     * @since 2.0.0
     */
    @Override
    public boolean equals(Object group) {
        return group != null && group.getClass() == Group.class && (name.equals(((Group) group).getName()));
    }
}
