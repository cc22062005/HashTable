import java.util.*;

public class MultiLevelCacheSystem {

    // L1 Cache: 10,000 most popular videos (in-memory, LRU)
    private final int L1_CAPACITY = 10000;
    // L2 Cache: 100,000 frequently accessed videos (SSD-backed, LRU)
    private final int L2_CAPACITY = 100000;

    private final LinkedHashMap<String, String> l1Cache;
    private final LinkedHashMap<String, String> l2Cache;
    // L3 is the database (simulated with a HashMap)
    private final HashMap<String, String> database;

    private int l1Hits = 0, l1Misses = 0;
    private int l2Hits = 0, l2Misses = 0;
    private int l3Hits = 0, l3Misses = 0;

    public MultiLevelCacheSystem() {
        // Access-order LinkedHashMap for LRU eviction
        l1Cache = new LinkedHashMap<>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > L1_CAPACITY;
            }
        };
        l2Cache = new LinkedHashMap<>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > L2_CAPACITY;
            }
        };
        database = new HashMap<>();
    }

    // Add video to the database (L3)
    public void addToDatabase(String videoId, String videoData) {
        database.put(videoId, videoData);
    }

    // Fetch video: checks L1 -> L2 -> L3
    public String fetchVideo(String videoId) {
        // Check L1
        if (l1Cache.containsKey(videoId)) {
            l1Hits++;
            System.out.println("L1 Cache HIT for: " + videoId);
            return l1Cache.get(videoId);
        }
        l1Misses++;

        // Check L2
        if (l2Cache.containsKey(videoId)) {
            l2Hits++;
            System.out.println("L2 Cache HIT for: " + videoId);
            String data = l2Cache.get(videoId);
            // Promote to L1
            l1Cache.put(videoId, data);
            return data;
        }
        l2Misses++;

        // Check L3 (database)
        if (database.containsKey(videoId)) {
            l3Hits++;
            System.out.println("L3 (DB) HIT for: " + videoId);
            String data = database.get(videoId);
            // Promote to L2 and L1
            l2Cache.put(videoId, data);
            l1Cache.put(videoId, data);
            return data;
        }
        l3Misses++;
        System.out.println("Cache MISS (not found) for: " + videoId);
        return null;
    }

    // Invalidate cache when content updates
    public void invalidateCache(String videoId, String newData) {
        l1Cache.remove(videoId);
        l2Cache.remove(videoId);
        database.put(videoId, newData);
        System.out.println("Cache invalidated and DB updated for: " + videoId);
    }

    // Print cache hit ratios
    public void printStats() {
        System.out.println("\n--- Cache Statistics ---");
        int totalL1 = l1Hits + l1Misses;
        int totalL2 = l2Hits + l2Misses;
        int totalL3 = l3Hits + l3Misses;
        System.out.printf("L1 Hit Rate: %.1f%% (%d hits / %d total)%n",
                totalL1 == 0 ? 0.0 : (l1Hits * 100.0 / totalL1), l1Hits, totalL1);
        System.out.printf("L2 Hit Rate: %.1f%% (%d hits / %d total)%n",
                totalL2 == 0 ? 0.0 : (l2Hits * 100.0 / totalL2), l2Hits, totalL2);
        System.out.printf("L3 Hit Rate: %.1f%% (%d hits / %d total)%n",
                totalL3 == 0 ? 0.0 : (l3Hits * 100.0 / totalL3), l3Hits, totalL3);
    }

    public static void main(String[] args) {
        MultiLevelCacheSystem cache = new MultiLevelCacheSystem();

        // Populate database
        cache.addToDatabase("video001", "Avengers Endgame - 4K");
        cache.addToDatabase("video002", "The Dark Knight - HD");
        cache.addToDatabase("video003", "Inception - HD");
        cache.addToDatabase("video004", "Interstellar - 4K");

        System.out.println("=== Multi-Level Cache System Demo ===");

        // First fetch - goes to L3
        String v1 = cache.fetchVideo("video001");
        System.out.println("Fetched: " + v1);

        // Second fetch - should be L1 hit
        String v1Again = cache.fetchVideo("video001");
        System.out.println("Fetched again: " + v1Again);

        // New video - L3
        String v2 = cache.fetchVideo("video002");
        System.out.println("Fetched: " + v2);

        // Video not in DB
        String missing = cache.fetchVideo("video999");
        System.out.println("Fetched missing: " + missing);

        // Invalidate and update
        cache.invalidateCache("video001", "Avengers Endgame - 8K Remaster");
        String updated = cache.fetchVideo("video001");
        System.out.println("After update: " + updated);

        cache.printStats();
    }
}
