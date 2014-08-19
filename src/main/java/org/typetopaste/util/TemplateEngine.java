package org.typetopaste.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {
	private static final Pattern variablePattern = Pattern.compile("\\$\\{(.*?)\\}");

	private static class Replacement {
		private final int start;
		private final int end;
		private final String value;
		
		public Replacement(int start, int end, String value) {
			super();
			this.start = start;
			this.end = end;
			this.value = value;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}

		public String getValue() {
			return value;
		}
	}
	
	
	public String process(String template, Properties props) {
		Matcher m = variablePattern.matcher(template);
		
		Collection<Replacement> replacements = new ArrayList<>();
		
		for(int from = 0; m.find(from); ) {
			String varName = m.group(1);
			String varValue = props.getProperty(varName);
			String replacement = varValue == null ? String.format("${%s}", varName) : varValue;
			int start = m.start();
			int end = m.end();
			
			replacements.add(new Replacement(start, end, replacement));
			
			from = end;
		}
		
		StringBuilder buffer = new StringBuilder();
		int from = 0;
		for (Replacement replacement : replacements) {
			String before = template.substring(from, replacement.getStart());
			buffer.append(before).append(replacement.getValue());
			from = replacement.getEnd();
		}
		buffer.append(template.substring(from));
		
		return buffer.toString();
	}
}
