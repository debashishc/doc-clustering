package ml.kmeans;

/**
 * Created by dc on 16/08/2016.
 */
public class Term {

    String term;
    Integer freqInDoc;

    public Term(String term) {
        this.term = term;
        this.freqInDoc = 0;
    }

    public Term(String term, Integer freqInDoc) {
        this.term = term;
        this.freqInDoc = freqInDoc;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getFreqInDoc() {
        return freqInDoc;
    }

    public void setFreqInDoc(Integer freqInDoc) {
        this.freqInDoc = freqInDoc;
    }
}
