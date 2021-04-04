# Happycat
<p align = "center">
<img alt="Sym" src="https://user-images.githubusercontent.com/46141646/113502918-8de69f80-9561-11eb-8281-078e75bbe750.png">
<br><br>
a web server for learning tomcat server
<br><br>
</p>

## 💡 Introduction
this project is tiny as no more than 5000 lines but covers many features of a web server and totally writen by Java, is suitable for newbies to learn Java and web server knoweldge 

## ✨ Composition
1. happyBrowser :  send and review http message with code
2. unit test : stablely interate project and show use case of this project
3. happycat log : rotate log happycat server running info
4. parallel process requests
5. muti-port and muti-webapp

   1. Server、Service、Connector、Engine、Host、Context implementation
   2. parse context.xml、server.xml、web.xml
6. http specification

   1. 200 : normal response
   2. 404 : client error
   3. 500 : server error
   4. compression
   5. client jump
   6. server jump
   7. cookie
   8. session
7. servlet

   1. InvokeServlet : handle requests definied in webapp's web.xml
   2. DefalutServlet : handle static resource requests
   3. JspServlet : handle JSP request
   4. servlet life time
   5. servlet automatic start
8. class load mechanism

   1. CommonClassLoader : load happycat build-in classes
   2. WebClassLoader : load webapp classes
   3. JspClassLoader : load JSP classes
9. webapp deployment

   10. webapp hot loadding
   11. war file dynamic deployment
10. webapp filter
11. webapp listener

## 🔍Class-diagram for core classes
![image](https://user-images.githubusercontent.com/46141646/113503439-6cd37e00-9564-11eb-8921-0792f1b5a68d.png)


## 🔍 Sequence-diagram for a 200 request
![image](https://user-images.githubusercontent.com/46141646/113503469-a73d1b00-9564-11eb-940a-6e31b8105460.png)

## 💻 how to deploy
1. build jar with idea 
2. copy b/、cong/、javaweb/、lib/、log/、webapps/、work/  to the directory of jar file
3. config right docbase of happycat/conf/server.xml 
4. run with "java -jar happycat.jar"

## 👀 Reference 
* http://tomcat.apache.org/
* Apache Tomcat Cookbook
* Tomcat架构解析
* How Tomcat Works
* Tomcat the definitive guide
* Specification UML 2.5.1
* UMLDistilled
* PlantUML_Language_Reference_Guide_en
* UML_for_Java_Programmers-Book
* core Java

## 🙏 Acknowledgement
* [jsoup](https://github.com/jhy/jsoup): Java HTML parser
* [hutool](https://www.hutool.cn/)：A set of tools that keep Java sweet
* [tomcat](http://tomcat.apache.org/)：A widely used web server
