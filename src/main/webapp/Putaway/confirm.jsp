<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<tags:page title="Putaway">
	<jsp:attribute name="script">
	<script type="text/javascript">
$(document).ready(function(){
	$("#putawayId").focus();
	if ($("#message").val() != "" && $("#success").val() == "true") {
		noty({
			text : $("#message").val(),
			type : "success",
			layout : "topRight"
		});
	}
});

function enableLocation(index)
{
	$("#location"+index).removeAttr("readonly");
}
function validateForm(id)
{
	$("#"+id).validate();
}
</script>
</jsp:attribute>
	<jsp:body>
		<div id="content" class="col-sm-11 col-lg-10">
			<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Confirm Putaway</div>
				<div class="box-content">
				<c:if test="${not empty message and success ne true}">
					<div class="alert alert-danger">
						<button type="button" class="close" data-dismiss="alert">×</button>
						${message}
					</div>
				</c:if>
				<input type="hidden" id="message" value="${message}" />
			<input type="hidden" id="success" value="${success}" />
					<div class="row" style="margin-bottom: 20px;margin-top: 20px">
						<form action='<c:url value="/Putaway/confirmSearch"/>'
							method="post" id="form">
							<div class="col-lg-12">
								<div class="col-lg-4">
									<label>Putaway List Identifier :</label> <input type="text"
										name="listId" class="form-control digits"
										id="putawayId" />
								</div>
								<div class="col-lg-4">
									<label>CRI Code :</label> <input type="text"
										name="barcode" class="form-control"
										id="barcode" />
								</div>
								<div class="col-lg-4"
									style="text-align: center; margin-top: 25px">
									<input type="submit" value="Get Details"
										class="btn btn-primary" onclick="validateForm('form')">
								</div>
							</div>
						</form>
						</div>
						</div>
						</div>
						<c:if test="${fn:length(putawayList.inventoryList) gt 0}">
						<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Putaway List Details</div>
				<div class="box-content">
					<div class="row">
							<form action='<c:url value="/Putaway/confirm"/>'
								name="putawayPostList" method="post" id="confirmForm">
								<input type="hidden" name="warehouseBoy.id"
									value="${putawayList.warehouseBoy.id}" /> <input type="hidden"
									name="id" value="${putawayList.id}" />
								<div class="col-lg-12" style="margin-top: 20px">
									<table class="table table-striped table-bordered">
										<thead>
											<tr>
												<th>Barcode</th>
												<th>Product Name</th>
												<th>Seller</th>
												<th>Group</th>
												<th>Location</th>
												<th>Action</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="inventory"
												items="${putawayList.inventoryList}" varStatus="i">
												<tr>
													<td>${inventory.barcode}</td>
													<td width="35%">${inventory.productName}</td>
													<td>${inventory.sellerName}</td>
													<td>${inventory.groupName}</td>
													<td width="10%"><input type="hidden"
														name="inventoryList[${i.index}].id"
														value="${inventory.id}"> <input type="text"
														name="inventoryList[${i.index}].location"
														value="${inventory.location}"
														class="form-control required" id="location${i.index}"
														readonly="readonly" />
													</td>
													<td><a onclick="enableLocation(${i.index})"
														class="btn btn-danger">Relocate</a>																	
												
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
								<div class="col-lg-12"
									style="margin-top: 20px; text-align: center;">
									<input type="submit" value="Confirm" class="btn btn-success"
										onclick="validateForm('confirmForm');" />
								</div>
							</form>
							</div>
							</div>
							</div>
						</c:if>
					</div>
				
		</jsp:body>
</tags:page>