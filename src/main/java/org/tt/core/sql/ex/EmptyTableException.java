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
package org.tt.core.sql.ex;

/**
 * This specific exception can be required to deliver exact information about error, occurred during some query
 * to end user. This Exception throws when the user asks for timetable and group and department exist and correct.
 * For some strange reason, the table can be empty (mainly, due to SSU managers mistakes). That's why we don't want
 * to process the empty table and should return info that we have no info for the user.
 * <p>
 * Actually, TT Platform uses this exception to deliver informative error messages.
 *
 * @author Vlad Slepukhin
 * @since 1.0
 */
public class EmptyTableException extends Exception {
    public EmptyTableException() {

    }

    public EmptyTableException(String message) {

    }
}
