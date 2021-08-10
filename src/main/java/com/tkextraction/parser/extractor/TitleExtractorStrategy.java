package com.tkextraction.parser.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tkextraction.parser.api.ExtractorStrategy;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class TitleExtractorStrategy implements ExtractorStrategy {
  @Override
  public Optional<JsonNode> extract(PDDocument document) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      ObjectNode result = objectMapper.createObjectNode();

      String probablyName = "";
      probablyName = firstNonEmptyLine(document, probablyName);

      validProbablyName(probablyName)
          .ifPresent(n -> result.put("probableName", n));

      if (!result.has("probableName")) {
        probablyNameFromFormatting(document)
            .ifPresent(n -> result.put("probableName", n));
      }

      result.put("firstLine", probablyName);
      return Optional.of(result);
    } catch (IOException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  private Optional<String> validProbablyName(final String probablyName) {
    if (StringUtils.isNotBlank(probablyName)) {
      String[] nameTokens = StringUtils.split(probablyName);
      for (String nameToken : nameTokens) {
        if (!Character.isUpperCase(nameToken.charAt(0))) {
          return Optional.empty();
        }
      }
      return Optional.of(probablyName);
    }
    return Optional.empty();
  }

  private Optional<String> probablyNameFromFormatting(PDDocument pdfDoc) throws IOException {
    ArrayList<Tuple2<String, Float>> textWithFontWeight = new ArrayList<>();


    PDFTextStripper textStripper = new PDFTextStripper() {
      @Override
      protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        super.writeString(text, textPositions);

        float fontSize = 0.0f;
        for (TextPosition textPosition : textPositions) {
          fontSize = textPosition.getFontSizeInPt();
        }
        textWithFontWeight.add(Tuple.of(text, fontSize));
      }
    };

    textStripper.setEndPage(1);
    textStripper.getText(pdfDoc);

    textWithFontWeight.sort(Comparator.comparing(Tuple2::_2, Comparator.reverseOrder()));

    if (textWithFontWeight.isEmpty()) {
      return Optional.empty();
    } else {
      Tuple2<String, Float> highestFontText = textWithFontWeight.get(0);
      String line = highestFontText._1;
      String resultString = cleanup(line);
      return Optional.of(resultString);
    }
  }

  private String cleanup(String line) {
    if (line == null) return "";

    String result = line.trim();
    String[] words = result.split("\\s");
    StringBuilder name = new StringBuilder();
    for (String word : words) {
      String wordCap = StringUtils.capitalize(word.toLowerCase());
      name.append(wordCap).append(" ");
    }
    return name.toString().trim();
  }

  private String firstNonEmptyLine(PDDocument document, String firstNonEmptyLine) throws IOException {
    PDFTextStripper textStripper = new PDFTextStripper();
    textStripper.setEndPage(1);
    String firstPageText = textStripper.getText(document);

    String[] lines = firstPageText.split("\\r?\\n"); //split string in multiple lines
    for (String line : lines) {
      if (!Objects.isNull(line)) {
        firstNonEmptyLine = line.trim();
        break;
      }
    }
    return firstNonEmptyLine;
  }

  @Override
  public String getKeyName() {
    return "title";
  }
}
