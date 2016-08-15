<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="RTV">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(function() {
		$("#together").hide();
	});
	
	function join(element)
	{
		var vendorCode = "";
		var index = "";
		var checkBoxId = "";
		var details = "";
		var shippingMode = "";
		if($(".check:checked").length > 1)
		{
			if($(element).attr("checked"))
			{
				vendorCode = $(element).val();
				checkBoxId = $(element).attr("id");
			}
			else{
				vendorCode = $(".form").find("input.check:checked:first").val();
				checkBoxId = $(".form").find("input.check:checked:first").attr("id");
			}
			
			var error = 0;
			details = checkBoxId.split("check");
			index = details[1];
			shippingMode = $("input[name='rtvGroupList["+index+"].inventoryList[0].shippingMode']").val();
			
			$(".check:checked").each(function(){	
				
				checkBoxId = $(this).attr("id");
				details = checkBoxId.split("check");
				index = details[1];
				var curShippingMode = $("input[name='rtvGroupList["+index+"].inventoryList[0].shippingMode']").val();
				
				if($(this).val() != vendorCode || curShippingMode != shippingMode)
					{
						error = 1;		
					}
			});
			if(error == 1)
			{
				noty({
					text : "Selected Packages cannot be shipped together.",
					type : "error",
					layout : "topRight"
				});
				$("#together").hide();
			}
			else{
				$("#together").show();
			}
		}
		else{
			$("#together").hide();
		}
	}
	
	function split(rowIndex)
	{
		var vendorCode = $("#vendor"+rowIndex).val();
		var sellerName = $("#seller"+rowIndex).text();
		if($("#row-"+rowIndex+" table#table"+rowIndex+" tbody tr").length > 1)
		{
		$("#row-"+rowIndex+" table#table"+rowIndex+" tbody tr").each(function() {
			var lastRowIndex = $("#lastRow").val();
			var nextRowIndex = ++lastRowIndex;
			var row = $("#dummyRow").clone();
			row.attr("id","row-"+nextRowIndex);
			row.find("td input#dummyCheck").attr({"value":vendorCode,"id":"check"+nextRowIndex,
				"onclick":"join(this)"});
			row.find("td span#sellerSpan").html(sellerName);
			row.find("td span#sellerSpan").removeAttr("id");
			row.find("td#sellertd").attr("id","seller"+nextRowIndex);
			row.find("td input#dummyVendor").attr({"name":"rtvGroupList["+nextRowIndex+"].vendorCode",
				"id":"vendor"+nextRowIndex,"value":vendorCode});
			row.find("td input#dummySeller").attr({"name":"rtvGroupList["+nextRowIndex+"].sellerName",
				"id":"seller"+nextRowIndex,"value":sellerName});
			row.find("table#dummyInnerTable").attr("id","table"+nextRowIndex);
		    $this = $(this);
		    $this.find("td input").attr("name","rtvGroupList["+nextRowIndex+"].inventoryList[0].id");
		    row.find("td table tbody").append($this);
		    row.find("td input#dummyPackage").attr("id","package"+nextRowIndex);
		    row.find("td a").attr("onclick","split("+nextRowIndex+")");
		    $("#mainTable tr#row-"+rowIndex).after(row);
		    $("#lastRow").attr("value",nextRowIndex);
		});
		$("#row-"+rowIndex).remove();
			}
		else{
			noty({
				text : "Please select valid package to split.",
				type : "error",
				layout : "topRight"
			});
		}
	}
	
	function shipTogether()
	{
		var row = $(".check:checked").parents("tr:first");
		var rowId = $(".check:checked").parents("tr:first").attr("id");
		var details = rowId.split("-");
		var index = details[1];
		var packageIndex = row.find("input#package"+index).val();
		$(".check:checked").each(function(){
			if($(this).parents("tr:first").attr("id") != rowId)
			{
				$(this).parents("tr:first").find("td table tbody tr").each(function(){
					var packageIndex = row.find("input#package"+index).val();
					var nextIndex = ++packageIndex;
					$this = $(this);
					$this.find("td input").attr("name","rtvGroupList["+index+"].inventoryList["+nextIndex+"].id");
					row.find("td table#table"+index).append($this);
					row.find("input#package"+index).attr("value",nextIndex);
				});
				$(this).parents("tr:first").remove();
				row.find("td input.check").removeAttr("checked");
			}
		});
		$("#together").hide();
	}
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
			<div class="box-header" style="padding-left: 10px; padding-top: 10px">Packages</div>
			<div class="box-content">
				<div class="row">
					<form action='<c:url value="/Rtv/manifest" />' method="post"
						name="rtv" class="form">
						<input type="hidden" name="returnType"
							value="${returnType}" />
						<div class="col-lg-12"
							style="margin-top: 20px; margin-bottom: 20px;">
							<table class="table table-striped table-bordered" id="mainTable">
								<thead>
									<tr>
										<th></th>
										<th>Seller Name</th>
										<th>Package Details</th>
										<!-- <th>Action</th> -->
									</tr>
								</thead>
								<tbody id="mainBody">
									<c:forEach var="item" items="${groupList}" varStatus="i">
										<tr id="row-${i.index}">
											<td style="vertical-align: middle; text-align: center;"><input
												type="checkbox" value="${item.vendorCode}"
												id="check${i.index}" class="check" onclick="join(this)">
											</td>
											<td style="vertical-align: middle; text-align: center;"
												id="seller${i.index}"><span>${item.sellerName}</span><input
												type="hidden" name="rtvGroupList[${i.index}].vendorCode"
												value="${item.vendorCode}" id="vendor${i.index}" /> <input
												type="hidden" name="rtvGroupList[${i.index}].sellerName"
												value="${item.sellerName}" id="seller${i.index}" />
											</td>
											<td>
												<table class="table table-striped table-bordered"
													id="table${i.index}">
													<thead>
														<tr>
															<th>Barcode</th>
															<th>Product</th>
															<th>Location</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach var="inventory" items="${item.inventoryList}"
															varStatus="j">
															<tr>
																<td width="20%">${inventory.barcode}<input
																	type="hidden"
																	name="rtvGroupList[${i.index}].inventoryList[${j.index}].id"
																	value="${inventory.id}" class="inventory" />
																	<input type="hidden"
																	name="rtvGroupList[${i.index}].inventoryList[${j.index}].shippingMode"
																	value="${inventory.shippingMode}" />
																</td>
																<td width="60%">${inventory.productName}</td>
																<td width="20%">${inventory.location}</td>
															</tr>
															<c:set var="lastPackageIndex" value="${j.index}" />
														</c:forEach>
													</tbody>
												</table> <input type="hidden" id="package${i.index}"
												value="${lastPackageIndex}" />
											</td>
											<%-- <td style="text-align: center; vertical-align: middle;"><a
												class="btn btn-danger" onclick="split(${i.index})">Split</a>
											</td> --%>
										</tr>
										<c:set var="lastIndex" value="${i.index}" />
									</c:forEach>
								</tbody>
							</table>
							<input type="hidden" id="lastRow" value="${lastIndex}" />
						</div>
						<div class="col-lg-12"
							style="margin-top: 20px; text-align: center;">
							<a class="btn btn-success" style="margin-right: 20px"
								id="together" onclick="shipTogether()">Ship Together</a> <input
								type="submit" class="btn btn-primary" value="Manifest" />
						</div>
					</form>
				</div>
			</div>
		</div>
		<table class="hide">
			<tbody>
				<tr id="dummyRow">
					<td style="vertical-align: middle; text-align: center;"><input
						type="checkbox" id="dummyCheck" class="check">
					<td style="vertical-align: middle; text-align: center;" id="sellertd"><span
						id="sellerSpan"></span><input type="hidden" id="dummyVendor" /> <input
						type="hidden" id="dummySeller" />
					</td>
					<td>
						<table class="table table-striped table-bordered"
							id="dummyInnerTable">
							<thead>
								<tr>
									<th>Barcode</th>
									<th>Product</th>
									<th>Location</th>
								</tr>
							</thead>
							<tbody>

							</tbody>
						</table> <input type="hidden" id="dummyPackage" value="0" />
					</td>
					<!-- <td style="text-align: center; vertical-align: middle;"><a
						class="btn btn-danger">Split</a>
					</td> -->
				</tr>
			</tbody>
		</table>
	</div>
	</jsp:body>
</tags:page>

