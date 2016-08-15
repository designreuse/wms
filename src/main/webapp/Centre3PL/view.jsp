<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="3PL Management">
	<jsp:attribute name="script">
<script type="text/javascript">
  	$(document).ready(function() { 
  		if ($("#message").val() != "") { 
  			noty({ 
  				text : $("#message").val(), 
  				type : "success", 
  				layout : "topRight" 
  			}); 
  		} 
  	}); 
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Existing 3PL Centres</div>
			<div class="box-content">
				<input type="hidden" id="message" value="${message}" />
				<div class="row" style="margin-top: 30px">
					<div class="col-lg-12">
						<table
								class="table table-striped table-bordered bootstrap-datatable datatable">
							<thead>
								<tr>
									<th>3PL Centre Name</th>
									<th>Code</th>
									<th>City</th>
									<th>State</th>
									<th>Pincode</th>
									<th>Contact Number</th>
									<th>Warehouse</th>
									<th style="text-align: centre;">Action</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="centre3pl" items="${centre3pl}">
									<tr>
										<td>${centre3pl.name}</td>
										<td>${centre3pl.code}</td>
										<td>${centre3pl.address.city}</td>
										<td>${centre3pl.address.state}</td>
										<td>${centre3pl.address.pincode}</td>
										<td>${centre3pl.address.contactNumber}</td>
										<td>${centre3pl.warehouse.name}</td>
										<td style="text-align: centre;"><a
												href='<c:url value="/Centre3PL/edit/${centre3pl.id}"></c:url>'
												class="btn btn-primary">Edit</a> 
												<c:choose>
												<c:when test="${centre3pl.enabled}">
													<a
															href='<c:url value="/Centre3PL/disable/${centre3pl.id}"></c:url>'
															class="btn btn-danger" style="margin-left: 20px">Disable</a>
												</c:when>
												<c:otherwise>
													<a
															href='<c:url value="/Centre3PL/enable/${centre3pl.id}"></c:url>'
															class="btn btn-success" style="margin-left: 20px">Enable</a>
												</c:otherwise>
											</c:choose>
											</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</tags:page>