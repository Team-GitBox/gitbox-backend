INSERT INTO gitbox.member (created_at, updated_at, email, name, password, profile_image, role)
VALUES ('2024-05-31 14:53:10.064056', '2024-05-31 14:53:10.064056', 'string@naver.com', 'string',
        '$2a$10$Fqu6VJK47VbBhrtatHHg8OImUhm.A1il23PwejpU4IS9LM/ADgncO', null, 'USER');
INSERT INTO gitbox.member (created_at, updated_at, email, name, password, profile_image, role)
VALUES ('2024-05-31 14:54:30.576054', '2024-05-31 14:54:30.576054', 'string1@naver.com', 'string1',
        '$2a$10$G/hAil1Elo4dq86y6Eklvus//w/0VLKoD0CGeEdkbswq3RPxNoQSq', null, 'USER');

INSERT INTO gitbox.workspace (created_at, max_storage, owner_id, root_folder_id, updated_at, used_storage, name)
VALUES ('2024-05-31 14:54:47.664823', 1000000000, 1, 1, '2024-05-31 14:59:49.870526', 5910742, 'workspace1');
INSERT INTO gitbox.workspace (created_at, max_storage, owner_id, root_folder_id, updated_at, used_storage, name)
VALUES ('2024-05-31 14:54:53.038571', 1000000000, 1, 2, '2024-05-31 14:54:53.109172', 0, 'workspace2');

INSERT INTO gitbox.workspace_member (member_id, workspace_id)
VALUES (2, 1);
INSERT INTO gitbox.workspace_member (member_id, workspace_id)
VALUES (1, 1);
INSERT INTO gitbox.workspace_member (member_id, workspace_id)
VALUES (2, 2);
INSERT INTO gitbox.workspace_member (member_id, workspace_id)
VALUES (1, 2);

INSERT INTO gitbox.folder (created_at, parent_folder_id, updated_at, workspace_id, name)
VALUES ('2024-05-31 14:54:47.738147', null, '2024-05-31 14:54:47.738147', 1, 'home');
INSERT INTO gitbox.folder (created_at, parent_folder_id, updated_at, workspace_id, name)
VALUES ('2024-05-31 14:54:53.097527', null, '2024-05-31 14:54:53.097527', 2, 'home');
INSERT INTO gitbox.folder (created_at, parent_folder_id, updated_at, workspace_id, name)
VALUES ('2024-05-31 14:56:21.741918', 1, '2024-05-31 14:56:21.741918', 1, 'second folder');

INSERT INTO gitbox.file (is_deleted, is_latest, created_at, folder_id, parent_file_id, pull_request_id, root_file_id,
                         size, updated_at, version, workspace_id, writer_id, name, url, status, tag, type)
VALUES (false, true, '2024-05-31 14:58:06.197759', 1, null, null, 1, 2955371, '2024-05-31 14:58:06.221400', 1, 1, 1,
        'temp-image.jpeg', 'https://gitbox-file-bucket.s3.ap-northeast-2.amazonaws.com/temp-image.jpeg_30bc7',
        'APPROVED', null, 'JPEG');
-- INSERT INTO gitbox.file (is_deleted, is_latest, created_at, folder_id, parent_file_id, pull_request_id, root_file_id,
--                          size, updated_at, version, workspace_id, writer_id, name, url, status, tag, type)
-- VALUES (false, false, '2024-05-31 14:59:49.801787', 1, 1, 1, 1, 2955371, '2024-05-31 15:01:12.329042', 2, 1, 1,
--         '새로운 버전 파일', 'https://gitbox-file-bucket.s3.ap-northeast-2.amazonaws.com/temp-image-2.jpeg_c1cbc', 'PENDING',
--         'RED', 'JPEG');
--
-- INSERT INTO gitbox.pull_request (created_at, file_id, parent_file_id, updated_at, writer_id, message, title)
-- VALUES ('2024-05-31 14:59:49.821650', 2, null, '2024-05-31 14:59:49.821650', 1, '확인부탁드려요.', '새로운 파일 버전입니다.');

INSERT INTO gitbox.action_history (created_at, file_id, member_id, updated_at, workspace_id, file_name, member_name,
                                   action)
VALUES ('2024-05-31 14:58:06.210129', 1, 1, '2024-05-31 14:58:06.210129', 1, 'temp-image.jpeg', 'string', 'UPLOAD');
-- INSERT INTO gitbox.action_history (created_at, file_id, member_id, updated_at, workspace_id, file_name, member_name,
--                                    action)
-- VALUES ('2024-05-31 14:59:49.856268', 2, 1, '2024-05-31 14:59:49.856268', 1, 'temp-image-2.jpeg', 'string',
--         'PULL_REQUEST');
