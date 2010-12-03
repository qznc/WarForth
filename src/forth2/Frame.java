package forth2;

import forth2.words.Word;

public class Frame {
	public Word word;
	public int position;

	public Frame(Word w, int pos) {
		this.word = w;
		this.position = pos;
	}

	@Override
	public String toString() {
		return "["+word+","+position+"]";
	}
}
