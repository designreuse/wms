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
	});
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
		RTV Desk
		</div>
			<div class="box-content">
			<input type="hidden" id="message" value="${message}" />
				<div class="row" style="margin-bottom: 20px; margin-top: 20px">
					<form action='<c:url value="/Rtv/getPicklist" />' method="post"
							id="form">
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Picklist Identifier : </label> <input type="text"
										name="picklistId" class="form-control required" id="barcode" />
							</div>
							<div class="col-lg-4" style="text-align: center;">
								<input type="submit" class="btn btn-primary" value="Get Details"
										style="margin-top: 25px" id="putawayButton" />
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<c:if test="${fn:length(picklist.inventoryList) gt 0}">
			<div class="box">
				<div class="box-header"
						style="padding-left: 10px; padding-top: 10px">Picklist Identifier : ${picklist.id}</div>
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
									<c:forEach var="item" items="${picklist.inventoryList}">
										<tr>
											<td>${item.barcode}</td>
											<td>${item.productName}</td>
											<td>${item.sellerName}</td>
											<td>${item.location}</td>
											<td>${item.groupName}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<%-- <div class="col-lg-12"
								style="margin-top: 20px; text-align: center;">
							<a onclick="showChoice()" class="btn btn-success"
									id="printButton">Print</a> <a class="btn btn-primary"
									href='<c:url value="/Putaway/home"/>' style="margin-left: 20px"
									id="newPutaway">Start New Putaway</a>
						</div> --%>
					</div>
				</div>
			</div>
		</c:if>
	</div>
	</jsp:body>
</tags:page>