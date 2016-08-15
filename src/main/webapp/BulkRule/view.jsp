<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="Bulk Rule">
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
				Existing Bulk Rules</div>
			<div class="box-content">
			<input type="hidden" id="message" value="${message}" />
				<div class="row" style="margin-top: 30px">
					<div class="col-lg-12">
						<table
								class="table table-striped table-bordered bootstrap-datatable datatable">
							<thead>
								<tr>
									<th>Bulk Rule Name</th>
									<th>Group Name</th>
									<th>Number of Shelves</th>
									<th>Number of Floors per Shelf</th>
									<th>Number of Box per Floor</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="rule" items="${bulkRules}">
									<tr>
										<td>${rule.name}</td>
										<td>${rule.group.name}</td>
										<td>${rule.group.numberOfShelves}</td>
										<td>${rule.group.numberOfFloors}</td>
										<td>${rule.group.capacityOfFloors}</td>
										<td><a
												href='<c:url value="/Rule/bulkEdit/${rule.id}"></c:url>'
												class="btn btn-primary">Edit</a> <c:choose>
												<c:when test="${rule.enabled}">
													<a
															href='<c:url value="/Rule/disableBulk/${rule.id}"></c:url>'
															class="btn btn-danger" style="margin-left: 20px">Disable</a>
												</c:when>
												<c:otherwise>
													<a
															href='<c:url value="/Rule/enableBulk/${rule.id}"></c:url>'
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