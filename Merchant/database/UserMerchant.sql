CREATE TABLE IF NOT EXISTS User_Password
(ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
USER_NAME VARCHAR2(30),
PASSWORD VARCHAR2(70),
ROLES VARCHAR2(20)); 

--drop table User_Merchant_Password

INSERT INTO User_Password(USER_NAME, PASSWORD, ROLES)
VALUES('giri','$2a$10$T98adJy8syMRxYPyqvjnW.hzhXzBrQsTLZBh03SAvLnjP6yOQOtYe','merchant'),
('hari','$2a$10$NNKmMiHglM8ae7IBCoQRt.XJY8ZtYzP9Y.b7kaDfhtHag8GgHSOOO','merchant'),

('priya','$2a$10$Vd6fF3LTI9RKf4zbml6RSeha62vXa6JbWTTml7s9GBxl2G3DC3RKK','merchant'),
('gupta','$2a$10$BM.Rhg8u/4N5IY0yI9W02OFule1yggaZR.UavygywZdaE5zYzxnzy','merchant'),

('meha','$2a$10$f5eG58KSr9jfNNSCBg4JBu6a2PzS4uUfBeKPOwF8naqCfb6LRvC/O','merchant'),
('gauri','$2a$10$TMdznOyDi/peYyoPMr91guWhIBr1MZUfrz5imALaFKdYxnPP0fNMy','merchant'),

('kiran','$2a$10$IZSHkQFDAM7MnpQc4ZdDBuH9i0.z3L1aWC7G06XwYlPrt9L2WgLTK','merchant'),
('fiyas','$2a$10$2lEShXxaNkBwwMKTXzf0BOyro1T4yl6zeG4R3Oph//5FmpsejCYGi','merchant'),
('wilson','$2a$10$47KRRIIBG0wzFB5xER61huMhrw6HjhTcRZNr2fl.lye7RQwIBn8hm','merchant'),
('zyan','$2a$10$EA0Z0Mw0AO7O/3xAUfQeWOh.xVUHjdU.JuTt1.Z4sxjXlwJ5OH7fq','merchant'),

('admin1','$2a$10$dlxIcjUeBaLvW0enM.So/uZnFozUnwVPwIuGBB6asLNRpYlrsyZgG','admin'),
('admin2','$2a$10$Wum4rBnzPBYUIN.oMT.trOdPpGuNFkogy70tX6SDzVW8gfZHFgLE.','admin');


commit;
SELECT * FROM User_Password
--DELETE FROM User_Merchant_Password WHERE ID=11;