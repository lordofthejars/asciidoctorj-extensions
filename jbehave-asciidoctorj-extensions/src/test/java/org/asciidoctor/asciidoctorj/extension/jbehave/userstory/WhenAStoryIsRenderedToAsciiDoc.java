package org.asciidoctor.asciidoctorj.extension.jbehave.userstory;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.jbehave.core.model.Story;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.parsers.StoryParser;
import org.junit.Test;

public class WhenAStoryIsRenderedToAsciiDoc {

    private String story0 = "A story is a collection of scenarios\n" + 
            " \n" + 
            "Narrative:\n" + 
            "In order to communicate effectively to the business some functionality\n" + 
            "As a development team\n" + 
            "I want to use Behaviour-Driven Development\n" + 
            "     \n" + 
            "Scenario:  A scenario is a collection of executable steps of different type\n" + 
            " \n" + 
            "Given step represents a precondition to an event\n" + 
            "When step represents the occurrence of the event\n" + 
            "Then step represents the outcome of the event\n" + 
            " \n" + 
            "Scenario:  Another scenario exploring different combination of events\n" + 
            " \n" + 
            "Given a [precondition]\n" + 
            "When a negative event occurs\n" + 
            "Then a the outcome should [be-captured]    \n" + 
            " \n" + 
            "Examples: \n" + 
            "|precondition|be-captured|\n" + 
            "|abc|be captured    |\n" + 
            "|xyz|not be captured|";
    
    private String story1 = "Scenarios that are used as preconditions, used via GivenStories: Story_1#{idX:scenarioX}\n" + 
            "\n" + 
            "Scenario: Scenario 1\n" + 
            "\n" + 
            "Meta: @id1 scenario1\n" + 
            "\n" + 
            "Given a stock of symbol XXX and a threshold of 1.0\n" + 
            "\n" + 
            "Scenario: Scenario 2\n" + 
            "\n" + 
            "Meta: @id2 scenario2\n" + 
            "\n" + 
            "Given a stock of symbol XXX and a threshold of 2.0";
    
    private String story2 = "Scenario: Traders can be searched by name\n" + 
            "\n" + 
            "Given the trader ranks: \n" + 
            "|name |rank    |\n" + 
            "|Larry|Stooge 3|\n" + 
            "|Moe  |Stooge 1|\n" + 
            "|Curly|Stooge 2|\n" + 
            "Then the current trader activity is: \n" + 
            "|name |trades|\n" + 
            "|Larry|30000 |\n" + 
            "|Moe  |10000 |\n" + 
            "|Curly|20000 |\n" + 
            "!-- This is a comment, which will be ignored in the execution\n" + 
            "When traders are subset to \".*y\" by name\n" + 
            "!-- This is another comment, also ignored, \n" + 
            "but look Ma! I'm on a new line!\n" + 
            "Then the traders returned are:\n" + 
            "|name |rank    |\n" + 
            "|Larry|Stooge 3|\n" + 
            "|Curly|Stooge 2|\n" + 
            "\n" + 
            "Scenario: Traders can be searched by name in landscape format\n" + 
            "\n" + 
            "Given the trader ranks: \n" + 
            "{transformer=FROM_LANDSCAPE}\n" + 
            "|name |Larry   |Moe     |Curly   |\n" + 
            "|rank |Stooge 1|Stooge 2|Stooge 3|\n" + 
            "Then the current trader activity is: \n" + 
            "|name |trades|\n" + 
            "|Larry|30000 |\n" + 
            "|Moe  |10000 |\n" + 
            "|Curly|20000 |\n" + 
            "Then the traders returned are:\n" + 
            "|name |rank    |\n" + 
            "|Larry|Stooge 3|\n" + 
            "|Curly|Stooge 2|\n" + 
            "\n" + 
            "Scenario: Traders search fails\n" + 
            "\n" + 
            "Given the trader ranks: \n" + 
            "|name |rank    |\n" + 
            "|Larry|Stooge 3|\n" + 
            "|Moe  |Stooge 1|\n" + 
            "|Curly|Stooge 2|\n" + 
            "!-- Verification fails\n" + 
            "Then the traders returned are:\n" + 
            "|name |rank    |\n" + 
            "|Moe  |Stooge 1|\n" + 
            "|Curly|Stooge 2|";
    
