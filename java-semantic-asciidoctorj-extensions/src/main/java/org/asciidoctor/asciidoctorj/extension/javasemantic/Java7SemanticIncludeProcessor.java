package org.asciidoctor.asciidoctorj.extension.javasemantic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.asciidoctor.extension.IncludeProcessor;
import org.asciidoctor.internal.DocumentRuby;
import org.asciidoctor.internal.IOUtils;
import org.asciidoctor.internal.PreprocessorReader;

import com.github.antlrjavaparser.ParseException;

public class Java7SemanticIncludeProcessor extends IncludeProcessor {

	private static final String JAVA_EXTENSION = ".java";

	public Java7SemanticIncludeProcessor(DocumentRuby documentRuby) {
		super(documentRuby);
	}

	@Override
	public boolean handles(String extension) {
		return extension.endsWith(JAVA_EXTENSION);
	}

	@Override
	public void process(PreprocessorReader preprocessorReader, String target,
			Map<String, Object> attributes) {
		
		String content = "";
		
		if(attributes.isEmpty()) {
			//we can bypass the whole file.
			content = byPassContent(target, attributes);
		} else {
			content = extractContent(target, attributes);
		}
		
		preprocessorReader.push_include(content, target, target, 1, attributes);
	}

	private String extractContent(String target, Map<String, Object> attributes) {
		Java7CodeExtractor java7CodeExtractor = new Java7CodeExtractor();
		try {
			return java7CodeExtractor.extract(new FileInputStream(target), attributes);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private String byPassContent(
			String target, Map<String, Object> attributes) {
		try {
			return IOUtils.readFull(new FileInputStream(target));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
