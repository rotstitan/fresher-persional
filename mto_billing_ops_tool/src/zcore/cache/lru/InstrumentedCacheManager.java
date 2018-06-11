package zcore.cache.lru;

import com.reardencommerce.kernel.collections.shared.evictable.ConcurrentLinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InstrumentedCacheManager<K, V>
{
  private static Map<String, InstrumentedCache> INSTANCES = new HashMap();

  private static final Lock createLock_ = new ReentrantLock();

  public static InstrumentedCache getCache(String name, int size) {
    return getCache(name, size, ConcurrentLinkedHashMap.EvictionPolicy.SECOND_CHANCE);
  }

  public static InstrumentedCache getCache(String name, int size, ConcurrentLinkedHashMap.EvictionPolicy evictionPolicty) {
    String key = name;
    if (!INSTANCES.containsKey(key)) {
      createLock_.lock();
      try {
        if (!INSTANCES.containsKey(key))
          INSTANCES.put(key, new InstrumentedCache(name, size, evictionPolicty));
      }
      finally {
        createLock_.unlock();
      }
    }
    return (InstrumentedCache)INSTANCES.get(key);
  }

  public static InstrumentedCache getCacheExisted(String name) {
    String key = name;
    if (INSTANCES.containsKey(key)) {
      return (InstrumentedCache)INSTANCES.get(key);
    }
    return null;
  }
}