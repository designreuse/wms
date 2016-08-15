<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<tags:page title="Bulk Rule">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("form")
								.submit(
										function(e) {
											$("#form").validate();
											if ($("#group_name").hasClass(
													"error")
													|| $("#rule_name")
															.hasClass("error")) {
												e.preventDefault();
												noty({
													text : "Duplicate Rule Name or Group Name. Please correct and proceed.",
													type : "error",
													layout : "topRight"
												});
											}
										})
						$("#rule_name")
								.on(
										"change",
										function() {
											var rule_name = $(this).val();
											var request = $
													.ajax({
														type : "POST",
														url : "/Wms/Rule/checkBulkRule",
														data : {
															name : rule_name
														}
													});
											request
													.done(function(msg) {
														if (msg == "failure") {
															$("#rule_name")
																	.addClass(
																			"error");
															noty({
																text : "Rule Name already exists. Please Select a different name.",
																type : "error",
																layout : "topRight"
															});
														} else {
															$("#rule_name")
																	.removeClass(
																			"error");
														}
													});
											request
													.fail(function(jqXHR,
															textStatus) {
														noty({
															text : "Failed to check Rule Name.",
															type : "error",
															layout : "topRight"
														});
													});
										});
						$("#group_name")
								.on(
										"change",
										function() {
											var group_name = $(this).val();
											var request = $.ajax({
												type : "POST",
												url : "/Wms/Rule/checkGroup",
												data : {
													name : group_name
												}
											});
											request
													.done(function(msg) {
														if (msg == "failure") {
															$("#group_name")
																	.addClass(
																			"error");
															noty({
																text : "Group Name already exists. Please Select a different name.",
																type : "error",
																layout : "topRight"
															});
														} else {
															$("#group_name")
																	.removeClass(
																			"error");
														}
													});
											request
													.fail(function(jqXHR,
															textStatus) {
														noty({
															text : "Failed to check Group Name.",
															type : "error",
															layout : "topRight"
														});
													});
										});
						$(".checklist").on('click',function (){
							var $selected = $(this);
							if ($selected.attr("checked")) {
							    $(".checklist").attr("checked", false);
							    $selected.attr("checked", "true");
							  } else {
							    $selected.attr("checked", false);
							  }
						});
					});

