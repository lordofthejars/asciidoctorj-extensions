package org.asciidoctor.asciidoctorj.extension.javasemantic;

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

public class WhenAsciiDocFileIsRendered {

	private static final String All_SOURCE_CODE = "package org.foo;\n" + 
			"\n" + 
			"import java.util.List;\n" + 
			"\n" + 
			"public class MyClass {\n" + 
			"\n" + 
			"	private enum MyEnum {\n" + 
			"		A, B;\n" + 
			"	}\n" + 
			"\n" + 
			"	private class InnerClass {\n" + 
			"\n" + 
			"	}\n" + 
			"\n" + 
			"	private @interface MyAnnotation {\n" + 
			"\n" + 
			"	}\n" + 
			"\n" + 
			"	private List&lt;String&gt; list;\n" + 
			"\n" + 
			"	public MyClass(String test) {\n" + 
			"		System.out.println(test);\n" + 
			"	}\n" + 
			"\n" + 
			"	public synchronized void mymethod(String a) {\n" + 
			"		//yep\n" + 
			"		System.out.println(&quot;aaa&quot;);\n" + 
			"	}\n" + 
			"\n" + 
			"}";
	
	private static final String EXPECTED_FILTERED_CODE = "import java.util.List;\n" + 
			"\n" + 
			"public synchronized void mymethod(String a) {\n" + 
			"	//yep\n" + 
			"	System.out.println(&quot;aaa&quot;);\n" + 
			"}";
	
	
	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	
	@Test
	public void included_java_file_without_attributes_should_be_rendered_completly() throws IOException {
		
		Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        
        Options options = options().inPlace(false)
                .toFile(new File(testFolder.getRoot(), "rendersample.html"))
                .safe(SafeMode.UNSAFE).get();

        asciidoctor.renderFile(new File(
                "target/test-classes/allSource.adoc"), options);
        
        File renderedFile = new File(testFolder.getRoot(), "rendersample.html");
        Document doc = Jsoup.parse(renderedFile, "UTF-8");
	
        
        Element codeElement = doc.getElementsByTag("code").first();
        
        assertThat(codeElement.attr("class"), is("java language-java"));
        assertThat(codeElement.html(), is(All_SOURCE_CODE));
        
        asciidoctor.unregisterAllExtensions();
        
	}
	
	@Test
	public void included_java_file_with_filter_options_should_be_extracted() throws IOException {
		
		Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        
        Options options = options().inPlace(false)
                .toFile(new File(testFolder.getRoot(), "rendersample.html"))
                .safe(SafeMode.UNSAFE).get();

        asciidoctor.renderFile(new File(
                "target/test-classes/filterSource.adoc"), options);
        
        File renderedFile = new File(testFolder.getRoot(), "rendersample.html");
        Document doc = Jsoup.parse(renderedFile, "UTF-8");
	
        Element codeElement = doc.getElementsByTag("code").first();
        
        assertThat(codeElement.attr("class"), is("java language-java"));
        assertThat(codeElement.html(), is(EXPECTED_FILTERED_CODE));
        
        asciidoctor.unregisterAllExtensions();
		
	}
	
}
