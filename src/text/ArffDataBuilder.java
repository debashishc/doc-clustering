/** Produce an ARFF file from raw text data in directories labeled with class name.
 * 
 * @author Scott Sanner
 */

package text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import util.DocUtils;
import util.FileFinder;

public class ArffDataBuilder {

	public final static String ARFF_FILE = "src/ml/classifier/newsgroups.arff"; 

	public final static String DATA_SRC = "data/two_newsgroups/";
	//public final static String DATA_SRC = "data/newsgroups/";
	//public final static String DATA_SRC = "data/more_newsgroups/";

	public final static boolean REMOVE_STOPWORDS = true;
	public final static int     NUM_TOP_WORDS    = 1000;

	public ArffDataBuilder(String directory, HashMap<String,Integer> feature2index, String output_file) 
		throws Exception {

		// Open ARFF file for writing
		PrintStream ps = new PrintStream(new FileOutputStream(output_file));
		
		// Get list of data files
		ArrayList<File> files = FileFinder.GetAllFiles(directory, "", true);
		System.out.println("Found " + files.size() + " files.");
		
		// Get all class names -- put in classes
		TreeSet<String> classes = new TreeSet<String>();
		for (File f : files)
			classes.add(extractLastDirectoryName(f));
		System.out.println(classes.size() + " classes available: " + classes);
		
		///////////////////////////////////////////////////////////////////////
		
		// Invert the feature2index into a sorted TreeMap
		TreeMap<Integer,String> index2feature = new TreeMap<Integer,String>();
		for (Map.Entry<String, Integer> me : feature2index.entrySet())
			index2feature.put(me.getValue(), me.getKey());

		// Dump ARFF file header (attributes)
		ps.println("@relation newsgroup");		
		for (Integer index : index2feature.keySet()) {

			String word_feature = index2feature.get(index);
			
			// TODO: what to put here?

		}
		///////////////////////////////////////////////////////////////////////
		
		ps.print("@attribute 'class' {"); // Last attribute at index num_top_words is class
		for (String class_name : classes) 
			ps.print(" '" + class_name + "'");
		ps.println(" }");

		// Process features for every file
		ps.println("@data");
		int file_count = 0;
		for (File f : files) {
			
			// Get class name
			String class_name = extractLastDirectoryName(f);
			
			// Get word frequencies in this file
			String file_content = DocUtils.ReadFile(f);
			Map<Object,Double> word_counts = DocUtils.ConvertToFeatureMap(file_content);

			// Export class feature first
			ps.print("{ " + feature2index.size() + " '" + class_name + "'");

			///////////////////////////////////////////////////////////////////////

			// Then export all word features
			for (Map.Entry<Object, Double> me : word_counts.entrySet()) {
				String key = (String)me.getKey();
				Integer index = feature2index.get(key);
				
				// TODO: Complete this loop

			}
			ps.println("}");

			///////////////////////////////////////////////////////////////////////

			// Progress indicator
			if (++file_count % 500 == 0)
				System.out.println("Exported " + file_count + " feature vectors.");
		}

		// All done, close file!
		ps.close();
		System.out.println("ARFF file generation complete.");
	}
	
	public String extractLastDirectoryName(File f) {
		String parent_path = f.getParent();
		int last_separator = parent_path.lastIndexOf(File.separatorChar);
		return parent_path.substring(last_separator + 1, parent_path.length());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		// Get top unigrams
		UnigramBuilder ub = new UnigramBuilder(
				DATA_SRC, 
				NUM_TOP_WORDS,
				REMOVE_STOPWORDS);
		
		// Use top unigrams as features to build ARFF file for training and evaluation
		ArffDataBuilder db = new ArffDataBuilder(
				DATA_SRC, 
				ub._topWord2Index,
				ARFF_FILE); 
	}

}
