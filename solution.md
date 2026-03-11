# Problem 1: Social Media Username Availability Checker

## Problem Statement
Design a system to check username availability in O(1) time, suggest alternatives if taken, and track popularity of attempted usernames.

## Solution (Java)

```java
import java.util.*;

public class UsernameAvailabilityChecker {
    private HashMap<String, Integer> registeredUsers;  // username -> userId
    private HashMap<String, Integer> attemptFrequency; // username -> attempt count

    public UsernameAvailabilityChecker() {
        registeredUsers = new HashMap<>();
        attemptFrequency = new HashMap<>();
        // Pre-populate some users
        registeredUsers.put("john_doe", 1001);
        registeredUsers.put("admin", 1002);
        registeredUsers.put("alice", 1003);
    }

    // O(1) check
    public boolean checkAvailability(String username) {
        attemptFrequency.merge(username, 1, Integer::sum);
        return !registeredUsers.containsKey(username);
    }

    // Register a username
    public boolean register(String username, int userId) {
        if (checkAvailability(username)) {
            registeredUsers.put(username, userId);
            System.out.println("Registered: " + username);
            return true;
        }
        System.out.println(username + " is taken. Suggestions: " + suggestAlternatives(username));
        return false;
    }

    // Suggest alternatives
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String candidate = username + i;
            if (!registeredUsers.containsKey(candidate)) suggestions.add(candidate);
        }
        suggestions.add(username + "_");
        return suggestions;
    }

    // Get most attempted username
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
```

## Expected Output
```
john_doe available: false
jane_smith available: true
Suggestions for john_doe: [john_doe1, john_doe2, john_doe3, john_doe_]
Registered: bob
john_doe is taken. Suggestions: [john_doe1, john_doe2, john_doe3, john_doe_]
Most attempted: admin
```
