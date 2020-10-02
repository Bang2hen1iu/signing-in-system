## 实验室签到系统

#### 1. 用途
实验室成员到达、离开实验室时需要使用本系统进行签到。通过本系统，可以方便快捷了解到团队成员在实验室的情况，方便了实验室的团队管理。

#### 2. 技术框架
* 前端：bootstrap, jquery, angularjs
* 后端：spring boot 1.4.1
* 数据库：postgresql

#### 3. 部署细节
* 环境配置 ubuntu 16.04
    ##### Java 1.8
    1. 删除本机openJDK `sudo apt-get remove openjdk*`
    2. 下载并解压JDK8 `tar -xzvf jdk-8u261-linux-x64.tar.gz`
    3. 添加环境变量到/etc/profile中  
    `export JAVA_HOME=path2java/jdk1,8,0_261`  
    `export JRE_HOME=${JAVA_HOME}/jre`  
    `export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib`  
    `export PATH=${JAVA_HOME}/bin:$PATH`  
    4. 使修改生效  
    `source /etc/profile`
    5. 查看java版本 `java -version`
    ##### maven 3.6.3
    1. 下载并解压maven 3.6.3安装包
    2. 添加环境变量到/etc/profile中  
    `export M2_HOME=/opt/apache-maven-3.6.3`  
    `export CLASSPATH=$CLASSPATH:$M2_HOME/lib`  
    `export PATH=$PATH:$M2_HOME/bin`
    3. 查看maven版本 `mvn -v`
    ##### postgresql 9.5
    1. 查看ubuntu提供的psql版本 `sudo apt-show postgresql`
    2. 下载 `sudo apt install postgresql postgresql-contrib`
    3. 进入自动生成的用户postgres `sudo su postgres`
    4. 进入数据库并修改初始密码  
    `psql`  
    =# `alter user postgres with password '159951159'`   
    =# `create database signing_sys owner postgres`  
    `psql -u postgres -d signing_sys`  
    (数据库名和初始密码要与pom.xml和application.properties里边保持一致，也可以手动修改)  
    
* 打包程序
    1. 修改文件/signing-in-system/src/main/resources/application.properties  
    `spring.jpa.hibernate.ddl-auto=update`
    
