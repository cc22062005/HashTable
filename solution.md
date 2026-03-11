# Problem 2: E-commerce Flash Sale Inventory Manager

## Problem Statement
Implement an inventory management system that tracks product stock in real-time, processes purchase requests in O(1) time, and maintains a waiting list when stock runs out.

## Solution (Java)

```java
import java.util.*;

public class FlashSaleInventoryManager {
    private HashMap<String, Integer> stockMap;          // productId -> stock
    private HashMap<String, Queue<Integer>> waitingList; // productId -> waiting userIds

    public FlashSaleInventoryManager() {
        stockMap = new HashMap<>();
        waitingList = new HashMap<>();
    }

    public void addProduct(String productId, int quantity) {
        stockMap.put(productId, quantity);
        waitingList.put(productId, new LinkedList<>());
        System.out.println("Added product: " + productId + " with " + quantity + " units.");
    }

    public int checkStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    public synchronized String purchaseItem(String productId, int userId) {
        int stock = stockMap.getOrDefault(productId, 0);
        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success! User " + userId + " purchased " + productId + ". Remaining: " + (stock - 1);
        } else {
            waitingList.get(productId).add(userId);
            int position = waitingList.get(productId).size();
            return "Out of stock! User " + userId + " added to waiting list at position #" + position;
        }
    }

    public void processWaitingList(String productId, int newStock) {
        stockMap.put(productId, newStock);
        Queue<Integer> queue = waitingList.get(productId);
        System.out.println("\nRestocked " + productId + " with " + newStock + " units.");
        while (newStock > 0 && !queue.isEmpty()) {
            int userId = queue.poll();
            stockMap.put(productId, --newStock);
            System.out.println("Notified waiting user: " + userId);
        }
    }

    public static void main(String[] args) {
        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();
        manager.addProduct("IPHONE15_256GB", 3);

        System.out.println("Stock: " + manager.checkStock("IPHONE15_256GB"));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 11111));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 22222));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 33333));

        manager.processWaitingList("IPHONE15_256GB", 2);
    }
}
```

## Expected Output
```
Added product: IPHONE15_256GB with 3 units.
Stock: 3
Success! User 12345 purchased IPHONE15_256GB. Remaining: 2
Success! User 67890 purchased IPHONE15_256GB. Remaining: 1
Success! User 11111 purchased IPHONE15_256GB. Remaining: 0
Out of stock! User 22222 added to waiting list at position #1
Out of stock! User 33333 added to waiting list at position #2

Restocked IPHONE15_256GB with 2 units.
Notified waiting user: 22222
Notified waiting user: 33333
```
