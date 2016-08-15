<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="Liquidation">
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
											if ($("#liquidation_name")
													.hasClass("duplicate")
													|| $("#liquidation_code")
															.hasClass(
																	"duplicate")) {
												e.preventDefault();
												noty({
													text : "Duplicate Liquidation centre Name or Code. Please correct and proceed.",
													type : "error",
													layout : "topRight"
												});
											}
										});
						/* $("#liquidation_name")
								.on(
										"change",
										function() {
											var liquidation_name = $(this)
													.val();
											var request = $
													.ajax({
														type : "POST",
														url : "/Wms/Liquidation/checkName",
														data : {
															name : liquidation_name
														}
													});
											request
													.done(function(msg) {
														if (msg == "failure") {
															$(
																	"#liquidation_name")
																	.addClass(
																			"duplicate");
															noty({
																text : "Liquidation centre Name already exists. Please Select a different name.",
																type : "error",
																layout : "topRight"
															});
														} else {
															$(
																	"#liquidation_name")
																	.removeClass(
																			"duplicate");
														}
													});
											request
													.fail(function(jqXHR,
															textStatus) {
														noty({
															text : "Failed to check Liquidation centre Name.",
															type : "error",
															layout : "topRight"
														});
													});
										});
						$("#liquidation_code")
								.on(
										"change",
										function() {
											var liquidation_code = $(this)
													.val();
											var request = $
													.ajax({
														type : "POST",
														url : "/Wms/Liquidation/checkCode",
														data : {
															code : liquidation_code
														}
													});
											request
													.done(function(msg) {
														if (msg == "failure") {
															$(
																	"#liquidation_code")
																	.addClass(
																			"duplicate");
															noty({
																text : "Liquidation centre Code already exists. Please Select a different code.",
																type : "error",
																layout : "topRight"
															});
														} else {
															$(
																	"#liquidation_code")
																	.removeClass(
																			"duplicate");
														}
													});
											request
													.fail(function(jqXHR,
															textStatus) {
														noty({
															text : "Failed to check Liquidation centre Code.",
															type : "error",
															layout : "topRight"
														});
													});
										});*/
					}); 
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-lg-10 col-sm-11">
		<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Liquidation centre Management</div>
			<div class="box-content">
				<div class="row">
					<form name="liquidation"
							action='<c:url value="/Liquidation/save" />' method="post"
							id="form">
						<input type="hidden" name="id" value="${liquidation.id}" />
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Name : </label> <input type="text" name="name"
										class="form-control required" value="${liquidation.name}"
										id="liquidation_name">
							</div>
							<div class="col-lg-4">
								<label>Code : </label> <input type="text" name="code"
										class="form-control required" value="${liquidation.code}"
										id="liquidation_code">
							</div>
							<div class="col-lg-4">
								<label>Contact Number (+91) :</label> <br> <input
										type="text" class="form-control required digits"
										name="address.contactNumber"
										value="${liquidation.address.contactNumber}" />
							</div>
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Address Line 1 : </label> <input type="text"
										name="address.addressLine1" class="form-control required"
										value="${liquidation.address.addressLine1}">
							</div>
							<div class="col-lg-4">
								<label>Address Line 2 : </label> <input type="text"
										name="address.addressLine2" class="form-control required"
										value="${liquidation.address.addressLine2}">
							</div>
							<div class="col-lg-4">
								<label>City : </label> <br> <input type="text"
										name="address.city" value="${liquidation.address.city}"
										class="form-control required" />
							</div>
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>State : </label> <input type="text" name="address.state"
										class="form-control required"
										value="${liquidation.address.state}">
							</div>
							<div class="col-lg-4">
								<label>Pincode : </label> <input type="text"
										name="address.pincode" class="form-control digits required"
										value="${liquidation.address.pincode}">
							</div>
							<div class="col-lg-4">
								<label>Warehouse : </label> 
								<select name="warehouseID" class="form-control required">
								<option selected value="${liquidation.warehouse.id}">${liquidation.warehouse.name}</option>
								<c:forEach var="warehouse" items="${warehouse}">
											<c:if test="${warehouse.name != liquidation.warehouse.name}">
								<option value="${warehouse.id}">${warehouse.name}</option>
											</c:if>
								</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-lg-12"
								style="text-align:center ; margin-top: 20px">
							<input type="submit" value="Save" class="btn btn-primary">
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</tags:page>