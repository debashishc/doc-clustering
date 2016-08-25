package ml.kmeans;

import java.util.*;

/**
 * @date 16/08/2016
 * @author Debashish Chakraborty
 * Document class with name, cluster index, tf-idf and score
 */
public class Document {

    String name;
    Integer clusterID;
    Map<String, Double> termFrequencyIDMap;
    Double score;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }


    public Document(String name, Map<String, Double> termFrequencyIDMap) {
        this.name = name;
        this.termFrequencyIDMap = termFrequencyIDMap;
    }

    public Document(String name, int clusterID, Map<String, Double> termFrequencyIDMap) {
        this.name = name;
        this.clusterID = clusterID;
        this.termFrequencyIDMap = termFrequencyIDMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClusterID() {
        return clusterID;
    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public Map<String, Double> getTermFrequencyIDMap() {
        return termFrequencyIDMap;
    }

}
