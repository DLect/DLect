<%-- 
    Document   : login
    Created on : Sep 5, 2013, 9:21:22 PM
    Author     : lee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Login to DLect</h2>
        <form name="login" action="rest/l" method="POST" enctype="multipart/form-data">
            Username: <input type="text" name="username" value="" size="10" /><br />
            Password: <input type="password" name="password" value="" size="10" /><br />
            <input type="hidden" name="code" value="921" />
            <input type="submit" value="Submit" />
        </form>
        <h2>Login to UQ Directly</h2>
        <form name="login" action="https://learn.uq.edu.au/webapps/Bb-mobile-BBLEARN/sslUserLogin?v=1&language=en_GB&ver=3.1.2" method="POST" enctype="application/x-www-form-urlencoded">
            Username: <input type="text" name="username" value="" size="10" /><br />
            Password: <input type="password" name="password" value="" size="10" /><br />
            <input type="submit" value="Submit" />
        </form>
    </body>
</html>
