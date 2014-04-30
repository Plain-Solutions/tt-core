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
package org.tt.core.entity.datamanager;

/**
 * TTStatus is just enum of all the messages codes for communication between modules to improve structure and
 * create unified design of error messages.
 */
public enum TTStatus {
    GENSQL,
    TTSQL,
    DF,
    IO,

    DEPARTMENTERR,
    GROUPERR,
    TABLERR,
    IOERR,

    OK,
    OKMSG;

    public String message(TTStatus e) {
        switch (e) {
            case GENSQL:
                return "General SQL (Transaction) Error";
            case TTSQL:
                return "TT DB SQL Error";
            case DF:
                return "DataFetcher Error";
            case IO:
                return "General I/O Error";
            case DEPARTMENTERR:
                return "No such department";
            case GROUPERR:
                return "No such group";
            case TABLERR:
                return "Empty table";
            case IOERR:
                return "URL Exception";
            case OK:
                return "OK";
            case OKMSG:
            default:
                return "";
        }
    }

}