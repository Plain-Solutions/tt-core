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
package org.ssutt.core.dm;

import java.util.List;
import java.util.Map;

public interface AbstractDataConverter {
    String convertDepartmentList(Map<String, Map<String, String>> departments);

    String convertGroupList(List<String> names);

    String convertAbstractList(List<String> list);

    String convertGroupName(int id);

    String convertTT(List<String[]> table);

    String convertFailure(TTModule module, String msg);

    String convertFailure(TTModule module, TTModule err);

}
