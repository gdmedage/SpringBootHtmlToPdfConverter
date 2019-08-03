package com.example.SpringBootHtmlToPdfConverter.utils;


import java.sql.Timestamp;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class Helper.
 */
public class Helper {

  /**
   * hidding object creation.
   */
  private Helper() {
    super();
  }

  /**
   * Gets the current timestamp in SQL format.
   *
   * @return java.sql.Timestamp
   * @since 1
   */
  public static Timestamp rightNow() {
    return new Timestamp(new java.util.Date().getTime());
  }

  /**
   * Convert json to entity.
   *
   * @param obj the obj
   * @param entity the entity
   * @return the object
   */
  public static Object convertJsonToEntity(Object obj, Object entity) {

    final ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(obj, entity.getClass());

  }

  /**
   * generate identifier.
   *
   * @return the string
   */
  public static String generateIdentifier() {
    String random = RandomStringUtils.randomAlphanumeric(4);
    return System.currentTimeMillis() + "-" + random;
  }

}
