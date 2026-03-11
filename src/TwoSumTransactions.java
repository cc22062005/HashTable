import java.util.*;

public class TwoSumTransactions {

    // Classic Two Sum: find indices of two transactions that sum to target
    public int[] twoSum(int[] amounts, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < amounts.length; i++) {
            int complement = target - amounts[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(amounts[i], i);
        }
        return new int[]{};
    }

    // Find all pairs that sum to target
    public List<int[]> allPairs(int[] amounts, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();
        for (int amount : amounts) {
            int complement = target - amount;
            if (map.containsKey(complement) && map.get(complement) > 0) {
                result.add(new int[]{complement, amount});
                map.put(complement, map.get(complement) - 1);
            } else {
                map.merge(amount, 1, Integer::sum);
            }
        }
        return result;
    }

    // Check if any three transactions sum to target
    public boolean threeSum(int[] amounts, int target) {
        Arrays.sort(amounts);
        for (int i = 0; i < amounts.length - 2; i++) {
            HashSet<Integer> set = new HashSet<>();
            int remaining = target - amounts[i];
            for (int j = i + 1; j < amounts.length; j++) {
                if (set.contains(remaining - amounts[j])) return true;
                set.add(amounts[j]);
            }
        }
        return false;
    }

    public static void main(String[] args) {
        TwoSumTransactions solver = new TwoSumTransactions();
        int[] transactions = {200, 150, 350, 100, 50, 250, 400};

        System.out.println("Transactions: " + Arrays.toString(transactions));

        int[] pair = solver.twoSum(transactions, 450);
        System.out.println("\ntwoSum(target=450): indices " + Arrays.toString(pair)
                + " -> values [" + transactions[pair[0]] + ", " + transactions[pair[1]] + "]");

        List<int[]> allPairs = solver.allPairs(transactions, 300);
        System.out.println("\nallPairs(target=300):");
        for (int[] p : allPairs) System.out.println("  " + Arrays.toString(p));

        System.out.println("\nthreeSum(target=600): " + solver.threeSum(transactions, 600));
        System.out.println("threeSum(target=999): " + solver.threeSum(transactions, 999));
    }
}
