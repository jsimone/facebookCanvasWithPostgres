<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
	<script type="text/javascript">
		
		function redirect() {
			top.location.href='https://www.facebook.com/dialog/oauth?client_id=135651429859631&redirect_uri=http://apps.facebook.com/jscanvastest/&scope=email,read_stream'
		}
	</script>
</head>
<body>
hello, world
${test}

<form>
<input type="button" value="redirect" onclick="redirect()" />
</form>
</body>
</html>
