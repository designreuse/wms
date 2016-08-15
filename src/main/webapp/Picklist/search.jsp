<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<tags:page title="Picklist">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(function() {
		$('.date-picker').datepicker();
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

	function printList(index) {
		var boyId = $("#boyId"+index).val();
		var listId = $("#listId"+index).val();
		$("#putawayListId").attr("value", listId);
		if (boyId != "") {
			$("#boySelect")
					.html($("<option />").val(boyId).prop('selected', true));
			$("#assignForm").submit();
		} else {
			var request = $.ajax({
				type : "GET",
				url : "/Wms/WarehouseBoy/getBoyList"
			});
			request.done(function(data) {
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
	}
	
	function completePrint() {
		if($("#boySelect").val() != "")
		{
			$("#assignModal").modal('hide');
		}
	}
	
	function formatDate()
	{
		var date = $("#endDate").val();
		if(date != "")
		{
			var dateTime = date.concat(" 23:59:59");
			$("#endDate").val(dateTime);	
		}
	}
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
		<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Search Picklist</div>
			<div class="box-content">
			<input type="hidden" id="message" value="${message}" />
			<input type="hidden" id="success" value="${success}" />
				<div class="row" style="margin-bottom: 20px;margin-top: 20px">
					<form action='<c:url value="/Picklist/searchList"/>' name="search"
							method="post" id="form">
						<div class="col-lg-12">
							<div class="col-lg-4">
								<label>Start Date : </label>
								<div class="input-group date col-lg-12">
									<span class="input-group-addon"><i
											class="fa fa-calendar"></i> </span> <input type="text"
											class="form-control date-picker required" name="startDate"
											placeholder="Select start date">
								</div>
							</div>
							<div class="col-lg-4">
								<label>End Date : </label>
								<div class="input-group date col-lg-12">
									<span class="input-group-addon"><i
											class="fa fa-calendar"></i> </span> <input type="text"
											class="form-control date-picker required" name="endDate"
											placeholder="Select end date" id="endDate">
								</div>
							</div>
							<div class="col-lg-4"
									style="margin-top: 25px; text-align: center;">
								<input type="submit" value="Search" class="btn btn-primary"
										onclick="formatDate()" />
							</div>
						</div>
					</form>
					</div>
				</div>
			</div>
					<c:if test="${fn:length(list) gt 0}">
					<div class="box">
					<div class="box-content">
					<div class="row">
						<div class="col-lg-12"
								style="margin-top: 20px; margin-bottom: 20px">
							<table class="table table-striped table-bordered">
								<thead>
									<tr>
										<th>Picklist Identifier</th>
										<th>Warehouse Boy</th>
										<th>Created On</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${list}" varStatus="i">
										<tr>
											<td><input type="hidden" value="${item.id}"
													id="listId${i.index}" />${item.id}</td>
											<td><c:choose>
													<c:when test="${not empty item.warehouseBoy.name}">${item.warehouseBoy.name}</c:when>
													<c:otherwise>NA</c:otherwise>
												</c:choose> <input type="hidden" value="${item.warehouseBoy.id}"
													id="boyId${i.index}" />
												</td>
											<td><fmt:formatDate value="${item.created}" type="both"
														dateStyle="long" timeStyle="medium" />
												</td>
											<td><a onclick="printList(${i.index})"
													class="btn btn-primary">Print</a>
												</td>
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
	<div class="modal fade" id="assignModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="assignForm" action='<c:url value="/Picklist/printList" />'
						target="_blank" method="post">
					<div class="modal-header">
						<h4 class="modal-title">Assign Warehouse Boy</h4>
					</div>
					<div class="modal-body">
						<input type="hidden" name="listId" value="${listId}"
								id="putawayListId" />
						<div class="row">
							<div class="col-lg-6">
								<label>Warehouse Boy :</label> <select name="warehouseBoy"
										class="form-control required" id="boySelect">
								</select>
							</div>
							<div class="col-lg-6"
									style="text-align: center; margin-top: 28px">
								<input type="submit" class="btn btn-primary" value="Proceed"
										onclick="completePrint()" />
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