    private static final String EXPECTED_SIMPLE_OUTPUT = "[[Story_1]]\n" + 
    		"== Story_1\n" + 
    		"\n" + 
    		".Description\n" + 
    		"----\n" + 
    		"Scenarios that are used as preconditions, used via GivenStories: Story_1#{idX:scenarioX}\n" + 
    		"----\n" + 
    		"\n" + 
    		".Given Stories\n" + 
    		"[NOTE]\n" + 
    		"====\n" + 
    		"<<Story_1, Story_1>>#idX:scenarioX\n" + 
    		"====\n" + 
    		"\n" + 
    		"=== Scenario: Scenario 1\n" + 
    		"\n" + 
    		".Meta\n" + 
    		"----\n" + 
    		"+id1+: scenario1+\n" + 
    		"----\n" + 
    		"\n" + 
    		".Steps\n" + 
    		"----\n" + 
    		"Given a stock of symbol XXX and a threshold of 1.0\n" + 
    		"----\n" + 
    		"\n" + 
    		"=== Scenario: Scenario 2\n" + 
    		"\n" + 
    		".Meta\n" + 
    		"----\n" + 
    		"+id2+: scenario2+\n" + 
    		"----\n" + 
    		"\n" + 
    		".Steps\n" + 
    		"----\n" + 
    		"Given a stock of symbol XXX and a threshold of 2.0\n" + 
    		"----";
    
    
    private static final String EXPECTED_NARRATIVE_OUTPUT = "[[Story 0]]\n" +
            "== Story 0\n" + 
    		"\n" + 
    		".Description\n" + 
    		"----\n" + 
    		"A story is a collection of scenarios\n" + 
    		"----\n" + 
    		"\n" + 
    		".Narrative\n" + 
    		"****\n" + 
    		"*In Order To* communicate effectively to the business some functionality\n" + 
    		"\n" + 
    		"*As a* development team\n" + 
    		"\n" + 
    		"*I Want To* use Behaviour-Driven Development\n" + 
    		"\n" + 
    		"****\n" + 
    		"\n" + 
    		"=== Scenario: A scenario is a collection of executable steps of different type\n" + 
    		"\n" + 
    		".Steps\n" + 
    		"----\n" + 
    		"Given step represents a precondition to an event\n" + 
    		"When step represents the occurrence of the event\n" + 
    		"Then step represents the outcome of the event\n" + 
    		"----\n" + 
    		"\n" + 
    		"=== Scenario: Another scenario exploring different combination of events\n" + 
    		"\n" + 
    		".Steps\n" + 
    		"----\n" + 
    		"Given a [precondition]\n" + 
    		"When a negative event occurs\n" + 
    		"Then a the outcome should [be-captured]\n" + 
    		"----\n" + 
    		"\n" + 
    		"[options=\"header\"]\n" + 
    		".Examples\n" + 
    		"|===\n" + 
    		"|precondition|be-captured\n" + 
    		"|abc\n" + 
    		"|be captured\n" + 
    		"\n" + 
    		"|xyz\n" + 
    		"|not be captured\n" + 
    		"\n" + 
    		"|===";
    
