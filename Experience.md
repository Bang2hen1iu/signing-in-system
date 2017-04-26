#### 踩坑经验总结
* JPA的日期用java.sql.Date，其他时候处理日期时用joda的DateTime
* 编写线程代码时特别注意检查null，因为这时抛出的异常没显示出来，难以发现并debug
* 依赖注入时注意参数不能是private内部类的对象，否则会注入失败
* 日期直接发Date、Timestamp那些到前端是没问题的，前端直接发日期到后端，后端可以自动完成解析转换
* 使用Value注入时，注意类要加上component注解
* JPA多次save同一类对象，如果id是generated value，注意对象不要重复拿来save

#### 分层标准
* API（controller）层接受请求，调用service得到结果，返回。API层不应包含具体实现。

* Service层封装了具体实现的方法，并注重代码的复用，尽量降低代码冗余度。重复使用的代码要抽出来成为一个方法。复杂的代码注意要拆分成几个单一的任务代码。

* 一个API可以调用一个Service，而Service内部可以调用其他Service。

* 按照功能模块来划分API和Service。


#### Note
* [JS代码混淆](http://www.javascriptobfuscator.com/)
