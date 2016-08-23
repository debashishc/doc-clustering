/** Frequently used document utilities (reading files, tokenization, extraction
 *  of TF vectors).
 *
 * @author Scott Sanner (ssanner@gmail.com)
 *
 * This version is a modified versional of the original DocUtils class
 * Modified by Debashish Chakraborty
 */

package ml.kmeans;

import nlp.nicta.filters.SimpleTokenizer;
import nlp.nicta.filters.SnowballStemmer;
import nlp.nicta.filters.StopWordChecker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;


public class ModDocUtils {

    public static final SimpleTokenizer ST = new SimpleTokenizer();

    // Example file for the blog data
    public static final String BLOG_FILE = "data/blog_data/file_1.txt";

    public static final String SPLIT_TOKENS = "[!\"#$%&'()*+,./:;<=>?\\[\\]^`{|}~\\s]"; // missing: [_-@]

    public final static DecimalFormat DF2 = new DecimalFormat("#.##");
    public final static DecimalFormat DF3 = new DecimalFormat("#.###");

    public static String ReadFile(File file) {
        return ReadFile(file, false);
    }

    public static String ReadFile(File file, boolean keep_newline) {
        try {
            StringBuilder sb = new StringBuilder();
//            java.io.BufferedReader br = new BufferedReader(new FileReader(file));
            java.io.BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "ISO-8859-1"));
//            java.io.BufferedReader br = new BufferedReader(new InputStreamReader(
//                    new FileInputStream(file), "UTF8"));
            String line;
            if ((line = br.readLine()) != null) {
                do {
                    //System.out.println(line);
                    sb.append((sb.length() > 0 ? (keep_newline ? "\n" : " ") : "") + line);
                } while ((line = br.readLine()) != null);
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return null;
        }
    }

    /*
    Modified tokenize from original DocUtils
    ArrayList changed to List since it's better suited to change in implementation
    */
    public static List<String> tokenize(String sent, Boolean ignore_stop_words) {
        StopWordChecker stopWordChecker = new StopWordChecker();
        List<String> result = new ArrayList<>();
        String tokens[] = sent.split(SPLIT_TOKENS);
        for (String token : tokens) {

            token = standardiseToken(token);
            if (token.length() == 0 || (ignore_stop_words && stopWordChecker.isStopWord(token))) // like UnigramBuilder
                continue;
            result.add(token);
        }
        return result;
    }

    // Use a HashSet to store the tokens, since this is only storing keys
    // HashMap is not really required
    public static Set<String> convertToSet(List<String> tokenList) {
        List<String> newTokenList = new ArrayList<>();
        for (String token : tokenList){
            token = standardiseToken(token);
            newTokenList.add(token);
        }
        Set<String> tokenSet = new HashSet<>(tokenList);
        return tokenSet;
    }

    // Modified convertToFeatureMap into Map of Object with Integer
    public static Map<Object,Integer> convertToMap(String sent) {
        Map<Object,Integer> map = new HashMap<>();
        String tokens[] = sent.split(SPLIT_TOKENS);
        for (String token : tokens) {
            token = standardiseToken(token);
            if (token.length() == 0)
                continue;
            if (map.containsKey(token))
                map.put(token, map.get(token) + 1);
            else
                map.put(token, 1);
        }
        return map;
    }

    public static String standardiseToken (String token){
        // The SnowBall stemmer is not that good since it stems weirdly "issuing" -> "issu"
        SnowballStemmer snowballStemmer = new SnowballStemmer();
        String newToken = token.trim().toLowerCase();
        newToken = snowballStemmer.stem(newToken);
        // this � character needed to be replaced, maybe wrong encoding
        newToken = newToken.replaceAll("�", "");
        return newToken;
    }

    public static void main(String[] args) throws IOException {
        String file = "data/blog_data/file_1.txt";
        byte[] bytes = Files.readAllBytes(Paths.get(file));
        String content = new String(bytes);
        List<String> tokenList = tokenize(content, true);
        Set<String> tokenSet = new HashSet<>(tokenList);
        for (Iterator<String> iterator = tokenSet.iterator(); iterator.hasNext(); ) {
            String token = iterator.next();
//            System.out.println(token);
        }

        Map<Object, Integer> tokenMap = convertToMap(content);
        System.out.println(tokenMap);
//        System.out.println(tokenMap.entrySet());
//        System.out.println(tokenSet.size());


    }
}

