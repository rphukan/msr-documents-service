package com.msr.documents.mongo.repository;

import com.msr.documents.mongo.entity.MongoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ranjan on 15/10/20.
 */
@Repository
public interface MongoDocumentRepository extends MongoRepository<MongoDocument, String> {

}
