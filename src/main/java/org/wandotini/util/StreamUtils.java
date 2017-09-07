package org.wandotini.util;

import java.util.function.BinaryOperator;

public class StreamUtils {
    public static BinaryOperator<Integer> add() {
        return (pre, cur) -> pre + cur;
    }
}
