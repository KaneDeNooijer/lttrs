package me.kanedenooijer.lttrs.util;

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

    @Test
    void givenSnakeCase_whenConvertingToCamelCase_thenReturnsCamelCase() {
        assertEquals("thisIsATest", Util.toCamelCase("this_is_a_test"));
    }

    @Test
    void givenCamelCase_whenConvertingToCamelCase_thenReturnsSameString() {
        assertEquals("thisIsATest", Util.toCamelCase("thisIsATest"));
    }

    @Test
    void givenEmptyString_whenConvertingToCamelCase_thenReturnsEmptyString() {
        assertEquals("", Util.toCamelCase(""));
    }

}
