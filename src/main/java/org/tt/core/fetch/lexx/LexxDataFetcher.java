package org.tt.core.fetch.lexx;

import org.tt.core.fetch.AbstractDataFetcher;
import org.tt.core.fetch.lexx.entity.Department;
import org.tt.core.fetch.lexx.entity.Lesson;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sun.misc.BASE64Encoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
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
* Author: Avetisyan Sevak
* Date: 23.04.14.
 */
public class LexxDataFetcher implements AbstractDataFetcher {
    private static final String globDepartmentsURL = "http://www.sgu.ru/exchange/schedule_ssu_4vlad.php";
    private static final String departmentURLTemplate = "http://www.sgu.ru/exchange/schedule_ssu_4vlad.php?dep=%s";
    private static final String loginPassword = "";

    private static URLConnection getConnection(String globUrl) {
        URL url;
        URLConnection connection = null;

        try {
            url = new URL(globUrl);
            connection = url.openConnection();
            String encoded = new BASE64Encoder().encode(loginPassword.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encoded);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connection;
    }

    @Override
    public List<Department> getDepartments() {
        List<Department> departments = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(getConnection(globDepartmentsURL).getInputStream());

            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                NamedNodeMap attributes = node.getAttributes();

                String name = attributes.getNamedItem("name").getNodeValue();
                String tag = attributes.getNamedItem("id").getNodeValue();
                Node child = node.getFirstChild().getFirstChild();
                String message = child == null ? "" : child.getNodeValue();

                if (tag.equals("kgl") || tag.equals("cre")) continue;
                departments.add(new Department(name, tag, message));
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        return departments;
    }

    @Override
    public List<String> getGroups(String department) {
        List<String> groups = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(getConnection(String.format(departmentURLTemplate, department)).getInputStream());

            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                NamedNodeMap attributes = node.getAttributes();

                String name = attributes.getNamedItem("number_rus").getNodeValue();
                String type = attributes.getNamedItem("edu_form").getNodeValue();

                if (type.equals("1")) continue;
                groups.add(name);
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        Collections.sort(groups);
        return groups;
    }

    public List<List<Lesson>> getTT(String department, String group) {
        List<List<Lesson>> tt = new ArrayList<>();

        int sequence = -1;
        String parity = "";
        String subgroup = "";
        String activity = "";
        String subject = "";
        String teacher = "";
        String building = "";
        String room = "";
        long timestamp = -1L;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(getConnection(String.format(departmentURLTemplate, department)).getInputStream());

            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                NamedNodeMap attributes = node.getAttributes();

                String eduForm = attributes.getNamedItem("edu_form").getNodeValue();
                String name = attributes.getNamedItem("number_rus").getNodeValue();

                if (eduForm.equals("1")) continue;
                if (!name.equals(group)) continue;

                NodeList days = node.getChildNodes();
                for (int j = 0; j < days.getLength(); ++j) {
                    List<Lesson> curDayLessons = new ArrayList<>();
                    Node dayNode = days.item(j);

                    NodeList lessonNode = dayNode.getChildNodes();
                    for (int k = 0; k < lessonNode.getLength(); ++k) {
                        Node lessons = lessonNode.item(k);

                        NodeList lesson = lessons.getChildNodes();
                        for (int z = 0; z < lesson.getLength(); ++z) {
                            Node curLesson = lesson.item(z);
                            NamedNodeMap lessonAttr = curLesson.getAttributes();

                            activity = lessonAttr.getNamedItem("type").getNodeValue();
                            parity = lessonAttr.getNamedItem("weektype").getNodeValue();
                            sequence = Integer.parseInt(lessonAttr.getNamedItem("num").getNodeValue());
                            timestamp = Long.parseLong(lessonAttr.getNamedItem("updated").getNodeValue());

                            NodeList lessonChild = curLesson.getChildNodes();
                            for (int f = 0; f < lessonChild.getLength(); ++f) {
                                Node les = lessonChild.item(f);
                                switch (les.getNodeName()) {
                                    case "name":
                                        subject = les.getTextContent();
                                        break;
                                    case "place":
                                        building = les.getTextContent();
                                        break;
                                    case "subgroup":
                                        subgroup = les.getTextContent();
                                        break;
                                    case "teacher":
                                        teacher = les.getChildNodes().item(3).getTextContent();
                                        break;
                                }
                            }

                            curDayLessons.add(new Lesson(sequence,
                                    parity,
                                    subgroup,
                                    activity,
                                    subject,
                                    teacher,
                                    building,
                                    room,
                                    timestamp));
                        }
                    }
                    tt.add(curDayLessons);
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        return tt;
    }
}
