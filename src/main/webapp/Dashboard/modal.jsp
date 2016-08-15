<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script>
$('#myModal').carousel({
	interval: 85714,
	pause: "hover"
	});

</script>
<div class="modal fade bs-example-modal-lg" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content" style="width:1100px;" >
     <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">

			  <!-- Wrapper for slides -->
			  <div class="carousel-inner">
			 <div class="item active">
				  <c:choose>
		      		 <c:when test="${!empty pickupGraph}">
					     <div id="pendingMod" style="width:1100px; height:600px;margin-left:15px" ></div> 
					 </c:when>
					 <c:otherwise>
						 <div style="width:1000px; height:400px;" align="center"> 
				      		<h2>${graphPickup}</h2>${warehouse.name}
				      	</div>
					 </c:otherwise>
				  </c:choose>
			</div>
			<div class="item">
				 <c:choose>
	    	  	 <c:when test="${!empty putawayBreakup}">
				      <div id="putawayModal" style="width:1100px; height:600px;"  ></div> 
				 </c:when>
				 <c:otherwise>
			      	<div style="width:1000px; height:400px;" align="center"> 
			      		<h2>${graphPutaway} </h2>${warehouse.name}
			      	</div>
			      </c:otherwise>
	      		</c:choose>
	      	</div>
	      	  <div class="item" style="width:1050px; height:550px;margin-top:20px;margin-left:15px;margin-right:15px" align="center">
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
	    	  	 <c:when test="${!empty td}">
				      <div id="tdModal" style="width:1100px; height:600px;"  ></div> 
				 </c:when>
				 <c:otherwise>
			      	<div style="width:1000px; height:400px;" align="center"> 
			      		<h2>${graphTd} </h2>${warehouse.name}
			      	</div>
			      </c:otherwise>
	      		</c:choose>
	      	</div>
	      	<div class="item">
				 <c:choose>
	    	  	 <c:when test="${!empty dailyNumber}">
				      <div id="dailyModal" style="width:1100px; height:600px;"  ></div> 
				 </c:when>
				 <c:otherwise>
			      	<div style="width:1000px; height:400px;" align="center"> 
			      		<h2>${graphDaily} </h2>${warehouse.name}
			      	</div>
			      </c:otherwise>
	      		</c:choose>
	      	</div>
			  </div>
			  <!-- Controls -->
			  <a class="left carousel-control" style="height: 80px;top:230px"  href="#carousel-example-generic" role="button" data-slide="prev">
			    <span class="glyphicon glyphicon-chevron-left" style="right: auto;left: 5px;"></span>
			  </a>
			  <a class="right carousel-control" style="height: 80px;top:230px"  href="#carousel-example-generic" role="button" data-slide="next">
			    <span class="glyphicon glyphicon-chevron-right"  style="right: 5px;left: auto;"></span>
			  </a>
			</div>
    </div>
  </div>
</div>
