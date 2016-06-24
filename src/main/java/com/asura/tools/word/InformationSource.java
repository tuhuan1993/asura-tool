package com.asura.tools.word;

public class InformationSource {
	String sourceString;

	public InformationSource() {
	}

	public InformationSource(String sourceString) {
		this.sourceString = sourceString;
	}

	public String getSourceString() {
		return this.sourceString;
	}

	public void setSourceString(String sourceString) {
		this.sourceString = sourceString;
	}

	public boolean equals(Object obj) {
		InformationSource source = (InformationSource) obj;
		return source.getSourceString().equals(this.sourceString);
	}

	public int hashCode() {
		return this.sourceString.hashCode();
	}
}
