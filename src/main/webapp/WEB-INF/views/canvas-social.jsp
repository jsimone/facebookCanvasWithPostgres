<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <!DOCTYPE html>
 <html>
    <head>
		
			<c:if test="${sendRedirect}">
    			<script type="text/javascript">
					top.location.href='https://www.facebook.com/dialog/oauth?client_id=135651429859631&redirect_uri=http://apps.facebook.com/jsseconddemo/&scope=email,user_checkins,friends_checkins'
				</script>
			</c:if>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
		<link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.3.0/bootstrap.min.css">
		<style type="text/css">
		  html { height: 100% }
		  body { height: 100%; margin: 0; padding: 0 }
		  #map_canvas { height: 400px; width: 500px; }
		</style>
      
    </head>
    <body>
  		<div class="container" style="max-width: 720px">
		      <script src="js/jquery-1.6.2.min.js">
		      </script>
		      <br><br>
		      <h2>Welcome to The Check-In Note Taker</h2>
		      
		      <h3>Places Where You've Checked In</h3>
		      <table class="zebra-striped">
		          <tr><th>Name</th><th>Note</th></tr>
			      <c:forEach var="checkinNote" items="${checkInNotes}">
			      	<tr>
			      		<td>${checkinNote.checkin.place.name}</td>
			      		<td>
			      			<c:choose>
				      			<c:when test="${not empty checkinNote.noteText}">
				      				${checkinNote.noteText}
					      		</c:when>
					      		<c:otherwise>
					      			<form action="/note/${profileId}/${checkinNote.checkin.place.id}" method="post">
					      				<input type="text" name="noteText"/>
					      				<input type="hidden" name="accessToken" value="${accessToken}"/>
					      				<input type="submit" name="save"/>
					      			</form>
					      		</c:otherwise>
				      		</c:choose>
			      		</td>
			      	</tr>
			      </c:forEach>
		      </table>
	  	</div>
    </body>
 </html>
 
 
 
 
 
 
 
 
 
 
