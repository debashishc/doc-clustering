package ml.kmeans;

import java.util.*;

/**
 *
 * Created by dc on 16/08/2016.
 *
 * @author Debashish Chakraborty
 *
 *  Kmeans clustering
 */

public class Kmeans {

    private static List<Document> documentVectorList;
    private static Map<Integer, Map> centroids;
    private static Map<Integer, List<Document>> clusterMap;


    public static Map<Integer, Map> intialiseCentroids(int K, List<Document> documentVectorList) {
        Map<Integer, Map> centroids = new HashMap<>();
        Random random = new Random();

        for (int i = 0; i < K; i++) {
            Document randomDocument = documentVectorList.get(random.nextInt(documentVectorList.size()));
            centroids.put(i, randomDocument.getTermFrequencyIDMap());
        }

        return centroids;
    }

    // Compute similarity score
    public static Double getSimilarityScore(Map<String, Double> A, Map<String, Double> B) {
        double score;

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        // find the smaller matrices and iterate over them to calculate
        if (A.size() <= B.size()) {
            for (Map.Entry<String, Double> term : A.entrySet()) {
                if (B.containsKey(term.getKey())) {
                    dotProduct += term.getValue() * B.get(term.getKey());
                    normA += Math.pow(term.getValue(), 2);
                    normB += Math.pow(B.get(term.getKey()), 2);
                }
            }
        }
        else {
            for (Map.Entry<String, Double> term : B.entrySet()) {
                if (A.containsKey(term.getKey())) {
                    dotProduct += term.getValue() * A.get(term.getKey());
                    normB += Math.pow(term.getValue(), 2);
                    normA += Math.pow(A.get(term.getKey()), 2);
                }
            }
        }

        if (normA != 0.0 | normB != 0.0) {
            score = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        } else {
            return 0.0;
        }


        return score;
    }


    public static void getCluster() {
        clusterMap = new HashMap<Integer, List<Document>>();

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
            if (!clusterMap.containsKey(document.getClusterID())) {
                clusterMap.put(document.getClusterID(), new ArrayList<>());
            }
            clusterMap.get(document.getClusterID()).add(document);

        }
    }

    public static void findMaxScore() {

        // Looping over each centroid
        for (Map.Entry<Integer, Map> centroid : centroids.entrySet())    {

            Map<String, Double> tfIdfMapOfCentroid = centroid.getValue();
            List<Document> documentsInCluster = clusterMap.get(centroid.getKey());
            int noOfDocsInCluster = documentsInCluster.size();

            // iterating every term of a centroid's value
            for (Map.Entry<String, Double> term : tfIdfMapOfCentroid.entrySet()) {

                String word = term.getKey();

                double score = 0.0;
                for (Document document : documentsInCluster) {
                    if (document.getTermFrequencyIDMap().containsKey(word)) {
                        score += document.getTermFrequencyIDMap().get(word);
                    }
                }

                // normalise the score
                score /= noOfDocsInCluster;
                tfIdfMapOfCentroid.put(word, score);
            }
        }
    }

    // set maximum iterations for K-means
    private static void runKmeans(int maxIterations) {
        for (int i = 0; i < maxIterations; i++) {
            getCluster();
            findMaxScore();
        }
    }

    // Print the first x number of documents as prescribed
    private static void getDocuments(int noOfDocs) {
        // initialise cluster
        int clusterNo = 1;
        for (Map.Entry<Integer, List<Document>> cluster : clusterMap.entrySet()) {
            List<Document> documents = cluster.getValue();

            Collections.reverse(documents);

            System.out.println("cluster 0" + clusterNo +
                    "\n--------------------------------------------------------------------------------");
            clusterNo += 1;

            // retrieved only prescribed number of documents from the cluster
            for (int i = 0; i < noOfDocs; i++)
                System.out.println(documents.get(i).getName());

            System.out.println();
        }
    }

    public static void Kmeans (int K, int numIter, int numDocs) {

        System.out.println("No of Clusters: " + K + "\n\nExecuting K Means...\n");

        runKmeans(numIter);
        getDocuments(numDocs);

    }


    public static void main(String[] args) {

        int K = 3;
        int numOfDocs = 5;
        int maxIter = 100;

//        String directory = "data/blog_data";
        String directory = "data/blog_data_test/";
//        boolean ignore_stop_words = true;

        Map<String, Map> fileHashMap = HashMapBuilder.hashMapBuilder(directory, true);

//        System.out.println("\nConverting Documents to TFIDF vectors ...");
        documentVectorList = VectorRepresentation.convertToVector(fileHashMap); // Convert all files to TfIdf Vectors
//        System.out.println("Initialising Centroids ...");
        centroids = intialiseCentroids(K, documentVectorList);

        Kmeans(K, maxIter, numOfDocs);


    }

}
