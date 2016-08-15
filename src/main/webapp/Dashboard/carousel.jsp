<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script>
$('#myCarousel').carousel({
	interval: 85714,
	pause: "hover"
	});

</script>
<div class="container">
	  <br>
	  <div id="myCarousel" class="carousel slide" data-ride="carousel">
	
	    <!-- Wrapper for slides -->
	    <div class="carousel-inner" role="listbox">
	      <div class="item active">
	      <c:choose>
	      <c:when test="${!empty pickupGraph}">
	        <div id="pending" style="width:1000px; height:400px;"></div>
	      </c:when>
	      <c:otherwise>
	      <div class="item active">
	      	<div style="width:1000px; height:400px;" align="center"> 
	      		<h2>${graphPickup}</h2>${warehouse.name}
	      	</div>
	      </div>
	      </c:otherwise>
	      </c:choose>
	      </div>
	      <div class="item">
	       <c:choose>
	    	  <c:when test="${!empty putawayBreakup}" >
	        	<div id="putaway" style="width:1000px; height:400px;"></div>
	      	  </c:when>
	       <c:otherwise>
	      	<div style="width:1000px; height:400px;" align="center" > 
	      		<h2>${graphPutaway} </h2>${warehouse.name}
	      	</div>
	      </c:otherwise>
	      </c:choose>
	      </div>
	      <div class="item" style="width:1000px; height:450px;" align="center">
	      <c:choose><c:when test="${!empty rpr}">
	        	<jsp:include page="/Dashboard/rprQc.jsp"></jsp:include>
	      </c:when>
	      <c:otherwise>
	      		<h2>${graphRpr} </h2>${warehouse.name}
	      </c:otherwise>
	      </c:choose>
	      </div>
	      
	      <div class="item">
	       <c:choose>
	    	  <c:when test="${!empty td}" >
	        	<div id="td" style="width:1000px; height:400px;"></div>
	      	  </c:when>
	       <c:otherwise>
	      	<div style="width:1000px; height:400px;" align="center" > 
	      		<h2>${graphTd} </h2>${warehouse.name}
	      	</div>
	      </c:otherwise>
	      </c:choose>
	      </div>
	      <div class="item">
	       <c:choose>
	    	  <c:when test="${!empty dailyNumber}" >
	        	<div id="daily" style="width:1000px; height:400px;"></div>
	      	  </c:when>
	       <c:otherwise>
	      	<div style="width:1000px; height:400px;" align="center" > 
	      		<h2>${graphDaily} </h2>${warehouse.name}
	      	</div>
	      </c:otherwise>
	      </c:choose>
	      </div>
	      
	    </div>
	
	    <!-- Left and right controls -->
	    <a class="left carousel-control" style="height: 80px;top:150px"   href="#myCarousel" role="button" data-slide="prev">
	      <span class="glyphicon glyphicon-chevron-left" style="right: auto;left: -20px;"  aria-hidden="true"></span>
	      <span class="sr-only">Previous</span>
	    </a>
	    <a class="right carousel-control" style="height: 80px;top:150px"  href="#myCarousel"  role="button" data-slide="next">
	      <span class="glyphicon glyphicon-chevron-right" style="right: -20px;left: auto;"  aria-hidden="true"></span>
	      <span class="sr-only">Next</span>
	    </a>
	  </div>
</div>

