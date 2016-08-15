<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<tags:page title="Putaway">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(function() {
		$("#barcode").focus();
		if ($("#message").val() != "") {
			noty({
				text : $("#message").val(),
				type : "error",
				layout : "topRight"
			});
		}
		if ($("#alertSet").val() == "true") {
			$("#myModal").modal({
				backdrop : 'static'
			});
		} /* else if ($("#inventoryId").val() != "") {
			var inventoryId = $("#inventoryId").val();
			location.href = "/Wms/Putaway/printBarcode/" + inventoryId;
		} */
		$("#form").validate();
		$("#assignForm").validate();
		$("#printButton").removeAttr('disabled');
	});

	function showChoice() {
		$("#putawayButton").attr("disabled", "disabled");
		/* $("#printButton").attr("disabled", "disabled"); */
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

	function printBarcode(inventoryId) {
// 		var inventoryId = $("#inventoryId").val();
		location.href = "/Wms/PutawayDirect/printBarcode/" + inventoryId;
	}
	
	function returnList()
	{
		$("#form2").submit();
	}
	
	function confirmRtv(){
		$("#printButton").attr("disabled", "disabled");
		$("#direct").submit();
		
	};
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
		Direct Dispatch Desk
		</div>
			<div class="box-content">
			<input type="hidden" id="message" value="${message}" />
			<input type="hidden" value="${alert}" id="alertSet" />
				<div class="row" style="margin-bottom: 20px;margin-top: 20px">
					<form action='<c:url value="/PutawayDirect/check" />' method="post"
							id="form" >
						<input type="hidden" name="listId" value="${putawayList.id}" />
						<div class="col-lg-12" style="margin-top: 20px">

							<div class="col-lg-4">
								<label>CRICode : </label> <input type="text" name="barcode"
										class="form-control required" id="barcode" />
							</div>
							<div class="col-lg-4" style="text-align: center;">
								<input type="submit" class="btn btn-primary" value="Get Putaway"
										style="margin-top: 25px" id="putawayButton" />
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<c:if test="${fn:length(putawayList.inventoryList) gt 0}">
			<div class="box">
				<div class="box-header" style="padding-left: 10px; padding-top: 10px">Putaway
					List Identifier : ${putawayList.id}</div>
				<div class="box-content">
					<div class="row">
						<div class="col-lg-12" style="margin-top: 20px;">
							<table class="table table-striped table-bordered">
								<thead>
									<tr>
										<th>Barcode</th>
										<th>Product Name</th>
										<th>Seller</th>
										<th>Location</th>
										<th>Group</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${putawayList.inventoryList}">
										<tr>
											<td>${item.barcode}</td>
											<td>${item.productName}</td>
											<td>${item.sellerName}</td>
											<td>${item.location}</td>
											<td>${item.groupName}</td>
											<td><button class="btn btn-warning" onclick="printBarcode(${item.id});">Download</button></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="col-lg-12"
								style="margin-top: 20px; text-align: center;">
							<form id="form2" method="post" action="<c:url value="/PutawayDirect/Directdispatch"/>">
							<input name="putawayid" type="hidden" value="${putawayList.id}">
							<input name="putawaylist" type="hidden" value="${putawayList.inventoryList}">
							<button onclick="showChoice()" class="btn btn-success" type="button"
									id="printButton">Print</button> 
									
									<a class="btn btn-primary"
									href='<c:url value="/PutawayDirect/home"/>' style="margin-left: 20px"
									id="newPutaway">Start New Putaway</a>
<%-- 							<c:if test="${not empty directrtv}" > --%>
<!-- 							<a  class="btn btn-danger" onclick="returnList();" -->
<!-- 									 style="display:none;margin-left: 20px" id="dispatchButton" >Dispatch</a> -->
<%-- 							</c:if> --%>
							</form>
						</div>
					</div>
				</div>
			</div>
		</c:if>
	</div>

	<div class="modal fade" id="myModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="direct" action='<c:url value="/Putaway/Directdispatch"/>' method="post" target="_blank">
					<div class="modal-header">
						<!-- <button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button> -->
						<h4 class="modal-title">RTV Choice</h4>
					</div>
					<div class="modal-body">
						<input type="hidden" name="listId" value="${putawayList.id}" /> 
						<input type="hidden" name="inventoryId" value="${inventoryId}"
								id="inventoryId" /> <label>Product
							eligible for RTV. Do you want to perform RTV Now ?</label>
					</div>
					<div class="modal-footer">
<!-- 						<input type="submit" class="btn btn-primary"  id="confirmDirectRTV" value="Yes" /> -->
						<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="confirmRtv();">Yes</button>
<!-- 						<a class="btn btn-primary" data-dismiss="modal" -->
<!-- 								onclick="confirmRtv()">Yes</a> -->
						<a class="btn btn-success" data-dismiss="modal">No</a>
					</div>
				</form>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	<div class="modal fade" id="assignModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="assignForm" action='<c:url value="/Putaway/printList" />'
						target="_blank" method="post">
					<div class="modal-header">
						<h4 class="modal-title">Assign Warehouse Boy</h4>
					</div>
					<div class="modal-body">
						<input type="hidden" name="listId" value="${putawayList.id}" />
						<div class="row">
							<div class="col-lg-6">
								<label>Warehouse Boy :</label> <select name="warehouseBoy"
										class="form-control required" id="boySelect">
									<!-- <option value="">Please Select</option> -->
								</select>
							</div>
							<div class="col-lg-6"
									style="text-align: center; margin-top: 28px">
								<input type="submit" class="btn btn-primary" value="Proceed"
										onclick="completePrint();" />
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