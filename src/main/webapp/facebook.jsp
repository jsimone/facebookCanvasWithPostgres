<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
	<script type="text/javascript">
		
		<c:if test="sendRedirect">
			top.location.href='https://www.facebook.com/dialog/oauth?client_id=135651429859631&redirect_uri=http://apps.facebook.com/jscanvastest/&scope=email,read_stream'
		</c:if>
	</script>
</head>
<body>
hello, world<br>
data: ${data}<br>
oauth: ${oauth}<br>

</body>
</html>
