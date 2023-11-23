package ml.empee.templateplugin.model.entities;

public interface AutoIncrementEntity extends Entity<Long> {
  /**
   * @return a copy of this entity with the given id
   */
  Entity<Long> withId(Long id);
}
