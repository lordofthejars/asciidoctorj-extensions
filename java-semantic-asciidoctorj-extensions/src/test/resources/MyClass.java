package org.foo;

import java.util.List;

public class MyClass {
	
	private enum MyEnum {
		A, B;
	}
	
	private class InnerClass {
		
	}
	
	private @interface MyAnnotation {
		
	}
	
	private List<String> list;
	
	public MyClass(String test) {
		System.out.println(test);
	}
	
	public synchronized void mymethod(String a) {
		//yep
		System.out.println("aaa");
	}
	
}