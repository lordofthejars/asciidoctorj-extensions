package org.asciidoctor.asciidoctorj.extension.jbehave.userstory;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.asciidoctor.OptionsBuilder.options;

import java.io.File;
import java.io.IOException;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class WhenUserNeedsToIncludeUserStoriesInAsciiDoc {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    
    @Test
    public void user_stories_should_be_embed_inside_document() throws IOException {
        
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.extensionRegistry().includeProcessor(JBehaveUserStoriesIncludeProcessor.class);
        
        Options options = options().inPlace(false)
                .toFile(new File(testFolder.getRoot(), "rendersample.html"))
                .safe(SafeMode.UNSAFE).get();

        asciidoctor.renderFile(new File(
                "target/test-classes/manualTest.adoc"), options);
        
        File renderedFile = new File(testFolder.getRoot(), "rendersample.html");
        Document doc = Jsoup.parse(renderedFile, "UTF-8");
       
        Element tradeAlertStory = doc.getElementById("trade_alerts.story");
        assertThat(tradeAlertStory.text(), is("trade_alerts.story"));
        
        Element refreshableStocksStory = doc.getElementById("refreshable_stocks.story");
        assertThat(refreshableStocksStory.text(), is("refreshable_stocks.story"));
    }
    
}
