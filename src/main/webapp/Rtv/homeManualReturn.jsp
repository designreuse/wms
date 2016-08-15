<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<tags:page title="Returns">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(function() {
		$("#barcode").focus();
		
		$("#form").validate();
		
		if ($("#message").val() != "") {
			noty({
				text : $("#message").val(),
				type : "error",
				layout : "topRight"
			});
		}
		
		/* $('#barcode').keypress(function(e){
		    if (e.which == 13){
		    	$('#getDetails').click();
		    }
		}); */
		
		$('#getDetails').on("click",function(){
			setTarget('scan');
		});
		
		$('#shipProducts').on("click",function(){
			setTarget('manifest');
		});		
		
	});
	function setTarget(value) {
		if(value == "scan") {	
			$("#form").attr("action","/Wms/Rtv/searchInventory");

			 $("#form").submit(function (e) {
			      e.preventDefault();
			 });
			
			if($('#barcode').val().trim() != ""){
				$.ajax({
					type : "POST",
					url : "/Wms/Rtv/checkInventory",
					data: {
						barcode: $('#barcode').val(),
						returnType: $('#returnType').val()
	                }
				}).done(function(result) {
					 if(result && result != ""){
						 if(confirm(result)){
							 $("#form")[0].submit();
						 }
					 }
					 else {
						 $("#form")[0].submit();
					 }
				});
			}
		}
		else{
			$("#form").attr("action","/Wms/Rtv/manualManifest");
			$("#barcode").removeClass("required");
			$("#form")[0].submit();
		}
	}
	function removeProduct(index){
		$("#row-"+index).remove();
	}
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<form method="post" id="form" name="data">
			<input type="hidden" name="returnType" id="returnType" value="${data.returnType}" />
			<div class="box">
				<div class="box-header"
					style="padding-top: 10px; padding-left: 10px">Manual Product
					Return</div>
				<div class="box-content">
					<input type="hidden" id="message" value="${message}" />
					<div class="row" style="margin-bottom: 20px; margin-top: 20px">

						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Barcode : </label> <input type="text" name="barcode"
									class="form-control required" id="barcode" />
							</div>
							<div class="col-lg-4" style="text-align: center;">
								<input type="submit" class="btn btn-primary" value="Get Details" id="getDetails"
									style="margin-top: 25px"/>
							</div>
						</div>
					</div>
				</div>
			</div>
			<c:if test="${fn:length(data.inventoryList) gt 0}">
				<div class="box">
					<div class="box-header"
						style="padding-left: 10px; padding-top: 10px">Products to
						Return</div>
					<div class="box-content">
						<div class="row">
							<div class="col-lg-12" style="margin-top: 20px;">
								<table class="table table-striped table-bordered">
									<thead>
										<tr>
											<th>Barcode</th>
											<th>Product Name</th>
											<th>Seller</th>
											<th>Fulfillment Model</th>
											<th></th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="item" items="${data.inventoryList}"
											varStatus="i">
											<tr id="row-${i.index}">
												<td>${item.barcode}<input type="hidden"
													name="inventoryList[${i.index}].id" value="${item.id}" />
													<input type="hidden"
													name="inventoryList[${i.index}].barcode"
													value="${item.barcode}" />
												</td>
												<td>${item.productName}<input type="hidden"
													name="inventoryList[${i.index}].productName"
													value="${item.productName}" />
												</td>
												<td>${item.sellerName}<input type="hidden"
													name="inventoryList[${i.index}].sellerName"
													value="${item.sellerName}" />
												</td>
												<td>${item.fulfillmentModel}<input type="hidden"
													name="inventoryList[${i.index}].fulfillmentModel"
													value="${item.fulfillmentModel}" />
												</td>
												<td style="text-align: center;"><a onclick="removeProduct(${i.index})"><img
														src="<c:url value="/img/delete.jpg" />" alt="Delete" height="25px" width="35px">
												</a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<div class="col-lg-12"
								style="margin-top: 20px; text-align: center;">
								<input type="submit" class="btn btn-primary" id="shipProducts" value="Ship Products">
							</div>
						</div>
					</div>
				</div>
			</c:if>
		</form>
	</div>
	</jsp:body>
</tags:page>