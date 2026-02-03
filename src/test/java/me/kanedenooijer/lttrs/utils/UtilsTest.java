package me.kanedenooijer.lttrs.utils;

import me.kanedenooijer.lttrs.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest extends BaseTest {

    @Test
    void givenPascalCase_whenConvertingToSnakeCase_thenReturnsSnakeCase() {
        assertEquals("this_is_a_test", Utils.toSnakeCase("ThisIsATest"));
    }

    @Test
    void givenCamelCase_whenConvertingToSnakeCase_thenReturnsSnakeCase() {
        assertEquals("this_is_a_test", Utils.toSnakeCase("thisIsATest"));
    }

    @Test
    void givenSnakeCase_whenConvertingToSnakeCase_thenReturnsSameString() {
        assertEquals("this_is_a_test", Utils.toSnakeCase("this_is_a_test"));
    }

    @Test
    void givenEmptyString_whenConvertingToSnakeCase_thenReturnsEmptyString() {
        assertEquals("", Utils.toSnakeCase(""));
    }

}
