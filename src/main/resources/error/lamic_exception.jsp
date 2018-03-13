<%@ page isErrorPage="true" %>

<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.apache.log4j.LogManager" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="com.hltx.lamic.pay.utils.ExceptionEmailUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>服务器出错啦！</title>
</head>
<body>

<%
    Logger logger = LogManager.getLogger(Exception.class);

    logger.error("[lamic_exception]：" + JSON.toJSONString(exception));

    response.getWriter().write("服务器出错啦!");

    try {
        ExceptionEmailUtils.sendException("莱米新支付异常", exception);
    } catch (Exception e) {
    }
%>
</body>
</html>
