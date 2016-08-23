package ml.kmeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dc on 16/08/2016.
 */
public class VectorRepresentation {

    public static List<Document> convertToVector (Map<String, Map> fileTermHashMap) {

        List<Document> vector = new ArrayList<>();

        for (Map.Entry<String, Map> entry : fileTermHashMap.entrySet()) {
            String file = entry.getKey();
            Map<String, Integer> termHashMap = entry.getValue();
            Map<String, Double> docTfMap = getDocTFMap(termHashMap);
            Map<String, Double> tfIdfMap = getTfFIdMap(docTfMap, fileTermHashMap);

            vector.add(new Document(file, tfIdfMap));
        }
        return vector;

    }

    // Create a map of documents
    public static Map<String, Double> getDocTFMap ( Map<String, Integer> objFreqMap) {

        Map<String, Double> doctfmap = new HashMap<>();

        for (Map.Entry<String, Integer> objFreq : objFreqMap.entrySet()) {

            Double freq =  objFreq.getValue().doubleValue();
            String obj = objFreq.getKey();

            Double objF = freq / objFreqMap.size();
            doctfmap.put(objFreq.getKey(), objF);
        }

        return doctfmap;

    }

    public static Map<String, Double> getTfFIdMap (Map<String, Double> docTFMap, Map<String, Map> docHashMap) {
        Map<String, Double> tfIdfMap = new HashMap<>();
        int numDocs = docHashMap.size();

        for (Map.Entry<String, Double> obj : docTFMap.entrySet()) {
            String term = obj.getKey();
            Double tf = obj.getValue();
            Double idf = Math.log((double) numDocs / HashMapBuilder.termMap.get(term).getFreqInDoc());
            Double tfIdf = tf * idf;

            tfIdfMap.put(term, tfIdf);
        }
        return tfIdfMap;
    }

    public static void main(String[] args) {
        String path = "data/blog_data/";
        Map<String, Map> fileTermHashMap = HashMapBuilder.hashMapBuilder(path, true);
        System.out.println(fileTermHashMap);

//        Map<String, Double> docTFMap = getDocTFMap(fileTermHashMap);

//        Map<String, Double> TfFIdMap = getTfFIdMap();

        List<Document> documentList = convertToVector(fileTermHashMap);
        System.out.println(documentList);

    }

}
