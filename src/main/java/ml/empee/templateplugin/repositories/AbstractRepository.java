package ml.empee.templateplugin.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import ml.empee.templateplugin.config.client.DbClient;
import ml.empee.templateplugin.model.entities.Entity;

/**
 * AbstractRepository
 */

public abstract class AbstractRepository<T extends Entity> {

  private final DbClient client;
  private final String table;

  protected AbstractRepository(DbClient client, String table) {
    this.client = client;
    this.table = table;

    createTable(schema());
  }

  protected abstract List<String> schema();

  @SneakyThrows
  private void createTable(List<String> columns) {
    var query = "CREATE TABLE IF NOT EXISTS " + table + " (";
    query += String.join(", ", columns);
    query += ");";

    try (var stm = client.getJdbcConnection().createStatement()) {
      stm.executeUpdate(query);
    }
  }

  /**
   * Find all entities
   */
  public CompletableFuture<List<T>> findAll() {
    return CompletableFuture.supplyAsync(() -> {
      var query = "SELECT * FROM " + table;
      try (var stm = client.getJdbcConnection().createStatement()) {
        var rs = stm.executeQuery(query);
        var plots = new ArrayList<T>();
        while (rs.next()) {
          plots.add(parse(rs));
        }

        return plots;
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }, client.getThreadPool());
  }

  /**
   * Save an entity
   */
  public CompletableFuture<Void> save(T entity) {
    return CompletableFuture.runAsync(() -> {
      var values = schema().stream().map(s -> "?").collect(Collectors.toList());
      var query = "INSERT OR REPLACE INTO " + table + " VALUES (" + String.join(", ", values) + ");";
      
      try (var stm = client.getJdbcConnection().prepareStatement(query)) {
        prepareStatement(stm, entity);
        stm.executeUpdate();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }, client.getThreadPool());
  }

  public CompletableFuture<Void> delete(Long id) {
    return CompletableFuture.runAsync(() -> {
      var query = "DELETE FROM " + table + " WHERE id = ?;";
      try (var stm = client.getJdbcConnection().prepareStatement(query)) {
        stm.setLong(1, id);
        stm.executeUpdate();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }, client.getThreadPool());
  }

  protected abstract void prepareStatement(PreparedStatement stm, T data) throws SQLException;

  protected abstract T parse(ResultSet rs) throws SQLException;

}
