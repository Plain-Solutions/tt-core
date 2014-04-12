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
package org.tt.core.dm;

/**
 * TTData is a library-wide class which provides delivering converted raw data (in JSON or some other format) to
 * the world.
 * <p/>
 * It has a field for response code/status and the contents.
 */
public class TTData {
    private int httpCode;
    private String message;

    public TTData() {
    }

    public TTData(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int getHttpCode() {
        return httpCode;
    }

    public String getMessage() {
        return message;
    }
}
