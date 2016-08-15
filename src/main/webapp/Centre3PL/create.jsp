<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="3PL">
	<jsp:attribute name="script">
<script type="text/javascript">
 	$(document) .ready( 
 					function() { 
 						$("#form").validate(); 
 						$("form") 
 								.submit( 
 										function(e) { 
 											$("#form").validate(); 
 											if ($("#centre3pl_name") 
 													.hasClass("duplicate") 
 													|| $("#centre3pl_code") 
 															.hasClass( 
 																	"duplicate")) { 
 												e.preventDefault(); 
 												noty({ 
 													text : "Duplicate 3PL centre Name or Code. Please correct and proceed.", 
 													type : "error", 
 													layout : "topRight" 
 												});  											
 												}  										
 											}); 
//  						 $("#centre3pl_name") 
// 								.on( 
//  										"change", 
//  										function() { 
//  											var centre3pl_name = $(this) 
//  													.val(); 
//  											var request = $ 
//  													.ajax({ 
//  														type : "POST", 
//  														url : "/Wms/centre3PL/checkName", 
//  														data : { 
//  															name : centre3pl_name 
//  														} 
//  													});
//  											request 
//  													.done(function(msg) { 
//  														if (msg == "failure") { 
//  															$( 
//  																	"#centre3pl_name") 
//  																	.addClass( 
//  																			"duplicate"); 
//  															noty({ 
//  																text : "3PL centre Name already exists. Please Select a different name.",  
//  																type : "error", 
//  																layout : "topRight" 
//  															}); 
//  														} else {
// 															$( 
//  																	"#centre3pl_name") 
//  																	.removeClass( 
//  																			"duplicate"); 
//  														} 
//  													}); 
//  											request.fail(function(jqXHR, 															textStatus) {  
//   														noty({
//   															text : "Failed to check 3PL centre Name.", 
//   															type : "error",  
//   															layout : "topRight"  
//   														});  
//   													});  
//   										});  
//   						$("#centre3pl_code")  
//   								.on(  
//   										"change",  
//   										function() {  
//   											var centre3pl_code = $(this)  
//   													.val();  
//   											var request = $  
//   													.ajax({  
//   														type : "POST",  
//   														url : "/Wms/centre3PL/checkCode",  
//   														data : {  
//   															code : centre3pl_code  
//   														}  
//   													});  
//   											request  
//   													.done(function(msg) {  
//   														if (msg == "failure") {  
//   															$(  
//   																	"#centre3pl_code")  
//   																	.addClass(  
//   																			"duplicate");  
//   															noty({  
//   																text : "3PL centre Code already exists. Please Select a different code.",  
//   																type : "error",  
//   																layout : "topRight"  
//   															});  
//   														} else {  
//   															$(  
//  																	"#centre3pl_code")  
//  																	.removeClass(  
//  																			"duplicate");  
//  														}  
//  													});  
//  											request  
//  													.fail(function(jqXHR,  
//  															textStatus) {  
//  														noty({  
//  															text : "Failed to check 3PL centre Code.",  
//  															type : "error",  
//  															layout : "topRight"  
//  														}); 
//  													}); 
//  										}); 
 					});
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-lg-10 col-sm-11">
		<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				3PL centre Management</div>
			<div class="box-content">
				<div class="row">
					<form name="centre3pl"
							action='<c:url value="/Centre3PL/save" />' method="post"
							id="form">
						<input type="hidden" name="id" value="${centre3pl.id}" />
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Name : </label> <input type="text" name="name"
										class="form-control required" value="${centre3pl.name}"
										id="centre3pl_name">
							</div>
							<div class="col-lg-4">
								<label>Code : </label> <input type="text" name="code"
										class="form-control required" value="${centre3pl.code}"
										id="centre3pl_code">
							</div>
							<div class="col-lg-4">
								<label>Contact Number (+91) :</label> <br> <input
										type="text" class="form-control required digits"
										name="address.contactNumber"
										value="${centre3pl.address.contactNumber}" />
							</div>
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Address Line 1 : </label> <input type="text"
										name="address.addressLine1" class="form-control required"
										value="${centre3pl.address.addressLine1}">
							</div>
							<div class="col-lg-4">
								<label>Address Line 2 : </label> <input type="text"
										name="address.addressLine2" class="form-control required"
										value="${centre3pl.address.addressLine2}">
							</div>
							<div class="col-lg-4">
								<label>City : </label> <br> <input type="text"
										name="address.city" value="${centre3pl.address.city}"
										class="form-control required" />
							</div>
						</div>
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>State : </label> <input type="text" name="address.state"
										class="form-control required"
										value="${centre3pl.address.state}">
							</div>
							<div class="col-lg-4">
								<label>Pincode : </label> <input type="text"
										name="address.pincode" class="form-control digits required"
										value="${centre3pl.address.pincode}">
							</div>
							<div class="col-lg-4">
								<label>Warehouse : </label> 
								<select name="warehouseID" class="form-control required">
								<option selected value="${centre3pl.warehouse.id}">${centre3pl.warehouse.name}</option>
								<c:forEach var="warehouse" items="${warehouse}">
											<c:if test="${warehouse.name != centre3pl.warehouse.name}">
								<option value="${warehouse.id}">${warehouse.name}</option>
											</c:if>
								</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-lg-12"
								style="text-align: center; margin-top: 20px">
							<input type="submit" value="Save" class="btn btn-primary">
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</tags:page>