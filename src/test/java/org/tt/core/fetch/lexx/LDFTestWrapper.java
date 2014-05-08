package org.tt.core.fetch.lexx;

import org.tt.core.entity.datafetcher.Department;
import org.w3c.dom.Attr;
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
    public LDFTestWrapper() {
        super("test");
        LexxDataFetcher.setGlobDepartmentsURL("departments");
    }

    @Override
    protected Document getDocFromURL(String link) throws IOException, SAXException, ParserConfigurationException {
        if (link.equals("departments")) {
            return mockAllDepartmentsXMLFile();
        }
        return null;
    }

    private Document mockAllDepartmentsXMLFile() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

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

    private Document mockTTXMLFile() {
        return null;
    }
}
