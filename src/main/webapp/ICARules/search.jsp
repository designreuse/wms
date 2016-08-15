<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<tags:page title="Search ICA Rules">
     <jsp:attribute name="script">
    	<script type="text/javascript">
	    	 $(document).ready(function() {
	    		 // Menu Options hidden when page is loaded.
	    		 $("#typeMenu").hide();
	    		 $("#locationMenu").hide();
	    		 $("#courierMenu").hide();
	    		 
	    		 //Date Picker
	    		 $('.date-picker').datepicker({
	    			 format: 'dd/mm/yyyy',
	    			 autoclose: true,
	    			 startDate: new Date()
	    			});
	    		 
	    		 $('#startDate').datepicker('setDate', new Date());
	    		 
	    		 $('#returnType').on('change', function() {
	    			 if($(this).val() != "--Select--"){
	    				 $("#type").val("--Select--");
	    				 $("#location").val("--Select--");
	    				 $("#typeMenu").slideDown();
	    				 $("#locationMenu").slideUp();
	    				 $("#courierMenu").slideUp();
	    			 }
	    			 else{
	    				 $("#typeMenu").slideUp();
	    				 $("#locationMenu").slideUp();
	    				 $("#courierMenu").slideUp();
	    			 }
	    		 });
	    		
	    		 $('#type').on('change', function() {
					$("#courierMenu").hide();
		    		  if($(this).val() != "--Select--"){
		    			  $("#locationMenu").show();
		    		  	  $("#endDateMenu").show();
		    			  $("#location").empty();
		    			  $("#location").append("<option value='--Select--'>--Select--</option>");
						  $(".chosen-select").chosen();
						  
			    		  if($(this).val() == "All"){
			    			  $("#endDateMenu").hide();
			    			  $("#location").append("<option value='All'>All</option>");
			    			  $("#location").trigger("chosen:updated");
			    		  }
			    		  else if($(this).val() == "City"){
			    			  // Get the city list and populate location dropdown list.
			    			  var request = $.ajax({
									type : "POST",
									url : "/Wms/ICARules/getCity"
								});
								request
										.done(function(result) {
											 $.each(result, function() {
												$("#location").append($("<option />").val(this).text(this));
										}); 
										$("#location").trigger("chosen:updated");
									});
			    		  }
			    		  else {
			    			// Get the state list and populate location dropdown list.
			    			  var request = $.ajax({
									type : "POST",
									url : "/Wms/ICARules/getState"
								});
								request
										.done(function(result) {
											$.each(result, function() {
												$("#location").append($("<option />").val(this).text(this));
										});
										$("#location").trigger("chosen:updated");
									});
			    		  }
		    		  }
		    		  else {
		    			  $("#locationMenu").hide();
		    		  }
		    		});
	    		 
	    		 var curCourier = "";
	    		 $('#location').on('change', function() {
	    			 if($(this).val() != "--Select--"){
	    				// Check that whether rule already exists.
		    			  var request = $.ajax({
								type : "POST",
								url : "/Wms/ICARules/getICARules",
								data: {
			                        location: $(this).val(),
			                        type: $('#type').val(),
			                        returnType: $("#returnType").val(),
			                        isRuleEnabled: $("#showRuleEnabled").prop('checked')
			                    }
							});
							request.done(function(result) {
								 if(result.length > 0){
									 $("#courierMenu").show();
									 var i = 0;
									 $.each(result, function() {
										 curCourier = this.courierCode;
										 if(i < 1)
											 $('#tblPriorityList').appendGrid('load', [
	   	                                        { courierCodeList: this.courierCode, priorityList: this.priority}
	   	                                    ]);
										 else
											 $('#tblPriorityList').appendGrid('appendRow', [
			                                        { courierCodeList: this.courierCode, priorityList: this.priority}
			                                    ]); 
										 i++;
								 	});
									var startDate = new Date(result[0].startDate);
									$("#startDate").val(startDate.toLocaleDateString("en-GB"));
									if(result[0].endDate != null){
										var endDate = new Date(result[0].endDate);
										$("#endDate").val(endDate.toLocaleDateString("en-GB"));
									}
									else
										$("#endDate").val("");
									$('#isRuleEnabled').prop('checked', result[0].isRuleEnabled);
									
									if(result[0].type == "All")
										$('#notAllEnabled').hide();
								 }
								 else{
					 	    		 $("#courierMenu").hide();
					 	    		 $('#location').val("--Select--");
									 alert("Rule doesn't exists. Please select different one.");
								 }
								});
	    			 }
	 	    		  else
	 	    			$("#courierMenu").hide();
		    		});
	    		 
	    		 //form validationr
	    		 $("#form").validate({
	 	        
	 	            // Specify the validation rules
	 	            rules: {
	 	            	startDate: "required",
	 	            	endDate: { greaterThan: "#startDate" }
	 	            },
	 	            
	 	            // Specify the validation error messages
	 	            messages: {
	 	                startDate: "Please select a valid start date"
	 	            },
	 	            
	 	            submitHandler: function(form) {
		                form.submit();
		            }
	    		 });
		    	 
		    	 //Dynamic Table
		    	 $(function () {
							
		    		    // Initialize appendGrid
		    		    $('#tblPriorityList').appendGrid({
		    		        initRows: 1,
		    		        //hideRowNumColumn: true,
		    		        columns: [
		    		                { name: 'courierCodeList', display: 'Courier Code', type: 'select',  ctrlCss: { width: '250px'}, ctrlClass: 'form-control'/*, 
		    		                	ctrlOptions: [<c:forEach var="item" items="${courierList}">
		    		                    		 		"<c:out value="${item}"/>",
		    		                    		 	</c:forEach>]*/},
		    		                { name: 'priorityList', display: 'Priority', type: 'text', ctrlCss: { width: '150px'},
		    		                    		 		ctrlClass: 'form-control priority',ctrlAttr: { maxlength: 10 }},
		    		                //{ name: 'courierCode', display: 'courier Code' ,type: 'hidden'},
		    		              	{ name: 'RecordId', type: 'hidden', value: 0 }
		    		            ],
		    		            nameFormatter: function (idPrefix, name, uniqueIndex) {
		    		                return name;
		    		            },
		    		            hideButtons: {
		    		                removeLast: true,
		    		                insert: true,
		    		                moveUp: true,
		    		                moveDown: true
		    		            },
		    		            afterRowAppended: function (caller, parentRowIndex, addedRowIndex) {
		
			   		       			var rowData = $(caller).appendGrid('getRowValue', parentRowIndex);
			  	    		    	 if(rowData != null && (rowData.priorityList == "" || !$.isNumeric( rowData.priorityList))){
			  	    		    		$(caller).appendGrid('removeRow', addedRowIndex[0]);
			  	    		    		alert("Please enter numeric value for priority of Courier Code : "+rowData.courierCodeList);
			  	    		    	}
			  	    		    	else{ 
			  	    		    		var count =  $('#tblPriorityList').appendGrid('getRowCount');
			    	        		    var totalCourier = ${fn:length(courierList)};
			   		       			  	if(count < totalCourier)
			   		    	    		  	$(".append").show();
			 		    	    		else
			   		    	    			$(".append").hide();
		
				   		       			var elem = $(caller).appendGrid('getCellCtrl', 'courierCodeList', addedRowIndex[0]);
				   		       			
				   		       			if($(elem).val() != null || curCourier == ""){
					   		       			<c:forEach var="item" items="${courierList}">
				   		       					$(elem).append('<option value=<c:out value="${item}"/>><c:out value="${item}"/></option>');
		                    		 		</c:forEach>
					   		       			for(var i = parentRowIndex; i>=0;i--){   		       			  		
				   		       					var parentValue = $(caller).appendGrid('getCtrlValue', 'courierCodeList', i);
				   	   		       				$(elem).children('option[value='+parentValue+']').remove();	   		       				
				   		       			  	}
					   		       			
				  	    		    		// Set the courierCode hidden field with the current selected dropdown list value.
				  	    		    		//caller.appendGrid('setCtrlValue', 'courierCode', count-2, rowData.courierCodeList);
				  	    		    		var elem = $(caller).appendGrid('getCellCtrl', 'courierCodeList', parentRowIndex);
				  	    		    		$(elem).children('option:not(:selected)').attr('disabled', true);
				  	    		    		$(elem).attr('readonly',true);
				  	    		    		
				  	    		    		//elem = caller.appendGrid('getCellCtrl', 'priorityList', count-2);
				  	    		    		//$(elem).attr('readonly', true);
				   		       			}
				   		       			else {
				   		       				$(elem).append('<option value='+curCourier+'>'+curCourier+'</option>');
				   		       				$(elem).attr('readonly',true);
				   		       				curCourier = "";
				   		       			}
			  	    		    	}
		    		            },
		    		            beforeRowRemove: function (caller, rowIndex) {
		    		            	var count =  $('#tblPriorityList').appendGrid('getRowCount');
		    		            	if($("#location").val() == 'All' && count <= 1){
		    		            		alert("Rules for all cannot be removed");
		    		            		return false;
		    		            	}
		    		            	else
		    		            		return true;
		    		            },
		    		            afterRowRemoved: function (caller, rowIndex) {
		   		    	    		$(".append").show();
		    		            }
		    		    });
		    		    
		    		    $.validator.addClassRules('priority', {
		    		        required: true,
		    		        number: true
		    		    });
		    		    
		    		});
	    	 });
	    	 
	    	 //Checking greter than validation for dates.
	    	 jQuery.validator.addMethod("greaterThan", 
	    			 function(value, element, params) {
			    		 
						if(value != ""){
							
				    		 var arrStartDate = $(params).val().split("/");
				    		 var startDate = new Date(arrStartDate[2], arrStartDate[1], arrStartDate[0]);
				    		 var arrEndDate = value.split("/");
				    		 var endDate = new Date(arrEndDate[2], arrEndDate[1], arrEndDate[0]);
							return endDate > startDate;
						}
						else{
							//$('#endDate').val('01/01/2000');
							return true;
						}
	    			 },'End date must be greater than start date.');
    	</script>
    </jsp:attribute>
   	<jsp:body>
		<div id="content" class="col-lg-10 col-sm-11">
			<div class="row">
				<div class="col-lg-12">
					<form name="iCARules" action='<c:url value="/ICARules/update" />' method="post" id="form">
						<div class="box">
							<div class="box-header">
								<h2>Search ICA Rules</h2>
							</div>
							<div class="box-content">
								<c:if test="${not empty message}">
									<div class="alert alert-success">
										<button type="button" class="close" data-dismiss="alert">×</button>
										${message}
									</div>
								</c:if>
								<div class="form-group">
								  <label class="col-sm-2 control-label">Return Type</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<select id="returnType" name="returnType" class="form-control">
											<option value="--Select--">--Select--</option>
											<c:forEach var="returnType" items="${returnTypeList}">
												<option value="${returnType.code}">${returnType.code}</option>
											</c:forEach>
										</select>
									</div>	
								  </div>
								</div>
								<div id="typeMenu" class="form-group">
								  <label class="col-sm-2 control-label">Type</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<select id="type" name="type" class="form-control">
											<option value="--Select--">--Select--</option>
											<option value="City">City</option>
											<option value="State">State</option>
											<option value="All">All</option>
										</select>
									</div>	
								  </div>
								</div>
								<div id="locationMenu" class="form-group">
								  <label class="col-sm-2 control-label">Location</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<select id="location" name="location" class="form-control chosen-select">
											<option value="--Select--">--Select--</option>
											<option value="All">All</option>
										</select>
									</div>	
								  </div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">Enabled</label>
									  	<div class="input-group col-sm-6">
										     <input type="checkbox" name="showRuleEnabled" id="showRuleEnabled" checked="checked">
								      </div>
								</div>
							</div>
						</div>
						<div class="box" id="courierMenu">
							<div class="box-header">
								<h2>ICA Rule Details</h2>
							</div>
							<div class="box-content">							
								<div class="form-group">
								  <label class="col-sm-2 control-label">Add Priority</label>
								  <div class="controls">
									<div class="input-group col-sm-6">	
										<table id="tblPriorityList" class="table table-striped table-bordered">
										</table>
									</div>	
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-2 control-label">Start Date</label>
								  <div class="controls">
									<div class="input-group date col-sm-6">
										<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
										<input type="text" class="form-control date-picker" id="startDate" name="startDate">
										(00:00:00)
									</div>	
								  </div>
								</div>
								<div class="form-group" id="endDateMenu">
								  <label class="col-sm-2 control-label">End Date</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
										<input type="text" class="form-control date-picker" id="endDate" name="endDate">
										(23:59:59)
									</div>
								  </div>
								</div>
								<div id="notAllEnabled" class="form-group">
								  <label class="col-sm-2 control-label">Enabled</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<input type="checkbox" name="isRuleEnabled" id="isRuleEnabled">
									</div>	
								  </div>
								</div> 
								
								<div class="form-actions">
									<button id="submit" type="submit" class="col-sm-offset-2 btn btn-primary">Submit</button>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</jsp:body>
</tags:page>