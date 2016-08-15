<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="Rule - Seller Mapping">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(function() {
		$("#form").validate();
		if ($("#message").val() != "") {
			noty({
				text : $("#message").val(),
				type : "success",
				layout : "topRight"
			});
		}
		$("form").submit(function(e) {
			$("#form").validate();
			if ($(".value").hasClass("invalidValue")) {
				e.preventDefault();
				noty({
					text : "Invalid Seller Initial entered for some shelf. Please correct and proceed.",
					type : "error",
					layout : "topRight"
				});
			}
		});
	});
	
	function addShelf()
	{
		var lastRowIndex = $("#lastRow").val();
		var nextRowIndex = ++lastRowIndex;
		var shelfNumber = $("#lastShelfNumber").val();
		var shelfName = $("#groupName").val()+"-"+(++shelfNumber);
		var row = $("#dummyRow").clone();
		row.attr("id","row"+nextRowIndex);
		row.find(".id").attr({"name":"shelfList["+nextRowIndex+"].id",
			"value":"-1"});
		row.find("td label").text(shelfName);
		row.find("td input.name").attr({"name":"shelfList["+nextRowIndex+"].name",
			"value":shelfName});
		row.find("td input.value").attr("name","shelfList["+nextRowIndex+"].sellerInitial");
		/* row.find("a.delete").attr("onclick","removeShelf("+nextRowIndex+")"); */
		$("#mappingTable").append(row);
		$("#lastRow").attr("value",nextRowIndex);
		$("#lastShelfNumber").attr("value",shelfNumber);
		$("#removeButton").attr("onclick","removeShelf("+lastRowIndex+")");
	}

	function removeShelf(index)
	{
		$("#row"+index).remove();
		var lastRowIndex = $("#lastRow").val();
		var newRowIndex = lastRowIndex - 1;
		$("#lastRow").attr("value",newRowIndex);
		$("#removeButton").attr("onclick","removeShelf("+newRowIndex+")");
	}
	
	function checkValue(object)
	{
		var initial = $(object).val();
		var allInitials = initial.split(",");
		var error = 0;
		var pattern = new RegExp("^[A-Z]{1}$");
		for (i = 0; i < allInitials.length; i++) {
			if(!pattern.test(allInitials[i]))
	    	{
	    		error = -1;
	    		break;
	    	}
		}
		if(error == -1)
		{
			$(object).addClass("invalidValue");
			noty({
				text : "Invalid Seller Initial.Should be capital alphabet between A-Z.",
				type : "error",
				layout : "topRight"
			});
		}
		else
		{
			$(object).removeClass("invalidValue");
		}
	}
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
		<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Shelf - Seller Initial Mapping</div>
			<div class="box-content">
				<input type="hidden" id="message" value="${message}" />
				<div class="row">
					<form name="group" action="<c:url value="/Rule/saveMapping/" />"
							method="post" id="form">
						<input type="hidden" name="id" value="${group.id}" /> <input
								type="hidden" name="name" value="${group.name}" id="groupName" />
						<div class="col-lg-12">
							<div class="col-lg-6" style="margin-top: 20px; margin-left: 25%; margin-right: 25%">
								<table class="table table-striped table-bordered"
										id="mappingTable">
									<thead>
										<tr>
											<th>Shelf Name</th>
											<th>Seller Initial</th>
											<c:if test="${edit}">
												<th>Action</th>
											</c:if>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="shelf" items="${group.shelfList}"
												varStatus="rowNumber">
											<tr id="row${rowNumber.index}">
												<input type="hidden" name="shelfList[${rowNumber.index}].id"
														value="${shelf.id}" class="id" />
												<td><label>${shelf.name} </label><input type="hidden"
														name="shelfList[${rowNumber.index}].name"
														value="${shelf.name}" class="name" />
													</td>
												<td><input type="text"
														name="shelfList[${rowNumber.index}].sellerInitial"
														value="${shelf.sellerInitial}"
														class="form-control required value" 
														onchange="checkValue(this)"/>
													</td>
												<c:if test="${edit}">
													<td style="text-align: center"><a
															href='<c:url value="/Rule/editShelf/${shelf.id}" />'
															class="btn btn-primary">Edit Configuration</a> 
													
													</c:if>
											</tr>
											<c:set var="lastIndex" value="${rowNumber.index}" />
										</c:forEach>
									</tbody>
								</table>
								<input type="hidden" id="lastRow" value="${lastIndex}" /> <input
										type="hidden" id="lastShelfNumber" value="${shelfNumber}" />
							</div>
						</div>
						<div class="col-lg-12"
								style="text-align: center; margin-top: 20px">
							<input type="submit" value="Update" class="btn btn-primary">
							<c:if test="${edit}">
								<a onclick="addShelf()" class="btn btn-success"
										style="margin-left: 20px">Add Shelf</a>
								<a onclick="removeShelf(${lastIndex})"
										class="btn btn-danger delete" style="margin-left: 20px"
										id="removeButton">Remove</a>
							</c:if>
						</div>
					</form>
					<div class="hide">
						<table id="dummyTable">
							<tr id="dummyRow">
								<input type="hidden" name="" class="id" />
								<td><label></label><input type="hidden" name="" value=""
										class="name" />
								</td>
								<td><input type="text" name=""
										class="form-control required value" 
										onchange="checkValue(this)" />
								</td>
								<td>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</tags:page>