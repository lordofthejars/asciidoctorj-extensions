package org.asciidoctor.asciidoctorj.extension.jbehave.userstory;

import org.jbehave.core.model.Story;

public interface AsciiDocWriter {

    String writeStory(Story story);
    
}
