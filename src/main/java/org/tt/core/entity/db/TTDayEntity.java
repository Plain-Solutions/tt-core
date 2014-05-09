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
package org.tt.core.entity.db;


import java.util.ArrayList;
import java.util.List;

/**
 * TTDayEntity is an entity, required for formatting {@link org.tt.core.entity.db.TTEntity}. It
 * contains the name of the day and list of all lessons which are on that day.
 * <p/>
 * Don't mess up {@link org.tt.core.entity.db.TTEntity} and {@link org.tt.core.entity.datafetcher.Lesson}. Here
 * the <b>first</b> is used.
 *
 * @author Vlad Slpeukhin
 * @since 2.0.0
 */
public class TTDayEntity {
    /**
     * The name of the day.
     */
    private String name;
    /**
     * The list of the lessons this day.
     */
    private List<TTLesson> lessons;

    /**
     * Empty constructor.
     *
     * @since 2.0.0
     */
    public TTDayEntity() {
        lessons = new ArrayList<>();
    }

    /**
     * Configured constructor.
     *
     * @param name    The name of the day.
     * @param lessons The list of the lessons.
     * @see org.tt.core.entity.db.TTLesson
     * @since 2.0.0
     */
    public TTDayEntity(String name, List<TTLesson> lessons) {
        lessons = new ArrayList<>();

        this.name = name;
        this.lessons = lessons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TTLesson> getLessons() {
        return lessons;
    }

    /**
     * Set the whole list of lessons.
     *
     * @param lessons The list of the lessons.
     * @since 2.0.0
     */
    public void setLessons(List<TTLesson> lessons) {
        this.lessons = lessons;
    }

    /**
     * Append a lesson.
     *
     * @param lesson The instance of {@link org.tt.core.entity.db.TTLesson}.
     * @see org.tt.core.entity.db.TTLesson
     */
    public void append(TTLesson lesson) {
        lessons.add(lesson);
    }
}
