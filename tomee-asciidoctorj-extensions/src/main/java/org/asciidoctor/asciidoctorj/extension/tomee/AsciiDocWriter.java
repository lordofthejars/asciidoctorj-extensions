package org.asciidoctor.asciidoctorj.extension.tomee;

import java.util.Scanner;

import org.w3c.dom.Element;

public class AsciiDocWriter {

    private static final String SECTION_KEY = "=";

    private static final String NEW_LINE = System.getProperty("line.separator");

    private StringBuilder outputContent = new StringBuilder();
    private String initialSection = SECTION_KEY;

    public AsciiDocWriter(String initialSection) {
        this.initialSection = initialSection;
    }

    public void startResourceSection() {
        this.outputContent.append(this.initialSection).append(" ")
                .append("Resources").append(NEW_LINE).append(NEW_LINE);
    }

    public void writeResource(Element resource) {

        String resourceName = resource.getAttribute("id");
        String resourceType = resource.hasAttribute("type") ? resource
                .getAttribute("type") : resource.getAttribute("class-name");

        this.outputContent.append(this.initialSection).append(SECTION_KEY)
                .append(" ").append(resourceName).append(" [")
                .append(resourceType).append("]").append(NEW_LINE)
                .append(NEW_LINE);

        this.writeProperties(resource);

    }

    public void startContainerSection() {
        this.outputContent.append(this.initialSection).append(" ")
                .append("Containers").append(NEW_LINE).append(NEW_LINE);
    }
    
    public void writeContainer(Element container) {
        
        String resourceName = container.getAttribute("id");
        String resourceType = container.getAttribute("type") ;

        this.outputContent.append(this.initialSection).append(SECTION_KEY)
                .append(" ").append(resourceName).append(" [")
                .append(resourceType).append("]").append(NEW_LINE)
                .append(NEW_LINE);

        this.writeProperties(container);
        
    }
    
    public void startTransactionManagerSection() {
        this.outputContent.append(this.initialSection).append(" ")
                .append("Transaction Manager").append(NEW_LINE).append(NEW_LINE);
    }
    
    public void writeTransactionManager(Element transactionManager) {
        
        String resourceType = transactionManager.getAttribute("type") ;

        this.outputContent.append(this.initialSection).append(SECTION_KEY)
                .append(" ").append(resourceType).append(NEW_LINE)
                .append(NEW_LINE);

        this.writeProperties(transactionManager);
        
    }
    
    public String output() {
        return this.outputContent.toString();
    }

    private void writeProperties(Element resource) {
        
        if("".equals(resource.getTextContent().trim())) {
            writeInfoMesage();
            
        } else {
            writePropertiesTable(resource);
        }
    }

    private void writeInfoMesage() {
        this.outputContent.append("CAUTION:").append(" No properties has been definde and deafult values are being used.").append(NEW_LINE).append(NEW_LINE);
    }

    private void writePropertiesTable(Element resource) {
        this.outputContent.append("|===").append(NEW_LINE);
        this.outputContent.append("|").append("*Property Name* ")
                .append("|*Property Value*").append(NEW_LINE);
        this.outputContent.append(NEW_LINE);

        String properties = resource.getTextContent();

        Scanner scanner = new Scanner(properties);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (isNotAComment(line) && isNotEmpty(line)) {

                int initialWhiteSapceIndex = getPropertiesIndex(line);

                this.outputContent
                        .append("|")
                        .append(line.substring(0, initialWhiteSapceIndex)
                                .trim()).append(NEW_LINE);
                
                if(initialWhiteSapceIndex != line.length()) {
                
                this.outputContent
                        .append("|")
                        .append(line.substring(initialWhiteSapceIndex+1,
                                line.length()).trim()).append(NEW_LINE);
                } else {
                    this.outputContent
                    .append("|")
                    .append(" ").append(NEW_LINE);
                }
                this.outputContent.append(NEW_LINE);
            }

        }

        this.outputContent.append("|===").append(NEW_LINE).append(NEW_LINE);
    }

    private boolean isNotEmpty(String line) {
        return !"".equals(line.trim());
    }

    private int getPropertiesIndex(String value) {

        int index = value.indexOf('=');

        if (index == -1) {
            index = value.length();
        }

        return index;

    }

    private boolean isNotAComment(String line) {
        return !line.startsWith("#");
    }

}
