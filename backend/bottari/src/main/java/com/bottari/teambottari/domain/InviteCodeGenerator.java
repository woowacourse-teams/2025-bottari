package com.bottari.teambottari.domain;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 팀 보따리 초대를 위한 8자리 초대코드 생성기입니다.
 *
 * <p> 이 클래스는 8자리의 랜덤한 초대 코드를 생성하는 기능을 제공합니다.
 * 초대 코드는 대문자 알파벳과 숫자로 구성되며, 보안성을 위해 SecureRandom을 사용합니다.
 * <p> 생성된 초대 코드는 중복되지 않도록 설계되어야 하며,
 * 실제 사용 시에는 데이터베이스나 다른 저장소에 저장하여 중복을 방지해야 합니다.
 */
public class InviteCodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHJKLMNOPQRSTUVWXYZ123456789";
    private static final int CODE_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    private InviteCodeGenerator() {
    }

    public static String generate() {
        return IntStream.range(0, CODE_LENGTH)
                .map(i -> random.nextInt(CHARACTERS.length()))
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
