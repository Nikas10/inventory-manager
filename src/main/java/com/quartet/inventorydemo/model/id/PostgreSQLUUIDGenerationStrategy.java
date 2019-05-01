package com.quartet.inventorydemo.model.id;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerationStrategy;

public class PostgreSQLUUIDGenerationStrategy implements UUIDGenerationStrategy {

  @Override
  public int getGeneratedVersion() {
    return 4;
  }

  @Override
  public UUID generateUUID(SharedSessionContractImplementor session) {
    return ((Session) session)
        .doReturningWork(
            connection -> { // CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
              try (Statement statement = connection.createStatement();
                  ResultSet resultSet = statement.executeQuery("select uuid_generate_v4()")) {
                while (resultSet.next()) {
                  return (UUID) resultSet.getObject(1);
                }
              }
              throw new IllegalArgumentException("Can't fetch a new UUIDString");
            });
  }
}
