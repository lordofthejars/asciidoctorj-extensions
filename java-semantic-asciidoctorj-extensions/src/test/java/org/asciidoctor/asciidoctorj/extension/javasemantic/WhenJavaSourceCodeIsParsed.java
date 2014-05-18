package org.asciidoctor.asciidoctorj.extension.javasemantic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.github.antlrjavaparser.ParseException;

public class WhenJavaSourceCodeIsParsed {

	private static final String EXPECTED_METHOD1 = "public synchronized void mymethod1(String a) {\n" +
			"	//yep\n" + 
			"	System.out.println(\"aaa\");\n" + 
			"}";

    private static final String EXPECTED_METHOD2 = "public void mymethod2(final String a) {\n" +
            "\n" +
            "    System.out.println(\"aaa\"); // some comments\n" +
            "}";

    private static final String EXPECTED_IMPORT = "import java.util.List;";
	
	private static final String EXPECTED_CLASS = "public class MyClass {\n" + 
			"	\n" + 
			"	private enum MyEnum {\n" + 
			"		A, B;\n" + 
			"	}\n" + 
			"	\n" + 
			"	private class InnerClass {\n" + 
			"		\n" + 
			"	}\n" + 
			"	\n" + 
			"	private @interface MyAnnotation {\n" + 
			"		\n" + 
			"	}\n" + 
			"	\n" + 
			"	private List<String> list;\n" + 
			"	\n" + 
			"	public MyClass(String test) {\n" + 
			"		System.out.println(test);\n" + 
			"	}\n" + 
			"	\n" + 
			"	public synchronized void mymethod1(String a) {\n" +
			"		//yep\n" + 
			"		System.out.println(\"aaa\");\n" + 
			"	}\n\n" +
            "    public void mymethod2(final String a) {\n" +
            "\n" +
            "        System.out.println(\"aaa\"); // some comments\n" +
            "    }\n" +
			"}";
	
	private static final String EXPECTED_ENUM = "private enum MyEnum {\n" + 
			"	A, B;\n" + 
			"}";
	
	private static final String EXPECTED_FIELDS = "private List<String> list;";
	
	private static final String EXPECTED_CONSTRUCTOR = "public MyClass(String test) {\n" + 
			"	System.out.println(test);\n" + 
			"}";
	
	private static final String EXPECTED_INNER_CLASS = "private class InnerClass {\n" + 
			"	\n" + 
			"}";
	
	private static final String EXPECTED_ANNOTATION = "private @interface MyAnnotation {\n" + 
			"	\n" + 
			"}";
	
	@Test
	public void only_set_method_should_be_extracted_if_method_attribute_set1() throws ParseException, IOException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("method", "void mymethod1(String)");
		

		Java7CodeExtractor Java7CodeExtractor = new Java7CodeExtractor();
		final InputStream resourceAsStream = ClassLoader.class.getResourceAsStream("/MyClass.java");
		
		assertThat(Java7CodeExtractor.extract(resourceAsStream, params).trim(), is(EXPECTED_METHOD1));

	}

    @Test
    public void only_set_method_should_be_extracted_if_method_attribute_set2() throws ParseException, IOException {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("method", "void mymethod2(String)");

        Java7CodeExtractor Java7CodeExtractor = new Java7CodeExtractor();
        final InputStream resourceAsStream = ClassLoader.class.getResourceAsStream("/MyClass.java");

        assertThat(Java7CodeExtractor.extract(resourceAsStream, params).trim(), is(EXPECTED_METHOD2));

    }

	
	@Test
	public void only_imports_should_be_extracted_if_imports_attribute_set() throws ParseException, IOException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("1", "imports");
		

		Java7CodeExtractor Java7CodeExtractor = new Java7CodeExtractor();
		final InputStream resourceAsStream = ClassLoader.class.getResourceAsStream("/MyClass.java");
		
		assertThat(Java7CodeExtractor.extract(resourceAsStream, params).trim(), is(EXPECTED_IMPORT));
		
	}
	
	@Test
	public void whole_class_should_be_extracted_if_class_attribute_set() throws ParseException, IOException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("1", "class");

		Java7CodeExtractor Java7CodeExtractor = new Java7CodeExtractor();
		final InputStream resourceAsStream = ClassLoader.class.getResourceAsStream("/MyClass.java");
		
		assertThat(Java7CodeExtractor.extract(resourceAsStream, params).trim(), is(EXPECTED_CLASS));
		
	}
	
	@Test
	public void only_fields_should_be_extracted_if_fields_attribute_set() throws ParseException, IOException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("1", "fields");

		Java7CodeExtractor Java7CodeExtractor = new Java7CodeExtractor();
		final InputStream resourceAsStream = ClassLoader.class.getResourceAsStream("/MyClass.java");
		
		
		assertThat(Java7CodeExtractor.extract(resourceAsStream, params).trim(), is(EXPECTED_FIELDS));
		
	}
	
	@Test
	public void only_enum_should_be_extracted_if_enum_attribute_set() throws ParseException, IOException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("enum", "MyEnum");

		Java7CodeExtractor Java7CodeExtractor = new Java7CodeExtractor();
		final InputStream resourceAsStream = ClassLoader.class.getResourceAsStream("/MyClass.java");
		
		
		assertThat(Java7CodeExtractor.extract(resourceAsStream, params).trim(), is(EXPECTED_ENUM));
		
	}
	
	@Test
	public void only_contructor_should_be_extracted_if_contructor_attribute_set() throws ParseException, IOException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("constructor", "MyClass(String)");
		

		Java7CodeExtractor Java7CodeExtractor = new Java7CodeExtractor();
		final InputStream resourceAsStream = ClassLoader.class.getResourceAsStream("/MyClass.java");
		
		assertThat(Java7CodeExtractor.extract(resourceAsStream, params).trim(), is(EXPECTED_CONSTRUCTOR));

	}
	
	@Test
	public void only_inner_class_should_be_extracted_if_class_attribute_is_set() throws ParseException, IOException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("class", "InnerClass");
		

		Java7CodeExtractor Java7CodeExtractor = new Java7CodeExtractor();
		final InputStream resourceAsStream = ClassLoader.class.getResourceAsStream("/MyClass.java");
		
		assertThat(Java7CodeExtractor.extract(resourceAsStream, params).trim(), is(EXPECTED_INNER_CLASS));

	}
	
	@Test
	public void only_annotation_should_be_extracted_if_annotation_attribute_test() throws ParseException, IOException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("annotation", "MyAnnotation");
		

		Java7CodeExtractor Java7CodeExtractor = new Java7CodeExtractor();
		final InputStream resourceAsStream = ClassLoader.class.getResourceAsStream("/MyClass.java");
		
		assertThat(Java7CodeExtractor.extract(resourceAsStream, params).trim(), is(EXPECTED_ANNOTATION));

	}
	
}
