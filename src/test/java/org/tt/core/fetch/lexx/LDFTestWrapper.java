package org.tt.core.fetch.lexx;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by fau on 08/05/14.
 */
public class LDFTestWrapper extends LexxDataFetcher {
    public LDFTestWrapper() {
        super("test");
    }

    @Override
    protected Document getDocFromURL(String link) throws IOException, SAXException, ParserConfigurationException {
        return null;
    }

    private Document mockAllDepartmentsXMLFile() {
        return null;
    }

    private Document mockTTXMLFile() {
        return null;
    }
}
