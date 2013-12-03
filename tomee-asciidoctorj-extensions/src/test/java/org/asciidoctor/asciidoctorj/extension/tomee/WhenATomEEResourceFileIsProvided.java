package org.asciidoctor.asciidoctorj.extension.tomee;


import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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


public class WhenATomEEResourceFileIsProvided {

    private static final String EXPECTED_ASCIIDOC_OUTPUT = "= Resources\n" + 
    		"\n" + 
    		"== database1 [DataSource]\n" + 
    		"\n" + 
    		"|===\n" + 
    		"|*Property Name* |*Property Value*\n" + 
    		"\n" + 
    		"|JdbcDriver\n" + 
    		"|org.hsqldb.jdbcDriver\n" + 
    		"\n" + 
    		"|JdbcUrl\n" + 
    		"|jdbc:hsqldb:mem:db1\n" + 
    		"\n" + 
    		"|UserName\n" + 
    		"|sa\n" + 
    		"\n" + 
    		"|Password\n" + 
    		"| \n" + 
    		"\n" + 
    		"|JtaManaged\n" + 
    		"|true\n" + 
    		"\n" + 
    		"|===\n" + 
    		"\n" + 
    		"== database2 [DataSource]\n" + 
    		"\n" + 
    		"|===\n" + 
    		"|*Property Name* |*Property Value*\n" + 
    		"\n" + 
    		"|JdbcDriver\n" + 
    		"|org.hsqldb.jdbcDriver\n" + 
    		"\n" + 
    		"|JdbcUrl\n" + 
    		"|jdbc:hsqldb:mem:db2\n" + 
    		"\n" + 
    		"|UserName\n" + 
    		"|sa\n" + 
    		"\n" + 
    		"|Password\n" + 
    		"| \n" + 
    		"\n" + 
    		"|JtaManaged\n" + 
    		"|true\n" + 
    		"\n" + 
    		"|===\n" + 
    		"\n" + 
    		"== config [org.superbiz.Configuration]\n" + 
    		"\n" + 
    		"|===\n" + 
    		"|*Property Name* |*Property Value*\n" + 
    		"\n" + 
    		"|Url\n" + 
    		"|http://foo.com/bar\n" + 
    		"\n" + 
    		"|Name\n" + 
    		"|bar\n" + 
    		"\n" + 
    		"|===\n" + 
    		"\n" + 
    		"== Routed Datasource [org.apache.openejb.resource.jdbc.Router]\n" + 
    		"\n" + 
    		"|===\n" + 
    		"|*Property Name* |*Property Value*\n" + 
    		"\n" + 
    		"|Router\n" + 
    		"|My Router\n" + 
    		"\n" + 
    		"|===\n" + 
    		"\n" + 
    		"= Containers\n" + 
    		"\n" + 
    		"== Foo [MESSAGE]\n" + 
    		"\n" + 
    		"CAUTION: No properties has been definde and deafult values are being used.\n" + 
    		"\n" + 
    		"= Transaction Manager\n" + 
    		"\n" + 
    		"== javax.transaction.TransactionManager\n" + 
    		"\n" + 
    		"CAUTION: No properties has been definde and deafult values are being used.\n" + 
    		"\n";
    
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    
    @Test
    public void resources_should_be_transformed_to_AsciiDoc_format() {
        
        File resourcesFile = new File("target/test-classes/resources.xml");
        
        ResourcesParser resourcesParser = new ResourcesParser();
        String transform = resourcesParser.transform(resourcesFile.getAbsolutePath(), 0);
        
        assertThat(transform, is(EXPECTED_ASCIIDOC_OUTPUT));
        
    }
    
    @Test
    public void documentation_should_be_rendered_with_TomEE_configuration() throws IOException {
        
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.extensionRegistry().includeProcessor(TomEEResourcesIncludeProcessor.class);
        
        Options options = options().inPlace(false)
                .toFile(new File(testFolder.getRoot(), "rendersample.html"))
                .safe(SafeMode.UNSAFE).get();

        asciidoctor.renderFile(new File(
                "target/test-classes/infrastructure.adoc"), options);
        
        File renderedFile = new File(testFolder.getRoot(), "rendersample.html");
        Document doc = Jsoup.parse(renderedFile, "UTF-8");
       
        Element tomeeConfiguration = doc.getElementById("_tomee_configuration");
        
        assertThat(tomeeConfiguration, is(notNullValue()));
        
    }
    
}
