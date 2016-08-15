<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="Liquidation Management">
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
				Existing Liquidation Centers</div>
			<div class="box-content">
				<input type="hidden" id="message" value="${message}" />
				<div class="row" style="margin-top: 30px">
					<div class="col-lg-12">
						<table
								class="table table-striped table-bordered bootstrap-datatable datatable">
							<thead>
								<tr>
									<th>Liquidation Center Name</th>
									<th>Code</th>
									<th>City</th>
									<th>State</th>
									<th>Pincode</th>
									<th>Contact Number</th>
									<th>Warehouse</th>
									<th style="text-align: center;">Action</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="liquidation" items="${liquidation}">
									<tr>
										<td>${liquidation.name}</td>
										<td>${liquidation.code}</td>
										<td>${liquidation.address.city}</td>
										<td>${liquidation.address.state}</td>
										<td>${liquidation.address.pincode}</td>
										<td>${liquidation.address.contactNumber}</td>
										<td>${liquidation.warehouse.name}</td>
										<td style="text-align: center;"><a
												href='<c:url value="/Liquidation/edit/${liquidation.id}"></c:url>'
												class="btn btn-primary">Edit</a> <c:choose>
												<c:when test="${liquidation.enabled}">
													<a
															href='<c:url value="/Liquidation/disable/${liquidation.id}"></c:url>'
															class="btn btn-danger" style="margin-left: 20px">Disable</a>
												</c:when>
												<c:otherwise>
													<a
															href='<c:url value="/Liquidation/enable/${liquidation.id}"></c:url>'
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