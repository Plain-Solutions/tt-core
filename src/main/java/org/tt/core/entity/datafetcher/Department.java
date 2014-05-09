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
 * Department is an entity to store fetched data about each department in the database and
 * return it to the users.
 *
 * @author Vlad Slepukhin
 * @author Sevak Avetisyan
 * @since 2.0.0
 */
public class Department {
    /**
     * The full displayable name.
     */
    private String name;
    /**
     * The department abbreviation (mm, knt, sf or so)
     */
    private String tag;
    /**
     * The department message - some crucial information from the dispatchers
     * or head of department: additional classes, even/odd weeks schedule or so.
     */
    private String message;

    /**
     * Empty constructor.
     * @since 2.0.0
     */
    public Department() {
    }

    /**
     * Configured constructor.
     * @param name The displayable name.
     * @param tag The department abbreviation.
     * @param message Crucial information.
     * @since 2.0.0
     */
    public Department(String name, String tag, String message) {
        this.name = name;
        this.tag = tag;
        this.message = message;
    }

    /**
     * Get the name.
     * @return {@link org.tt.core.entity.datafetcher.Department#name}.
     * @since 2.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * Get the abbreviation.
     * @return {@link org.tt.core.entity.datafetcher.Department#tag}.
     * @since 2.0.0
     */
    public String getTag() {
        return tag;
    }

    /**
     * Get the message.
     * @return {@link org.tt.core.entity.datafetcher.Department#message}.
     * @since 2.0.0
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the name.
     * @param name A {@link org.tt.core.entity.datafetcher.Department#name}.
     * @since 2.0.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the tag.
     * @param tag A {@link org.tt.core.entity.datafetcher.Department#tag}.
     * @since 2.0.0
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Set the message.
     * @param message A {@link org.tt.core.entity.datafetcher.Department#message}.
     * @since 2.0.0
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Check the equality by main features of Department: its name and tag.
     * @param dep An object to compare to.
     * @return <code>true</code> if equals, otherwise <code>false</code>.
     * @since 2.0.0
     */
    @Override
    public boolean equals(Object dep) {
        return dep != null && dep.getClass() == Department.class && tag.equals(((Department) dep).getTag()) &&
                name.equals(((Department) dep).getName());

    }

    /**
     * Fancy output for tracing.
     * @return {@link org.tt.core.entity.datafetcher.Department#name},
     * {@link org.tt.core.entity.datafetcher.Department#tag}, {@link org.tt.core.entity.datafetcher.Department#message}
     * split by whitespaces.
     * @since 2.1.0
     */
    @Override
    public String toString() {
        return new StringBuilder().append(name).append(" ").append(tag).append(" ").append(message).toString();
    }
}
