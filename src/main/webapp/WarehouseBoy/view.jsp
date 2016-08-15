<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="Warehouse Boy">
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
				Existing Warehouse Boys</div>
			<div class="box-content">
			<input type="hidden" id="message" value="${message}" />
				<div class="row" style="margin-top: 30px">
					<div class="col-lg-12">
						<table
								class="table table-striped table-bordered bootstrap-datatable datatable">
							<thead>
								<tr>
									<th>Warehouse Boy Name</th>
									<th>Contact Number</th>
									<!-- <th>Address</th> -->
									<th>Action</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="warehouseBoy" items="${boys}">
									<tr>
										<td>${warehouseBoy.name}</td>
										<td>${warehouseBoy.contactNumber}</td>
										<td><a
												href='<c:url value="/WarehouseBoy/edit/${warehouseBoy.id}"></c:url>'
												class="btn btn-primary">Edit</a>
												<a
												href='<c:url value="/WarehouseBoy/remove/${warehouseBoy.id}"></c:url>'
												class="btn btn-danger" style="margin-left: 20px">Remove</a>
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
