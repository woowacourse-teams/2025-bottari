package com.bottari.support;

import com.vane.badwordfiltering.BadWordFiltering;

public final class BadWordValidator {

    private static final BadWordFiltering badWordFiltering = new BadWordFiltering();

    public static boolean hasBadWord(final String input) {
        final String lettersOnly = input.replaceAll("[^가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z]", "");

        return badWordFiltering.check(lettersOnly);
    }
}
