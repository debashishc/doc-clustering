package ml.kmeans;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * @date 16/08/2016
 * @author Debashish Chakraborty
 *
 *  Kmeans clustering - randomly initialise centroids of the size of document list, maximise the calculated
 *  cosine similarity from the helper class and run k-means for a maximum number of provided iterations
 *  to obtain the required number of clusters with names for the documents
 */

public class Kmeans {

    // Randomly assign K centroids for entire document size
    public static Map<Integer, Map> randomIntialise(int K, List<Document> docList) {
        Map<Integer, Map> centroids = new HashMap<>();
        int index = 0;
        while (true) {
            Document randomDocument = docList.get(new Random().nextInt(docList.size()));
            centroids.put(index, randomDocument.getTermFrequencyIDMap());
            index += 1;
            if (centroids.size() == K)
                break;
        }
        return centroids;
    }

    // Maximise score for each document
    public static void maximiseScore(Map<Integer, Map> centroids, Map<Integer, List<Document>> clusterMap) {
        for (Map.Entry<Integer, Map> centroid : centroids.entrySet())    {
            Map<String, Double> tfIdfMapCentroid = centroid.getValue();
            List<Document> documentsInCluster = clusterMap.get(centroid.getKey());

            for (Map.Entry<String, Double> term : tfIdfMapCentroid.entrySet()) {
                String word = term.getKey();
                double score = 0;
                for (Document document : documentsInCluster) {
                    if (document.getTermFrequencyIDMap().containsKey(word)) {
                        score += document.getTermFrequencyIDMap().get(word);
                    }
                }
                // normalise the score
                score /= documentsInCluster.size();
                tfIdfMapCentroid.put(word, score);
            }
        }
    }

    // Running kmeans for number of clusters, with maximum number of iterations
    // and number of docs in each cluster to printed out
    public static void runKmeans (int numIter, Map<Integer, Map> centroids,
                                  List<Document> docList, Map<Integer, List<Document>> clusterMap) {
        for (int i = 0; i < numIter; i++) {
            Cluster.getClusters(centroids, docList);
            maximiseScore(centroids, clusterMap);
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        int K = 3;
        int numOfDocs = 5;
        int maxIter = 100;

//        String directory = "data/blog_data";
        String directory = "data/blog_data_test/";

        Map<String, Map> fileHashMap = HashMapBuilder.hashMapBuilder(directory, true);
        List<Document> docList = VectorRepresentation.convertToList(fileHashMap);
        System.out.println("No of Clusters: " + K + "\n\nExecuting K Means...\n");
        System.out.println("\nInitialising Centroids Randomly ...");

        Map<Integer, Map> centroids = randomIntialise(K, docList);
        Map<Integer, List<Document>> clusterMap = Cluster.getClusters(centroids, docList);
        runKmeans(maxIter, centroids, docList, clusterMap);
        Helper.getDocuments(clusterMap, numOfDocs);

    }

}
