package org.asciidoctor.asciidoctorj.extension.jbehave.userstory;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbehave.core.model.Description;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.GivenStories;
import org.jbehave.core.model.GivenStory;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Narrative;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;

public class UserStoryAsciiDocWriter implements AsciiDocWriter {

    private static final int SECTION_KEY = 1;

    private static final String NEW_LINE = System.getProperty("line.separator");

    private int initialLevel = SECTION_KEY;
    private boolean manual = false;

    public UserStoryAsciiDocWriter(int initialLevel) {
        this.initialLevel = initialLevel;
    }

    public UserStoryAsciiDocWriter(int initialLevel, boolean manual) {
        this.initialLevel = initialLevel;
        this.manual = manual;
    }

    @Override
    public String writeStory(Story story) {

        StringBuilder outputContent = new StringBuilder();

        outputContent.append("[[").append(story.getName()).append("]]")
                .append(NEW_LINE).append(getInitialSection(initialLevel))
                .append(" ").append(story.getName()).append(NEW_LINE)
                .append(NEW_LINE);

        Description description = story.getDescription();
        if (isDescriptionProvided(description)) {
            outputContent.append(renderDescription(description));
        }

        Meta meta = story.getMeta();
        if (isMetaProvided(meta)) {
            outputContent.append(renderMetaInformation(meta));
        }

        Narrative narrative = story.getNarrative();
        if (isNarrativeProvided(narrative)) {
            outputContent.append(renderNarrative(narrative));
        }

        GivenStories givenStories = story.getGivenStories();
        if (areGivenStoriesProvided(givenStories)) {
            outputContent.append(renderGivenStories(givenStories));
        }

        List<Scenario> scenarios = story.getScenarios();
        if (areScenariosProvided(scenarios)) {
            outputContent.append(writeScenarios(scenarios));
        } else {
            // WARNING?¿
        }

        return outputContent.toString();
    }

    private boolean areScenariosProvided(List<Scenario> scenarios) {
        return scenarios != null && scenarios.size() > 0;
    }

    private boolean areGivenStoriesProvided(GivenStories givenStories) {
        return givenStories != null && givenStories.getStories().size() > 0;
    }

    private boolean isNarrativeProvided(Narrative narrative) {
        return narrative != null && !Narrative.EMPTY.equals(narrative);
    }

    private boolean isDescriptionProvided(Description description) {
        return description != null && !"".equals(description.asString().trim());
    }

    private boolean isMetaProvided(Meta meta) {
        return meta != null && !Meta.EMPTY.equals(meta);
    }

    public String renderNarrative(Narrative narrative) {

        String narrativeInformation = ".Narrative" + NEW_LINE + "****"
                + NEW_LINE;

        String inOrderTo = narrative.inOrderTo();

        if (inOrderTo != null) {
            narrativeInformation += "*In Order To* " + inOrderTo + NEW_LINE
                    + NEW_LINE;
        }

        String asA = narrative.asA();

        if (asA != null) {
            narrativeInformation += "*As a* " + asA + NEW_LINE + NEW_LINE;
        }

        String iWantTo = narrative.iWantTo();

        if (iWantTo != null) {
            narrativeInformation += "*I Want To* " + iWantTo + NEW_LINE
                    + NEW_LINE;
        }

        narrativeInformation += "****" + NEW_LINE + NEW_LINE;

        return narrativeInformation;

    }

    private String renderGivenStories(GivenStories givenStories) {

        String givenStoriesInformation = ".Given Stories" + NEW_LINE + "[NOTE]"
                + NEW_LINE + "====" + NEW_LINE;

        List<GivenStory> stories = givenStories.getStories();

        for (GivenStory givenStory : stories) {
            String path = givenStory.getPath();
            givenStoriesInformation += "<<" + getStoryIdentifier(path) + ", " + path + ">>";
            
            if(givenStory.getAnchor() != null && !"".equals(givenStory.getAnchor().trim())) {
                givenStoriesInformation += "#" + givenStory.getAnchor();
            }
            
            givenStoriesInformation += NEW_LINE;
            
        }

        givenStoriesInformation += "====" + NEW_LINE + NEW_LINE;

        return givenStoriesInformation;

    }

    private String renderDescription(Description description) {

        String descriptionInformation = ".Description" + NEW_LINE + "----"
                + NEW_LINE;

        descriptionInformation += description.asString() + NEW_LINE;

        descriptionInformation += "----" + NEW_LINE + NEW_LINE;

        return descriptionInformation;

    }

    private String renderMetaInformation(Meta meta) {
        String metaInformation = ".Meta" + NEW_LINE + "----" + NEW_LINE;

        Set<String> propertyNames = meta.getPropertyNames();

        for (String propertyName : propertyNames) {
            String propertyValue = meta.getProperty(propertyName);

            metaInformation += "+" + propertyName + "+: " + propertyValue + "+"
                    + NEW_LINE;
        }

        metaInformation += "----" + NEW_LINE + NEW_LINE;

        return metaInformation;

    }

