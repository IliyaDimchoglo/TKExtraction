package com.tkextraction.parser.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.control.Try;

public interface ResumeParserEngine {
  /**
   * Will extract some basic properties from the pdf file. For example, name, email, phone number etc.
   *
   * @param fileContent The pdf file's content as a byte array
   * @return A Json object containing the extracted information as a key/value pair. The whole json is wrapped in a Try.
   */
  Try<JsonNode> parseFile(byte[] fileContent);

  /**
   * Will return the first image that is more than 128px in height from the first page of the pdf file.
   *
   * @param fileContent The pdf file's content as a byte array
   * @return A byte array containing the image, wrapped in a Try object.
   */
  Try<byte[]> extractImage(byte[] fileContent);
}
