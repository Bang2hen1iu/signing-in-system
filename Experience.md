#### 踩坑经验总结
* JPA的日期用java.sql.Date，其他时候处理日期时用joda的DateTime
* 编写线程代码时特别注意检查null，因为这时抛出的异常没显示出来，难以发现并debug