package me.kanedenooijer.lttrs.utils;

import me.kanedenooijer.lttrs.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilTest extends BaseTest {

    @Test
    void givenPascalCase_whenConvertingToSnakeCase_thenReturnsSnakeCase() {
        assertEquals("this_is_a_test", Util.toSnakeCase("ThisIsATest"));
    }

    @Test
    void givenCamelCase_whenConvertingToSnakeCase_thenReturnsSnakeCase() {
        assertEquals("this_is_a_test", Util.toSnakeCase("thisIsATest"));
    }

    @Test
    void givenSnakeCase_whenConvertingToSnakeCase_thenReturnsSameString() {
        assertEquals("this_is_a_test", Util.toSnakeCase("this_is_a_test"));
    }

    @Test
    void givenEmptyString_whenConvertingToSnakeCase_thenReturnsEmptyString() {
        assertEquals("", Util.toSnakeCase(""));
    }

}
