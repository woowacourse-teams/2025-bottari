package com.bottari.support;

import com.vane.badwordfiltering.BadWordFiltering;

public final class BadWordValidator {

    private static final BadWordFiltering badWordFiltering = new BadWordFiltering();
    private static final String KOREAN_AND_ENGLISH_ONLY_REGEX = "[^가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z]";

    public static boolean hasBadWord(final String input) {
        final String lettersOnly = input.replaceAll(KOREAN_AND_ENGLISH_ONLY_REGEX, "");

        return badWordFiltering.check(lettersOnly);
    }
}
