package com.ethlo.jsons2xsd;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class RunJsonToXsd {

    public static void main(String[] args) {
        String basePath = "./";
        File baseDir = new File(basePath);
        File[] jsonFiles = baseDir.listFiles((dir1, name) -> name.endsWith(".json"));

        if (jsonFiles == null || jsonFiles.length == 0) {
            System.err.println("There is no files ending with .json in path " + baseDir.getAbsolutePath());
            System.exit(1);
        }

        for (File jsonFile : jsonFiles) {
            try (final Reader r = new FileReader(jsonFile)) {
                String xmlFileName = jsonFile.getName().substring(0, jsonFile.getName().indexOf('.')).concat(".xsd");
                final Config cfg = new Config.Builder()
                        .targetNamespace("http://example.com/" + xmlFileName)
                        .name("array")
                        .build();

                final Document doc = Jsons2Xsd.convert(r, cfg);

                XMLSerializer xml = new XMLSerializer(new FileWriter(xmlFileName), null);
                xml.serialize(doc);
            } catch (IOException e) {
                e.printStackTrace(System.err);
                System.exit(1);
            }

        }
    }
}
