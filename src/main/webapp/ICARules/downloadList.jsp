<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<tags:page title="Download List">
     <jsp:attribute name="script">
    	<script type="text/javascript">
		    $(document).ready(function() {
		    	  
			    // From validation
		        $("#form").validate({
		        
		            // Specify the validation rules
		            rules: {
		            	type: { notEqual: "--Select--" }
		            },
		            
		            submitHandler: function(form) {
		                form.submit();
		            }
		        });
		    });
		    
	        jQuery.validator.addMethod("notEqual", function(value, element, param) {
	        	  return this.optional(element) || value != param;
	        	}, "Please select a type");
	    </script>
    </jsp:attribute>
   	<jsp:body>
		<div id="content" class="col-lg-10 col-sm-11">
			<div class="row">
				<div class="col-lg-12">
					<div class="box">
						<div class="box-header">
							<h2>Download List</h2>
						</div>
						<div class="box-content">
							<form name="downloadList" action='<c:url value="/ICARules/download" />' method="post" id="form">
								<div class="form-group">
								  <label class="col-sm-2 control-label">Type</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<select id="type" name="type" class="form-control">
											<option value="--Select--">--Select--</option>
											<option value="City">City</option>
											<option value="State">State</option>
											<option value="Courier Code">Courier Code</option>
										</select>
									</div>	
								  </div>
								</div>
								<div class="form-actions">
									<button id="submit" type="submit" class="col-sm-offset-2 btn btn-success">Download</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</tags:page>