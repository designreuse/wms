<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<tags:page title="Returns">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(function() {
		$("#form").validate();
		if ($("#message").val() != "" && $("#success").val() != "") {
			noty({
				text : $("#message").val(),
				type : "success",
				layout : "topRight"
			});
		}
		else if($("#message").val() != "")
		{
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
				Search Returns Sheet</div>
			<div class="box-content">
				<div class="row" style="margin-bottom: 20px;margin-top: 20px">
					<input type="hidden" id="message" value="${message}" /> 
					<input type="hidden" id="success" value="${success}" />
					<form action='<c:url value="/Rtv/searchByCRI" />' method="post" id="form">
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>CRICode : </label> 
								<input type="text" name="barcode" class="form-control required" id="barcode" />
							</div>
							<div class="col-lg-4" style="text-align: center;">
								<input type="submit" class="btn btn-primary" value="Search" style="margin-top: 25px" id="search" />
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<c:if test="${fn:length(list) gt 0}">
			<div class="box">
				<div class="box-content">
					<div class="row" style="margin-top: 20px; margin-bottom: 20px">
						<div class="col-lg-12">
							<table class="table table-striped table-bordered">
								<thead>
									<tr>
										<th>Returns Sheet Code</th>
										<th>Seller Name</th>
										<th>Created On</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${list}" varStatus="i">
										<tr>
											<td>RTVL-${item.id}</td>
											<td>${item.sellerName}</td>
											<td><fmt:formatDate value="${item.created}" type="both"
													dateStyle="long" timeStyle="medium" /></td>
											<td><a
												href='<c:url value="/Rtv/displaySheet/${item.id}"/>'
												class="btn btn-primary">Details</a></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</c:if>
	</div>
	</jsp:body>
</tags:page>