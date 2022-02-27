package com.msr.documents.repository;

import com.msr.documents.entity.FileMetaData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ranjan on 15/10/20.
 */
@Repository
public interface FileMetaDataRepository extends MongoRepository<FileMetaData, String> {

}
