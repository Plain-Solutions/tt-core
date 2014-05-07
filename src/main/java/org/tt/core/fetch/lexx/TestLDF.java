package org.tt.core.fetch.lexx;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fau on 07/05/14.
 */
public class TestLDF extends LexxDataFetcher {
    public TestLDF(String mockPassword) {
        super(mockPassword);
        setGlobDepartmentsURL("./src/test/resources/departments.xml");
        setDepartmentURLTemplate("./src/test/resources/dep-test-%s.xml");
    }

    @Override
    public InputStream parseURL(String link) throws IOException {
        return new FileInputStream(link);
    }
}
