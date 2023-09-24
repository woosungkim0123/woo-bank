INSERT INTO user_entity (id, name, password, email, role, created_at, updated_at)
VALUES (1, 'test2', '$2a$10$2Nd9w5S0rBFnWtyQwSO/b.FozLo/jkQgv2851tCR5bq9hTf7nRSyy', 'test@test.com', 'CUSTOMER', '2023-09-18 23:58:33', '2023-09-18 23:58:33');

INSERT INTO account_entity (id, number, password, full_number, balance, user_id, created_at, updated_at, type)
VALUES (1, 12121, '$2a$10$2Nd9w5S0rBFnWtyQwSO/b.FozLo/jkQgv2851tCR5bq9hTf7nRSyy', 123444444, 10000, 1, '2023-09-18 23:58:33', '2023-09-18 23:58:33', 'SAVING');

INSERT INTO account_entity (id, number, password, full_number, balance, user_id, created_at, updated_at, type)
VALUES (2, 12121, '$2a$10$2Nd9w5S0rBFnWtyQwSO/b.FozLo/jkQgv2851tCR5bq9hTf7nRSyy', 123123124, 10000, 1, '2023-09-18 23:58:33', '2023-09-18 23:58:33', 'SAVING');