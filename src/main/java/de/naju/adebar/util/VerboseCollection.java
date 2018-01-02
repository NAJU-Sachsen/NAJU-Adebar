package de.naju.adebar.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection which will log all of its method calls. In order to work, it must be able to print
 * info messages to the log. Optionally, it may also log information about the calling method.
 * 
 * @author Rico Bergmann
 *
 * @param <E> the entities to store in the collection
 */
public class VerboseCollection<E> implements Collection<E> {

  private static final Logger log = LoggerFactory.getLogger(VerboseCollection.class);
  private static final int STACK_DESCEND_DEPTH = 3;

  private Collection<E> store;
  private boolean logCaller;

  /**
   * Creates a new collection which will not log information about its caller
   * 
   * @throws IllegalStateException if the log has info disabled
   */
  public VerboseCollection() {
    this(false);
  }

  /**
   * Creates a new collection
   * 
   * @param logCaller whether the collection should log information about its caller
   * @throws IllegalStateException if the log has info disabled
   */
  public VerboseCollection(boolean logCaller) {
    if (!log.isInfoEnabled()) {
      throw new IllegalStateException("The log must have info enabled");
    }
    this.store = new ArrayList<>();
    this.logCaller = logCaller;
    log.info(appendCallerIfNecessary("Initializing new collection"));
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#add(java.lang.Object)
   */
  @Override
  public boolean add(E arg0) {
    log.info(appendCallerIfNecessary("Adding element " + arg0));
    return store.add(arg0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#addAll(java.util.Collection)
   */
  @Override
  public boolean addAll(Collection<? extends E> arg0) {
    log.info(appendCallerIfNecessary("Adding collection " + arg0));
    return store.addAll(arg0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#clear()
   */
  @Override
  public void clear() {
    log.info(appendCallerIfNecessary("Clearing elements"));
    store.clear();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#contains(java.lang.Object)
   */
  @Override
  public boolean contains(Object arg0) {
    log.info(appendCallerIfNecessary("Searching for " + arg0));
    return store.contains(arg0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#containsAll(java.util.Collection)
   */
  @Override
  public boolean containsAll(Collection<?> arg0) {
    log.info(appendCallerIfNecessary("Searching for collection " + arg0));
    return store.containsAll(arg0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#isEmpty()
   */
  @Override
  public boolean isEmpty() {
    log.info(appendCallerIfNecessary("Checking for emptiness"));
    return store.isEmpty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#iterator()
   */
  @Override
  public Iterator<E> iterator() {
    log.info(appendCallerIfNecessary("Providing iterator"));
    return store.iterator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#remove(java.lang.Object)
   */
  @Override
  public boolean remove(Object arg0) {
    log.info(appendCallerIfNecessary("Removing " + arg0));
    return store.remove(arg0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#removeAll(java.util.Collection)
   */
  @Override
  public boolean removeAll(Collection<?> arg0) {
    log.info(appendCallerIfNecessary("Removing collection " + arg0));
    return store.removeAll(arg0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#retainAll(java.util.Collection)
   */
  @Override
  public boolean retainAll(Collection<?> arg0) {
    log.info(appendCallerIfNecessary("Retaining only " + arg0));
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#size()
   */
  @Override
  public int size() {
    log.info(appendCallerIfNecessary("Providing size"));
    return store.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#toArray()
   */
  @Override
  public Object[] toArray() {
    log.info(appendCallerIfNecessary("Providing array"));
    return store.toArray();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#toArray(java.lang.Object[])
   */
  @Override
  public <T> T[] toArray(T[] arg0) {
    log.info(appendCallerIfNecessary("Providing parameterized array for type " + arg0.getClass()));
    return store.toArray(arg0);
  }

  private String appendCallerIfNecessary(String msg) {
    if (logCaller) {
      StackTraceElement[] st = Thread.currentThread().getStackTrace();
      StackTraceElement caller = st[STACK_DESCEND_DEPTH];
      msg += "; called from " + caller.toString();
    }
    return msg;
  }

}
