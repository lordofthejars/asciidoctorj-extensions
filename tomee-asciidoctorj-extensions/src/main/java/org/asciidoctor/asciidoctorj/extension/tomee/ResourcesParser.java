package org.asciidoctor.asciidoctorj.extension.tomee;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ResourcesParser {

    private static final String TRANSACTION_MANAGER = "TransactionManager";
    private static final String CONTAINER = "Container";
    private static final String RESOURCE = "Resource";

    public String transform(String target, int initialLevel) {

        org.w3c.dom.Document resourcesFile = getResourcesDocument(target);

        AsciiDocWriter asciiDocWriter = new AsciiDocWriter(
                getInitialSection(initialLevel));

        writeResources(resourcesFile, asciiDocWriter);
        writeContainers(resourcesFile, asciiDocWriter);
        writeTransactionManager(resourcesFile, asciiDocWriter);
        
        return asciiDocWriter.output();

    }

    private void writeTransactionManager(Document resourcesFile,
            AsciiDocWriter asciiDocWriter) {
        
        asciiDocWriter.startTransactionManagerSection();
        
        NodeList resources = resourcesFile.getElementsByTagName(TRANSACTION_MANAGER);

        for (int i = 0; i < resources.getLength(); i++) {
            asciiDocWriter.writeTransactionManager((Element) resources.item(i));
        }
    }

    private void writeContainers(org.w3c.dom.Document resourcesFile,
            AsciiDocWriter asciiDocWriter) {
        
        asciiDocWriter.startContainerSection();
        
        NodeList resources = resourcesFile.getElementsByTagName(CONTAINER);

        for (int i = 0; i < resources.getLength(); i++) {
            asciiDocWriter.writeResource((Element) resources.item(i));
        }
    }
    
    private void writeResources(org.w3c.dom.Document resourcesFile,
            AsciiDocWriter asciiDocWriter) {
        
        asciiDocWriter.startResourceSection();
        
        NodeList resources = resourcesFile.getElementsByTagName(RESOURCE);

        for (int i = 0; i < resources.getLength(); i++) {
            asciiDocWriter.writeResource((Element) resources.item(i));
        }
    }

    private String getInitialSection(int initialLevel) {
        StringBuilder zeroLevel = new StringBuilder("=");

        for (int i = 0; i < initialLevel; i++) {
            zeroLevel.append("=");
        }

        return zeroLevel.toString();

    }

    private org.w3c.dom.Document getResourcesDocument(String target) {

        try {
            Enumeration<InputStream> streams = Collections.enumeration(Arrays
                    .asList(new InputStream[] {
                            new ByteArrayInputStream("<root>".getBytes()),
                            new FileInputStream(target),
                            new ByteArrayInputStream("</root>".getBytes()), }));
            
            SequenceInputStream seqStream = new SequenceInputStream(streams);
            return ResourcesParser.parse(seqStream);
        } catch (ParserConfigurationException e) {
            throw new IllegalArgumentException(e);
        } catch (SAXException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    private static Document parse(InputStream resources)
            throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(resources);

    }

}
