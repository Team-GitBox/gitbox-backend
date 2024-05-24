INSERT INTO gitbox.member (created_at, id, updated_at, email, name, password, profile_image, role)
VALUES ('2024-05-20 09:27:28.554885', 1, '2024-05-20 09:27:28.554885', 'string@naver.com', 'string',
        '$2a$10$HRBOgQzOVHK.X/BPOj9lDuEowNiPFbrZbqYponQTEP70/5f3YiC.6', null, 'USER');
INSERT INTO gitbox.member (created_at, id, updated_at, email, name, password, profile_image, role)
VALUES ('2024-05-20 09:28:34.696628', 2, '2024-05-20 09:28:34.696628', 'string1@naver.com', 'string1',
        '$2a$10$xKErMpZhE/9dpXDkGn2Q.OYjpWKJHrDleia.CzPl4ChnigVSS0JOy', null, 'USER');

INSERT INTO gitbox.workspace (created_at, id, max_storage, owner_id, root_folder_id, updated_at, used_storage, name)
VALUES ('2024-05-20 09:28:06.306780', 1, 1000000000, 1, 1, '2024-05-20 09:46:39.482480', 12252, 'workspace1');
INSERT INTO gitbox.workspace_member (id, member_id, workspace_id)
VALUES (1, 1, 1);
INSERT INTO gitbox.workspace_member (id, member_id, workspace_id)
VALUES (3, 2, 1);

INSERT INTO gitbox.folder (created_at, id, parent_folder_id, updated_at, workspace_id, name)
VALUES ('2024-05-20 09:28:06.323471', 1, null, '2024-05-20 09:28:06.323471', 1, 'home');
INSERT INTO gitbox.folder (created_at, id, parent_folder_id, updated_at, workspace_id, name)
VALUES ('2024-05-20 09:31:37.473576', 2, 1, '2024-05-20 09:34:10.245099', 1, 'picture');

INSERT INTO gitbox.file (is_latest, created_at, folder_id, id, parent_file_id, root_file_id, size, updated_at, version,
                         workspace_id, writer_id, name, url, status, type, tag, is_deleted)
VALUES (true, '2024-05-20 09:35:28.381074', 1, 1, null, 1, 4084, '2024-05-20 09:35:28.395005', 1, 1, 1,
        'test-image.png', 'https://gitbox-file-bucket.s3.ap-northeast-2.amazonaws.com/test-image.png', 'APPROVED',
        'PNG', 'RED', false);
INSERT INTO gitbox.file (is_latest, created_at, folder_id, id, parent_file_id, root_file_id, size, updated_at, version,
                         workspace_id, writer_id, name, url, status, type, tag, is_deleted)
VALUES (false, '2024-05-20 09:46:39.424304', 1, 3, 1, 1, 4084, '2024-05-20 09:46:39.424304', 2, 1, 1,
        'test-image 2.png', 'https://gitbox-file-bucket.s3.ap-northeast-2.amazonaws.com/test-image%202.png', 'PENDING',
        'PNG', 'BLUE', false);

INSERT INTO gitbox.pull_request (created_at, file_id, id, updated_at, writer_id, message, title)
VALUES ('2024-05-20 09:46:39.435614', 3, 1, '2024-05-20 09:46:39.435614', 1, 'PR 코멘트', '새로운 PR');

