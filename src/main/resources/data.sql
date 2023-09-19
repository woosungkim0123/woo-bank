INSERT INTO user_entity (id, name, password, email, role, created_at, updated_at)
VALUES (1, 'test2', '21312dsaas', 'test@test.com', 'CUSTOMER', '2023-09-18 23:58:33', '2023-09-18 23:58:33');

INSERT INTO account_entity (id, number, password, fullnumber, balance, user_id, created_at, updated_at, type)
VALUES (1, 12121, '21312dsaas', 123123123, 10000, 1, '2023-09-18 23:58:33', '2023-09-18 23:58:33', 'SAVING');

INSERT INTO account_entity (id, number, password, fullnumber, balance, user_id, created_at, updated_at, type)
VALUES (2, 12121, '21312dsaasd', 123123124, 10000, 1, '2023-09-18 23:58:33', '2023-09-18 23:58:33', 'SAVING');