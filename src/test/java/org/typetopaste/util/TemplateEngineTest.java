package org.typetopaste.util;

import java.util.Properties;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TemplateEngineTest {
	private final Properties johnLennonProps = properties(new String[][] {{"first.name", "John"}, {"last.name", "Lennon"}});
	
	@Test
	public void processEmptyTemplateNoVarsNoValues() {
		process("", new Properties(), "");
	}
	
	@Test
	public void processNoVarsNoValues() {
		process("something", new Properties(), "something");
	}

	@Test
	public void processNoVarsSomeValues() {
		process("something", johnLennonProps, "something");
	}
	
	@Test
	public void process1Var2Values() {
		process("singer ${first.name}", johnLennonProps, "singer John");
	}

	@Test
	public void process2Var2Values() {
		process("singer ${first.name} ${last.name}", johnLennonProps, "singer John Lennon");
	}

	@Test
	public void processVarWithoutValue() {
		process("The leader of ${band} is ${first.name} ${last.name}", johnLennonProps, "The leader of ${band} is John Lennon");
	}

	@Test
	public void process2Var2ValuesSuffixText() {
		process("${first.name} ${last.name} is the singer", johnLennonProps, "John Lennon is the singer");
	}
	
	@Test
	public void process2Var2ValuesPrefixAndSuffixText() {
		process("The singer ${first.name} ${last.name} is the leader of The Beatles", johnLennonProps, "The singer John Lennon is the leader of The Beatles");
	}
	
	private void process(String template, Properties props, String expected) {
		String actual = new TemplateEngine().process(template, props);
		assertEquals(expected, actual);
	}
	
	private Properties properties(String[][] keyValues) {
		Properties props = new Properties();
		
		for (String[] kv : keyValues) {
			props.put(kv[0], kv[1]);
		}

		return props;
	}
}