</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
		<div class="box-header" style="padding-top: 10px; padding-left: 10px">
		Bulk Rule Management
		</div>
			<div class="box-content">
				<!-- <h2 align="center">Rule</h2> -->
				<div class="row">
					<c:set var="action" value="/Wms/Rule/saveBulkRule"></c:set>
					<c:if test="${edit}">
						<c:set var="action" value="/Wms/Rule/updateBulkRule"></c:set>
					</c:if>
					<form name="bulkRule" action="${action}" method="post" id="form">
						<input type="hidden" name="id" value="${bulkRule.id}">
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Bulk Rule Name : </label> <input type="text" name="name"
										class="form-control required"
										placeholder="Enter Bulk Rule Name" value="${bulkRule.name}"
										id="rule_name">
							</div>
							<div class="col-lg-4">
								<c:choose>
									<c:when test="${edit}">
										<label>Group Name : </label>
										<input type="text" name="group.name"
												class="form-control required" placeholder="Enter group name"
												value="${bulkRule.group.name}" readonly="readonly">
									</c:when>
									<c:otherwise>
										<label>Group Name : </label>
										<input type="text" name="group.name"
												class="form-control required" placeholder="Enter group name"
												id="group_name">
									</c:otherwise>
								</c:choose>
							</div>
							<div class="col-lg-4">
								<c:choose>
									<c:when test="${edit}">
										<label>Number of Shelves : </label>
										<input type="text" name="group.numberOfShelves"
												class="form-control required" placeholder="Total shelves"
												value="${bulkRule.group.numberOfShelves}"
												readonly="readonly">
									</c:when>
									<c:otherwise>
										<label>Number of Shelves : </label>
										<input type="text" name="group.numberOfShelves"
												class="form-control required" placeholder="Total shelves">
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<c:choose>
									<c:when test="${edit}">
										<label>Number of Floors per Shelf : </label>
										<input type="text" name="group.numberOfFloors"
												class="form-control required" placeholder="Total shelves"
												readonly="readonly" value="${bulkRule.group.numberOfFloors}">
									</c:when>
									<c:otherwise>
										<label>Number of Floors per Shelf : </label>
										<input type="text" name="group.numberOfFloors"
												class="form-control required" placeholder="Total shelves">
									</c:otherwise>
								</c:choose>
							</div>
							<div class="col-lg-4">
								<c:choose>
									<c:when test="${edit}">
										<label>Capacity of each floor : </label>
										<input type="text" name="group.capacityOfFloors"
												class="form-control required" placeholder="Total shelves"
												value="${bulkRule.group.capacityOfFloors}"
												readonly="readonly">
									</c:when>
									<c:otherwise>
										<label>Capacity of each floor : </label>
										<input type="text" name="group.capacityOfFloors"
												class="form-control required" placeholder="Total shelves">
									</c:otherwise>
								</c:choose>
							</div>
							<div class="col-lg-4">
								<c:choose>
									<c:when test="${edit}">
										<label>Capacity of each box : </label>
										<input type="text" name="group.capacityOfBox"
												class="form-control required digits"
												placeholder="Total capacity"
												value="${bulkRule.group.capacityOfBox}" readonly="readonly">
									</c:when>
									<c:otherwise>
										<label>Capacity of each box : </label>
										<input type="text" name="group.capacityOfBox"
												class="form-control required digits"
												placeholder="Total capacity">
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-sm-2" style="margin-top: 25px">
								<c:choose>
									<c:when test="${bulkRule.rtv}">
										<input type="checkbox" name="rtv" value="true" checked class="checklist"/>
										<label>Eligible for RTV </label>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="rtv" value="true" class="checklist"/>
										<label>Eligible for RTV </label>
									</c:otherwise>
								</c:choose>
								</div>
								<div class="col-sm-2" style="margin-top: 25px">
								<c:choose>
									<c:when test="${bulkRule.rtc}">
										<input type="checkbox" name="rtc" value="true" checked class="checklist"/>
										<label>Eligible for RTC </label>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="rtc" value="true" class="checklist"/>
										<label>Eligible for RTC </label>
									</c:otherwise>
								</c:choose>
								</div>
								<div class="col-sm-3" style="margin-top: 25px">
								<c:choose>
									<c:when test="${bulkRule.liquidation}">
										<input type="checkbox" name="liquidation" value="true" checked class="checklist"/>
										<label>Eligible for Liquidation </label>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="liquidation" value="true" class="checklist"/>
										<label>Eligible for Liquidation </label>
									</c:otherwise>
								</c:choose>
								</div>
								<div class="col-sm-2" style="margin-top: 25px">
								<c:choose>
									<c:when test="${bulkRule.flag3pl}">
										<input type="checkbox" name="flag3pl" value="true" checked class="checklist"/>
										<label>Eligible for 3PL </label>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="flag3pl" value="true" class="checklist"/>
										<label>Eligible for 3PL </label>
									</c:otherwise>
								</c:choose>
								</div>
								<div class="col-sm-3" style="margin-top: 25px">
								<c:choose>
									<c:when test="${bulkRule.warehouseFlag}">
										<input type="checkbox" name="warehouseFlag" value="true" checked class="checklist"/>
										<label>Eligible for Warehouse </label>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="warehouseFlag" value="true" class="checklist" />
										<label>Eligible for Warehouse </label>
									</c:otherwise>
								</c:choose>
								</div>
							</div>
						
					
						
						
						<div class="col-lg-12"
								style="text-align: center; margin-top: 20px">
							<input type="submit" value="Save" class="btn btn-primary">
							<c:if test="${edit}">
								<a href="<c:url value="/Rule/editGroup/${bulkRule.group.id}" />"
										class="btn btn-primary" style="margin-left: 20px"> Edit
									Group Configuration </a>
							</c:if>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</tags:page>