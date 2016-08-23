package text;

public class WordCount implements Comparable {
	public String _word;
	public int    _count;
	public WordCount(String word, int count) {
		_word = word;
		_count = count;
	}
	public String toString() {
		return _word + " = " + _count;
	}
	// Hash word part only
	public int hashCode() {
		return _word.hashCode();
	}
	// This is used for hash equality -- word part only
	public boolean equals(Object o) {
		if (o instanceof WordCount) {
			WordCount wc = (WordCount)o;
			return wc._word.equals(_word);
		} else
			return false;
	}
	public int compareTo(Object o) {
		if (o instanceof WordCount) {
			WordCount wc = (WordCount)o;
			if (_count > wc._count) 
				return -1;
			else if (_count < wc._count)
				return 1;
			else // Break ties by alphabetical word order
				return _word.compareTo(wc._word);					
		}
		return 0; // Incomparable, just say equal
	}
	
}
