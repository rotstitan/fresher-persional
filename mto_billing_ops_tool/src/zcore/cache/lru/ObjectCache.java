package zcore.cache.lru;

public class ObjectCache<V>
{
  private V value;
  private long expire;

  ObjectCache(V value, long expire)
  {
    this.value = value;
    this.expire = expire;
  }

  public V getValue() {
    return this.value;
  }

  public long getExpire() {
    return this.expire;
  }
}