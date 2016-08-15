<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<tags:page title="Search">
	<jsp:attribute name="script">
<script type="text/javascript">
$(document).ready(function(){
	$("#box").hide();
})
function getHistory(code)
{
	var request = $.ajax({
		type : "GET",
		url : "/Wms/Search/history",
		data : {
			barcode : code
		}
	});
	request.done(function(data) {
			if(data != "")
			{
				$("#box").show();
				$("#historyBody").html("");
				$.each(data, function() {
					var row = $("#dummyRow").clone();
					row.find("td#barcode").text(this.barcode);
					row.find("td#action").text(this.action);
					row.find("td#date").text(this.date);
					$("#historyBody").append(row);
				});
			}else{
				$("#box").hide();
			}
	});
}
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Search Results</div>
			<div class="box-content">
				<div class="row" style="margin-top: 20px; margin-bottom: 20px">
					<c:choose>
						<c:when test="${not empty message}">
							<div class="col-lg-12">
								<h2>${message}</h2>
							</div>
						</c:when>
						<c:otherwise>
							<div class="col-lg-12">
								<table class="table table-striped table-bordered">
									<thead>
										<tr>
											<th>Barcode</th>
											<th>Seller</th>
											<th>Location</th>
											<th>Status</th>
											<th>Last Updated</th>
											<th style="text-align: center;">Action</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="inventory" items="${list}">
											<tr>
												<td>${inventory.barcode}</td>
												<td>${inventory.sellerName}</td>
												<td>${inventory.location}</td>
												<td>${inventory.status}</td>
												<td><fmt:formatDate value="${inventory.updated}"
														type="both" dateStyle="long" timeStyle="medium" />
												</td>
												<td style="text-align: center;"><a onclick="getHistory('${inventory.barcode}')"
													class="btn btn-primary">History</a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<div class="box" id="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				History</div>
			<div class="box-content">
				<div class="row" style="margin-top: 20px; margin-bottom: 20px">
					<div class="col-lg-12">
						<table class="table table-striped table-bordered">
							<thead>
								<tr>
									<th>Barcode</th>
									<th>Action</th>
									<th>Action Date</th>
								</tr>
							</thead>
							<tbody id="historyBody">

							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<table class="hide">
			<tr id="dummyRow">
				<td id="barcode"></td>
				<td id="action"></td>
				<td id="date">
				</td>
			</tr>
		</table>
	</div>
	</jsp:body>
</tags:page>
