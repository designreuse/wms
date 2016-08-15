<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<tags:page title="Rule">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#form").validate();
						var editMode = $("#editMode").val();
						var savedOperator = $("#operator").val();
						if (savedOperator != "") {
							if (editMode == "true"
									&& (savedOperator == "BETWEEN" || savedOperator == "NOT BETWEEN")) {
								$("#price_after").show();
								$("#from").text("From :");
								$("#price_single").addClass("required");
								$("#price_double").addClass("required");
							} else {
								$("#price_after").hide();
								$("#price_single").addClass("required");
							}
						}
						var savedManifestOperator = $("#manifest_operator")
								.val();
						if (savedManifestOperator != "") {
							$("#manifestValue").addClass("required");
						}
						if ($("input.check:checkbox:checked").length == 1) {
							$("input.check:checkbox").each(function() {
								if (!$(this).attr("checked")) {
									$(this).attr("disabled", "disabled");
								}
							});
						}
						$("form")
								.submit(
										function(e) {
											$("#form").validate();
											if ($("#group_name").hasClass(
													"myerror")
													|| $("#rule_name")
															.hasClass("myerror")) {
												e.preventDefault();
												noty({
													text : "Duplicate Rule Name or Group Name. Please correct and proceed.",
													type : "error",
													layout : "topRight"
												});
											}
										});
						$(".check")
								.on(
										"change",
										function() {
											if ($(this).attr("checked")) {
												$notSelected = $(this)
														.parents(
																"div#warehouseDiv")
														.find(
																"div input:checkbox:not(:checked)");
												$notSelected.attr("disabled",
														"disabled");
											} else {
												$(".check").removeAttr(
														"disabled");
											}
										});
						$("#model")
								.on(
										"change",
										function() {
											if ($(this).find("option:checked").length == 1) {
												if ($(this).find(
														"option:checked")
														.text().trim() == "FC_VOI") {
													$("#warehouseDiv").show();
													$("#rtvDiv").hide();
													$("#rtvDiv").find(
															"input:checkbox")
															.removeAttr(
																	"checked");
													$("#rtvDiv").find(
															"input:checkbox")
															.attr("value",
																	"false");
												} else {
													$("#rtvDiv").show();
													$("#warehouseDiv").hide();
													$("#warehouseDiv").find(
															"input:checkbox")
															.removeAttr(
																	"checked");
													$("#warehouseDiv").find(
															"input:checkbox")
															.attr("value",
																	"false");
												}
											} else {
												$("#rtvDiv").show();
												$("#warehouseDiv").hide();
												$("#warehouseDiv").find(
														"input:checkbox")
														.removeAttr("checked");
												$("#warehouseDiv").find(
														"input:checkbox").attr(
														"value", "false");
											}
										});

						$("#rule_name")
								.on(
										"change",
										function() {
											var rule_name = $(this).val();
											var request = $.ajax({
												type : "POST",
												url : "/Wms/Rule/checkRule",
												data : {
													name : rule_name
												}
											});
											request
													.done(function(msg) {
														if (msg == "failure") {
															$("#rule_name")
																	.addClass(
																			"myerror");
															noty({
																text : "Rule Name already exists. Please Select a different name.",
																type : "error",
																layout : "topRight"
															});
														} else {
															$("#rule_name")
																	.removeClass(
																			"myerror");
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
																			"myerror");
															noty({
																text : "Group Name already exists. Please Select a different name.",
																type : "error",
																layout : "topRight"
															});
														} else {
															$("#group_name")
																	.removeClass(
																			"myerror");
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
						$("#manifest_operator").on("change", function() {
							var operator = $(this).val();
							if (operator != "") {
								$("#manifestValue").addClass("required");
							} else {
								$("#manifestValue").removeClass("required");
							}
						});
						if ($("#model").find("option:checked").length == 1
								&& $("#model").find("option:checked").text()
										.trim() == "FC_VOI") {
							$("#warehouseDiv").show();
							$("#rtvDiv").hide();
							$("#rtvDiv").find("input:checkbox").removeAttr(
									"checked");
						} else {
							$("#warehouseDiv").hide();
							$("#warehouseDiv").find("input:checkbox")
									.removeAttr("checked");
							$("#rtvDiv").show();
						}
						
						$(".checklist").on('click',function (){
// 							alert("here");
							var $selected = $(this);
							if ($selected.attr("checked")) {
							    $(".checklist").attr("checked", false);
							    $selected.attr("checked", "true");
							  } else {
							    $selected.attr("checked", false);
							  }
						});
					});

	function enablePriceBox() {
		var operator = $("#operator").val();
		if (operator != "") {
			if (operator == "BETWEEN" || operator == "NOT BETWEEN") {
				$("#price_after").show();
				$("#price_double").attr("value", "");
				$("#from").text("From :");
				$("#price_single").addClass("required");
				$("#price_double").addClass("required");
			} else {
				$("#from").text("Amount :");
				$("#price_after").hide();
				$("#price_single").addClass("required");
				$("#price_double").removeClass("required");
			}
		} else {
			$("#price_single").removeClass("required");
			$("#price_double").removeClass("required");
		}

	}
	function createPrice() {
		var final_operator = $("#operator").val();
		var final_value = "";
		if (final_operator == "BETWEEN" || final_operator == "NOT BETWEEN") {
			final_value = $("#price_single").val();
			final_value = final_value.concat(" AND ");
			final_value = final_value.concat($("#price_double").val());
		} else {
			final_value = $("#price_single").val();
		}
		$("#price_operator").attr("value", final_operator);
		$("#price_value").attr("value", final_value);
	}
	

//		$("input:checkbox").on('click',function(){alert("here");
// 	 var group = ":checkbox[name='"+ $(this).attr("name") + "']";
// 	   if($(this).is(':checked')){
// 	     $(group).not($(this)).attr("checked",false);
// 	   }
// 		$('input:checkbox').not(this).prop('checked', false);
//	 });
//
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Rule Management</div>
			<div class="box-content">
				<div class="row">
					<c:set var="action" value="/Wms/Rule/save"></c:set>
					<c:if test="${edit}">
						<c:set var="action" value="/Wms/Rule/update"></c:set>
					</c:if>
					<input type="hidden" id="editMode" value="${edit}" />
					<form name="rule" action="${action}" method="post" id="form">
						<input type="hidden" name="id" value="${rule.id}">
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Rule Name : </label> <input type="text" name="name"
									class="form-control required" placeholder="Enter Rule Name"
									value="${rule.name}" id="rule_name">
							</div>
							<div class="col-lg-4">
								<label>CC Status : </label> <select name="ccStatus[]"
									class="form-control required" multiple="multiple"
									data-rel="chosen">
									<div class="chosen-container-multi chosen-container">
										<c:forEach var="status" items="${ccstatus}">
											<c:set var="found" value="false" />
											<c:forEach var="savedStatus" items="${rule.ccStatusList}">
												<c:if test="${status.id eq savedStatus.id}">
													<option value="${status.id}" selected="selected">${status.code}
													</option>
													<c:set var="found" value="true" />
												</c:if>
											</c:forEach>
											<c:if test="${not found}">
												<option value="${status.id}">${status.code}</option>
											</c:if>
										</c:forEach>
									</div>
								</select>
							</div>
							<div class="col-lg-4">
								<label>Fulfillment Models : </label> <select
									name="fulfillmentModel[]" class="form-control"
									multiple="multiple" data-rel="chosen" id="model">
									<div class="chosen-container-multi chosen-container">
										<c:forEach var="model" items="${models}">
											<c:set var="found" value="false" />
											<c:forEach var="savedModel"
												items="${rule.fulfillmentModelList}">
												<c:if test="${model.id eq savedModel.id}">
													<option value="${model.id}" selected="selected">${model.name}
													</option>
													<c:set var="found" value="true" />
												</c:if>
											</c:forEach>
											<c:if test="${not found}">
												<option value="${model.id}">${model.name}</option>
											</c:if>
										</c:forEach>
									</div>
								</select>
							</div>
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Issue Category : </label> <select name="issueCategory[]"
									class="form-control" multiple="multiple" data-rel="chosen">
									<div class="chosen-container-multi chosen-container">
										<c:forEach var="issue" items="${issuecategory}">
											<c:set var="found" value="false" />
											<c:forEach var="savedIssue" items="${rule.issueCategoryList}">
												<c:if test="${issue.id eq savedIssue.id}">
													<option value="${issue.id}" selected="selected">${issue.code}</option>
													<c:set var="found" value="true" />
												</c:if>
											</c:forEach>
											<c:if test="${not found}">
												<option value="${issue.id}">${issue.code}</option>
											</c:if>
										</c:forEach>
									</div>
								</select>
							</div>
							<div class="col-lg-4">
								<label>Sub Category : </label> <select name="subcategory[]"
									class="form-control" multiple="multiple" data-rel="chosen">
									<div class="chosen-container-multi chosen-container">
										<c:forEach var="subcategory" items="${subcategory}">
											<c:set var="found" value="false" />
											<c:forEach var="savedSubcategory"
												items="${rule.subCategoryList}">
												<c:if test="${subcategory.id eq savedSubcategory.id}">
													<option value="${subcategory.id}" selected="selected">${subcategory.url}</option>
													<c:set var="found" value="true" />
												</c:if>
											</c:forEach>
											<c:if test="${not found}">
												<option value="${subcategory.id}">${subcategory.url}</option>
											</c:if>
										</c:forEach>
									</div>
								</select>
							</div>
<!-- 							<div class="col-lg-4" style="margin-top: 20px;"> -->
<%-- 								<c:choose> --%>
<%-- 									<c:when test="${rule.liquidation}"> --%>
<!-- 										<input type="checkbox" name="liquidation" value="true" -->
<!-- 											checked="checked" /> -->
<!-- 										<label>Eligible for Liquidation </label> -->
<%-- 									</c:when> --%>
<%-- 									<c:otherwise> --%>
<!-- 										<input type="checkbox" name="liquidation" value="true" /> -->
<!-- 										<label>Eligible for Liquidation </label> -->
<%-- 									</c:otherwise> --%>
<%-- 								</c:choose> --%>
<!-- 							</div> -->
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>QC Remarks :</label> <input type="text" name="qcRemarks"
									class="form-control" value="${rule.qcRemarks}"
									placeholder="Enter QC Remarks">
							</div>
							<div class="col-lg-2">
								<label>Operator :</label> <select id="manifest_operator"
									class="form-control" name="manifestOperator">
									<option value="">Please Select</option>
									<c:choose>
										<c:when test="${rule.manifestOperator eq '<'}">
											<option selected="selected" value="<">Less Than</option>
										</c:when>
										<c:otherwise>
											<option value="<">Less Than</option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${rule.manifestOperator eq '>'}">
											<option selected="selected" value=">">Greater Than</option>
										</c:when>
										<c:otherwise>
											<option value=">">Greater Than</option>
										</c:otherwise>
									</c:choose>
								</select>
							</div>
							<div class="col-lg-2">
								<label>Manifest days :</label> <input type="text"
									name="daysPassedManifest" class="form-control digits"
									value="${rule.daysPassedManifest}" id="manifestValue"
									placeholder="Manifest Days">
							</div>
							<div class="col-lg-4">
								<label>Priority :</label> <input type="text" name="priority"
									class="form-control required digits" value="${rule.priority}"
									placeholder="Enter Priority">
							</div>
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-2">
								<label>Price : </label> <select id="operator"
									class="form-control" onchange="enablePriceBox()">
									<c:set var="priceValue"
										value="${fn:split(rule.price.value, 'AND')}" />
									<option value="">Please Select</option>
									<c:choose>
										<c:when test="${rule.price.operator eq '<'}">
											<option selected="selected" value="<">Less Than</option>
										</c:when>
										<c:otherwise>
											<option value="<">Less Than</option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${rule.price.operator eq '>'}">
											<option selected="selected" value=">">Greater Than</option>
										</c:when>
										<c:otherwise>
											<option value=">">Greater Than</option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${rule.price.operator eq 'BETWEEN'}">
											<option selected="selected" value="BETWEEN">Between</option>
										</c:when>
										<c:otherwise>
											<option value="BETWEEN">Between</option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${rule.price.operator eq 'NOT BETWEEN'}">
											<option selected="selected" value="NOT BETWEEN">Not
												Between</option>
										</c:when>
										<c:otherwise>
											<option value="NOT BETWEEN">Not Between</option>
										</c:otherwise>
									</c:choose>
								</select>
							</div>
							<div class="col-lg-2">
								<label id="from">Amount :</label> <input type="text"
									id="price_single" class="form-control digits"
									value="${priceValue[0]}" placeholder="Enter Amount">
							</div>
							<div id="price_after" class="col-lg-2">
								<label>To :</label> <input type="text"
									class="form-control digits" id="price_double"
									value="${priceValue[1]}" placeholder="Enter Amount">
							</div>
							<input type="hidden" name="price.operator" id="price_operator" />
							<input type="hidden" name="price.value" id="price_value" />
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<c:choose>
									<c:when test="${edit}">
										<label>Group Name : </label>
										<input type="text" name="group.name"
											class="form-control required" placeholder="Enter group name"
											value="${rule.group.name}" readonly="readonly">
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
											class="form-control required digits"
											placeholder="Total shelves"
											value="${rule.group.numberOfShelves}" readonly="readonly">
									</c:when>
									<c:otherwise>
										<label>Number of Shelves : </label>
										<input type="text" name="group.numberOfShelves"
											class="form-control required digits"
											placeholder="Total shelves">
									</c:otherwise>
								</c:choose>
							</div>
							<div class="col-lg-4">
								<c:choose>
									<c:when test="${edit}">
										<label>Number of Floors per Shelf : </label>
										<input type="text" name="group.numberOfFloors"
											class="form-control required digits"
											placeholder="Total shelves" readonly="readonly"
											value="${rule.group.numberOfFloors}">
									</c:when>
									<c:otherwise>
										<label>Number of Floors per Shelf : </label>
										<input type="text" name="group.numberOfFloors"
											class="form-control required digits"
											placeholder="Total shelves">
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<c:choose>
									<c:when test="${edit}">
										<label>Capacity of each floor : </label>
										<input type="text" name="group.capacityOfFloors"
											class="form-control required digits"
											placeholder="Total shelves"
											value="${rule.group.capacityOfFloors}" readonly="readonly">
									</c:when>
									<c:otherwise>
										<label>Capacity of each floor : </label>
										<input type="text" name="group.capacityOfFloors"
											class="form-control required digits"
											placeholder="Total shelves">
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
											value="${rule.group.capacityOfBox}" readonly="readonly">
									</c:when>
									<c:otherwise>
										<label>Capacity of each box : </label>
										<input type="text" name="group.capacityOfBox"
											class="form-control required digits"
											placeholder="Total capacity">
									</c:otherwise>
								</c:choose>
							</div>
							<div class="col-lg-4" style="margin-top: 25px" id="rtvDiv">
								<c:choose>
									<c:when test="${rule.rtv}">
										<input type="checkbox" name="rtv" value="true" class="checklist"
											checked="checked" />
										<label>Eligible for RTV </label>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="rtv" value="true" class="checklist"/>
										<label>Eligible for RTV </label>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="col-lg-4" style="margin-top: 15px" id="warehouseDiv">
								<div>
									<c:choose>
										<c:when test="${rule.warehouseFlag}">
											<input type="checkbox" name="warehouseFlag" value="true"
												checked="checked" class="check checklist" />
											<label>Eligible for Warehouse </label>
										</c:when>
										<c:otherwise>
											<input type="checkbox" name="warehouseFlag" value="true"
												class="check checklist" />
											<label>Eligible for Warehouse </label>
										</c:otherwise>
									</c:choose>
								</div>
								
									
							</div>
							
						</div>
						<div class="col-lg-16" style="margin-top: 15px;padding-left: 15px" >
						<div class="col-sm-4" style="margin-top: 15px">
									<c:choose>
										<c:when test="${rule.rtc}">
											<input type="checkbox" name="rtc" value="true"
												 class="checklist" checked="checked"/>
											<label>Eligible for RTC </label>
										</c:when>
										<c:otherwise>
											<input type="checkbox" name="rtc" value="true" class="checklist" />
											<label>Eligible for RTC </label>
										</c:otherwise>
									</c:choose></div>
								
									<div class="col-sm-4" style="margin-top: 15px;">
									<c:choose>
										<c:when test="${rule.flag3pl}">
											<input type="checkbox" name="flag3pl" value="true"
												 class="checklist" checked="checked"/>
											<label>Eligible for 3PL </label>
										</c:when>
										<c:otherwise>
											<input type="checkbox" name="flag3pl" value="true" class="checklist" />
											<label>Eligible for 3PL </label>
										</c:otherwise>
									</c:choose>
									</div>
									<div class="col-sm-4" style="margin-top: 20px;">
								<c:choose>
									<c:when test="${rule.liquidation}">
										<input type="checkbox" name="liquidation" value="true"
											checked="checked" class="checklist"/>
										<label>Eligible for Liquidation </label>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="liquidation" value="true" class="checklist"/>
										<label>Eligible for Liquidation </label>
									</c:otherwise>
								</c:choose>
							</div>
								</div>
						<div class="col-lg-12"
							style="text-align: center; margin-top: 20px">
							<input type="submit" value="Save" class="btn btn-primary"
								onclick="createPrice()">
							<c:if test="${edit}">
								<a href="<c:url value="/Rule/editGroup/${rule.group.id}" />"
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