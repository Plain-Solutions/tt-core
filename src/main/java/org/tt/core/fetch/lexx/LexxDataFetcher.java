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
package org.tt.core.fetch.lexx;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.tt.core.entity.datafetcher.Department;
import org.tt.core.entity.datafetcher.Group;
import org.tt.core.entity.datafetcher.Lesson;
import org.tt.core.fetch.AbstractDataFetcher;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * LexxDataFetcher is an implementation of AbstractDataFetcher. LDF is a set of XML files brought to us by SSU from
 * their internal database.
 *
 * @author Sevak Avetisyan
 * @since 2.0
 */
public class LexxDataFetcher implements AbstractDataFetcher {
    private static String globDepartmentsURL = "http://www.sgu.ru/exchange/schedule_ssu_4vlad.php";
    private static String departmentURLTemplate = "http://www.sgu.ru/exchange/schedule_ssu_4vlad.php?dep=%s";
    private String loginPassword = "";

    public LexxDataFetcher() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("property"));
            loginPassword = properties.getProperty("loginPassword");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LexxDataFetcher(String loginPassword) { this.loginPassword = loginPassword; }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public static void setGlobDepartmentsURL(String globDepartmentsURL) {
        LexxDataFetcher.globDepartmentsURL = globDepartmentsURL;
    }

    public static void setDepartmentURLTemplate(String departmentURLTemplate) {
        LexxDataFetcher.departmentURLTemplate = departmentURLTemplate;
    }

    protected InputStream parseURL(String link) throws IOException {
        URL url;
        if (link.contains("./src/test/resources")) {
            return new FileInputStream(new File(link));
        }
        URLConnection conn = null;

        try {
            url = new URL(link);
            conn = url.openConnection();
            String encoded = Base64.encodeBase64String(StringUtils.getBytesUtf8(loginPassword));
            conn.setRequestProperty("Authorization", "Basic " + encoded);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (conn != null) {
            return conn.getInputStream();
        }
        else
            throw new UnknownHostException();
    }

    @Override
    public List<Department> getDepartments() {
        List<Department> departments = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(parseURL(globDepartmentsURL));

            NodeList nodeList = doc.getDocumentElement().getChildNodes();
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
    public List<Group> getGroups(String department) {
        List<Group> groups = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(parseURL(String.format(departmentURLTemplate, department)));

            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                NamedNodeMap attributes = node.getAttributes();

                String name = attributes.getNamedItem("number_rus").getNodeValue();
                String eduForm = attributes.getNamedItem("edu_form").getNodeValue();
                String groupType = attributes.getNamedItem("grp_type").getNodeValue();

                if (eduForm.equals("1")||(eduForm.equals("2")&&groupType.equals("1"))) {
                    continue;
                }

                groups.add(new Group(name.trim()));
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
            Document document = builder.parse(parseURL(String.format(departmentURLTemplate, department)));

            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                NamedNodeMap attributes = node.getAttributes();

                String eduForm = attributes.getNamedItem("edu_form").getNodeValue();
                String name = attributes.getNamedItem("number_rus").getNodeValue().trim();
                String groupType = attributes.getNamedItem("grp_type").getNodeValue();

                if (eduForm.equals("1")||(eduForm.equals("2")&&groupType.equals("1"))) continue;
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
                            timestamp = Long.parseLong(lessonAttr.getNamedItem("updated").getNodeValue()) * 1000;
                            String dateBeginStr = lessonAttr.getNamedItem("date_begin").getNodeValue();
                            String dateEndStr = lessonAttr.getNamedItem("date_end").getNodeValue();

                            if(!(dateBeginStr.equals("") && dateEndStr.equals(""))) {
                                long dateBegin = Long.parseLong(dateBeginStr) * 1000;
                                long dateEnd = Long.parseLong(dateEndStr) * 1000;
                                long curTime = System.currentTimeMillis();
                                if(!(curTime >= dateBegin && curTime <= dateEnd)) continue;
                            }

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
                                        teacher = teacher.replace('.', ' ').trim();
                                        break;
                                }
                            }

                            curDayLessons.add(new Lesson(sequence, parity, subgroup, activity, subject,
                                                         teacher, building, room, timestamp));
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
