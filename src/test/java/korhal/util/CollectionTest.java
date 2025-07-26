package korhal.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

import korhal.BaseTest;

public class CollectionTest extends BaseTest {
  @Test
  public void arraylist_test() {
    List<Integer> al = new ArrayList<>();
  }

  @Test
  public void linkedlist_test() {
    LinkedList<Integer> ll = new LinkedList<>();
    ll.add(1);
    ll.add(2);
    ll.add(3);
    assertEquals(3, ll.peekLast());
    assertEquals(3, ll.peekLast());
    assertEquals(3, ll.peekLast());

    assertEquals(3, ll.pollLast());
    assertEquals(2, ll.pollLast());
    assertEquals(1, ll.pollLast());
  }

  @Test
  public void priority_queue_test() {
    List<Integer> d1 = List.of(9, 3, 7, 5, 8, 1, 4, 6, 2);
    assertTrue(d1 instanceof List);
    PriorityQueue<Integer> heap = new PriorityQueue<>(Comparator.reverseOrder());
    for (Integer i : d1) {
      heap.add(i);
    }

    List<Integer> sortedLst = new ArrayList<>(d1);
    Collections.sort(sortedLst, Comparator.reverseOrder());

    List<Integer> iterQ = new ArrayList<>(d1.size());
    Iterator<Integer> iter = heap.iterator();     // unreliable order
    while (iter.hasNext()) {
      iterQ.add(iter.next());
    }
    assertNotEquals(sortedLst, iterQ);            // not pass every time

    PriorityQueue<Integer> heapCopy = new PriorityQueue<>(heap);
    List<Integer> prioQ = new ArrayList<>(d1.size());
    while (!heapCopy.isEmpty()) {
      prioQ.add(heapCopy.poll());
    }
    assertEquals(sortedLst, prioQ);
  }
}
