package com.msr.documents.mongo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by ranjan on 15/10/20.
 */
@Data
@NoArgsConstructor
@Document("docs")
public class MongoDocument {

  public MongoDocument(Binary bytes) {
    this.doc = bytes;
  }

  @Id
  private String docId;

  private Binary doc;

}
