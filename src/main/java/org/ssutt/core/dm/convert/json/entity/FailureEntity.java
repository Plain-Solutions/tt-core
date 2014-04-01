/*
 * Copyright 2014 Plain Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ssutt.core.dm.convert.json.entity;


/**
 * FailureEntity is a accessor class for {@link org.ssutt.core.dm.convert.json.JSONConverter} to
 * reformat Java Object of {@link org.ssutt.core.dm.TTModule} to JsonObject properly.
 *
 * It is formatted as <code>{"module":"SOMEMODULE", "message":"exact info"}</code>
 *
 * @author Vlad Slepukhin
 * @since 1.2
 */
public class FailureEntity {
    private String error;
    private String message;

    /**
     * Creates entity.
     * @param error a string describing error (module, usually).
     * @param message a string with detailed information on error.
     */
    public FailureEntity(String error, String message) {
        this.message = message;
        this.error = error;
    }
}
