<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="Warehouse">
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
											if ($("#warehouse_name").hasClass(
													"duplicate")
													|| $("#warehouse_code")
															.hasClass(
																	"duplicate")) {
												e.preventDefault();
												noty({
													text : "Duplicate Warehouse Name or Code. Please correct and proceed.",
													type : "error",
													layout : "topRight"
												});
											}
										});
						$("#warehouse_name")
								.on(
										"change",
										function() {
											var warehouse_name = $(this).val();
											var request = $
													.ajax({
														type : "POST",
														url : "/Wms/Warehouse/checkName",
														data : {
															name : warehouse_name
														}
													});
											request
													.done(function(msg) {
														if (msg == "failure") {
															$("#warehouse_name")
																	.addClass(
																			"duplicate");
															noty({
																text : "Warehouse Name already exists. Please Select a different name.",
																type : "error",
																layout : "topRight"
															});
														} else {
															$("#warehouse_name")
																	.removeClass(
																			"duplicate");
														}
													});
											request
													.fail(function(jqXHR,
															textStatus) {
														noty({
															text : "Failed to check Warehouse Name.",
															type : "error",
															layout : "topRight"
														});
													});
										});
						$("#warehouse_code")
								.on(
										"change",
										function() {
											var warehouse_code = $(this).val();
											var request = $
													.ajax({
														type : "POST",
														url : "/Wms/Warehouse/checkCode",
														data : {
															code : warehouse_code
														}
													});
											request
													.done(function(msg) {
														if (msg == "failure") {
															$("#warehouse_code")
																	.addClass(
																			"duplicate");
															noty({
																text : "Warehouse Code already exists. Please Select a different code.",
																type : "error",
																layout : "topRight"
															});
														} else {
															$("#warehouse_code")
																	.removeClass(
																			"duplicate");
														}
													});
											request
													.fail(function(jqXHR,
															textStatus) {
														noty({
															text : "Failed to check Warehouse Code.",
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
				Warehouse Management</div>
			<div class="box-content">
				<div class="row">
					<form name="warehouse" action='<c:url value="/Warehouse/save" />'
						method="post" id="form">
						<input type="hidden" name="id" value="${warehouse.id}" />
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Name : </label> <input type="text" name="name"
									class="form-control required" value="${warehouse.name}"
									id="warehouse_name">
							</div>
							<div class="col-lg-4">
								<label>Code : </label> <input type="text" name="code"
									class="form-control required" value="${warehouse.code}"
									id="warehouse_code">
							</div>
							<div class="col-lg-4">
								<label>Contact Number (+91) :</label> <br> <input type="text"
									class="form-control required digits" name="address.contactNumber"
									value="${warehouse.address.contactNumber}" />
							</div>
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Address Line 1 : </label> <input type="text"
									name="address.addressLine1" class="form-control required"
									value="${warehouse.address.addressLine1}">
							</div>
							<div class="col-lg-4">
								<label>Address Line 2 : </label> <input type="text"
									name="address.addressLine2" class="form-control required"
									value="${warehouse.address.addressLine2}">
							</div>
							<div class="col-lg-4">
								<label>City : </label> <br> <input type="text"
									name="address.city" value="${warehouse.address.city}"
									class="form-control required" />
							</div>
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>State : </label> <input type="text" name="address.state"
									class="form-control required"
									value="${warehouse.address.state}">
							</div>
							<div class="col-lg-4">
								<label>Pincode : </label> <input type="text"
									name="address.pincode" class="form-control digits required"
									value="${warehouse.address.pincode}">
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