<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="RTV">
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
		$("#barcode").on("change", function() {
			var barcode = $(this).val();
			var result = $("#mainTable").find("tr#" + barcode);
			if (result.text() != "") {
				$("#scanTable").prepend(result);
			} else {
				noty({
					text : "Package does not belong to this Picklist.",
					type : "error",
					layout : "topRight"
				});
			}
			$(this).val("");
			$(this).focus();
		});
		$("#mainForm").submit(function(e) {
			var missing = $("#mainBody tr").length;
			if (missing > 0) {
				var packages = $("#mainTable").clone();
				$("#missingTable").html(packages);
				e.preventDefault();
				$("#missingModal").modal({
					backdrop : 'static'
				});
			}
		});
		$("#missingForm").submit(function(e) {
			var choice = $("#choice").val();
			if (choice == 0) {
				e.preventDefault();
				$("#missingModal").modal('hide');
				$("#barcode").focus();
			}
		});
	});

	function proceed(choice) {
		if (choice == "Yes") {
			$("#choice").val("1");
		} else {
			$("#choice").val("0");
		}
	}
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
			<div class="box-header" style="padding-left: 10px; padding-top: 10px">Picklist
				Identifier : ${picklist.id}</div>
			<div class="box-content">
				<input type="hidden" id="message" value="${message}" />
				<div class="row">
					<div class="col-lg-12"
							style="margin-top: 20px; margin-bottom: 20px">
						<div class="col-lg-6">
							<label>Barcode : </label> <input type="text" id="barcode"
									class="form-control" />
						</div>
					</div>
					<div class="col-lg-12" style="margin-top: 20px">
						<div class="col-lg-6" style="margin-top: 10px;">
							<h2>Packages to Scan</h2>
							<table class="table table-striped table-bordered" id="mainTable">
								<thead>
									<tr>
										<th>Barcode</th>
										<th>Product Name</th>
										<th>Seller</th>
									</tr>
								</thead>
								<tbody id="mainBody">
									<c:forEach var="item" items="${picklist.inventoryList}"
											varStatus="i">
										<tr id="${item.barcode}">
											<td>${item.barcode}</td>
											<td>${item.productName}</td>
											<td>${item.sellerName}</td>
											<input type="hidden" name="missingInventory[${i.index}].id"
													value="${item.id}" />
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="col-lg-6" style="margin-top: 10px">
							<h2>RTV Packages</h2>
							<table class="table table-striped table-bordered" id="scanTable">
								<thead>
									<tr>
										<th>Barcode</th>
										<th>Product Name</th>
										<th>Seller</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					<div class="col-lg-12"
							style="margin-top: 20px; text-align: center;">
						<form action='<c:url value="/Rtv/generateGroup"/>' method="post"
								id="mainForm" name="rtvData">
							<input type="hidden" name="listId" value="${picklist.id}" /> <input
									type="submit" value="Generate Groups" class="btn btn-primary" />
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="missingModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="missingForm" action='<c:url value="/Rtv/generateGroup" />'
						method="post" name="rtvData">
					<div class="modal-header">
						<h2 class="modal-title">Remaining Packages</h2>
					</div>
					<div class="modal-body">
						<input type="hidden" name="listId" value="${picklist.id}" /> <input
								type="hidden" id="choice" value="0" />
						<div class="row">
							<div class="col-lg-12">
								<h3>Following Packages are not scanned :</h3>
							</div>
							<div class="col-lg-12" style="margin-top: 10px" id="missingTable">
							</div>
							<div class="col-lg-12" style="margin-top: 20px">
								<label>Are you sure you want to proceed ? </label>
							</div>
							<div class="col-lg-12"
									style="margin-top: 20px; text-align: center;">
								<input type="submit" value="Yes" class="btn btn-primary"
										onclick="proceed('Yes')" /> <input type="submit" value="No"
										class="btn btn-success" style="margin-left: 20px"
										onclick="proceed('No')" />
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