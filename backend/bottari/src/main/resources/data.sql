-- 회원 데이터
INSERT INTO member (ssaid, name)
VALUES ('test_ssaid_1', '다이스'),
       ('test_ssaid_2', '방벨로'),
       ('test_ssaid_3', '이오이'),
       ('test_ssaid_4', '이시아'),
       ('test_ssaid_5', '장코기'),
       ('test_ssaid_6', '민호떡'),
       ('test_ssaid_7', '김훌라');

-- 보따리 데이터
INSERT INTO bottari (title, member_id, created_at)
VALUES
    ('제주도 여행', 1, '2024-01-10 10:00:00'),
    ('출장용 가방', 1, '2024-07-12 10:05:00'),
    ('캠핑 준비물', 2, '2024-08-14 10:10:00'),
    ('해외여행 짐', 2, '2025-01-25 10:15:00'),
    ('일상 외출용', 3, '2025-02-26 10:20:00'),
    ('운동용품', 3, '2025-03-25 10:25:00'),
    ('아이 외출준비', 4, '2025-03-28 10:30:00'),
    ('주말 나들이', 4, '2025-05-25 10:35:00'),
    ('등산용품', 5, '2025-06-25 10:40:00'),
    ('출장 1박2일', 5, '2025-07-25 10:45:00');

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
INSERT INTO bottari_template (title, member_id, created_at, taken_count)
VALUES ('기본 여행 템플릿', 1, '2024-01-15 10:00:00',5),
       ('출장 기본 템플릿', 1, '2024-01-16 14:30:00',2),
       ('캠핑 필수품', 2, '2024-01-17 09:15:00',3),
       ('해외여행 체크리스트', 2, '2024-01-18 16:45:00',5),
       ('아이 외출 필수품', 4, '2024-01-19 11:20:00',0),
       ('운동 기본템', 3, '2024-01-20 08:00:00',0),
       ('등산 준비물', 5, '2024-01-21 13:10:00',0);

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
    (true, 1, '08:00:00', 'EVERY_DAY_REPEAT', null, 127, true, 33.4996, 126.5312, 100),
    -- 출장용 가방 알람 (bottari_id: 2, member_id: 1, 평일 오전 7시)
    (true, 2, '07:00:00', 'EVERY_WEEK_REPEAT', null, 31, null, null, null, null),
    -- 캠핑 준비물 알람 (bottari_id: 3, member_id: 2, 주말 오후 6시)
    (true, 3, '18:00:00', 'EVERY_WEEK_REPEAT', null, 96, null, null, null, null),
    -- 해외여행 짐 알람 (bottari_id: 4, member_id: 2, 출발 당일 1회)
    (false, 4, '06:00:00', 'NON_REPEAT', '2024-03-15', 0, null, null, null, null),
    -- 일상 외출용 알람 (bottari_id: 5, member_id: 3, 매일 오전 8시 30분, 집 근처 위치 알람)
    (true, 5, '08:30:00', 'EVERY_DAY_REPEAT', null, 127, true, 37.5665, 126.9780, 100);

INSERT INTO bottari_template_history (receiver_id, template_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (2, 1),
    (2, 4),
    (4, 1),
    (4, 4),
    (6,1),
    (6,2),
    (6,3),
    (6,4),
    (7,1),
    (7,3),
    (7,4);

-- =======================================================================
-- 팀 보따리 관련 목업 데이터 (수정됨)
-- =======================================================================

-- 팀 보따리 데이터
INSERT INTO team_bottari (title, owner_id, invite_code, created_at)
VALUES
    ('다이스의 첫 여행 준비', 1, 'team-1-invite-code', '2024-01-10 10:00:00'),
    ('방벨로의 출장 준비', 2, 'team-2-invite-code', '2024-07-12 10:05:00');

-- 팀 멤버 데이터
INSERT INTO team_member (team_bottari_id, member_id)
VALUES
    -- '다이스의 첫 여행 준비' (team_bottari_id: 1) -> team_member_id: 1, 2, 3
    (1, 1), -- owner
    (1, 2),
    (1, 3),
    -- '방벨로의 출장 준비' (team_bottari_id: 2) -> team_member_id: 4, 5, 6
    (2, 2), -- owner
    (2, 1),
    (2, 5);

-- 팀 공유 아이템 정보 (team_shared_item_info)
INSERT INTO team_shared_item_info (team_bottari_id, name)
VALUES
    (1, '공용 간식'),
    (1, '상비약'),
    (2, '공용 노트북 충전기');

-- 팀 공유 아이템 (team_shared_item)
INSERT INTO team_shared_item (team_shared_item_info_id, team_member_id, is_checked)
VALUES
    (1, 1, false),
    (1, 2, true),
    (1, 3, true),
    (2, 1, true),
    (2, 2, true),
    (2, 3, false),
    (3, 1, true),
    (3, 2, false),
    (3, 5, true);

-- 팀 개인 아이템 (team_personal_item)
-- team_member_id 1: '다이스의 첫 여행 준비' 팀의 '다이스'
-- team_member_id 5: '방벨로의 출장 준비' 팀의 '다이스'
INSERT INTO team_personal_item (team_member_id, name, is_checked)
VALUES
    (1, '개인 여권', true),
    (1, '선글라스', false),
    (5, '개인 명함', true);

-- 팀 할당 아이템 정보 (team_assigned_item_info)
INSERT INTO team_assigned_item_info (team_bottari_id, name)
VALUES
    (1, '항공권'),
    (1, '크레파스'),
    (2, '물파스');

-- 팀 할당 아이템 (team_assigned_item)
-- team_member_id 1: '다이스의 첫 여행 준비' 팀의 '다이스'
-- team_member_id 2: '다이스의 첫 여행 준비' 팀의 '방벨로'
-- team_member_id 6: '방벨로의 출장 준비' 팀의 '장코기'
INSERT INTO team_assigned_item (team_assigned_item_info_id, team_member_id, is_checked)
VALUES
    (1, 1, true),
    (2, 2, false),
    (3, 6, true);
