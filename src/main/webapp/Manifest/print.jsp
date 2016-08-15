<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Manifest</title>
	<script type="text/javascript">
		var loc = opener.location.pathname;
		var curPath = loc.substring(loc.lastIndexOf('/')+1, loc.length);
		if(curPath != 'searchManifest')
			opener.location.reload();
		window.print();
	</script>
	<style>
	body {		
		-webkit-print-color-adjust: exact; 
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
	
	.header{
		margin-bottom: 10px;
    	display: inline-block;
    	width: 100%
	}
	
	table {
		-fs-table-paginate: paginate;
		width: 100%;
		border-spacing: 0;
	}
	
	td {
		font: normal 10px; Arial , Helvetica, sans-serif;
		word-wrap: break-word;
		border: .3px #000 solid;
		padding: .5px;
		text-align: center;
	}
	
	th {
		font: bold 12px; Arial , Helvetica, sans-serif;
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
		font: bold 12px;
		Arial
		,
		Helvetica,
		sans-serif;
	}
	
	h1 {
		font-family: "Time New Roman";
		font-size: 18px;
	}
	
	h2 {
		font-family: "Time New Roman";
		font-size: 14px;
	}
	
	h3 {
		font-family: "Time New Roman";
		font-size: 12px;
	}
	
	p {
		font-family: "Times New Roman";
		font-size: 12px;
	}
	
	div.line-height-medium {
		border-bottom: 2px #000000 solid;
		padding-bottom: 4px;
	}
	
	div.line-height-big {
		border-bottom: 5px #000000 solid;
		padding-bottom: 4px;
	}
	
	.code {
        position:absolute;
	    font-size: 15px;
		border-radius: 4px;
		background-color: #111111;
		color: #ffffff;
		border: 1px solid #111111;
  		padding: 4px 15px;  
  		left: 30px;  
  		top: 20px;  
	}
		
	.right {
    	float: right;
	}
	
	.center {			    
		text-align: center;
		
	}
	
	.barcode {
		line-height: 0;
		text-align: center;
	}
	</style>
	</head>
	<body>
		<div style="width: 100%">
			<c:choose>
				<c:when test="${not empty message}">
					<div class="header">
						${message}
					</div>
				</c:when>
				<c:otherwise>
					<div class="header">		
					
						<c:if test="${not empty courierCode}">
							<span class="code">${courierCode}</span>
						</c:if>
						<div class="right">
						<img src='<c:url value="/Barcode/generate/${rtvSheetList[0].bag}"/>'
								alt="code" />
							<img src='<c:url value="/Barcode/generate/Manifest${id}"/>'
								alt="code" />
							
					</div>
						
						</div>
				<div align=center >
					<b>BAG${rtvSheetList[0].bag}</b>
				<div class="right">
						<b>Manifest${id}</b>
					</div>	
				</div>
				<div class="line-height-medium"></div>
					<div class="itemDetail" style="padding-top: 5px;">
						<table class="itemTable table-border">
							<thead>
								<tr>
									<th class="table-border">SNo</th>
									<th class="table-border">Suborder Code</th>
									<th class="table-border">RTVL no.</th>
									<th class="table-border">Product Name</th>
									<th class="table-border">Tracking No.</th>
									<th class="table-border">Seller Name</th>
									<th class="table-border">Bag</th>
								</tr>
							</thead>
							<tbody>
								<c:set var="count" value="0" scope="page" />
								<c:forEach var="rtvSheet" items="${rtvSheetList}" varStatus="i">
									<c:forEach var="inventory" items="${rtvSheet.productDetails}" varStatus="j">
										<c:set var="count" value="${count + 1}" scope="page"/>
										<tr>
											<td class="table-border">${count}</td>
											<td class="table-border">${inventory.suborderCode}</td>
											<td class="table-border">RTVL${rtvSheet.id}</td>
											<td class="table-border">${fn:substring(inventory.productName,
												0, 50)}..</td>
											<td class="table-border">${rtvSheet.awbNumber}</td>
											<td class="table-border">${inventory.sellerName}</td>
											<td class="table-border">${rtvSheet.bag}</td>
										</tr>
									</c:forEach>
								</c:forEach>
							</tbody>
						</table>
					</div>			
				</c:otherwise>
			</c:choose>
		</div>
	</body>
</html>