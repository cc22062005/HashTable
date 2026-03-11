import java.util.*;

public class RateLimiter {
    private final int maxRequests;
    private final long windowMillis;
    private HashMap<String, Queue<Long>> requestLog;

    public RateLimiter(int maxRequests, int windowSeconds) {
        this.maxRequests  = maxRequests;
        this.windowMillis = windowSeconds * 1000L;
        this.requestLog   = new HashMap<>();
    }

    public boolean allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        requestLog.putIfAbsent(clientId, new LinkedList<>());
        Queue<Long> timestamps = requestLog.get(clientId);

        // Remove expired timestamps outside the window
        while (!timestamps.isEmpty() && now - timestamps.peek() > windowMillis) {
            timestamps.poll();
        }

        if (timestamps.size() < maxRequests) {
            timestamps.add(now);
            System.out.println("[ALLOWED] " + clientId + " | Requests in window: " + timestamps.size());
            return true;
        } else {
            System.out.println("[BLOCKED] " + clientId + " | Limit reached: " + maxRequests + " req/window");
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Allow 3 requests per 5 seconds
        RateLimiter limiter = new RateLimiter(3, 5);

        System.out.println("--- Client A requests ---");
        limiter.allowRequest("clientA");
        limiter.allowRequest("clientA");
        limiter.allowRequest("clientA");
        limiter.allowRequest("clientA"); // should be blocked

        System.out.println("\n--- Client B requests ---");
        limiter.allowRequest("clientB");
        limiter.allowRequest("clientB");

        System.out.println("\nWaiting 5 seconds for window to reset...");
        Thread.sleep(5000);

        System.out.println("\n--- Client A again after reset ---");
        limiter.allowRequest("clientA"); // should be allowed
    }
}
