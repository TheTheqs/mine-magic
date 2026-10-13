package com.matheqs.finalmagic.blockentity;

import java.util.function.IntUnaryOperator;

public final class InscriptionPatterns {
    private InscriptionPatterns() {}

    public static boolean matchesFire(IntUnaryOperator symbolAt) {
        for (int cell = 0; cell < 9; cell++) {
            int expected = (cell == 4 || cell == 6 || cell == 8) ? 1 : 0;
            if (symbolAt.applyAsInt(cell) != expected) {
                return false;
            }
        }
        return true;
    }
}