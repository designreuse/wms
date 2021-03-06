<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<tags:page title="Add Property">
     <jsp:attribute name="script">
    	<script type="text/javascript">
		    $(document).ready(function() {
		    	  
			    // From validation
		        $("#form").validate({
		        
		            // Specify the validation rules
		            rules: {
		            	name: {
		                    required: true,
		                    remote: {
		                    	url : "/Wms/Property/checkPropertyName",
		                        type: "post",
		                        data: {
		                          name: function() {
		                            return $( "#name" ).val();
		                          }
		                        }
		                    }
		            	},
		            	value : "required" 
		            },
		            
		         	// Specify the validation error messages
		            messages: {
		            	name: {
		            		required: "Please enter name",
		            		remote: "Name already exists. Please enter different one."
		            	},
		            	value : "Please enter value"
		            },
		            
		            submitHandler: function(form) {
		                form.submit();
		            }
		        });
		    });
	    </script>
    </jsp:attribute>
   	<jsp:body>
		<div id="content" class="col-lg-10 col-sm-11">
			<div class="row">
				<div class="col-lg-12">
					<div class="box">
						<div class="box-header">
							<h2>Add Property</h2>
						</div>
						<div class="box-content">
							<c:if test="${not empty message}">
								<div class="alert alert-success">
									<button type="button" class="close" data-dismiss="alert">�</button>
									${message}
								</div>
							</c:if>
							<form name="property" action='<c:url value="/Property/save" />' method="post" id="form">
							  <div class="form-group">
								  <label class="col-sm-2 control-label">Name</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<input type="text" class="form-control" name="name" id="name" placeholder="Enter property name">
									</div>	
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-2 control-label">Value</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<textarea class="form-control" name="value" id="value" placeholder="Enter property value"></textarea>										
									</div>	
								  </div>
								</div>
								<div class="form-actions">
									<button id="submit" type="submit" class="col-sm-offset-2 btn btn-success">Submit</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</tags:page>