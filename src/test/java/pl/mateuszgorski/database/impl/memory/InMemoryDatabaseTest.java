package pl.mateuszgorski.database.impl.memory;

import pl.mateuszgorski.database.Database;
import pl.mateuszgorski.database.impl.DatabaseTest;

public class InMemoryDatabaseTest extends DatabaseTest {

  @Override
  public Database getDatabase() {
    return new InMemoryDatabase();
  }
}