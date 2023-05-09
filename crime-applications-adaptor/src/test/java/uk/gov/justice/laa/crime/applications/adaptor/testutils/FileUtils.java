package uk.gov.justice.laa.crime.applications.adaptor.testutils;


import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FileUtils {

    private static final String CHARACTER_ENCODING = "UTF-8";

    public  static String readFileToString(String filePath) throws IOException {
        String ret = StringUtils.EMPTY;
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        URL path = classLoader.getResource(filePath);
        if (path != null) {
            File file = new File(path.getFile());
            return org.apache.commons.io.FileUtils.readFileToString(file, CHARACTER_ENCODING);
        }
        return ret;
    }


}
