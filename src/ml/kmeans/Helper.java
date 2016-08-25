package ml.kmeans;

import java.util.*;

/**
 * @date 16/08/2016
 * @author Debashish Chakraborty
 *
 * Helper methods to calculate similarity score and print x number
 * of documents after running K-means algorithm
 */
public class Helper {

    // Compute similarity score
    public static Double getSimilarityScore(Map<String, Double> fV1, Map<String, Double> fV2) {
        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;
        int fv1_size = fV1.size();
        int fv2_size = fV2.size();

        // find the smaller map and iterate over it
        if (fv1_size <= fv2_size) {
            for (Map.Entry<String, Double> term : fV1.entrySet()) {
                if (fV2.containsKey(term.getKey())) {
                    dotProduct += term.getValue() * fV2.get(term.getKey());
                    norm1 += Math.pow(term.getValue(), 2);
                    norm2 += Math.pow(fV2.get(term.getKey()), 2);
                }
            }
        }
        else {
            for (Map.Entry<String, Double> term : fV2.entrySet()) {
                if (fV1.containsKey(term.getKey())) {
                    dotProduct += term.getValue() * fV1.get(term.getKey());
                    norm2 += Math.pow(term.getValue(), 2);
                    norm1 += Math.pow(fV1.get(term.getKey()), 2);
                }
            }
        }

        double score;
        if (norm1 != 0 | norm2 != 0) {
            score = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        } else {
            return 0.0;
        }

        return score;
    }

    // Print the first x number of documents as prescribed
    public static void getDocuments(Map<Integer, List<Document>> clusterMap, int numDocs) {
        int numCluster = 1;
        System.out.println();
        for (Map.Entry<Integer, List<Document>> cluster : clusterMap.entrySet()) {
            System.out.println("cluster 0" + numCluster + "\n------------");
            List<Document> documents = cluster.getValue();
            Collections.reverse(documents);
            numCluster += 1;

            // retrieved only prescribed number of documents from the cluster
            for (int i = 0; i < numDocs; i++)
                System.out.println(documents.get(i).getName());
            System.out.println();

        }
    }
}
