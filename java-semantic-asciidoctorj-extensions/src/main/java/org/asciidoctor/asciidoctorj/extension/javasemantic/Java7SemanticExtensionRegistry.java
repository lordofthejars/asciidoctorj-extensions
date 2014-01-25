package org.asciidoctor.asciidoctorj.extension.javasemantic;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.extension.ExtensionRegistry;

public class Java7SemanticExtensionRegistry implements ExtensionRegistry {

	@Override
	public void register(Asciidoctor asciidoctor) {
		asciidoctor.javaExtensionRegistry().includeProcessor(Java7SemanticIncludeProcessor.class);
	}

}
