<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="Warehouse Boy">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#form").validate();
						$("form")
								.submit(
										function(e) {
											$("#form").validate();
											if ($("#boy_name").hasClass(
													"duplicate")) {
												e.preventDefault();
												noty({
													text : "Duplicate Name for Warehouse Boy. Please correct and proceed.",
													type : "error",
													layout : "topRight"
												});
											}
										});
						$("#boy_name")
								.on(
										"change",
										function() {
											var boy_name = $(this).val();
											var request = $
													.ajax({
														type : "POST",
														url : "/Wms/WarehouseBoy/checkName",
														data : {
															name : boy_name
														}
													});
											request
													.done(function(msg) {
														if (msg == "failure") {
															$("#boy_name")
																	.addClass(
																			"duplicate");
															noty({
																text : "Warehouse Boy Name already exists. Please Select a different name.",
																type : "error",
																layout : "topRight"
															});
														} else {
															$("#boy_name")
																	.removeClass(
																			"duplicate");
														}
													});
											request
													.fail(function(jqXHR,
															textStatus) {
														noty({
															text : "Failed to check Warehouse Boy Name.",
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
		Warehouse Boy Management
		</div>
			<div class="box-content">
				<div class="row">
					<form name="warehouseBoy"
							action='<c:url value="/WarehouseBoy/save" />' method="post"
							id="form">
						<input type="hidden" name="id" value="${warehouseBoy.id}" />
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Name : </label> <input type="text" name="name"
										class="form-control required" value="${warehouseBoy.name}"
										id="boy_name">
							</div>
							<div class="col-lg-4">
								<label>Contact Number : </label> <input type="text"
										name="contactNumber" class="form-control required digits"
										value="${warehouseBoy.contactNumber}">
							</div>
							<%-- <div class="col-lg-4">
								<label>Address : </label> <br>
								<textarea rows="3" cols="40" name="address">${warehouse.address}</textarea>
							</div> --%>
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