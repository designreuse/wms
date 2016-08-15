<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page>
	<jsp:attribute name="title">User Management</jsp:attribute>
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#form").validate();
						if ($("#message").val() != "") {
							noty({
								text : $("#message").val(),
								type : "error",
								layout : "topRight"
							});
						}
						$("form")
								.submit(
										function(e) {
											$("#form").validate();
											if ($("#user_name").hasClass(
													"error")) {
												e.preventDefault();
												noty({
													text : "Please enter a different Username.",
													type : "error",
													layout : "topRight"
												});
											}
											if($("#warehouse").val() == null || $("#roles").val() == null)
											{
												e.preventDefault();
												noty({
													text : "Please select role and warehouse for the user.",
													type : "error",
													layout : "topRight"
												});
											}
										})
						$("#user_name")
								.on(
										"change",
										function() {
											var user_name = $(this).val();
											var request = $.ajax({
												type : "POST",
												url : "/Wms/User/checkUser",
												data : {
													name : user_name
												}
											});
											request
													.done(function(msg) {
														if (msg == "failure") {
															noty({
																text : "Username already exists. Please Select a different name.",
																type : "error",
																layout : "topRight"
															});
															$("#user_name")
																	.addClass(
																			"error");
														} else {
															$("#user_name")
																	.removeClass(
																			"error");
														}
													});
											request
													.fail(function(jqXHR,
															textStatus) {
														noty({
															text : "Failed to check Username.",
															type : "error",
															layout : "topRight"
														});
													});
										});
					});
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-lg-10 col-sm-11">
		<div class="box">
		<div class="box-header" style="padding-top: 10px; padding-left: 10px">
		User Management
		</div>
			<div class="box-content">
			<div class="row">
				<input type="hidden" id="message" value="${message}" />
					<form name="user" action='<c:url value="/User/save" />'
							method="post" id="form">
						<input type="hidden" name="id" value="${user.id}" />
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Username : </label> <input type="text" name="userName"
										class="form-control required" value="${user.userName}"
										id="user_name">
							</div>
							<c:if test="${not edit}">
								<div class="col-lg-4">
									<label>Password : </label> <input type="password"
											name="password" class="form-control required">
								</div>
							</c:if>
							<div class="col-lg-4">
								<label>Roles : </label> <select name="role[]"
										class="form-control required" multiple="multiple"
										data-rel="chosen" id="roles">
									<div class="chosen-container-multi chosen-container">
										<c:forEach items="${roles}" var="role">
											<c:set var="found" value="false" />
											<c:forEach var="savedRole" items="${user.userRoles}">
												<c:if test="${role.id eq savedRole.id}">
													<option value="${role.id}" selected="selected">${role.role}</option>
													<c:set var="found" value="true" />
												</c:if>
											</c:forEach>
											<c:if test="${not found}">
												<option value="${role.id}">${role.role}</option>
											</c:if>
										</c:forEach>
									</div>
								</select>
							</div>
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Warehouses : </label> <select name="warehouse[]"
										class="form-control required" multiple="multiple"
										data-rel="chosen" id="warehouse">
									<div class="chosen-container-multi chosen-container">
										<c:forEach items="${warehouses}" var="warehouse">
											<c:set var="found" value="false" />
											<c:forEach var="savedWarehouse"
													items="${user.userWarehouses}">
												<c:if test="${warehouse.id eq savedWarehouse.id}">
													<option value="${warehouse.id}" selected="selected">${warehouse.name}</option>
													<c:set var="found" value="true" />
												</c:if>
											</c:forEach>
											<c:if test="${not found}">
												<option value="${warehouse.id}">${warehouse.name}</option>
											</c:if>
										</c:forEach>
									</div>
								</select>
							</div>
						</div>
						<div class="col-lg-12"
								style="text-align: center; margin-top: 20px">
							<input type="submit" value="Save" class="btn btn-primary">
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</tags:page>