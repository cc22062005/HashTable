import java.util.*;

public class AutocompleteSystem {
    private HashMap<String, Integer> searchFrequency;

    public AutocompleteSystem() {
        searchFrequency = new HashMap<>();
    }

    public void recordSearch(String query) {
        searchFrequency.merge(query.toLowerCase(), 1, Integer::sum);
    }

    public List<String> getSuggestions(String prefix) {
        prefix = prefix.toLowerCase();
        List<Map.Entry<String, Integer>> matches = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : searchFrequency.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                matches.add(entry);
            }
        }

        matches.sort((a, b) -> b.getValue() - a.getValue());

        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < Math.min(5, matches.size()); i++) {
            suggestions.add(matches.get(i).getKey() + " (" + matches.get(i).getValue() + " searches)");
        }
        return suggestions;
    }

    public static void main(String[] args) {
        AutocompleteSystem system = new AutocompleteSystem();

        system.recordSearch("java");
        system.recordSearch("java");
        system.recordSearch("java programming");
        system.recordSearch("javascript");
        system.recordSearch("javascript tutorial");
        system.recordSearch("javascript tutorial");
        system.recordSearch("javascript tutorial");
        system.recordSearch("python");
        system.recordSearch("python tutorial");
        system.recordSearch("java spring boot");

        System.out.println("Suggestions for 'java': " + system.getSuggestions("java"));
        System.out.println("Suggestions for 'java': " + system.getSuggestions("javascript"));
        System.out.println("Suggestions for 'py'  : " + system.getSuggestions("py"));
    }
}
