import java.util.*;

public class AnalyticsDashboard {
    private HashMap<String, Integer> pageViews;
    private HashMap<String, Integer> uniqueVisitors;
    private HashMap<String, Integer> userSessions;

    public AnalyticsDashboard() {
        pageViews     = new HashMap<>();
        uniqueVisitors = new HashMap<>();
        userSessions  = new HashMap<>();
    }

    public void recordVisit(String page, String userId) {
        pageViews.merge(page, 1, Integer::sum);
        userSessions.merge(userId, 1, Integer::sum);
        uniqueVisitors.putIfAbsent(page, 0);
        uniqueVisitors.merge(page, 1, Integer::sum);
        System.out.println("Visit recorded: " + userId + " -> " + page);
    }

    public void printDashboard() {
        System.out.println("\n===== Analytics Dashboard =====");
        System.out.println("Page\t\t\tViews\tUnique Visitors");
        System.out.println("---------------------------------------------");
        for (String page : pageViews.keySet()) {
            System.out.printf("%-20s\t%d\t%d%n",
                    page, pageViews.get(page), uniqueVisitors.get(page));
        }
        System.out.println("\nTop Page: " + getTopPage());
    }

    public String getTopPage() {
        return pageViews.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");
    }

    public static void main(String[] args) {
        AnalyticsDashboard dashboard = new AnalyticsDashboard();

        dashboard.recordVisit("/home",    "user1");
        dashboard.recordVisit("/home",    "user2");
        dashboard.recordVisit("/home",    "user1");
        dashboard.recordVisit("/about",   "user3");
        dashboard.recordVisit("/products","user1");
        dashboard.recordVisit("/products","user4");
        dashboard.recordVisit("/products","user2");
        dashboard.recordVisit("/contact", "user5");

        dashboard.printDashboard();
    }
}
