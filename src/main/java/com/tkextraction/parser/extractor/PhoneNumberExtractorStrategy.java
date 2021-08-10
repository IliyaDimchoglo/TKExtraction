package com.tkextraction.parser.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.tkextraction.parser.api.ExtractorStrategy;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;


@Component
public class PhoneNumberExtractorStrategy implements ExtractorStrategy {
  @Override
  public Optional<JsonNode> extract(PDDocument document) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      ObjectNode result = objectMapper.createObjectNode();

      PDFTextStripper textStripper = new PDFTextStripper();
      textStripper.setEndPage(1);
      String firstPageText = textStripper.getText(document);
      Optional<String> phoneNumberOpt = extractPhoneNumberFromText(firstPageText);
      if (phoneNumberOpt.isPresent()) {
        result.put("phone", phoneNumberOpt.get());
        return Optional.of(result);
      } else {
        return Optional.empty();
      }
    } catch (IOException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }


  private static Optional<String> findFirstPhoneNumber(String content) {
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    String[] lines = content.split("\\r?\\n"); //search in every line separately
    for (String line : lines) {
      Iterable<PhoneNumberMatch> numbers = phoneNumberUtil.findNumbers(line, null);
      if (numbers.iterator().hasNext()) {
        PhoneNumberMatch phoneNumberMatch = numbers.iterator().next();
        String phoneNumber = phoneNumberMatch.rawString();
        String cleanPhoneNumber = cleanup(phoneNumber);
        return Optional.of(cleanPhoneNumber);
      }
    }

    return Optional.empty();
  }

  /**
   * Remove everything except numbers and the plus character (if present)
   *
   * @param phoneNumber input
   * @return cleaned up version
   */
  private static String cleanup(String phoneNumber) {
    if (phoneNumber == null) return "";
    return phoneNumber.replaceAll("[^+0-9]", "");
  }

  private Optional<String> extractPhoneNumberFromText(String rawText) {
    return findFirstPhoneNumber(rawText);
  }

  @Override
  public String getKeyName() {
    return "phone_number";
  }
}
