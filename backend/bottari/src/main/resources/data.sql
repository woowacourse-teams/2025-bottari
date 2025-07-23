-- H2 데이터베이스용 INSERT 문

-- 회원 데이터
INSERT INTO member (ssaid, name)
VALUES ('test_ssaid_1', '김철수'),
       ('test_ssaid_2', '이영희'),
       ('test_ssaid_3', '박민수'),
       ('test_ssaid_4', '정수진'),
       ('test_ssaid_5', '최동욱');

-- 보따리 데이터
INSERT INTO bottari (title, member_id)
VALUES ('제주도 여행', 1),
       ('출장용 가방', 1),
       ('캠핑 준비물', 2),
       ('해외여행 짐', 2),
       ('일상 외출용', 3),
       ('운동용품', 3),
       ('아이 외출준비', 4),
       ('주말 나들이', 4),
       ('등산용품', 5),
       ('출장 1박2일', 5);

-- 보따리 아이템 데이터
INSERT INTO bottari_item (name, bottari_id, is_checked)
VALUES
-- 제주도 여행 (bottari_id: 1, member_id: 1)
('여권', 1, false),
('선크림', 1, true),
('수영복', 1, false),
('여행용 가방', 1, true),
('카메라', 1, false),
('충전기', 1, true),
('선글라스', 1, false),

-- 출장용 가방 (bottari_id: 2, member_id: 1)
('노트북', 2, true),
('서류', 2, false),
('명함', 2, true),
('정장', 2, false),
('구두', 2, false),
('넥타이', 2, true),

-- 캠핑 준비물 (bottari_id: 3, member_id: 2)
('텐트', 3, false),
('침낭', 3, true),
('랜턴', 3, false),
('버너', 3, false),
('라면', 3, true),
('물', 3, true),
('모기약', 3, false),

-- 해외여행 짐 (bottari_id: 4, member_id: 2)
('여권', 4, true),
('항공권', 4, true),
('해외용 어댑터', 4, false),
('현금', 4, false),
('의약품', 4, true),
('여행가이드북', 4, false),

-- 일상 외출용 (bottari_id: 5, member_id: 3)
('지갑', 5, true),
('핸드폰', 5, true),
('키', 5, true),
('마스크', 5, false),
('손수건', 5, false),

-- 운동용품 (bottari_id: 6, member_id: 3)
('운동복', 6, false),
('운동화', 6, true),
('수건', 6, false),
('물통', 6, true),
('이어폰', 6, false),

-- 아이 외출준비 (bottari_id: 7, member_id: 4)
('기저귀', 7, true),
('젖병', 7, true),
('아이옷', 7, false),
('장난감', 7, false),
('간식', 7, true),
('물티슈', 7, true),

-- 주말 나들이 (bottari_id: 8, member_id: 4)
('피크닉 매트', 8, false),
('도시락', 8, false),
('음료수', 8, true),
('휴지', 8, true),
('카메라', 8, false),

-- 등산용품 (bottari_id: 9, member_id: 5)
('등산화', 9, true),
('등산복', 9, false),
('등산스틱', 9, false),
('물', 9, true),
('간식', 9, true),
('구급약', 9, false),

-- 출장 1박2일 (bottari_id: 10, member_id: 5)
('속옷', 10, false),
('세면용품', 10, true),
('파자마', 10, false),
('충전기', 10, true),
('노트북', 10, false);

-- 보따리 템플릿 데이터
INSERT INTO bottari_template (title, member_id, created_at)
VALUES ('기본 여행 템플릿', 1, '2024-01-15 10:00:00'),
       ('출장 기본 템플릿', 1, '2024-01-16 14:30:00'),
       ('캠핑 필수품', 2, '2024-01-17 09:15:00'),
       ('해외여행 체크리스트', 2, '2024-01-18 16:45:00'),
       ('아이 외출 필수품', 4, '2024-01-19 11:20:00'),
       ('운동 기본템', 3, '2024-01-20 08:00:00'),
       ('등산 준비물', 5, '2024-01-21 13:10:00');

-- 보따리 템플릿 아이템 데이터
INSERT INTO bottari_template_item (name, bottari_template_id)
VALUES
-- 기본 여행 템플릿 (template_id: 1, member_id: 1)
('여권', 1),
('충전기', 1),
('의약품', 1),
('여행용품', 1),
('카메라', 1),
('선크림', 1),

-- 출장 기본 템플릿 (template_id: 2, member_id: 1)
('노트북', 2),
('서류', 2),
('명함', 2),
('정장', 2),
('구두', 2),
('세면용품', 2),

-- 캠핑 필수품 (template_id: 3, member_id: 2)
('텐트', 3),
('침낭', 3),
('랜턴', 3),
('버너', 3),
('식기류', 3),
('모기약', 3),

-- 해외여행 체크리스트 (template_id: 4, member_id: 2)
('여권', 4),
('항공권', 4),
('해외용 어댑터', 4),
('현금', 4),
('여행자보험', 4),
('비상연락처', 4),

-- 아이 외출 필수품 (template_id: 5, member_id: 4)
('기저귀', 5),
('젖병', 5),
('아이옷', 5),
('간식', 5),
('물티슈', 5),
('장난감', 5),

-- 운동 기본템 (template_id: 6, member_id: 3)
('운동복', 6),
('운동화', 6),
('수건', 6),
('물통', 6),
('이어폰', 6),

-- 등산 준비물 (template_id: 7, member_id: 5)
('등산화', 7),
('등산복', 7),
('등산스틱', 7),
('물', 7),
('간식', 7),
('구급약', 7);

-- 알람 데이터 (루틴 알람과 위치 알람 조합)
INSERT INTO alarm (is_active, bottari_id, time, type, date, repeat_day_of_weeks_bitmask, is_location_alarm_active,
                   latitude, longitude, radius)
VALUES
-- 제주도 여행 알람 (bottari_id: 1, member_id: 1, 매일 오전 8시, 위치 알람 포함)
(true, 1, '08:00:00', 'EVERY_DAY_REPEAT', '2024-02-01', 127, true, 33.4996, 126.5312, 1000),
-- 출장용 가방 알람 (bottari_id: 2, member_id: 1, 평일 오전 7시)
(true, 2, '07:00:00', 'EVERY_WEEK_REPEAT', null, 31, false, null, null, 0),
-- 캠핑 준비물 알람 (bottari_id: 3, member_id: 2, 주말 오후 6시)
(true, 3, '18:00:00', 'EVERY_WEEK_REPEAT', null, 96, false, null, null, 0),
-- 해외여행 짐 알람 (bottari_id: 4, member_id: 2, 출발 당일 1회)
(false, 4, '06:00:00', 'NON_REPEAT', '2024-03-15', 0, false, null, null, 0),
-- 일상 외출용 알람 (bottari_id: 5, member_id: 3, 매일 오전 8시 30분, 집 근처 위치 알람)
(true, 5, '08:30:00', 'EVERY_DAY_REPEAT', null, 127, true, 37.5665, 126.9780, 500);
