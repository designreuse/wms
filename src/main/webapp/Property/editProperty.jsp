<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<tags:page title="Edit Property">
     <jsp:attribute name="script">
    	<script type="text/javascript">
		    $(document).ready(function() {
		    	
		    	// Hide Email Menu on Page load.
		    	$("#valueMenu").hide();
		    	  
			    // From validation
		        $("#form").validate({
		        
		            // Specify the validation rules
		            rules: {
		            	name: { notEqual: "--Select--" },
		            	value: "required"
		            },
		            
		         	// Specify the validation error messages
		            messages: {
		            	value : "Please enter value"
		            },
		            
		            submitHandler: function(form) {
		                form.submit();
		            }
		        });
		        
		        $('#name').on('change', function() {
	    			 if($(this).val() != "--Select--"){
		    		  	  $("#valueMenu").show();
		    			  // Get the value for the selected Property.
		    			  var request = $.ajax({
								type : "POST",
								url : "/Wms/Property/getValue",
								data: {
			                        name: $(this).val()
			                    }
							});
							request
									.done(function(result) {
										 if(result != null)
											 $("#value").val(result);
								});
	    			 }
	 	    		  else
	 	    			$("#valueMenu").hide();
		    		});
		    });
		    
	        jQuery.validator.addMethod("notEqual", function(value, element, param) {
	        	  return this.optional(element) || value != param;
	        	}, "Please select a property");
	    </script>
    </jsp:attribute>
   	<jsp:body>
		<div id="content" class="col-lg-10 col-sm-11">
			<div class="row">
				<div class="col-lg-12">
					<div class="box">
						<div class="box-header">
							<h2>Edit Property</h2>
						</div>
						<div class="box-content">
							<c:if test="${not empty message}">
								<div class="alert alert-success">
									<button type="button" class="close" data-dismiss="alert">×</button>
									${message}
								</div>
							</c:if>
							<form name="reports" action='<c:url value="/Property/updateProperty" />' method="post" id="form">
								<div class="form-group">
								  <label class="col-sm-2 control-label">Property Name</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<select id="name" name="name" class="form-control">
											<option value="--Select--">--Select--</option>
											<c:forEach var="name" items="${propertyName}">
												<option value="${name}">${name}</option>
											</c:forEach>
										</select>
									</div>	
								  </div>
								</div>
								<div id="valueMenu">								
									<div class="form-group">
									  <label class="col-sm-2 control-label">Value</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<!-- <input type="text" class="form-control" name="value" id="value" placeholder="Enter Value"> -->
											<textarea class="form-control" name="value" id="value" placeholder="Enter property value"></textarea>
										</div>	
									  </div>
									</div>
									<div class="form-actions">
										<button id="submit" type="submit" class="col-sm-offset-2 btn btn-success">Submit</button>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</tags:page>