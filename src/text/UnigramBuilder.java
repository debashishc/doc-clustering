/** Simple Unigram analysis of data.
 * 
 * @author Scott Sanner
 */

package text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import nlp.nicta.filters.StopWordChecker;

import util.DocUtils;
import util.FileFinder;

public class UnigramBuilder {

	public StopWordChecker _swc = new StopWordChecker();
	public TreeSet<WordCount> _wordCounts;
	public HashMap<String,Integer> _topWord2Index;
	
	public UnigramBuilder(String directory, int num_top_words, boolean ignore_stop_words) {
		ArrayList<File> files = FileFinder.GetAllFiles(directory, "", true);
		HashMap<String,WordCount> word2count = new HashMap<String,WordCount>();
		System.out.println("Found " + files.size() + " files.");
		
		int file_count = 0;
		for (File f : files) {
			String file_content = DocUtils.ReadFile(f);
			Map<Object,Double> word_counts = DocUtils.ConvertToFeatureMap(file_content);
			for (Map.Entry<Object, Double> me : word_counts.entrySet()) {
				String key = (String)me.getKey();
				WordCount wc = word2count.get(key);
				if (wc == null) {
					wc = new WordCount(key, (int)me.getValue().doubleValue());
					word2count.put(key, wc);
				} else
					wc._count++;
			}
			if (++file_count % 500 == 0)
				System.out.println("Read " + file_count + " files.");
		}
		System.out.println("Extracted " + word2count.size() + " unique tokens.");
		
		_wordCounts = new TreeSet<WordCount>(word2count.values());
		_topWord2Index = new HashMap<String,Integer>();
		int index = 0;
		for (WordCount wc : _wordCounts) {
			if (ignore_stop_words && _swc.isStopWord(wc._word))
				continue;
			System.out.println("[index:" + index + "] " + wc);
			_topWord2Index.put(wc._word, index);
			if (++index >= num_top_words)
				break;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UnigramBuilder UB = new UnigramBuilder("data/two_newsgroups/" /* data source */, 
				/* num top words */100, /* remove stopwords */true);
	}

}
