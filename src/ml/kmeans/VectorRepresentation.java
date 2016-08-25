package ml.kmeans;

import java.util.*;

/**
 * @date 16/08/2016
 * @author Debashish Chakraborty
 * Creating a vector representation of the documents and mapping the tf-idf for each document
 */
public class VectorRepresentation {

    // Creating a list of documents with the tf-idf
    public static List<Document> convertToList (Map<String, Map> fileTermHashMap) {
        List<Document> vector = new ArrayList<>();
        for (Map.Entry<String, Map> entry : fileTermHashMap.entrySet()) {
            Map<String, Integer> termHashMap = entry.getValue();
            Map<String, Double> tfIdfMap = getTfFIdMap(termHashMap, fileTermHashMap);
            vector.add(new Document(entry.getKey(), tfIdfMap));
        }
        return vector;
    }

    // Getting a map of the term frequency- inverse document frequency for the top terms extracted with HashMapBuilder
    public static Map<String, Double> getTfFIdMap (Map<String, Integer> objFreqMap, Map<String, Map> docHashMap) {
        Map<String, Double> tfIdfMap = new HashMap<>();
        int numDocs = docHashMap.size();

        for (Map.Entry<String, Integer> obj : objFreqMap.entrySet()) {
            String term = obj.getKey();
            Double freq = obj.getValue().doubleValue();
            Double termF = freq / objFreqMap.size();
            Double idf = Math.log((double) numDocs / HashMapBuilder.termMap.get(term).getFreqInDoc());
            Double tfIdf = termF * idf;
            tfIdfMap.put(term, tfIdf);

        }
        return tfIdfMap;
    }

    public static void main(String[] args) {
        String path = "data/blog_data/";
        Map<String, Map> fileTermHashMap = HashMapBuilder.hashMapBuilder(path, true);
        System.out.println(fileTermHashMap);
        List<Document> docList = convertToList(fileTermHashMap);
        System.out.println(docList);
    }

}
