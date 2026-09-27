CREATE TABLE IF NOT EXISTS User_Password
(ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
USER_NAME VARCHAR2(30),
PASSWORD VARCHAR2(70),
ROLES VARCHAR2(20)); 

--drop table User_Merchant_Password
ALTER TABLE USER_PASSWORD 
ADD MERCHANT_ID NUMBER; 

INSERT INTO User_Password(USER_NAME, PASSWORD, ROLES, MERCHANT_ID)
VALUES('giri','$2a$10$T98adJy8syMRxYPyqvjnW.hzhXzBrQsTLZBh03SAvLnjP6yOQOtYe','merchant',1),
('hari','$2a$10$NNKmMiHglM8ae7IBCoQRt.XJY8ZtYzP9Y.b7kaDfhtHag8GgHSOOO','merchant',2),

('priya','$2a$10$Vd6fF3LTI9RKf4zbml6RSeha62vXa6JbWTTml7s9GBxl2G3DC3RKK','merchant',3),
('gupta','$2a$10$BM.Rhg8u/4N5IY0yI9W02OFule1yggaZR.UavygywZdaE5zYzxnzy','merchant',4),

('meha','$2a$10$f5eG58KSr9jfNNSCBg4JBu6a2PzS4uUfBeKPOwF8naqCfb6LRvC/O','merchant',5),
('gauri','$2a$10$TMdznOyDi/peYyoPMr91guWhIBr1MZUfrz5imALaFKdYxnPP0fNMy','merchant',6),

('kiran','$2a$10$IZSHkQFDAM7MnpQc4ZdDBuH9i0.z3L1aWC7G06XwYlPrt9L2WgLTK','merchant',7),
('fiyas','$2a$10$2lEShXxaNkBwwMKTXzf0BOyro1T4yl6zeG4R3Oph//5FmpsejCYGi','merchant',8),
('wilson','$2a$10$47KRRIIBG0wzFB5xER61huMhrw6HjhTcRZNr2fl.lye7RQwIBn8hm','merchant',9),
('zyan','$2a$10$EA0Z0Mw0AO7O/3xAUfQeWOh.xVUHjdU.JuTt1.Z4sxjXlwJ5OH7fq','merchant',10),

('admin1','$2a$10$dlxIcjUeBaLvW0enM.So/uZnFozUnwVPwIuGBB6asLNRpYlrsyZgG','admin',11),
('admin2','$2a$10$Wum4rBnzPBYUIN.oMT.trOdPpGuNFkogy70tX6SDzVW8gfZHFgLE.','admin',12);


commit;
SELECT * FROM User_Password
--DELETE FROM User_Merchant_Password WHERE ID=11;
