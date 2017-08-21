<%--
  Created by IntelliJ IDEA.
  User: YangJing
  Date: 2017/7/18
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录界面</title>
</head>
<body>
<form method="POST" action="login">
    <%--这里的required=“required”能够控制在登录界面中如果值为空，系统会自动提醒输入--%>
    用户名：<input type="text" name="username" required="required">
    <br>
    密码：<input type="password" name="password" required="required">
    <br>
    <input type="submit" value="登录" name="b1">

</form>
</body>
</html>
