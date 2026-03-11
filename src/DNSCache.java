import java.util.*;

public class DNSCache {
    static class DNSEntry {
        String ipAddress;
        long expiryTime;

        DNSEntry(String ip, int ttlSeconds) {
            this.ipAddress = ip;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000L);
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final HashMap<String, DNSEntry> cache;
    private int hits = 0, misses = 0;

    public DNSCache() {
        cache = new HashMap<>();
    }

    public String resolve(String domain) {
        DNSEntry entry = cache.get(domain);
        if (entry != null && !entry.isExpired()) {
            hits++;
            System.out.println("Cache HIT  : " + domain + " -> " + entry.ipAddress);
            return entry.ipAddress;
        }
        if (entry != null) {
            cache.remove(domain);
            System.out.println("Cache EXPIRED: " + domain);
        } else {
            System.out.println("Cache MISS : " + domain);
        }
        misses++;
        String ip = queryUpstreamDNS(domain);
        cache.put(domain, new DNSEntry(ip, 5));
        return ip;
    }

    private String queryUpstreamDNS(String domain) {
        Map<String, String> upstream = new HashMap<>();
        upstream.put("google.com", "172.217.14.206");
        upstream.put("github.com", "140.82.113.4");
        upstream.put("amazon.com", "205.251.242.103");
        return upstream.getOrDefault(domain, "0.0.0.0");
    }

    public void printStats() {
        int total = hits + misses;
        double rate = total == 0 ? 0 : (hits * 100.0 / total);
        System.out.printf("Stats -> Hits: %d, Misses: %d, Hit Rate: %.1f%%%n", hits, misses, rate);
    }

    public static void main(String[] args) throws InterruptedException {
        DNSCache dns = new DNSCache();
        dns.resolve("google.com");
        dns.resolve("google.com");
        dns.resolve("github.com");
        dns.resolve("github.com");
        dns.resolve("amazon.com");
        System.out.println("\nWaiting 6s for TTL to expire...");
        Thread.sleep(6000);
        dns.resolve("google.com");
        dns.resolve("google.com");
        System.out.println();
        dns.printStats();
    }
}
