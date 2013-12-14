package org.asciidoctor.asciidoctorj.extension.jbehave.userstory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.asciidoctor.GlobDirectoryWalker;
import org.asciidoctor.extension.IncludeProcessor;
import org.asciidoctor.internal.DocumentRuby;
import org.asciidoctor.internal.IOUtils;
import org.asciidoctor.internal.PreprocessorReader;
import org.jbehave.core.model.Story;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.parsers.StoryParser;

public class JBehaveUserStoriesIncludeProcessor extends IncludeProcessor {

    private static final String INITIAL_LEVEL_ATTRIBUTE = "initialLevel";
    private static final String MANUAL_ATTRIBUTE = "manual";
    
    public JBehaveUserStoriesIncludeProcessor(DocumentRuby documentRuby) {
        super(documentRuby);
    }

    @Override
    public boolean handles(String target) {
        return target.toLowerCase().startsWith("jbehave::user");
    }

    @Override
    public void process(PreprocessorReader reader, String target,
            Map<String, Object> attributes) {
        
        StringBuilder stories = new StringBuilder();
        
        int initialLevel = 1;
        
        if(attributes.containsKey(INITIAL_LEVEL_ATTRIBUTE)) {
            initialLevel = Integer.parseInt((String)attributes.get(INITIAL_LEVEL_ATTRIBUTE));
        }
        
        boolean manual = false;
        if(attributes.containsKey(MANUAL_ATTRIBUTE) && Boolean.parseBoolean((String) attributes.get(MANUAL_ATTRIBUTE))) {
            manual = true;
        }
        
        GlobDirectoryWalker globDirectoryWalker = new GlobDirectoryWalker(".", getUserStoryExpression(target));
        List<File> userStories = globDirectoryWalker.scan();
        
        AsciiDocWriter asciiDocWriter = new UserStoryAsciiDocWriter(initialLevel, manual);
        StoryParser storyParser = new RegexStoryParser();
        
        for (File userStory : userStories) {
            
            try {
                Story parseStory = storyParser.parseStory(readStory(userStory), userStory.getName());
                stories.append(asciiDocWriter.writeStory(parseStory));
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        }
        
        reader.push_include(stories.toString(), target, target, 1, attributes);
        
    }

    private String readStory(File userStory) throws FileNotFoundException {
        return IOUtils.readFull(new FileInputStream(userStory));
    }
    
    private String getUserStoryExpression(String target) {
        return target.substring(target.lastIndexOf(":")+1);
    }
    
}
