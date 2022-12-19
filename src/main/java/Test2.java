import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test2<K, V> implements Iterable<CustomKeyValue<K, V>> {
    //Write a new thread-safe HashMap class.?
    // for make hashmap safe class make method to wrap method in synchronized format

    private static int BUCKET_CAPACITY = 64;
    private final Object editLock = new Object();
    private CustomLinkList<K, V>[] buckets;
    private int hash(K key) {
        return Math.abs(key.hashCode()) % BUCKET_CAPACITY;
    }


    private CustomLinkList<K, V> fetchListHoldingKey(K key) {
        int index = hash(key);
        return this.buckets[index];
    }

    private Stream<CustomLinkList<K, V>> nonEmptyBucketsStream() {
        return Arrays.stream(this.buckets).filter(bucket -> !bucket.isEmpty());
    }

    int getNextNonEmptyBucketFromIndex(int index) {
        if (index >= BUCKET_CAPACITY)
            return 0;
        int i = index;
        while (i < BUCKET_CAPACITY && this.buckets[i].isEmpty())
            i++;
        return i == BUCKET_CAPACITY ? 0 : i;
    }

    CustomLinkList<K, V> getListInBucket(int index) {
        return this.buckets[index];
    }


    public void put(K key, V value) {
        synchronized (editLock) {
            CustomLinkList<K, V> list = fetchListHoldingKey(key);
            list.add(key, value);
        }
    }

    public V get(K key) throws Exception {
        return fetchListHoldingKey(key).fetchValueInNodeWithKey(key);
    }

    public V get(K key, V defaultValue) {
        return fetchListHoldingKey(key).fetchValueInNodeWithKey(key, defaultValue);
    }

    public V getThreadSafe(K key, V defaultValue) {
        synchronized (editLock) {
            return fetchListHoldingKey(key).fetchValueInNodeWithKey(key, defaultValue);
        }
    }

    public void remove(K key) throws Exception {
        synchronized (editLock) {
            CustomLinkList<K, V> listContainingNodeToRemove = this.fetchListHoldingKey(key);
            if (!listContainingNodeToRemove.removeNodeWithKeyIfExists(key))
                throw new Exception(key.toString());
        }
    }


    public Set<K> keys() {
        return nonEmptyBucketsStream().flatMap(CustomLinkList::getAllKeys).collect(Collectors.toSet());
    }

    public List<V> values() {
        return nonEmptyBucketsStream().flatMap(CustomLinkList::getAllValues).collect(Collectors.toList());
    }

    public List<CustomKeyValue<K, V>> keyValuePojo() {
        return nonEmptyBucketsStream().flatMap(CustomLinkList::getAllPojo).collect(Collectors.toList());
    }

    public boolean isPresent(K key) {
        return !fetchListHoldingKey(key).isEmpty();
    }


    @Override
    public Iterator<CustomKeyValue<K, V>> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super CustomKeyValue<K, V>> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<CustomKeyValue<K, V>> spliterator() {
        return Iterable.super.spliterator();
    }
}
