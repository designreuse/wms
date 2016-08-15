<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<tags:page title="Picklist">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#selectDiv").hide();
				$("#form").validate();
				if ($("#message").val() != "") {
					noty({
						text : $("#message").val(),
						type : "error",
						layout : "topRight"
					});
				}
				$("#assignForm").validate();
				/* $("#form").submit(
						function(e) {
							$("#form").validate();
							if ($("#select").hasClass("empty")
									|| $("#select").val() == null) {
								e.preventDefault();
								noty({
									text : "Please select Seller Name.",
									type : "error",
									layout : "topRight"
								});
							}
						}); */
				$("#typeSelect").on(
						"change",
						function() {
							var type = $(this).val();
							if (type != "") {
								var request = $.ajax({
									type : "GET",
									url : "/Wms/Picklist/getGroups",
									data : {
										choice : type
									}
								});
								request.done(function(data) {
										$("#groupSelect").html(
												$("<option />").val("").text(
														"Please Select"));
										$.each(data, function() {
											$("#groupSelect").append(
													$("<option />").val(this)
															.text(this));
										});
										$("#selectDiv").hide();
										$("#select").addClass("empty");
								});
							}
						});
				$("#groupSelect").on(
						"change",
						function() {
							var groupName = $(this).val();
							var request = $.ajax({
								type : "GET",
								url : "/Wms/Picklist/getSellerList",
								data : {
									name : groupName
								}
							});
							request.done(function(data) {
								if (data != "") {
									if($("#selectDiv option").length != 0)
									{
										$(".chosen-select").chosen('destroy');
									}
									$("#selectDiv").show(); 
									$("#select").html("");
									$("#select").removeClass("empty");
									$.each(data, function() {
										$("#select").append(
												$("<option />").val(
														this.vendorCode).text(
														this.sellerName));
									});
									$(".chosen-select").chosen({
										max_selected_options : 5
									});
								} else {
									$("#selectDiv").hide();
									$(".chosen-select").chosen('destroy');
									$("#select").html("");
									$("#select").addClass("empty");
								}
							});
						});
			});
	function showChoice() {
		var request = $.ajax({
			type : "GET",
			url : "/Wms/WarehouseBoy/getBoyList"
		});
		request
				.done(function(data) {
					$("#boySelect").html(
							$("<option />").val("").text("Please Select"));
					$.each(data, function() {
						$("#boySelect").append(
								$("<option />").val(this.id).text(this.name));
					});
					$("#assignModal").modal({
						backdrop : 'static'
					});
				});
	}
	function completePrint() {
		if($("#boySelect").val() != "")
		{
			$("#assignModal").modal('hide');
		}
	}
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Picklist Desk</div>
			<div class="box-content">
				<input type="hidden" id="message" value="${message}" />
				<div class="row" style="margin-bottom: 20px; margin-top: 20px">
					<form action='<c:url value="/Picklist/search"/>' method="post"
						id="form">
						<div class="col-lg-12">
							<div class="col-lg-4">
								<label>Type :</label> <select name="returnType"
									class="form-control required" id="typeSelect">
									<option value="">Please Select</option>
									<option value="rtv">Return To Vendor</option>
									<option value="rtw">Return To Warehouse</option>
									<option value="rtc">Return To Customer</option>
									<option value="3pl">Return To 3PL</option>
									<option value="liq">Return To Liquidation</option>
								</select>
							</div>
							<div class="col-lg-4">
								<label>Group :</label> <select name="groupName"
									class="form-control required" id="groupSelect">
									<option value="">Please Select</option>
								</select>
							</div>
							<div class="col-lg-4">
								<div id="selectDiv">
									<label>Seller Name :</label> <select name="sellerCode[]"
										class="form-control chosen-select" id="select"
										multiple="multiple">
									</select>
								</div>
							</div>
						</div>
						<div class="col-lg-12"
							style="margin-top: 20px; text-align: center;">
							<input type="submit" value="Generate" class="btn btn-primary" />
						</div>
					</form>
				</div>
			</div>
		</div>
		<c:if test="${fn:length(picklist.inventoryList) gt 0}">
			<div class="box">
				<div class="box-header"
					style="padding-top: 10px; padding-left: 10px">Picklist
					Identifier : ${picklist.id}</div>
				<div class="box-content">
					<div class="row">
						<div class="col-lg-12" style="margin-top: 20px">
							<table class="table table-striped table-bordered">
								<thead>
									<tr>
										<th>Barcode</th>
										<th>Product Name</th>
										<th>Seller</th>
										<th>Group</th>
										<th>Location</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="inventory" items="${picklist.inventoryList}">
										<tr>
											<td>${inventory.barcode}</td>
											<td>${inventory.productName}</td>
											<td>${inventory.sellerName}</td>
											<td>${inventory.groupName}</td>
											<td>${inventory.location}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="col-lg-12"
							style="margin-top: 20px; text-align: center;">
							<input type="button" class="btn btn-success" value="Print"
								onclick="showChoice()" /> <a class="btn btn-primary"
								href='<c:url value="/Picklist/home"/>' style="margin-left: 20px">Start
								New Picklist</a>
						</div>
					</div>
				</div>
			</div>
		</c:if>
	</div>
	<div class="modal fade" id="assignModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="assignForm" action='<c:url value="/Picklist/printList" />'
					target="_blank" method="post">
					<div class="modal-header">
						<h4 class="modal-title">Assign Warehouse Boy</h4>
					</div>
					<div class="modal-body">
						<input type="hidden" name="listId" value="${picklist.id}" />
						<div class="row">
							<div class="col-lg-6">
								<label>Warehouse Boy :</label> <select name="warehouseBoy"
									class="form-control required" id="boySelect">
								</select>
							</div>
							<div class="col-lg-6"
								style="text-align: center; margin-top: 28px">
								<input type="submit" class="btn btn-primary" value="Proceed"
									onclick="completePrint()" />
							</div>
						</div>
					</div>
				</form>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	</jsp:body>
</tags:page>