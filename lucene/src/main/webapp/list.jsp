<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>分页</title>
    </head>
    <body>
        <!-- 输入区 -->
        <form action="${pageContext.request.contextPath}/articleServlet" method="post">
            <input type="hidden" name="curPage" value="1">
            <table border="2" align="center">
                <tr>
                    <th>输入关键字</th>
                    <td><input type="text" name="keywords" value="${requestScope.keywords}" maxlength="10"></td>
                    <td><input id="search" type="button" value="站内搜索"></td>
                </tr>
            </table>
        </form>

    <!-- 显示区 -->
    <table border="2" align="center" width="70%">
        <tr>
            <th>编号</th>
            <th>标题</th>
            <th>内容</th>
        </tr>
        <c:forEach var="article" items="${requestScope.page.articleList}">
            <tr>
                <th>${article.id}</th>
                <th>${article.title}</th>
                <th>${article.content}</th>
            </tr>
        </c:forEach>
        <tr>
            <th colspan="3" align="center">
                <a href="">首页</a>
                <a href="">上一页</a>
                <a href="">下一页</a>
                <a href="">尾页</a>
            </th>
        </tr>
    </table>

    <script type="text/javascript">
        document.getElementById("search").onclick = function () {
            var formElement = document.forms[0];
            var keywords = formElement.keywords.value;
            keywords = trim(keywords);
            if(keywords.length === 0) {
                alert("你没有填关键字")
            } else {
                formElement.submit();
            }
        }
        function trim(str) {
            str.replace(/^[\s*]/, "");
            str.replace(/[\s*]$/, "");
            return str;
        }
    </script>
    </body>
</html>