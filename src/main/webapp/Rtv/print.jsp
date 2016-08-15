<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="<c:url value="/js/jquery-2.0.3.min.js"/>"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Returns</title>

<script type="text/javascript">
	$(document).ready(function(){
	 	var page=$("#returnpage").val();
		
	 	if(page == "putaway")
	 	{
	 		window.opener.location.href = "/Wms/Putaway/home";
	 		window.opener.close();
	 		window.print();
	 		
	 	}
	 	else if(page == "putawaydirect")
	 	{
	 		window.opener.location.href = "/Wms/PutawayDirect/home";
	 		window.opener.close();
	 		window.print();
	 	}
	 	else
	 	{
			window.opener.location.href = "home";
			window.print();
	 	}
	});
	
</script>
<style>
	body {		
		-webkit-print-color-adjust: exact; 
		width: 3.2in; 
		margin: 0 auto;
	}
		
	.table-border {
		border: 1px solid black;
		border-collapse: collapse;
	}
	
	.productDetail {
		align: center;
	}
	
	.productDetail {
		position: relative;
	}
	
	table {
		-fs-table-paginate: paginate;
		width: 100%;
		border-spacing: 0;
		font-size: 12px;
	}
	
	td {
		font: normal 6px; Arial , Helvetica, sans-serif;
		word-wrap: break-word;
		border: .3px #000 solid;
		padding: .5px;
		text-align: center;
	}
	
	th {
		font: bold 8px; Arial , Helvetica, sans-serif;
		word-wrap: break-word;
		text-align: center;
		background: #ffcc65;
		padding: 1px .5px;
	}
	
	tr {
		page-break-inside: avoid;
	}
	
	tbody {
		border-left: 1px #000 solid;
		border-right: 1px #000 solid;
	}
	
	.watermark {
		color: #d0d0d0;
		font-size: 50pt;
		position: absolute;
		margin-top: 95%;
		margin-left: 20%;
		margin-right: 10%;
		width: 70%;
		z-index: -1;
		margin-right: 10%;
		width: 70%;
	}
	
	.reason {
		background: #ffcc65;
		text-align: center;
		font: bold 8px;
		Arial
		,
		Helvetica,
		sans-serif;
	}
	
	h1 {
		font-family: "Time New Roman";
		font-size: 13px;
		/* margin-top: -0.5px;
		margin-bottom: -0.5px; */
	}
	
	h2 {
		font-family: "Time New Roman";
		font-size: 10px;
	}
	
	h3 {
		font-family: "Time New Roman";
		font-size: 8px;
	}
	
	p {
		font-family: "Times New Roman";
		font-size: 10px;
	}
	
	img {
		width: 230px;
		height: 30px;
	}
	
	div.line-height-medium {
		border-bottom: 1px #000000 solid;
		padding-bottom: 0.5px;
	}
	
	div.line-height-big {
		border-bottom: 2px #000000 solid;
		padding-bottom: 0.5px;
	}
	
	.right {
	    font-size: 12px;
	    float: right;
		border-radius: 4px;
		background-color: #111111;
		color: #ffffff;
		border: 1px solid #111111;
		padding: 3px 10px;
		width: auto;
		-webkit-print-color-adjust: exact; 
	}
	
	.barcode {
		line-height: 0;
		text-align: center;
	}
