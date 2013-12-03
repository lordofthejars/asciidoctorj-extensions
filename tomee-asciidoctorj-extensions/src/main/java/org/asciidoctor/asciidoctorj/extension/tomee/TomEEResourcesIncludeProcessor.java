package org.asciidoctor.asciidoctorj.extension.tomee;

import java.util.Map;

import org.asciidoctor.extension.IncludeProcessor;
import org.asciidoctor.internal.DocumentRuby;
import org.asciidoctor.internal.PreprocessorReader;

public class TomEEResourcesIncludeProcessor extends IncludeProcessor {

    private static final String INITIAL_LEVEL_ATTRIBUTE = "initialLevel";
    
    public TomEEResourcesIncludeProcessor(DocumentRuby documentRuby) {
        super(documentRuby);
    }

    @Override
    public boolean handles(String target) {
        return target.toLowerCase().startsWith("tomee:");
    }

    @Override
    public void process(PreprocessorReader reader, String target,
            Map<String, Object> attributes) {
        
        int initialLevel = 1;
        
        if(attributes.containsKey(INITIAL_LEVEL_ATTRIBUTE)) {
            initialLevel = Integer.parseInt((String)attributes.get(INITIAL_LEVEL_ATTRIBUTE));
        }
        
        ResourcesParser resourcesParser = new ResourcesParser();
        String asciiDocOutput = resourcesParser.transform(getResourceFile(target), initialLevel);
        
        reader.push_include(asciiDocOutput.toString(), target, target, 1, attributes);

    }

    private String getResourceFile(String target) {
        return target.substring(target.indexOf(":")+1);
    }
    
}
