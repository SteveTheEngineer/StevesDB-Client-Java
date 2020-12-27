package me.ste.stevesdbclient.entry.filter;

public enum ComparatorOperation {
    /**
     * a.equals(b)
     */
    EQUAL_TO,
    /**
     * !a.equals(b)
     */
    NOT_EQUAL_TO,
    /**
     * a.equalsIgnoreCase(b)
     */
    EQUAL_TO_IGNORE_CASE,
    /**
     * !a.equalsIgnoreCase(b)
     */
    NOT_EQUAL_TO_IGNORE_CASE,
    /**
     * a > b
     */
    GREATER_THAN,
    /**
     * a < b
     */
    LESS_THAN,
    /**
     * a >= b
     */
    GREATER_THAN_OR_EQUAL_TO,
    /**
     * a <= b
     */
    LESS_THAN_OR_EQUAL_TO,
    /**
     * a.startsWith(b)
     */
    STARTS_WITH,
    /**
     * a.endsWith(b)
     */
    ENDS_WITH,
    /**
     * a.toLowerCase().startsWith(b.toLowerCase())
     */
    STARTS_WITH_IGNORE_CASE,
    /**
     * a.toLowerCase().endsWith(b.toLowerCase())
     */
    ENDS_WITH_IGNORE_CASE,
    /**
     * !a.startsWith(b)
     */
    DOES_NOT_START_WITH,
    /**
     * !a.endsWith(b)
     */
    DOES_NOT_END_WITH,
    /**
     * !a.toLowerCase().startsWith(b.toLowerCase())
     */
    DOES_NOT_START_WITH_IGNORE_CASE,
    /**
     * !a.toLowerCase().endsWith(b.toLowerCase())
     */
    DOES_NOT_END_WITH_IGNORE_CASE,
    /**
     * a.contains(b)
     */
    CONTAINS,
    /**
     * !a.contains(b)
     */
    DOES_NOT_CONTAIN,
    /**
     * a.toLowerCase().contains(b.toLowerCase())
     */
    CONTAINS_IGNORE_CASE,
    /**
     * !a.toLowerCase().contains(b.toLowerCase())
     */
    DOES_NOT_CONTAIN_IGNORE_CASE,
    /**
     * Pattern.compile(b).matcher(a).matches()
     */
    MATCHES_REGEXP,
    /**
     * !Pattern.compile(b).matcher(a).matches()
     */
    DOES_NOT_MATCH_REGEXP,
    /**
     * a.length() == b
     */
    LENGTH_EQUAL_TO,
    /**
     * a.length() != b
     */
    LENGTH_NOT_EQUAL_TO,
    /**
     * a.length() > b
     */
    LENGTH_GREATER_THAN,
    /**
     * a.length() < b
     */
    LENGTH_LESS_THAN,
    /**
     * a.length() >= b
     */
    LENGTH_GREATER_THAN_OR_EQUAL_NO,
    /**
     * a.length() <= b
     */
    LENGTH_LESS_THAN_OR_EQUAL_TO
}