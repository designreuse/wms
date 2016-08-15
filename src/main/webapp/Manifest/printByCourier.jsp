<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<tags:page title="Print Manifest By Courier">
     <jsp:attribute name="script">
    	<script type="text/javascript">
		    $(document).ready(function() {
				$('.alert').hide();
				$('#manifestList').hide();
		    	  
			    // From validation
		        $("#addForm").validate({
		        
		            // Specify the validation rules
		            rules: {
		            	name: { notEqual: "--Select--" },
		            	rtvlCode : "required",
		            	bag : "required"
		            },	   
		            
		         	// Specify the validation error messages
		            messages: {
		            	rtvlCode: "Please enter RTVL Code",
		            	bag: "Please enter bag number"
		            }/*,
		            
		            submitHandler: function(form) {
		                form.submit();
		            }*/
		        });
			    
			    function showAlert(msg){
			    	$('.alert').html('<button type="button" class="close">×</button>' + msg);
    				$('.alert').slideDown();
			    }
				
				$('.alert').on("click",".close",function(){
					$('.alert').slideUp();
				});
				
				$('#print').on('submit', function(e){
					if($('#manifestTable tr').length < 2){
						showAlert('Please add at least one RTV in the manifest list.');
						e.preventDefault();
					}
				});
			    
		        $('#addForm').on('submit', function(form){
		        	
		        	if($('#name').val() != '--Select--'){
			    		$('#name').attr('disabled',true);
		        		$('#courierCode').val($('#name').val());
		        	}
			        
		        	if($('#name').val() != '--Select--' && $("#rtvlCode").val().trim() != "" && $("#bag").val().trim() != ""){
		        		
		        		var exists = false;
		        		
		        		$("#manifestTable tr").find('td:first').each(function(){
		        			if($(this).text() == $('#rtvlCode').val()){
		        				showAlert('RTV already present in the list.');
		        				exists = true;
		        				return false;
		        			}	        				
		        		});	        		
		        		
			        	// GET RTV
			           if(!exists){
							$('#manifestList').slideDown();
		    			   var request = $.ajax({
								type : "POST",
								url : "/Wms/Manifest/searchByCourier",
								data: {
			                        courierCode: $('#name').val(),
			                        rtvlCode: $('#rtvlCode').val()
			                    }
							});
							request.done(function(result) {
								//result=JSON.parse(json);
								//alert(result.id);
								if(result.id == -1)
								 {
									 showAlert('RTV Sheet already manifested');
								 }
								else if(result && result.id){
									 $('.alert').slideUp();
									 bag = $('#bag').val();
									 var row = '<tr>';
									 row += '<td>' + $('#rtvlCode').val();
									 row += '<input type="hidden" name="rtvList" value="'+ result.id +'" /></td>';
									 row += '<input type="hidden" name="bagList" value="'+ bag +'" /></td>';
									 row += '<td>' + result.courierCode + '</td>';
									 row += '<td>' + result.awbNumber + '</td>';
									 row += '<td>' + result.sellerName + '</td>';
									 row += '<td>' + bag + '</td>';
									 row += '<td><button type="button" class="remove btn btn-danger">Remove</button></td>';
									 row += '<tr>';
									 $('#manifestTable').prepend(row);
									 $('#rtvlCode').val("");
								 }
								 else if(result && !result.id){
									 showAlert('Connection lost or Session expires. Login to continue.');
								 }
								 
								 else {
									 showAlert('RTV not found for the selected courier.');
								 }
							}); 
			           }
		        	}		        	
		        	return false;
		        });
		        
		        $('#download').click(function(){
		        	  $('#print').attr('action', '<c:url value="/Manifest/downloadCSV" />');
		        });
		        
		        $('#printManifest').click(function(){
		        	  $('#print').attr('action', '<c:url value="/Manifest/print" />');
		        });
		        
		        $('#shareManifest').click(function(){
		        	  $('#print').attr('action', '<c:url value="/Manifest/printshare" />');
		        });
		        
		        $("#manifestTable").on("click",".remove",function(){
		        	$(this).closest('tr').remove();
		         });
		        
		    
	        jQuery.validator.addMethod("notEqual", function(value, element, param) {
	        	  return this.optional(element) || value != param;
	        	}, "Please select a courier code");
	        
		    });
	    </script>
    </jsp:attribute>
   	<jsp:body>
		<div id="content" class="col-lg-10 col-sm-11">
			<div class="row">
				<div class="col-lg-12">
					<div class="box">
						<div class="box-header">
							<h2>Manifest</h2>
						</div>
						
						<div class="box-content">
							<div class="alert alert-danger">
							<input type="hidden" id="message" value="${message}" />
							</div>
							<%-- <form name="print" action='<c:url value="/Manifest/print" />' method="post" id="form" target="_blank">
								<div class="form-group">
								  <label class="col-sm-2 control-label"><span class="pull-right">Courier</span></label>
								  <div class="controls">
									<div class="input-group col-sm-4">
										<select id="name" name="name" class="form-control">
											<option value="--Select--">--Select--</option>
											<c:forEach var="name" items="${courierCodeList}">
												<option value="${name}">${name}</option>
											</c:forEach>
										</select>
									</div>	
								  </div>
								</div>							
								<div class="form-actions">
									<input type="hidden" value="Courier" name="type" id="type"/>
									<button id="submit" type="submit" class="col-sm-offset-2 btn btn-primary">Print</button>
								</div>
							</form> --%>
							<form name="addForm" action='<c:url value="/Manifest/searchByCourier" />' method="post" id="addForm" target="_blank">
								<div class="row" style="margin-bottom: 20px;margin-top: 20px">
									<div class="col-lg-12">
										<div class="col-lg-4">
											<label>Courier :</label>
											<select id="name" name="name" class="form-control">
												<option value="--Select--">--Select--</option>
												<c:forEach var="name" items="${courierCodeList}">
													<option value="${name}">${name}</option>
												</c:forEach>
											</select>
										</div>
										<div class="col-lg-4">
											<label>RTVLCode : </label> 
											<input type="text" name="rtvlCode" class="form-control" id="rtvlCode" />
										</div>
										<div class="col-lg-2">
											<label>Bag : </label> 
											<input type="text" name="bag" class="form-control" id="bag" />
										</div>
										<div class="col-lg-2" style="margin-top: 25px" >
											<input type="hidden" value="Courier" name="type" id="type"/>
											<button id="submit" type="submit" class="col-sm-offset-2 btn btn-primary">Add To Manifest</button>
										</div>
									</div>
								</div>							
							</form>
						</div>
					</div>
				</div>
			</div>
			<div id="manifestList" class="box">
				<div class="box-header" style="padding-left: 10px; padding-top: 10px">
					Manifest List :
				</div>
				<div class="box-content">
					<div class="row">
						<form name="print" action='<c:url value="/Manifest/print" />' method="post" id="print" target="_blank">
							<div class="col-lg-12" style="margin-top: 20px;">
								<table class="table table-striped table-bordered" id="manifestTable">
									<thead>
										<tr>
											<th>RTVL Code</th>
											<th>Courier Code</th>
											<th>AWB Number</th>
											<th>Seller</th>
											<th>Bag</th>
											<th>Action</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<div class="col-lg-12" style="margin-top: 20px; text-align: center;">
								<input type="hidden" name="name" id="courierCode" />
								<input type="hidden" value="Courier" name="type" id="type"/>								
								<button type="submit" id="download" class="btn btn-success">Download</button>
								<button type="submit" id="printManifest" class="btn btn-primary">Print</button>
								<button type="submit" id="shareManifest" class="btn btn-primary">Print & Share</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</tags:page>