    private static final String EXPECTED_STEPS_WITH_EXAMPLES_OUTPUT = "[[Story 2]]\n" +
            "== Story 2\n" + 
    		"\n" + 
    		"=== Scenario: Traders can be searched by name\n" + 
    		"\n" + 
    		".Steps\n" + 
    		"----\n" + 
    		"Given the trader ranks: \n" + 
    		"|name |rank    |\n" + 
    		"|Larry|Stooge 3|\n" + 
    		"|Moe  |Stooge 1|\n" + 
    		"|Curly|Stooge 2|\n" + 
    		"Then the current trader activity is: \n" + 
    		"|name |trades|\n" + 
    		"|Larry|30000 |\n" + 
    		"|Moe  |10000 |\n" + 
    		"|Curly|20000 |\n" + 
    		"!-- This is a comment, which will be ignored in the execution\n" + 
    		"When traders are subset to \".*y\" by name\n" + 
    		"!-- This is another comment, also ignored, \n" + 
    		"but look Ma! I'm on a new line!\n" + 
    		"Then the traders returned are:\n" + 
    		"|name |rank    |\n" + 
    		"|Larry|Stooge 3|\n" + 
    		"|Curly|Stooge 2|\n" + 
    		"----\n" + 
    		"\n" + 
    		"=== Scenario: Traders can be searched by name in landscape format\n" + 
    		"\n" + 
    		".Steps\n" + 
    		"----\n" + 
    		"Given the trader ranks: \n" + 
    		"{transformer=FROM_LANDSCAPE}\n" + 
    		"|name |Larry   |Moe     |Curly   |\n" + 
    		"|rank |Stooge 1|Stooge 2|Stooge 3|\n" + 
    		"Then the current trader activity is: \n" + 
    		"|name |trades|\n" + 
    		"|Larry|30000 |\n" + 
    		"|Moe  |10000 |\n" + 
    		"|Curly|20000 |\n" + 
    		"Then the traders returned are:\n" + 
    		"|name |rank    |\n" + 
    		"|Larry|Stooge 3|\n" + 
    		"|Curly|Stooge 2|\n" + 
    		"----\n" + 
    		"\n" + 
    		"=== Scenario: Traders search fails\n" + 
    		"\n" + 
    		".Steps\n" + 
    		"----\n" + 
    		"Given the trader ranks: \n" + 
    		"|name |rank    |\n" + 
    		"|Larry|Stooge 3|\n" + 
    		"|Moe  |Stooge 1|\n" + 
    		"|Curly|Stooge 2|\n" + 
    		"!-- Verification fails\n" + 
    		"Then the traders returned are:\n" + 
    		"|name |rank    |\n" + 
    		"|Moe  |Stooge 1|\n" + 
    		"|Curly|Stooge 2|\n" + 
    		"----";
    
