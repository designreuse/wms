<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="Shelf Mapping">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(function() {
		$("#form").validate();
		$(".checkCapacity").on("change",function(){
			var capacity = $(this).val();
			var used = $(this).parents("tr:first").find("td input.used").val();
			if(capacity < used)
			{
				$(this).addClass("invalidCapacity");
				noty({
					text : "Invalid Capacity.Should be greater than used value.",
					type : "error",
					layout : "topRight"
				});
			}
			else
			{
				$(this).removeClass("invalidCapacity");
			}
		});
		$("form").submit(function(e) {
					$("#form").validate();
					if ($(".checkCapacity").hasClass("invalidCapacity")) {
						e.preventDefault();
						noty({
							text : "Invalid Capacity entered for some box. Please correct and proceed.",
							type : "error",
							layout : "topRight"
						});
					}
				});
	});
	function addFloor()
	{
		var lastRowIndex = $("#lastRow").val();
		var nextRowIndex = ++lastRowIndex;
		var floorName = $("#floorName").val();
		var newFloorDetails = floorName.split("-");
		var newNumber = ++newFloorDetails[2];
		var newFloorName = newFloorDetails[0]+"-"+newFloorDetails[1]+"-"+newNumber;
		var row = $("#dummyFloorRow").clone();
		row.attr("id","row"+nextRowIndex);
		row.find("input.floorId").attr({"name":"floorList["+nextRowIndex+"].id",
			"value":"-1"});
		row.find("td label").text(newFloorName);
		row.find("td input.floorName").attr({"name":"floorList["+nextRowIndex+"].name",
			"value":newFloorName});
		row.find("td table#box").attr("id","box"+nextRowIndex);
		row.find("td tr#rowBox").attr("id","rowBox0");
		row.find("td input.id").attr({"name":"floorList["+nextRowIndex+"].boxList[0].id",
			"value":"-1"});
		row.find("td table td label").text(newFloorName+"-1");
		row.find("td table td input.name").attr({"name":"floorList["+nextRowIndex+"].boxList[0].name",
			"value":newFloorName+"-1"});
		row.find("td table td input.capacity").attr({"name":"floorList["+nextRowIndex+"].boxList[0].capacity",
			"value":"","class":"form-control required digits checkCapacity"});
		row.find("td table td input.used").attr({"name":"floorList["+nextRowIndex+"].boxList[0].used",
			"value":"0","class":"form-control used"});
		/* row.find("td table td a.removeBox").attr("onclick","removeBox(0,"+nextRowIndex+")"); */
		row.find("td input#lastRow").attr({"value":"0",
			"id":"lastRow"+nextRowIndex});
		row.find("td input#boxName").attr({"id":"boxName"+nextRowIndex,
			"value":newFloorName+"-1"});
		row.find("td a.boxAdd").attr("onclick","addBox("+nextRowIndex+")");
		row.find("td a.removeBox").attr({"onclick":"removeBox(0,"+nextRowIndex+")","id":"removeBoxButton"+nextRowIndex});
		/* row.find("td a.delete").attr("onclick","removeFloor("+nextRowIndex+")"); */
		$("#removeFloorButton").attr("onclick","removeFloor("+nextRowIndex+")");
		$("#mappingTable").append(row);
		$("#lastRow").attr("value",nextRowIndex);
		$("#floorName").attr("value",newFloorName);
	}

	function removeFloor(index)
	{
		var lastRowIndex = $("#lastRow").val();
		var newRowIndex = lastRowIndex - 1;
		$("#row"+index).remove();
		$("#removeFloorButton").attr("onclick","removeFloor("+newRowIndex+")");
		$("#lastRow").attr("value",newRowIndex);
	}
	
	function addBox(index)
	{
		var lastRowIndex = $("#lastRow"+index).val();
		var nextRowIndex = ++lastRowIndex;
		var boxName = $("#boxName"+index).val();
		var newBoxDetails = boxName.split("-");
		var newNumber = ++newBoxDetails[3];
		var newBoxName = newBoxDetails[0]+"-"+newBoxDetails[1]+"-"+newBoxDetails[2]+"-"+newNumber;
		var row = $("#dummyBoxRow").clone();
		row.attr("id","rowBox"+nextRowIndex);
		row.find("input.id").attr({"name":"floorList["+index+"].boxList["+nextRowIndex+"].id",
			"value":"-1"});
		row.find("td label").text(newBoxName);
		row.find("td input.name").attr({"name":"floorList["+index+"].boxList["+nextRowIndex+"].name",
			"value":newBoxName});
		row.find("td input.capacity").attr({"name":"floorList["+index+"].boxList["+nextRowIndex+"].capacity",
			"class":"form-control required digits checkCapacity"});
		row.find("td input.used").attr({"name":"floorList["+index+"].boxList["+nextRowIndex+"].used",
			"class":"form-control used","value":"0"});
		/* row.find("a.removeBox").attr("onclick","removeBox("+nextRowIndex+","+index+")"); */
		$("#box"+index).append(row);
		$("#removeBoxButton"+index).attr("onclick","removeBox("+nextRowIndex+","+index+")");
		$("#lastRow"+index).attr("value",nextRowIndex);
		$("#boxName"+index).attr("value",newBoxName);
	}
	
	function removeBox(boxIndex,index)
	{
		var lastRowIndex = $("#lastRow"+index).val();
		var newLastRowIndex = lastRowIndex - 1;
		$("table#box"+index+" tr#rowBox"+boxIndex).remove();
		$("#removeBoxButton"+index).attr("onclick","removeBox("+newLastRowIndex+","+index+")");
		$("#lastRow"+index).attr("value",newLastRowIndex);
	}
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
		<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Shelf - Floor Mapping for shelf ${shelf.name}</div>
			<div class="box-content">
			<div class="row">
			<form name="shelf" action="<c:url value="/Rule/saveFloorMapping/" />"
							method="post" id="form">
							<input type="hidden" name="id" value="${shelf.id}" /> <input
								type="hidden" name="name" value="${shelf.name}" id="shelfName" />
						<div class="col-lg-12">
							<div class="col-lg-7" style="margin-left: 21%; margin-right: 21%; margin-top: 20px">
								<table class="table table-striped table-bordered"
										id="mappingTable">
									<thead>
										<tr>
											<th>Floor Name</th>
											<th>Box Details</th>
											<!-- <th>Action</th> -->
										</tr>
									</thead>
									<tbody>
										<c:forEach var="floor" items="${shelf.floorList}"
												varStatus="rowNumber">
											<tr id="row${rowNumber.index}">
												<input type="hidden" name="floorList[${rowNumber.index}].id"
														value="${floor.id}" class="floorId" />
												<td><label>${floor.name} </label><input type="hidden"
														name="floorList[${rowNumber.index}].name"
														value="${floor.name}" class="floorName" />
												</td>
												<td>
													<table id="box${rowNumber.index}" class="table-bordered"
															style="margin-bottom: 20px">
														<thead>
															<tr>
																<th>Box Name</th>
																<th>Box Capacity</th>
																<th>Used</th>
																<!-- <th>Action</th> -->
														
															
															
															
														
															
															</thead>
														<tbody>
															<c:forEach var="box" items="${floor.boxList}"
																	varStatus="rowCount">
																<tr id="rowBox${rowCount.index}">
																	<input type="hidden"
																			name="floorList[${rowNumber.index}].boxList[${rowCount.index}].id"
																			value="${box.id}" class="id" />
																	<td><label>${box.name}</label><input type="hidden"
																			name="floorList[${rowNumber.index}].boxList[${rowCount.index}].name"
																			value="${box.name}" class="name" />
																	</td>
																	<td><input type="text"
																			name="floorList[${rowNumber.index}].boxList[${rowCount.index}].capacity"
																			value="${box.capacity}"
																			class="form-control required digits checkCapacity" />
																	</td>
																	<td><input type="text"
																			name="floorList[${rowNumber.index}].boxList[${rowCount.index}].used"
																			value="${box.used}" readonly="readonly"
																			class="form-control used" />
																	</td>
																</tr>
																<c:set var="lastRow" value="${rowCount.index}" />
																<c:set var="boxName" value="${box.name}" />
															</c:forEach>
														</tbody>
													</table> <input type="hidden" value="${lastRow}"
														id="lastRow${rowNumber.index}" /> <input type="hidden"
														value="${boxName}" id="boxName${rowNumber.index}" /> <a
														onclick="addBox(${rowNumber.index})"
														class="btn btn-success boxAdd" style="margin-left: 20%">Add
														Box</a><a class="btn btn-danger removeBox"
														onclick="removeBox(${lastRow},${rowNumber.index})"
														style="margin-left: 20px"
														id="removeBoxButton${rowNumber.index}">Remove Box</a>
												</td>
												<%-- <td><a onclick="removeFloor(${rowNumber.index})"
													class="btn btn-danger delete">Remove Floor</a></td> --%>
											</tr>
											<c:set var="lastIndex" value="${rowNumber.index}" />
											<c:set var="floorName" value="${floor.name}" />
										</c:forEach>
									</tbody>
								</table>
								<input type="hidden" id="lastRow" value="${lastIndex}" /> <input
										type="hidden" id="floorName" value="${floorName}" />
							</div>
						
						</div>
						<div class="col-lg-12"
								style="text-align: center; margin-top: 20px">
							<input type="submit" value="Update" class="btn btn-primary">
							<a onclick="addFloor()" class="btn btn-success"
									style="margin-left: 20px">Add Floor</a> <a
									onclick="removeFloor(${lastRow})" style="margin-left: 20px"
									class="btn btn-danger delete" id="removeFloorButton">Remove
								Floor</a>
						</div>
							
						</form>
					</div>
					<div class="hide">
						<table id="dummyBoxTable">
							<tr id="dummyBoxRow">
								<input type="hidden" name="" value="" class="id" />
								<td><label></label><input type="hidden" name="" value=""
									class="name" />
									</td>
								<td><input type="text" name="" value="" class="capacity" />
								</td>
								<td><input type="text" name="" value="" readonly="readonly"
									class="used" />
									</td>
								<!-- <td><a class="btn btn-danger removeBox">Remove Box</a>
								</td> -->
							</tr>
						</table>
					</div>
					
					<div class="hide">
						<table id="dummyFloorTable">
							<tr id="dummyFloorRow">
								<input type="hidden" name="" value="" class="floorId" />
								<td><label></label><input type="hidden" name="" value=""
									class="floorName" />
								</td>
								<td>
									<table id="box" class="table-bordered">
										<thead>
											<tr>
												<th>Box Name</th>
												<th>Box Capacity</th>
												<th>Used</th>
												<!-- <th>Action</th> -->
										
											
											
											
										
									
										</thead>
										<tbody>
											<tr id="rowBox">
												<input type="hidden" name="" value="" class="id" />
												<td><label></label><input type="hidden" name=""
													value="" class="name" />
													</td>
												<td><input type="text" name="" value=""
													class="capacity" />
													</td>
												<td><input type="text" name="" value=""
													readonly="readonly" class="used" />
													</td>
											</tr>
										</tbody>
									</table> <input type="hidden" value="" id="lastRow" /> <input
									type="hidden" value="" id="boxName" /> <a onclick="addBox()"
									class="btn btn-success boxAdd" style="margin-left: 20%">Add
										Box</a> <a class="btn btn-danger removeBox">Remove Box</a>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</tags:page>