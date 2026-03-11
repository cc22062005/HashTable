import java.util.*;

public class PlagiarismDetector {
    private HashMap<String, Set<String>> documentWords;

    public PlagiarismDetector() {
        documentWords = new HashMap<>();
    }

    public void addDocument(String docName, String content) {
        Set<String> words = new HashSet<>(Arrays.asList(content.toLowerCase().split("\\W+")));
        documentWords.put(docName, words);
        System.out.println("Added document: " + docName + " (" + words.size() + " unique words)");
    }

    public double getSimilarity(String doc1, String doc2) {
        Set<String> set1 = documentWords.get(doc1);
        Set<String> set2 = documentWords.get(doc2);
        if (set1 == null || set2 == null) return 0.0;

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size() * 100;
    }

    public void checkAll() {
        List<String> docs = new ArrayList<>(documentWords.keySet());
        System.out.println("\n--- Plagiarism Report ---");
        for (int i = 0; i < docs.size(); i++) {
            for (int j = i + 1; j < docs.size(); j++) {
                double sim = getSimilarity(docs.get(i), docs.get(j));
                String verdict = sim > 70 ? "HIGH PLAGIARISM" : sim > 40 ? "MODERATE" : "LOW";
                System.out.printf("%s vs %s -> Similarity: %.1f%% [%s]%n",
                        docs.get(i), docs.get(j), sim, verdict);
            }
        }
    }

    public static void main(String[] args) {
        PlagiarismDetector detector = new PlagiarismDetector();

        detector.addDocument("doc1", "the quick brown fox jumps over the lazy dog");
        detector.addDocument("doc2", "the quick brown fox jumps over the lazy cat");
        detector.addDocument("doc3", "java is a programming language used for software development");
        detector.addDocument("doc4", "the quick brown fox leaps over the sleepy dog near the river");

        detector.checkAll();
    }
}
