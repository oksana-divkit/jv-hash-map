package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;

    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 >= threshold) {
            grow();
        }
        int index = getIndexForKey(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> previousNode = table[index];

            while (true) {
                if (key == previousNode.key || Objects.equals(previousNode.key, key)) {
                    previousNode.value = value;
                    return;
                }
                if (previousNode.next == null) {
                    break;
                } else {
                    previousNode = previousNode.next;
                }
            }

            previousNode.next = newNode;
            size++;
        }
    }

    private void grow() {
        size = 0;
        Node<K, V>[] tempTable = table;
        table = new Node[table.length * GROW_FACTOR];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (int i = 0; i < tempTable.length; i++) {
            Node<K, V> node = tempTable[i];
            if (node == null) {
                continue;
            }
            while (true) {
                put(node.key, node.value);
                if (node.next == null) {
                    break;
                } else {
                    node = node.next;
                }
            }
        }
    }

    private int getIndexForKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public V getValue(K key) {
        int index = getIndexForKey(key);
        Node<K, V> node = table[index];
        while (node != null && node.next != null) {
            if (key == node.key || Objects.equals(node.key, key)) {
                break;
            }
            node = node.next;
        }
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = key == null ? 0 : key.hashCode();
        }
    }
}
