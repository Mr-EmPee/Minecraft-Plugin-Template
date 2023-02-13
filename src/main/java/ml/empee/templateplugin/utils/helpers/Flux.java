package ml.empee.templateplugin.utils.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class allows you to create a simple flux stream.
 **/
public abstract class Flux<T> {

  private Consumer<T> consumer;

  public void forEach(Consumer<T> consumer) {
    this.consumer = consumer;
    produceItems();
  }

  /**
   * Save all the items into a list.
   **/
  public List<T> toList() {
    List<T> list = new ArrayList<>();
    consumer = list::add;
    produceItems();
    return list;
  }

  public abstract void produceItems();

  protected void supply(T item) {
    if (consumer != null) {
      consumer.accept(item);
    }
  }

}