</style>
</head>
<body>
	<div>
		<input type="hidden" id="returnpage" value="${returnTo}"></input>
		<c:forEach var="rtvSheet" items="${sheets}" varStatus="k">
			<div>
				<c:choose>
					<c:when test="${k.index ne 0}">
						<div style="page-break-before: always;">
					</c:when>
					<c:otherwise>
						<div>
					</c:otherwise>
				</c:choose>
							<div class="line-height-medium">
								<c:if test="${not empty rtvSheet.areaCode}">
									<p class="right">${rtvSheet.areaCode}</p>
								</c:if>
								<h3>//SHIPPER ADDRESS//</h3>
								<p style="margin-top: -4px">
									${rtvSheet.warehouseDetails.name}<br />
									${rtvSheet.warehouseDetails.address.addressLine1}<br />
									${rtvSheet.warehouseDetails.address.addressLine2}<br />
									${rtvSheet.warehouseDetails.address.city},
									${rtvSheet.warehouseDetails.address.state}, India<br />
									${rtvSheet.warehouseDetails.address.pincode} <br />
								</p>
							</div>
							<div class="barcode line-height-medium">
								<h3>//RETURN TO VENDOR CODE//</h3>
								<img src='<c:url value="/Barcode/generate/RTVL${rtvSheet.id}"/>'
									alt="code" />
									<h2>RTVL${rtvSheet.id}</h2>
							</div>
							<div class="line-height-big">
								<h3>//DELIVERY ADDRESS//</h3>
								<h2 style="margin-top: -4px">${rtvSheet.receiverDetail.name}</h2>
								<p>
									${rtvSheet.receiverDetail.addressLine1}<br />
									${rtvSheet.receiverDetail.addressLine2}<br />
								</p>
								<p>
									<strong>CITY</strong>&nbsp;${rtvSheet.receiverDetail.city} / <strong>STATE</strong>&nbsp;${rtvSheet.receiverDetail.state}
									<br />
									<strong>PIN</strong>&nbsp;${rtvSheet.receiverDetail.pincode}
								</p>
								<p>
									<strong>MOBILE</strong>&nbsp;${rtvSheet.receiverDetail.contactNumber}
								</p>
							</div>
							<div style="text-align: center;" class="line-height-big">
								<h1>Courier Code : ${rtvSheet.courierCode}</h1>
							</div>
							<div class="barcode line-height-big">
								<h3>//COURIER TRACKING NUMBER//</h3>
								<img src='<c:url value="/Barcode/generate/${rtvSheet.awbNumber}"/>'
									alt="awb" />
									<h2>${rtvSheet.awbNumber}</h2>
							</div>
			</div>

			<div class="itemDetail" style="padding-top: 2px;">			
				<c:forEach var="inventory" items="${rtvSheet.productDetails}" varStatus="j">
					<c:if test="${not empty inventory.orderCode}">
						<div style="margin: 0 auto;" class="barcode line-height-big">
							<h3>//ORDER ID //</h3>
							<img src='<c:url value="/Barcode/generate/${inventory.orderCode}"/>'
								alt="orderCode" />
								<h2>${inventory.orderCode}</h2>
						</div>
					</c:if>
					<!-- <div class="reason" style="margin: 0 auto;">
						<h2>Physical Status:Product Received in OK condition and unused</h2>
					</div> -->
					<table style="margin-top: 5px;" class="itemTable table-border">
						<thead>
							<tr>
								<th class="table-border">S.No</th>
								<th class="table-border">SKU</th>
								<th class="table-border">Suborder Code</th>
								<th class="table-border">Price</th>
								<th class="table-border">Product Name</th>
								<%--<th class="table-border">Issue Category</th>
								<th class="table-border">QC Remarks</th> --%>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="table-border">${j.index + 1}</td>
								<td class="table-border">${inventory.sku}</td>
								<td class="table-border">${inventory.suborderCode}</td>
								<td class="table-border">${inventory.price}</td>
								<td class="table-border">${fn:substring(inventory.productName,
									0, 50)}..</td>
								<%--<td class="table-border">${inventory.issueCategory}</td>
								<td class="table-border">${inventory.qcRemarks}</td> --%>
							</tr>
						</tbody>
					</table>
				</c:forEach>
			</div>
			<%--<c:forEach var="i" begin="1" end="3"> --%>
			<%--<c:forEach var="i" begin="1" end="1">
				<c:choose>
					<c:when test="${i eq 1}">
						<c:set var="copyType" value="Vendor Copy"></c:set>
					</c:when>
					<c:when test="${i eq 2}">
						<c:set var="copyType" value="Rider Copy"></c:set>
					</c:when>
					<c:otherwise>
						<c:set var="copyType" value="Warehouse Copy"></c:set>
					</c:otherwise>
				</c:choose>
				<div class="pageDiv"
					style="page-break-before: always; width: 100%; padding-top: 20px">
					<div class="page-container">
						<div class="watermark">${copyType}</div>
						<table class="itemTable table-border" style="padding-top: 20px;">
							<tr>
								<td colspan="5" class="tableHeader table-border"
									style="text-align: center; border: none;">
									<div class="typeOfCopy">${copyType}</div>
								</td>
							</tr>
							<tr>
								<td colspan="2" class="table-border">RTV Sheet Code:
									RTVL-${rtvSheet.id}</td>
								<td colspan="3" class="table-border"><div
										style="text-align: center">
										<img
											src='<c:url value="/Barcode/generate/RTVL-${rtvSheet.id}"/>'>
									</div></td>
							</tr>
							<tr>
								<td colspan="2" class="table-border">RTV Sheet Date:</td>
								<td colspan="3" class="table-border"><fmt:formatDate
										value="${rtvSheet.updated}" type="both" dateStyle="long"
										timeStyle="medium" />
								</td>
							</tr>
							<tr>
								<td colspan="2" class="table-border">Receiver Address:
									<div class="vendorAddressDetails">
										<p></p>
										<p>
											${rtvSheet.receiverDetail.name}<br />
											${rtvSheet.receiverDetail.addressLine1}<br />
											${rtvSheet.receiverDetail.addressLine2}<br />
											${rtvSheet.receiverDetail.city},
											${rtvSheet.receiverDetail.state}, India<br />
											${rtvSheet.receiverDetail.pincode} <br />
										</p>
									</div>
								</td>
								<td colspan="3" class="table-border">Warehouse Center
									Address:
									<div class="oneShipAddress">
										<p></p>
										<p>
											${rtvSheet.warehouseDetails.name}<br />
											${rtvSheet.warehouseDetails.address.addressLine1}<br />
											${rtvSheet.warehouseDetails.address.addressLine2}<br />
											${rtvSheet.warehouseDetails.address.city},
											${rtvSheet.warehouseDetails.address.state}, India<br />
											${rtvSheet.warehouseDetails.address.pincode} <br />
										</p>
									</div>
								</td>
						</table>
					</div>
				</div>
			</c:forEach> --%>
	</div>
	</c:forEach>
	</div>
</body>
</html>