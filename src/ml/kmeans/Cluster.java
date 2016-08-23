package ml.kmeans;

import java.util.*;

/**
 * Created by dc on 21/08/2016.
 */
public class Cluster {

    public static Map<Integer, List<Document>> docClusters;


    public static Map<Integer, List<Document>> getDocClusters(List<Document> documentVectorList, Map<Integer, Map> centroids) {
        docClusters = new HashMap<Integer, List<Document>>();

        // Looping over all documents
        for (Document document : documentVectorList) {
            // Dictionary to store similarity scores for a document from each centroid
            Map<Integer, Double> scoresMap = new HashMap<>();


            // Getting score for a document from each centroid and appending each score to scoresMap
            for (Map.Entry<Integer, Map> centroid : centroids.entrySet()) {
                double score = getSimilarityScore(centroid.getValue(), document.getTermFrequencyIDMap());
                scoresMap.put(centroid.getKey(), score);
            }


            // HashMap
            Map.Entry<Integer, Double> maxScore = null;
            for (Map.Entry<Integer, Double> score : scoresMap.entrySet())
            {
                if (maxScore == null || score.getValue() > maxScore.getValue())
                    maxScore = score;
            }
            //System.out.println(maxScore.getKey());
            int k = maxScore.getKey();


            // Setting document to a cluster
            document.setClusterID(k);
            document.setScore(scoresMap.get(k));

            // Storing all documents belonging to a cluster in ClusterMap.
            // E.g. {cluster0: [doc1, doc2], cluster1: [doc4, doc6], ...}
            if (!docClusters.containsKey(document.getClusterID())) {
                docClusters.put(document.getClusterID(), new ArrayList<>());
            }
            docClusters.get(document.getClusterID()).add(document);

        }

        return docClusters;
    }


}
