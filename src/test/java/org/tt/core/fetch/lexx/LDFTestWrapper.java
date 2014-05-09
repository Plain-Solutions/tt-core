package org.tt.core.fetch.lexx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by fau on 08/05/14.
 */
public class LDFTestWrapper extends LexxDataFetcher {
    private static DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder docBuilder;


    public LDFTestWrapper() {
        super("test");
        LexxDataFetcher.setGlobalDepartmentsURL("departments");
        LexxDataFetcher.setDepartmentURLTemplate("%s");
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Document getDocFromURL(String link) throws IOException, SAXException, ParserConfigurationException {
        if (link.equals("departments")) {
            return mockAllDepartmentsXMLFile();
        }
        if (link.equals("groups")) {
            return mockGroupFile();
        }
        return mockTTXMLFile();
    }

    private Document mockAllDepartmentsXMLFile() throws ParserConfigurationException {
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("departments");
        doc.appendChild(root);

        Element bioDepartment = doc.createElement("department");
        bioDepartment.setAttribute("id", "bf");
        bioDepartment.setAttribute("name", "Biology House");
        bioDepartment.setAttribute("grid_type_id", "1");

        Element bioMessage = doc.createElement("titletext");
        bioMessage.setTextContent("Crucial information");
        bioDepartment.appendChild(bioMessage);

        root.appendChild(bioDepartment);

        Element geoDepartment = doc.createElement("department");
        geoDepartment.setAttribute("id", "gf");
        geoDepartment.setAttribute("name", "Geography House");
        geoDepartment.setAttribute("grid_type_id", "1");

        Element geoMessage = doc.createElement("titletext");
        geoDepartment.appendChild(geoMessage);

        root.appendChild(geoDepartment);

        return doc;
    }

    //just a TT file, without any non-used information
    private Document mockGroupFile() {
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("schedule");
        doc.appendChild(root);

        Element firstGroup = doc.createElement("group");

        String[] grParams = {"inner_group_id", "number", "number_rus", "edu_form", "grp_type"};
        String[] grValues = {"450", "111", "111", "0", "0"};
        for (int i = 0; i < grParams.length; i++) {
            firstGroup.setAttribute(grParams[i], grValues[i]);
        }
        root.appendChild(firstGroup);


        Element secondGroup = doc.createElement("group");

        grValues = new String[]{"451", "123", "123", "0", "1"};
        for (int i = 0; i < grParams.length; i++) {
            secondGroup.setAttribute(grParams[i], grValues[i]);
        }
        root.appendChild(secondGroup);


        Element shouldNotBeParsed = doc.createElement("group");

        grValues = new String[]{"452", "234", "234", "1", "0"};
        for (int i = 0; i < grParams.length; i++) {
            shouldNotBeParsed.setAttribute(grParams[i], grValues[i]);
        }

        root.appendChild(shouldNotBeParsed);

        Element shouldNotBeParsedToo = doc.createElement("group");

        grValues = new String[]{"453", "224", "224", "2", "1"};
        for (int i = 0; i < grParams.length; i++) {
            shouldNotBeParsedToo.setAttribute(grParams[i], grValues[i]);
        }

        root.appendChild(shouldNotBeParsedToo);

        Element stringGroup = doc.createElement("group");

        grValues = new String[]{"454", "String%2CName", "String Name", "0", "0"};
        for (int i = 0; i < grParams.length; i++) {
            stringGroup.setAttribute(grParams[i], grValues[i]);
        }
        root.appendChild(stringGroup);

        return doc;
    }

    private Document mockTTXMLFile() {
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("schedule");
        doc.appendChild(root);

        Element group = doc.createElement("group");

        String[] grParams = {"inner_group_id", "number", "number_rus", "edu_form", "grp_type"};
        String[] grValues = {"450", "111", "111", "0", "0"};
        for (int i = 0; i < grParams.length; i++) {
            group.setAttribute(grParams[i], grValues[i]);
        }

        root.appendChild(group);

        Element monday = doc.createElement("day");
        monday.setAttribute("id", "1");
        group.appendChild(monday);

        Element lessons = doc.createElement("lessons");

        lessons.appendChild(aClass(doc));
        lessons.appendChild(shouldNotBeFetchedClass(doc));

        monday.appendChild(lessons);
        return doc;
    }

    private static Element aClass(Document doc) {
        Element result = doc.createElement("lesson");
        String[] lessonParams = {"type", "weektype", "num", "updated", "date_begin", "date_end"};
        String[] lessonValues = {"practice", "full", "1", "3", "", ""};
        for (int i = 0; i < lessonParams.length; i++) {
            result.setAttribute(lessonParams[i], lessonValues[i]);
        }

        Element name = doc.createElement("name");
        name.setTextContent("Calculus");
        result.appendChild(name);

        Element place = doc.createElement("place");
        place.setTextContent("B3 room 100");
        result.appendChild(place);

        //leave it empty;
        Element subgroup = doc.createElement("subgroup");
        result.appendChild(subgroup);

        Element teacher = doc.createElement("teacher");
        teacher.setAttribute("id", "1");

        Element lastName = doc.createElement("lastname");
        lastName.setTextContent("Sakhno");

        Element tName = doc.createElement("name");
        tName.setTextContent("Ludmila");

        Element patr = doc.createElement("patronim");
        patr.setTextContent("Vladimirovna");

        Element comp = doc.createElement("compiled_fio");
        comp.setTextContent("Sakhno Ludmila Vladimirovna");

        teacher.appendChild(lastName);
        teacher.appendChild(tName);
        teacher.appendChild(patr);
        teacher.appendChild(comp);

        result.appendChild(teacher);

        return result;
    }

    private static Element shouldNotBeFetchedClass(Document doc) {
        Element result = doc.createElement("lesson");
        String[] lessonParams = {"type", "weektype", "num", "updated", "date_begin", "date_end"};
        String[] lessonValues = {"practice", "full", "2", "3", "1", "2"};
        for (int i = 0; i < lessonParams.length; i++) {
            result.setAttribute(lessonParams[i], lessonValues[i]);
        }

        Element name = doc.createElement("name");
        name.setTextContent("Empty");
        result.appendChild(name);

        return result;
    }
}
