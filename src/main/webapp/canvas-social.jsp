<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <!DOCTYPE html>
 <html>
    <head>
		
			<c:if test="${sendRedirect}">
    			<script type="text/javascript">
					top.location.href='https://www.facebook.com/dialog/oauth?client_id=135651429859631&redirect_uri=http://apps.facebook.com/jscanvastest/&scope=email,user_checkins,friends_checkins'
				</script>
			</c:if>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
		<style type="text/css">
		  html { height: 100% }
		  body { height: 100%; margin: 0; padding: 0 }
		  #map_canvas { height: 400px; width: 500px; }
		</style>
		<script type="text/javascript"
		    src="http://maps.googleapis.com/maps/api/js?sensor=false">
		</script>
		<script type="text/javascript">
		  
		  function initializeMap() {
		    var latlng = new google.maps.LatLng(37.784, -122.400);
		    var myOptions = {
		      zoom: 12,
		      center: latlng,
		      mapTypeId: google.maps.MapTypeId.ROADMAP
		    };
		    var map = new google.maps.Map(document.getElementById("map_canvas"),
		        myOptions);

		    return map;
		  }
		  
		  function addMarker(lat, lng, message, map) {
			  var marker = new google.maps.Marker({
			      position: new google.maps.LatLng(lat, lng),
			      title:message
			  });

			// To add the marker to the map, call setMap();
			marker.setMap(map);
			  
		  }
		  
         function populateCheckins(data, map){
        			  
			var checkins = data.data;
			for(var i = 0; i < checkins.length; i++) {
				addMarker(checkins[i].place.location.latitude, checkins[i].place.location.longitude, checkins[i].place.name, map);
			}
		    $('#check_in_list').html("Done");
        }
         
         function createMap() {
	         var map = initializeMap();
	         populateCheckins(eval(${checkins}), map);
         }
		
		window.onload(createMap());
		</script>
      
    </head>
    <body onload="checkLoginAndLoadData()">
      <script src="js/jquery-1.6.2.min.js">
      </script>
      <br><br>
      Where Have You Been?
      
      <form>
      	<input type="button" value="populate map" onclick="createMap()"/>
      </form>
      
      <div id="map_canvas" style="width:500px; height:400px"></div>
      
      <ul>
	      <c:forEach var="checkin" items="${checkInObjs}">
	      	<li>Checked In At: ${checkin.place.name}
	      </c:forEach>
      </ul>
    </body>
 </html>
 
 
 
 
 
 
 
 
 
 
