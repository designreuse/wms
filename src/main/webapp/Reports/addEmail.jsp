<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<tags:page title="Add/Edit Email Address">
     <jsp:attribute name="script">
    	<script type="text/javascript">
		    $(document).ready(function() {
		    	
		    	// Hide Email Menu on Page load.
		    	$("#emailMenu").hide();
		    	  
			    // From validation
		        $("#form").validate({
		        
		            // Specify the validation rules
		            rules: {
		            	name: { notEqual: "--Select--" }
		            },
		            
		            submitHandler: function(form) {
		                form.submit();
		            }
		        });
		        
		        $('#name').on('change', function() {
	    			 if($(this).val() != "--Select--"){
		    		  	  $("#emailMenu").show();
		    			  // Get the Email Address for the selected Report.
		    			  var request = $.ajax({
								type : "POST",
								url : "/Wms/Reports/getEmailAddress",
								data: {
			                        name: $(this).val()
			                    }
							});
							request
									.done(function(result) {
										 if(result != null)
											 $("#toEmail").val(result);
								});
	    			 }
	 	    		  else
	 	    			$("#emailMenu").hide();
		    		});
		    });
		    
	        jQuery.validator.addMethod("notEqual", function(value, element, param) {
	        	  return this.optional(element) || value != param;
	        	}, "Please select a report");
	    </script>
    </jsp:attribute>
   	<jsp:body>
		<div id="content" class="col-lg-10 col-sm-11">
			<div class="row">
				<div class="col-lg-12">
					<div class="box">
						<div class="box-header">
							<h2>Add/Edit Email Address</h2>
						</div>
						<div class="box-content">
							<c:if test="${not empty message}">
								<div class="alert alert-success">
									<button type="button" class="close" data-dismiss="alert">×</button>
									${message}
								</div>
							</c:if>
							<form name="reports" action='<c:url value="/Reports/saveEmailAddress" />' method="post" id="form">
								<div class="form-group">
								  <label class="col-sm-2 control-label">Report Name</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<select id="name" name="name" class="form-control">
											<option value="--Select--">--Select--</option>
											<c:forEach var="name" items="${reportsName}">
												<option value="${name}">${name}</option>
											</c:forEach>
										</select>
									</div>	
								  </div>
								</div>
								<div id="emailMenu">								
									<div class="form-group">
									  <label class="col-sm-2 control-label">Email Address</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<input type="text" class="form-control" name="toEmail" id="toEmail" placeholder="Comma separated email address">
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