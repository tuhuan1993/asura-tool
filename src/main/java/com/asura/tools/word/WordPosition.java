package com.asura.tools.word;

public class WordPosition {
	private InformationSource source;
	private String word;
	private int start;
	private int end;
	private int segment;
	private int sentence;
	private int phrase;

	public WordPosition() {
	}

	public WordPosition(int index, String source, String word) {
		this.source = SourceManager.getInstance().getSource(source);
		PositionUtil.computeStartEnd(this, source, word, index);
	}

	public int getSegment() {
		return this.segment;
	}

	public void setSegment(int segment) {
		this.segment = segment;
	}

	public int getSentence() {
		return this.sentence;
	}

	public void setSentence(int sentence) {
		this.sentence = sentence;
	}

	public int getPhrase() {
		return this.phrase;
	}

	public void setPhrase(int phrase) {
		this.phrase = phrase;
	}

	public WordPosition(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return this.start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return this.end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public boolean isBefore(WordPosition position) {
		return ((this.end < position.start) && (isSameSource(position)));
	}

	public boolean isBeforeNext(WordPosition position) {
		return ((this.end + 1 == position.start) && (isSameSource(position)));
	}

	public boolean isAfterNext(WordPosition position) {
		return ((this.start == position.end + 1) && (isSameSource(position)));
	}

	public boolean isNext(WordPosition position) {
		return ((isBeforeNext(position)) || ((isAfterNext(position)) && (isSameSource(position))));
	}

	public boolean isAfter(WordPosition position) {
		return ((this.start > position.end) && (isSameSource(position)));
	}

	public boolean contains(WordPosition position) {
		return ((isSamePhrase(position)) && (this.start <= position.start) && (this.end >= position.end));
	}

	public boolean isInclusive(WordPosition position) {
		return ((!(isBefore(position))) && (!(isAfter(position))) && (isSameSource(position)));
	}

	public int getWordDistance(WordPosition position) {
		if (isSameSource(position)) {
			if (isAfter(position))
				return (this.start - position.end);
			if (isBefore(position)) {
				return (position.start - this.end);
			}
			return 0;
		}

		return 2147483647;
	}

	public String getWordBetween(WordPosition position) {
		if (position.getSource().getSourceString().equals(getSource().getSourceString())) {
			if (isBefore(position))
				return this.source.getSourceString().substring(this.end, position.getStart() - 1);
			if (isAfter(position)) {
				return this.source.getSourceString().substring(position.getEnd(), this.start - 1);
			}
			return "";
		}

		return this.source.getSourceString() + position.getSource().getSourceString();
	}

	public boolean isValid() {
		return ((this.end >= this.start) && (this.start > 0) && (this.end > 0));
	}

	public String toString() {
		return "WordPosition[word:" + this.word + ", start:" + this.start + ",end:" + this.end + ",segment:"
				+ this.segment + ",sentence:" + this.sentence + ",phrase:" + this.phrase + "]";
	}

	public boolean isSamePhrase(WordPosition position) {
		return ((getSegment() == position.getSegment()) && (getSentence() == position.getSentence())
				&& (getPhrase() == position.getPhrase()) && (isSameSource(position)));
	}

	public boolean isSameSegment(WordPosition position) {
		return ((getSegment() == position.getSegment()) && (isSameSource(position)));
	}

	public boolean isSameSentence(WordPosition position) {
		return ((getSegment() == position.getSegment()) && (getSentence() == position.getSentence())
				&& (isSameSource(position)));
	}

	public InformationSource getSource() {
		return this.source;
	}

	public void setSource(InformationSource source) {
		this.source = source;
	}

	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	private boolean isSameSource(WordPosition position) {
		return getSource().equals(position.getSource());
	}
}
