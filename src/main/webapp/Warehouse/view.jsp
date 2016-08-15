<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="Warehouse Management">
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
				Existing Warehouses</div>
			<div class="box-content">
				<input type="hidden" id="message" value="${message}" />
				<div class="row" style="margin-top: 30px">
					<div class="col-lg-12">
						<table
								class="table table-striped table-bordered bootstrap-datatable datatable">
							<thead>
								<tr>
									<th>Warehouse Name</th>
									<th>Code</th>
									<th>City</th>
									<th>State</th>
									<th>Pincode</th>
									<th>Contact Number</th>
									<th style="text-align: center;">Action</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="warehouse" items="${warehouses}">
									<tr>
										<td>${warehouse.name}</td>
										<td>${warehouse.code}</td>
										<td>${warehouse.address.city}</td>
										<td>${warehouse.address.state}</td>
										<td>${warehouse.address.pincode}</td>
										<td>${warehouse.address.contactNumber}</td>
										<td style="text-align: center;"><a
												href='<c:url value="/Warehouse/edit/${warehouse.id}"></c:url>'
												class="btn btn-primary">Edit</a> <c:choose>
												<c:when test="${warehouse.enabled}">
													<a
															href='<c:url value="/Warehouse/disable/${warehouse.id}"></c:url>'
															class="btn btn-danger" style="margin-left: 20px">Disable</a>
												</c:when>
												<c:otherwise>
													<a
															href='<c:url value="/Warehouse/enable/${warehouse.id}"></c:url>'
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