package uk.gov.justice.laa.crime.applications.adaptor.utils;


import java.io.File;
import java.io.IOException;

public class FileUtils {

    public  static String readFileToString(String filePath) throws IOException {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        return org.apache.commons.io.FileUtils.readFileToString(file);
    }

}
