package com.tkextraction.parser.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.Optional;

public interface ExtractorStrategy {

  Optional<JsonNode> extract(PDDocument document);

  String getKeyName();
}
