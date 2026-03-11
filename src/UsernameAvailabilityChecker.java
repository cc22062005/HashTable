import java.util.*;

public class UsernameAvailabilityChecker {
    private HashMap<String, Integer> registeredUsers;
    private HashMap<String, Integer> attemptFrequency;

    public UsernameAvailabilityChecker() {
        registeredUsers = new HashMap<>();
        attemptFrequency = new HashMap<>();
        registeredUsers.put("john_doe", 1001);
        registeredUsers.put("admin", 1002);
        registeredUsers.put("alice", 1003);
    }

    public boolean checkAvailability(String username) {
        attemptFrequency.merge(username, 1, Integer::sum);
        return !registeredUsers.containsKey(username);
    }

    public boolean register(String username, int userId) {
        if (checkAvailability(username)) {
            registeredUsers.put(username, userId);
            System.out.println("Registered: " + username);
            return true;
        }
        System.out.println(username + " is taken. Suggestions: " + suggestAlternatives(username));
        return false;
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String candidate = username + i;
            if (!registeredUsers.containsKey(candidate)) suggestions.add(candidate);
        }
        suggestions.add(username + "_");
        return suggestions;
    }

    public String getMostAttempted() {
        return attemptFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");
    }

    public static void main(String[] args) {
        UsernameAvailabilityChecker checker = new UsernameAvailabilityChecker();

        System.out.println("john_doe available: " + checker.checkAvailability("john_doe"));
        System.out.println("jane_smith available: " + checker.checkAvailability("jane_smith"));
        System.out.println("Suggestions for john_doe: " + checker.suggestAlternatives("john_doe"));

        checker.register("bob", 2001);
        checker.register("john_doe", 2002);

        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        System.out.println("Most attempted: " + checker.getMostAttempted());
    }
}
