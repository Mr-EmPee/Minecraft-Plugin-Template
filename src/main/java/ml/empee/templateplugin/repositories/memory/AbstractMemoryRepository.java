package ml.empee.templateplugin.repositories.memory;

import lombok.Getter;
import ml.empee.templateplugin.model.entities.AutoIncrementEntity;
import ml.empee.templateplugin.model.entities.Entity;
import ml.empee.templateplugin.repositories.AbstractRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.TreeMap;

/**
 * FULL In-Memory repository with async persistence
 */

public abstract class AbstractMemoryRepository<R extends AbstractRepository<T, K>, T extends Entity<K>, K extends Comparable<K>> {

  private final TreeMap<K, T> cache = (TreeMap<K, T>) Collections.synchronizedMap(new TreeMap<K, T>());

  @Getter
  private final R backend;

  protected AbstractMemoryRepository(R repository) {
    this.backend = repository;

    loadFromRepository();
  }

  protected void loadFromRepository() {
    backend.findAll().join().forEach(
        e -> cache.put(e.getId(), e)
    );
  }

  public void reload() {
    cache.clear();
    loadFromRepository();
  }

  public Collection<T> getAll() {
    return Collections.unmodifiableCollection(cache.values());
  }

  public T save(T entity) {
    if (entity.getId() == null) {
      if (entity instanceof AutoIncrementEntity) {
        var id = cache.isEmpty() ? 0 : ((Long) cache.lastKey()) + 1L;
        entity = (T) ((AutoIncrementEntity) entity).withId(id);
      } else {
        throw new IllegalArgumentException("Missing id from entity!");
      }
    }

    cache.put(entity.getId(), entity);
    backend.save(entity);

    return entity;
  }

  public Optional<T> get(K id) {
    return Optional.ofNullable(cache.get(id));
  }

}
