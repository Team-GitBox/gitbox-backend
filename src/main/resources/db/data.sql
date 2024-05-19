INSERT INTO gitbox.member (id, moddate, regdate, email, name, password, profile_image, role)
VALUES (1, '2024-05-19 15:35:37.461435', '2024-05-19 15:35:37.461435', 'test@naver.com', 'test',
        '$2a$10$9Hg6Nuw/TXe39c2WsgTi7OpFcstFKB7RoVqXlqnRXaMbDqBrqDAGa', null, 'USER');

INSERT INTO gitbox.workspace (id, max_storage, owner_id, used_storage, name)
VALUES (1, 1000000000, 1, 916052, 'workspace1');

INSERT INTO gitbox.workspace_member (id, member_id, workspace_id)
VALUES (1, 1, 1);

INSERT INTO gitbox.folder (id, parent_folder_id, workspace_id, name)
VALUES (1, 0, 1, 'home');

INSERT INTO gitbox.file (is_latest, folder_id, id, parent_file_id, root_file_id, size, version, workspace_id, writer_id,
                         name, url, status, type)
VALUES (true, 1, 1, null, 1, 916052, 1, 1, 1, '9주차_AIoT네트워크.pdf',
        'https://gitbox-file-bucket.s3.ap-northeast-2.amazonaws.com/9%E1%84%8C%E1%85%AE%E1%84%8E%E1%85%A1_AIoT%E1%84%82%E1%85%A6%E1%84%90%E1%85%B3%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3.pdf',
        'APPROVED', 'PDF');

