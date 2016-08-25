package ml.kmeans;

import java.util.*;

/**
 * @date 16/08/2016
 * @author Debashish Chakraborty
 *
 * Create clusters from generated centroids and a list of documents containing terms with its tf-idf
 */
public class Cluster {

    public static Map<Integer, List<Document>> docClusters;

    // creating a cluster of documents
    public static Map<Integer, List<Document>> getClusters(Map<Integer, Map> centroids, List<Document> docList) {
        Map<Integer, List<Document>> clusterMap = new HashMap<>();

        for (Document doc : docList) {
            // Dictionary to store similarity scores for a document from each centroid
            Map<Integer, Double> scoresMap = new HashMap<>();
            Map.Entry<Integer, Double> maxScore = null;

            // Getting score for a document from each centroid and appending each score to scoresMap
            for (Map.Entry<Integer, Map> centroid : centroids.entrySet()) {
                double score = Helper.getSimilarityScore(centroid.getValue(), doc.getTermFrequencyIDMap());
                scoresMap.put(centroid.getKey(), score);
            }

            // assign the maximum score
            for (Map.Entry<Integer, Double> score : scoresMap.entrySet())
            {
                if (maxScore == null || score.getValue() > maxScore.getValue())
                    maxScore = score;
            }
            int k = maxScore.getKey();
            doc.setClusterID(k);
            doc.setScore(scoresMap.get(k));

            if (!clusterMap.containsKey(doc.getClusterID())) {
                clusterMap.put(doc.getClusterID(), new ArrayList<>());
            }
            clusterMap.get(doc.getClusterID()).add(doc);

        }
        return clusterMap;
    }


}
