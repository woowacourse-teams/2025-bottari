-- 복제용 계정 생성 및 권한 부여
CREATE USER 'repl'@'%' IDENTIFIED WITH mysql_native_password BY 'repl1234';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
FLUSH PRIVILEGES;
