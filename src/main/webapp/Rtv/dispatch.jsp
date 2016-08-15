<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--<c:set var="currURI" value="${requestScope['javax.servlet.forward.request_uri']}"/> --%>
<tags:page title="RTV">
	<jsp:attribute name="script">
		<script type="text/javascript">
			$(document).ready(function() {
				$(".form").validate();
				$(".override").click(function(){
					var index = $(".override").index(this);
					$("input[name='rtvSheets[" + index + "].courierCode']").attr("readonly",false);
					$("input[name='rtvSheets[" + index + "].awbNumber']").attr("readonly",false);
				});
			});
		</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
			<div class="box-header" style="padding-left: 10px; padding-top: 10px">Packages
				to Dispatch</div>
			<div class="box-content">
				<div class="row">
					<form action='<c:url value="/Rtv/dispatch" />' method="post"
						name="rtvDispatch" class="form" target="_blank">
						<input type="hidden" name="returnTo" id="returnTo" value="${returnTo}"/>
						<div class="col-lg-12"
							style="margin-top: 20px; margin-bottom: 20px;">
						<c:if test="${not empty returnType}">
								<div class="alert alert-success">
									<button type="button" class="close" data-dismiss="alert">×</button>
			  							Dispatching To : ${returnType}
								</div>
						</c:if>
							
							<table class="table table-striped table-bordered" id="mainTable">
								<thead>
									<tr>
										<th>Seller Name</th>
										<th>Package Details</th>
										<th>Courier Code</th>
										<th>Tracking Number</th>
										<%--<c:if test="${fn:contains(currURI, 'manifest')}"> --%>
											<th>Action</th>
										<%--</c:if> --%>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="sheet" items="${rtvSheets}" varStatus="i">
										<tr id="row-${i.index}">
											<td style="vertical-align: middle; text-align: center;">
												${sheet.sellerName} <input type="hidden"
												name="rtvSheets[${i.index}].id" value="${sheet.id}"/></td>
											<td>
												<table class="table table-striped table-bordered">
													<thead>
														<tr>
															<th>Barcode</th>
															<th>Product</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach var="inventory" items="${sheet.productDetails}">
															<tr>
																<td width="20%">${inventory.barcode}</td>
																<td width="80%">${inventory.productName}</td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</td>
											<td style="vertical-align: middle;"><c:choose>
													<c:when test="${empty sheet.courierCode}">
														<input type="text"
															name="rtvSheets[${i.index}].courierCode"
															class="form-control required" />
													</c:when>
													<c:otherwise>
														<input type="text"
															name="rtvSheets[${i.index}].courierCode"
															class="form-control required" readonly="readonly"
															value="${sheet.courierCode}" />
													</c:otherwise>
												</c:choose></td>
											<td style="vertical-align: middle;"><c:choose>
													<c:when test="${empty sheet.awbNumber}">
														<input type="text" name="rtvSheets[${i.index}].awbNumber"
															class="form-control required" />
													</c:when>
													<c:otherwise>
														<input type="text" name="rtvSheets[${i.index}].awbNumber"
															class="form-control required" readonly="readonly"
															value="${sheet.awbNumber}" />
													</c:otherwise>
												</c:choose>
												<div class="hide">
													<input type="text"
														name="rtvSheets[${i.index}].receiverDetail.id"
														value="${sheet.receiverDetail.id}"
														readonly="readonly">
													<input type="text"
														name="rtvSheets[${i.index}].receiverDetail.vendorCode"
														value="${sheet.receiverDetail.vendorCode}"
														readonly="readonly"> 
													<input type="text"
														name="rtvSheets[${i.index}].receiverDetail.name"
														value="${sheet.receiverDetail.name}" readonly="readonly">
													<input type="text"
														name="rtvSheets[${i.index}].receiverDetail.email"
														value="${sheet.receiverDetail.email}" readonly="readonly">
													<input type="text"
														name="rtvSheets[${i.index}].receiverDetail.addressLine1"
														value="${sheet.receiverDetail.addressLine1}"
														readonly="readonly"> <input type="text"
														name="rtvSheets[${i.index}].receiverDetail.addressLine2"
														value="${sheet.receiverDetail.addressLine2}"
														readonly="readonly"> <input type="text"
														name="rtvSheets[${i.index}].receiverDetail.city"
														value="${sheet.receiverDetail.city}" readonly="readonly">
													<input type="text"
														name="rtvSheets[${i.index}].receiverDetail.state"
														value="${sheet.receiverDetail.state}" readonly="readonly">
													<input type="text"
														name="rtvSheets[${i.index}].receiverDetail.pincode"
														value="${sheet.receiverDetail.pincode}"
														readonly="readonly"> <input type="text"
														name="rtvSheets[${i.index}].receiverDetail.contactNumber"
														value="${sheet.receiverDetail.contactNumber}"
														readonly="readonly">
												</div>
											</td>
											<%--<c:if test="${fn:contains(currURI, 'manifest')}"> --%>
												<td style="vertical-align: middle;">
													<input type="button" class="btn btn-danger override" value="Override" />
												</td>
											<%--</c:if> --%>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="col-lg-12"
							style="margin-top: 20px; text-align: center;">
							<input type="submit" class="btn btn-primary" value="Dispatch"/>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</tags:page>