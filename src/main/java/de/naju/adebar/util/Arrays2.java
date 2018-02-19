package de.naju.adebar.util;

import java.util.Arrays;

/**
 * More utilities for dealing with arrays
 *
 * @author Rico Bergmann
 * @see Arrays
 */
public class Arrays2 {

  private Arrays2() {}

  /**
   * Searches for the given key in the array, using linear search.
   *
   * <p>
   * This means that for an array {@code a}, the maximum time complexity is {@code O(|a|)}. However
   * in comparison to {@link Arrays#binarySearch(byte[], byte)} the array does not need to be sorted
   * beforehand. Therefore linear search will always work - although with pretty poor performance.
   *
   * @param a the array to search
   * @param key the key
   * @return the index of the first occurrence (the smallest index) of the key in the array, or
   *         {@code -1} if the key was not found
   */
  public static int linearSearch(byte[] a, byte key) {
    for (int i = 0; i < a.length; ++i) {
      if (a[i] == key) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Searches for the given key in the array, using linear search.
   *
   * <p>
   * This means that for an array {@code a}, the maximum time complexity is {@code O(|a|)}. However
   * in comparison to {@link Arrays#binarySearch(short[], short)} the array does not need to be
   * sorted beforehand. Therefore linear search will always work - although with pretty poor
   * performance.
   *
   * @param a the array to search
   * @param key the key
   * @return the index of the first occurrence (the smallest index) of the key in the array, or
   *         {@code -1} if the key was not found
   */
  public static int linearSearch(short[] a, short key) {
    for (int i = 0; i < a.length; ++i) {
      if (a[i] == key) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Searches for the given key in the array, using linear search.
   *
   * <p>
   * This means that for an array {@code a}, the maximum time complexity is {@code O(|a|)}. However
   * in comparison to {@link Arrays#binarySearch(int[], int)} the array does not need to be sorted
   * beforehand. Therefore linear search will always work - although with pretty poor performance.
   *
   * @param a the array to search
   * @param key the key
   * @return the index of the first occurrence (the smallest index) of the key in the array, or
   *         {@code -1} if the key was not found
   */
  public static int linearSearch(int[] a, int key) {
    for (int i = 0; i < a.length; ++i) {
      if (a[i] == key) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Searches for the given key in the array, using linear search.
   *
   * <p>
   * This means that for an array {@code a}, the maximum time complexity is {@code O(|a|)}. However
   * in comparison to {@link Arrays#binarySearch(long[], long)} the array does not need to be sorted
   * beforehand. Therefore linear search will always work - although with pretty poor performance.
   *
   * @param a the array to search
   * @param key the key
   * @return the index of the first occurrence (the smallest index) of the key in the array, or
   *         {@code -1} if the key was not found
   */
  public static int linearSearch(long[] a, long key) {
    for (int i = 0; i < a.length; ++i) {
      if (a[i] == key) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Searches for the given key in the array, using linear search.
   *
   * <p>
   * This means that for an array {@code a}, the maximum time complexity is {@code O(|a|)}. However
   * in comparison to {@link Arrays#binarySearch(float[], float)} the array does not need to be
   * sorted beforehand. Therefore linear search will always work - although with pretty poor
   * performance.
   *
   * @param a the array to search
   * @param key the key
   * @return the index of the first occurrence (the smallest index) of the key in the array, or
   *         {@code -1} if the key was not found
   */
  public static int linearSearch(float[] a, float key) {
    for (int i = 0; i < a.length; ++i) {
      if (a[i] == key) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Searches for the given key in the array, using linear search.
   *
   * <p>
   * This means that for an array {@code a}, the maximum time complexity is {@code O(|a|)}. However
   * in comparison to {@link Arrays#binarySearch(double[], double)} the array does not need to be
   * sorted beforehand. Therefore linear search will always work - although with pretty poor
   * performance.
   *
   * @param a the array to search
   * @param key the key
   * @return the index of the first occurrence (the smallest index) of the key in the array, or
   *         {@code -1} if the key was not found
   */
  public static int linearSearch(double[] a, double key) {
    for (int i = 0; i < a.length; ++i) {
      if (a[i] == key) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Searches for the given key in the array, using linear search.
   *
   * <p>
   * This means that for an array {@code a}, the maximum time complexity is {@code O(|a|)}. However
   * in comparison to {@link Arrays#binarySearch(boolean[], boolean)} the array does not need to be
   * sorted beforehand. Therefore linear search will always work - although with pretty poor
   * performance.
   *
   * @param a the array to search
   * @param key the key
   * @return the index of the first occurrence (the smallest index) of the key in the array, or
   *         {@code -1} if the key was not found
   */
  public static int linearSearch(boolean[] a, boolean key) {
    for (int i = 0; i < a.length; ++i) {
      if (a[i] == key) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Searches for the given key in the array, using linear search.
   *
   * <p>
   * This means that for an array {@code a}, the maximum time complexity is {@code O(|a|)}. However
   * in comparison to {@link Arrays#binarySearch(char[], char)} the array does not need to be sorted
   * beforehand. Therefore linear search will always work - although with pretty poor performance.
   *
   * @param a the array to search
   * @param key the key
   * @return the index of the first occurrence (the smallest index) of the key in the array, or
   *         {@code -1} if the key was not found
   */
  public static int linearSearch(char[] a, char key) {
    for (int i = 0; i < a.length; ++i) {
      if (a[i] == key) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Searches for the given key in the array, using linear search.
   *
   * <p>
   * This means that for an array {@code a}, the maximum time complexity is {@code O(|a|)}. However
   * in comparison to {@link Arrays#binarySearch(byte[], byte)} the array does not need to be sorted
   * beforehand. Therefore linear search will always work - although with pretty poor performance.
   *
   * @param a the array to search
   * @param key the key, may be {@code null}
   * @return the index of the first occurrence (the smallest index) of the key in the array, or
   *         {@code -1} if the key was not found
   */
  public static int linearSearch(Object[] a, Object key) {
    for (int i = 0; i < a.length; ++i) {
      if (a[i] == null && key != null) {
        continue;
      } else if (a[i] == null && key == null) {
        return i;
      } else if (a[i].equals(key)) {
        return i;
      }
    }
    return -1;
  }

}
