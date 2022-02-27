package com.msr.documents.common.model;

//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Information returned to the consumer of the document service. Using these details the consumer
 * can retrieve the same document
 *
 * Created by ranjan on 15/10/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentInfo {

  //@Schema(description = "The Id of the document, may not be the actual id used by the storage system", example = "asd121", required = true)
  private String id;

  //@Schema(description = "The actual name of the stored file", example = "my_passport.pdf")
  private String name;

  //@Schema(description = "The document type or any label used at the time of storing the document", example = "address proof")
  private String label;

  //@Schema(description = "The storage system used", example = "S3", required = true)
  private String storage;

  //@Schema(description = "The path where the document is stored in the actual storage system", example = "s3 url")
  private String storagePath;

}
