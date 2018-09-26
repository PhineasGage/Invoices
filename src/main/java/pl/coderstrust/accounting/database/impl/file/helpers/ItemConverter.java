package pl.coderstrust.accounting.database.impl.file.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public class ItemConverter<T extends ItemsForDatabase> {

  private ObjectMapper mapper;
  private Class<T> classObject;

  @Autowired
  public ItemConverter(ObjectMapper mapper, Class<T> classObject) {
    this.mapper = mapper;
    this.classObject = classObject;
  }

  public String convertItemToJson(T item) {
    try {
      return mapper.writeValueAsString(item);
    } catch (JsonProcessingException exception) {
      throw new RuntimeException("cannot convert to Json", exception);
    }
  }

  public T convertJsonToItem(String json) {
    try {
      return mapper.readValue(json, classObject);
    } catch (IOException exception) {
      throw new RuntimeException("cannot convert from Json", exception);
    }
  }

  public List<String> convertListOfItemsToListOfStrings(List<T> items) {
    return items.stream()
        .map(this::convertItemToJson)
        .collect(Collectors.toList());
  }

  public List<T> convertListOfStringsToListOfItems(List<String> items) {
    return items.stream()
        .map(this::convertJsonToItem)
        .collect(Collectors.toList());
  }
}