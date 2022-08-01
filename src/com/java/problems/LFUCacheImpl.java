package com.java.problems;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

public class LFUCacheImpl {

  public static void main(String[] args) {
    LFUCacheImpl ca = new LFUCacheImpl(4);
    ca.refer(1);
    ca.refer(1);
    ca.refer(3);
    ca.refer(2);
    ca.refer(2);
    ca.refer(3);
    ca.refer(1);
    ca.refer(5);
    ca.refer(6);
    ca.display();
  }

  private int capacity;
  private Set<Cache> lfu;
  private PriorityQueue<Cache> pq;

  public LFUCacheImpl(int capacity){
    this.capacity = capacity;
    this.lfu = new LinkedHashSet<>(capacity);
    this.pq = new PriorityQueue<>(new Comparator<Cache>() {
      @Override
      public int compare(Cache o1, Cache o2) {
        return o1.freq- o2.freq;
      }
    });
  }

  public boolean get(Object input){
    if(lfu.contains(new Cache(input))){
      Iterator<Cache> itr = pq.iterator();
      while(itr.hasNext()){
        Cache currCache = itr.next();
        if(currCache.equals(new Cache(input))){
          currCache.freq +=1;
          break;
        }
      }
      return true;
    }
    return false;
  }

  public void refer(Object input){
    if(!get(input)){
      put(input);
    }
  }

  private void put(Object input){
    if(lfu.size() == capacity){
      Cache cache = pq.poll();
      lfu.remove(cache);
    }
    Cache newCache = new Cache(input);
      lfu.add(newCache);
      pq.add(newCache);

  }

  public void display(){

    Iterator<Cache> itr = pq.iterator();
    while(itr.hasNext()){
      Cache cache = itr.next();
      System.out.println(cache.input+" : freq  "+cache.freq);
    }
    System.out.println();
  }

}



class Cache {
   Object input;
   int freq;
   public Cache(Object input){
     this.input = input;
     this.freq = 1;
   }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cache that = (Cache) o;
    return Objects.equals(input, that.input);
  }

  @Override
  public int hashCode() {
    return Objects.hash(input);
  }
}
