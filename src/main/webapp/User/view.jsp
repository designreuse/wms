<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page>
	<jsp:attribute name="title">User Management</jsp:attribute>
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
		Existing Users
		</div>
			<div class="box-content">
			<input type="hidden" id="message" value="${message}" />
				<div class="row" style="margin-top: 30px">
					<div class="col-lg-12">
						<table
								class="table table-striped table-bordered bootstrap-datatable datatable">
							<thead>
								<tr>
									<th>User Name</th>
									<th>Roles</th>
									<th>Enabled</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="user" items="${users}">
									<tr>
										<td>${user.userName}</td>
										<td><c:forEach var="role" items="${user.userRoles}">
										${role.role}<br />
											</c:forEach>
											</td>
										<td><c:choose>
												<c:when test="${user.enabled}">
										Yes
										</c:when>
												<c:otherwise>
										No
										</c:otherwise>
											</c:choose>
											</td>
										<td><a
												href='<c:url value="/User/edit/${user.id}"></c:url>'
												class="btn btn-primary">Edit</a> <c:choose>
												<c:when test="${user.enabled}">
													<a href='<c:url value="/User/disable/${user.id}"></c:url>'
															class="btn btn-danger" style="margin-left: 20px">Disable</a>
												</c:when>
												<c:otherwise>
													<a href='<c:url value="/User/enable/${user.id}"></c:url>'
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