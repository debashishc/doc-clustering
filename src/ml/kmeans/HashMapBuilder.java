package ml.kmeans;

import util.FileFinder;

import java.io.File;
import java.util.*;

import static java.util.stream.Collectors.toConcurrentMap;

/**
 * @author Debashish Chakraborty
 *
 * Influenced by UnigramBuilder by Scott Sanner
 */
public class HashMapBuilder {

//    public static StopWordChecker _swc = new StopWordChecker();
//    public static TreeSet<WordCount> _wordCounts;
//    public static HashMap<String,Integer> _topWord2Index;

    //
//    public static Map<String, Term> termInDoc = new HashMap<>();
//    public static Set<Term> terms = new HashSet<>();

    //
    public static Map<String, Term> termMap;

    // Build dictionary of {File : {Term: Frequency}}
    public static Map<String, Map> hashMapBuilder (String directory, boolean ignore_stop_words){
        // Map of terms and their frequencies in each file
        HashMap<String, Map> fileTermHashMap = new HashMap<>();
        termMap = new HashMap<>();
//        HashMap<String, WordCount> word2count = new HashMap<>();


        // Get list of data files
        ArrayList<File> files = FileFinder.GetAllFiles(directory, "txt", true);
        System.out.println("Building HashMap representation of documents with terms and term frequency...\n");
        System.out.println("Found " + files.size() + " documents.");

        int file_count = 0;
        for (File f : files) {
            String file_content = ModDocUtils.ReadFile(f);
            List<String> tokenList = ModDocUtils.tokenize(file_content, ignore_stop_words);

//            Map<String, Integer> termFreqMap = new HashMap<String, Integer>();
//            for (String token: tokenList){
//                termFreqMap.put(token, (String w) -> 1, (a, b) -> Integer.sum(a, b)));
//            }

            Map<String, Integer> termFreqMap;
            termFreqMap = tokenList.parallelStream().flatMap(s -> Arrays.stream(s.split(" "))).
                    collect(toConcurrentMap(String::toLowerCase, (String w) -> 1, Integer::sum));

            // take the top 150
            termFreqMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(150);

//            Map<String, Integer> termFreqMap = tokenList.parallelStream().flatMap(s -> Arrays.asList(s.split(" ")).stream()).
//                    collect(Collectors.toConcurrentMap(w -> w.toLowerCase(), w -> 1, Integer::sum));

            fileTermHashMap.put(f.getName(), termFreqMap);


//            Set<String> tokenSet = new HashSet<>(tokenList);

            // Only make a set of the top x number of words
            Set<String> tokenSet = termFreqMap.keySet();
            for (String token : tokenSet) {
                Term termToken = new Term(token);
                if (!termMap.containsKey(token))
                    termMap.put(token, termToken);

                termMap.get(token).freqInDoc += 1;
            }



//            termMap.values()
//                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//                    .limit(100);

//            termMap.values().parallelStream().sorted()

//            termMap.values().parallelStream()
//                    .sorted(Map.Entry.comparingByValue().reversed())
//                    .limit(100);

//            termMap.values().removeAll(Collections.singleton(entry.getValue().freqInDoc < 100));

//            for(Iterator<Map.Entry<String, Term>> it = termMap.entrySet().iterator(); it.hasNext(); ) {
//                Map.Entry<String, Term> entry = it.next();
//                if(entry.getValue().freqInDoc < 100) {
//                    it.remove();
//                }
//            }

            // if set is used to store
            // harder to access a set than a map
//            Set<String> tokenSet = new HashSet<>(tokenList);
//            for (String token : tokenSet) {
//                Term termToken = new Term(token);
//                if (!terms.contains(token)){
//                    terms.add(termToken);
//                }
//                termToken.freqInDoc += 1;
//            }

//            if (++file_count % 100 == 0)
//            System.out.println("Read " + file_count + " files.");
        }
//        System.out.println("Extracted " + termMap.size() + " unique terms.");

        System.out.println("\nHashMap representation built.\n");



        return fileTermHashMap;

    }


    public static void main(String[] args) {
        String path = "data/blog_data_test/";
        Map<String, Map> hash = hashMapBuilder(path, true);
        System.out.println(hash.size());
    }


}
