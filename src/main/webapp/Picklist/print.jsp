<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Picklist</title>
<script type="text/javascript">
	window.print();
</script>
<style>
.table-border {
	border: 1px solid black;
	border-collapse: collapse;
}
</style>
</head>
<body>
	<div style="width: 100%; margin-top: 20px" align="center">
		<h2>Picklist Identifier : ${picklist.id}</h2>
		<img src='<c:url value="/Barcode/generate/${picklist.id}"/>'>
		<table style="margin-top: 20px;" class="table-border">
			<thead>
				<tr style="text-align: left;">
					<th style="padding: 3px" class="table-border">Barcode</th>
					<th style="padding: 3px" class="table-border">Seller Name</th>
					<th style="padding: 3px" class="table-border">Seller Code</th>
					<th width="35%" style="padding: 3px" class="table-border">Product
						Name</th>
					<th style="padding: 3px" class="table-border">Location</th>
					<th style="padding: 3px" class="table-border">Group Name</th>
				</tr>
			</thead>
			<tbody style="font-size: 0.8em;">
				<c:forEach var="inventory" items="${picklist.inventoryList}">
					<tr>
						<td style="padding: 3px" class="table-border">${inventory.barcode}</td>
						<td style="padding: 3px" class="table-border">${inventory.sellerName}</td>
						<td style="padding: 3px" class="table-border">${inventory.vendorCode}</td>
						<td width="35%" style="padding: 3px" class="table-border">${inventory.productName}</td>
						<td style="padding: 3px" class="table-border">${inventory.location}</td>
						<td style="padding: 3px" class="table-border">${inventory.groupName}</td>
				</c:forEach>
			</tbody>
		</table>
		<table style="margin-top: 50px" width="100%">
			<tr>
				<td width="50%" align="left">Warehouse Boy :&nbsp;&nbsp;${picklist.warehouseBoy.name}</td>
				<td width="50%" align="right">Date :&nbsp;&nbsp;<fmt:formatDate
						value="${date}" type="both" dateStyle="long" timeStyle="medium" />
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
