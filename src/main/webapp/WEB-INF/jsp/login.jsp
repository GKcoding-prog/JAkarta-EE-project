<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Connexion - SysConge</title>
</head>
<body>
    <h2>Connexion à SysConge</h2>
    <form action="login" method="post">
        <label>Email :</label>
        <input type="text" name="email"/><br/>
        <label>Mot de passe :</label>
        <input type="password" name="motDePasse"/><br/>
        <input type="submit" value="Se connecter"/>
    </form>
</body>
</html>
