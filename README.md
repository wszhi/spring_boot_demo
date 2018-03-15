database:mysql

1 install mysql
2 create database `spring_boot_test`
3 create use exercise user and grant permission.
```
mysql -u mysql_user -p

create database spring_boot_test;
show databases;
create user exercise;
grant all on spring_boot_test.* to 'exercise'@'localhost' identified by '12345678’;
```
4 start app 
`mvn spring-boot:run`