    private static final String EXPECTED_STEPS_WITH_MANUAL_DECISION = "[[Story Manual]]\n" + 
    		"== Story Manual\n" + 
    		"\n" +
    		"=== Scenario: Traders can be searched by name\n" + 
    		"\n" + 
    		".Steps\n" + 
    		"----\n" + 
    		"Given the trader ranks: \n" + 
    		"|name |rank    |\n" + 
    		"|Larry|Stooge 3|\n" + 
    		"|Moe  |Stooge 1|\n" + 
    		"|Curly|Stooge 2|\n" + 
    		"Then the current trader activity is: \n" + 
    		"|name |trades|\n" + 
    		"|Larry|30000 |\n" + 
    		"|Moe  |10000 |\n" + 
    		"|Curly|20000 |\n" + 
    		"!-- This is a comment, which will be ignored in the execution\n" + 
    		"When traders are subset to \".*y\" by name\n" + 
    		"!-- This is another comment, also ignored, \n" + 
    		"but look Ma! I'm on a new line!\n" + 
    		"Then the traders returned are:\n" + 
    		"|name |rank    |\n" + 
    		"|Larry|Stooge 3|\n" + 
    		"|Curly|Stooge 2|\n" + 
    		"----\n" + 
    		"\n" +
    		".Result\n"+
    		"****\n" + 
    		"- [ ] PASS\n" + 
    		"\n" + 
    		"- [ ] FAIL\n" + 
    		"\n" + 
    		"- [ ] COMMENTS: \n" + 
    		"****\n" + 
    		"\n" + 
    		"=== Scenario: Traders can be searched by name in landscape format\n" + 
    		"\n" + 
    		".Steps\n" + 
    		"----\n" + 
    		"Given the trader ranks: \n" + 
    		"{transformer=FROM_LANDSCAPE}\n" + 
    		"|name |Larry   |Moe     |Curly   |\n" + 
    		"|rank |Stooge 1|Stooge 2|Stooge 3|\n" + 
    		"Then the current trader activity is: \n" + 
    		"|name |trades|\n" + 
    		"|Larry|30000 |\n" + 
    		"|Moe  |10000 |\n" + 
    		"|Curly|20000 |\n" + 
    		"Then the traders returned are:\n" + 
    		"|name |rank    |\n" + 
    		"|Larry|Stooge 3|\n" + 
    		"|Curly|Stooge 2|\n" + 
    		"----\n" + 
    		"\n" +
    		".Result\n" + 
    		"****\n" + 
    		"- [ ] PASS\n" + 
    		"\n" + 
    		"- [ ] FAIL\n" + 
    		"\n" + 
    		"- [ ] COMMENTS: \n" + 
    		"****\n" + 
    		"\n" + 
    		"=== Scenario: Traders search fails\n" + 
    		"\n" + 
    		".Steps\n" + 
    		"----\n" + 
    		"Given the trader ranks: \n" + 
    		"|name |rank    |\n" + 
    		"|Larry|Stooge 3|\n" + 
    		"|Moe  |Stooge 1|\n" + 
    		"|Curly|Stooge 2|\n" + 
    		"!-- Verification fails\n" + 
    		"Then the traders returned are:\n" + 
    		"|name |rank    |\n" + 
    		"|Moe  |Stooge 1|\n" + 
    		"|Curly|Stooge 2|\n" + 
    		"----\n" + 
    		"\n" + 
    		".Result\n" + 
    		"****\n" + 
    		"- [ ] PASS\n" + 
    		"\n" + 
    		"- [ ] FAIL\n" + 
    		"\n" + 
    		"- [ ] COMMENTS: \n" + 
    		"****";
    
    @Test
    public void simple_scenarios_should_be_rendered() {
        
        StoryParser storyParser = new RegexStoryParser();
        Story story = storyParser.parseStory(story1, "Story_1");
        
        AsciiDocWriter asciiDocWriter = new UserStoryAsciiDocWriter(1, false);
        String asciiDocStory = asciiDocWriter.writeStory(story);
        
        assertThat(asciiDocStory.trim(), is(EXPECTED_SIMPLE_OUTPUT));
        
    }
    
    @Test
    public void story_with_narrative_should_be_rendered() {
        
        StoryParser storyParser = new RegexStoryParser();
        Story story = storyParser.parseStory(story0, "Story 0");
        
        AsciiDocWriter asciiDocWriter = new UserStoryAsciiDocWriter(1, false);
        String asciiDocStory = asciiDocWriter.writeStory(story);
        
        assertThat(asciiDocStory.trim(), is(EXPECTED_NARRATIVE_OUTPUT));
        
    }
    
    @Test
    public void steps_with_examples_should_be_rendered() {
        
        StoryParser storyParser = new RegexStoryParser();
        Story story = storyParser.parseStory(story2, "Story 2");
        
        AsciiDocWriter asciiDocWriter = new UserStoryAsciiDocWriter(1, false);
        String asciiDocStory = asciiDocWriter.writeStory(story);
        
        assertThat(asciiDocStory.trim(), is(EXPECTED_STEPS_WITH_EXAMPLES_OUTPUT));
        
    }
    
    @Test
    public void manual_steps_should_be_rendered_with_checkboxes() {
        
        StoryParser storyParser = new RegexStoryParser();
        Story story = storyParser.parseStory(story2, "Story Manual");
        
        AsciiDocWriter asciiDocWriter = new UserStoryAsciiDocWriter(1, true);
        String asciiDocStory = asciiDocWriter.writeStory(story);
        
        assertThat(asciiDocStory.trim(), is(EXPECTED_STEPS_WITH_MANUAL_DECISION));
        
    }
    
}
