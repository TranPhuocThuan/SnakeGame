import java.io.*;

class HashChainApp {
    public static void main(String[] args) throws IOException {
        int aKey;
        Link aDataItem;
        int size, n, keysPerCell = 100;

        // get sizes
        System.out.print("Enter size of hash table: ");
        size = getInt();
        System.out.print("Enter initial number of items: ");
        n = getInt();

        // make table
        HashTable theHashTable = new HashTable(size);

        // Keep track of the key sequence for initial filling of the table
        StringBuilder keySequence = new StringBuilder("Key sequence for initial filling: ");

        for (int j = 0; j < n; j++) {
            aKey = (int) (java.lang.Math.random() * keysPerCell * size);
            keySequence.append(aKey).append(" ");
            aDataItem = new Link(aKey);
            theHashTable.insert(aDataItem);
        }

        // Display key sequence for initial filling of the table
        System.out.println(keySequence.toString());

        // Calculate and display average probe length for initial filling
        double totalProbeLength = theHashTable.getTotalProbeLength();
        double averageProbeLength = totalProbeLength / n;
        System.out.println("Average probe length for initial filling: " + averageProbeLength);

        while (true) {
            System.out.print("Enter first letter of ");
            System.out.print("show, insert, delete, or find: ");
            char choice = getChar();

            switch (choice) {
                case 's':
                    theHashTable.displayTable();
                    break;
                case 'i':
                    System.out.print("Enter key value to insert: ");
                    aKey = getInt();
                    aDataItem = new Link(aKey);

                    // Display hash value and probe sequence for insert
                    theHashTable.displayInsertInfo(aDataItem);

                    theHashTable.insert(aDataItem);
                    break;
                case 'd':
                    System.out.print("Enter key value to delete: ");
                    aKey = getInt();
                    theHashTable.delete(aKey);
                    break;
                case 'f':
                    System.out.print("Enter key value to find: ");
                    aKey = getInt();

                    // Display hash value and probe sequence for find
                    theHashTable.displayFindInfo(aKey);

                    aDataItem = theHashTable.find(aKey);
                    if (aDataItem != null)
                        System.out.println("Found " + aKey);
                    else
                        System.out.println("Could not find " + aKey);
                    break;
                default:
                    System.out.print("Invalid entry\n");
            } // end switch
        } // end while
    } // end main()

    // Other methods remain unchanged...
}

class HashTable {
    // Other methods remain unchanged...

    // Calculate and return the total probe length for the table
    public double getTotalProbeLength() {
        double totalProbeLength = 0;
        for (SortedList list : hashArray) {
            totalProbeLength += list.getProbeLength();
        }
        return totalProbeLength;
    }

    // Display hash value and probe sequence for insert
    public void displayInsertInfo(Link theLink) {
        int key = theLink.getKey();
        int hashVal = hashFunc(key);
        int probeLength = hashArray[hashVal].getInsertProbeLength(key);

        System.out.println("Hash Value for Insert: " + hashVal);
        System.out.println("Probe Sequence for Insert: " + probeLength);
    }

    // Display hash value and probe sequence for find
    public void displayFindInfo(int key) {
        int hashVal = hashFunc(key);
        int probeLength = hashArray[hashVal].getFindProbeLength(key);

        System.out.println("Hash Value for Find: " + hashVal);
        System.out.println("Probe Sequence for Find: " + probeLength);
    }

    // Other methods remain unchanged...
}

class SortedList {
    // Other methods remain unchanged...

    // Calculate and return the probe length for the last insert operation
    public int getInsertProbeLength(int key) {
        int probeLength = 1;
        Link current = first;

        while (current != null && current.getKey() != key) {
            probeLength++;
            current = current.next;
        }

        return probeLength;
    }

    // Calculate and return the probe length for the last find operation
    public int getFindProbeLength(int key) {
        int probeLength = 1;
        Link current = first;

        while (current != null && current.getKey() != key) {
            probeLength++;
            current = current.next;
        }

        return probeLength;
    }

    // Calculate and return the total probe length for the list
    public int getProbeLength() {
        int probeLength = 0;
        Link current = first;

        while (current != null) {
            probeLength += current.getProbeLength();
            current = current.next;
        }

        return probeLength;
    }

    // Other methods remain unchanged...
}