    private String writeScenarios(List<Scenario> scenarios) {
        StringBuilder scenarionsOutput = new StringBuilder();
        for (int i = 0; i < scenarios.size(); i++) {
            scenarionsOutput.append(renderScenario(scenarios.get(i), i));
        }
        return scenarionsOutput.toString();
    }

    private String renderScenario(Scenario scenario, int currentScenarioNumber) {

        StringBuilder scenarioBuilder = new StringBuilder();

        scenarioBuilder.append(renderTitle(scenario, currentScenarioNumber));

        Meta meta = scenario.getMeta();
        if (isMetaProvided(meta)) {
            scenarioBuilder.append(renderMetaInformation(meta));
        }

        GivenStories givenStories = scenario.getGivenStories();
        if (areGivenStoriesProvided(givenStories)) {
            scenarioBuilder.append(renderGivenStories(givenStories));
        }

        List<String> steps = scenario.getSteps();
        if (areStepsDefined(steps)) {
            scenarioBuilder.append(renderSteps(steps));
        }

        ExamplesTable examplesTable = scenario.getExamplesTable();
        if (areExamplesTable(examplesTable)) {
            scenarioBuilder.append(renderExamplesTable(examplesTable));
        }

        if (this.manual) {
            scenarioBuilder.append(renderManualSteps());
        }

        return scenarioBuilder.toString();
    }

    private String renderManualSteps() {
        String manual = ".Result" + NEW_LINE + "****" + NEW_LINE;

        manual += "- [ ] PASS" + NEW_LINE + NEW_LINE;
        manual += "- [ ] FAIL" + NEW_LINE + NEW_LINE;
        manual += "- [ ] COMMENTS: " + NEW_LINE;

        manual += "****" + NEW_LINE + NEW_LINE;

        return manual;
    }

    private String renderExamplesTable(ExamplesTable examplesTable) {

        List<String> headers = examplesTable.getHeaders();
        String tableInformation = "[options=\"header\"]" + NEW_LINE;
        tableInformation += ".Examples" + NEW_LINE;
        tableInformation += "|===" + NEW_LINE;

        for (String header : headers) {
            tableInformation += "|" + header;
        }
        tableInformation += NEW_LINE;

        tableInformation += renderContentTable(examplesTable, headers);

        tableInformation += "|===" + NEW_LINE + NEW_LINE;

        return tableInformation;
    }

    private String renderContentTable(ExamplesTable examplesTable,
            List<String> headers) {

        String tableInformation = "";

        for (int numberOfRow = 0; numberOfRow < examplesTable.getRowCount(); numberOfRow++) {

            Map<String, String> row = examplesTable.getRow(numberOfRow);

            for (String header : headers) {
                tableInformation += "|" + row.get(header) + NEW_LINE;
            }

            tableInformation += NEW_LINE;

        }
        return tableInformation;
    }

    private String renderSteps(List<String> steps) {
        String stepsInformation = ".Steps" + NEW_LINE;
        stepsInformation += "----" + NEW_LINE;
        for (String step : steps) {
            stepsInformation += step + NEW_LINE;
        }

        stepsInformation += "----" + NEW_LINE;
        stepsInformation += NEW_LINE;

        return stepsInformation;
    }

    private boolean areExamplesTable(ExamplesTable examplesTable) {
        return examplesTable != null && examplesTable.getHeaders().size() > 0;
    }

    private boolean areStepsDefined(List<String> steps) {
        return steps != null && steps.size() > 0;
    }

    private String renderTitle(Scenario scenario, int currentScenarioNumber) {
        String title = scenario.getTitle();

        if (title != null) {
            return getTitle(title);
        } else {
            return getTitle(Integer.toString(currentScenarioNumber));
        }
    }

    private String getTitle(String title) {
        return getInitialSection(this.initialLevel + 1) + " Scenario: " + title
                + NEW_LINE + NEW_LINE;
    }

    private String getStoryIdentifier(String path) {
        int lastSlashNx = path.lastIndexOf("/");
        
        if(lastSlashNx > -1) {
            return path.substring(lastSlashNx+1);
        } else {
            int lastSlashWin = path.lastIndexOf("\\");
            if(lastSlashWin > -1) {
                return path.substring(lastSlashWin+1);
            } else {
                return path;
            }
        }
    }

    private String getInitialSection(int initialLevel) {
        StringBuilder zeroLevel = new StringBuilder("=");

        for (int i = 0; i < initialLevel; i++) {
            zeroLevel.append("=");
        }

        return zeroLevel.toString();

    }

}
