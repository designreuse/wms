<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<tags:page title="Add Receiver">
     <jsp:attribute name="script">
    	<script type="text/javascript">
		    $(document).ready(function() {
		    	  
			    // From validation
		        $("#form").validate({
		        
		            // Specify the validation rules
		            rules: {
		            	code: {
		                    required: true,
		                    remote: {
		                    	url : "/Wms/Receiver/checkReceiver",
		                        type: "post",
		                        data: {
		                          code: function() {
		                            return $( "#code" ).val();
		                          }
		                        }
		                    }
		            	},
		            	email: {
		            		required: true,
		            		email: true
		            	}
		            },
		            
		         	// Specify the validation error messages
		            messages: {
		            	code: {
		            		required: "Please enter vendor code",
		            		remote: "Receiever already exists. Please enter different one."
		            	},
		            	email: {
		            		required: "Please enter email address",
		            		email: "Please enter valid email address"
		            	}
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
							<h2>Add Receiver</h2>
						</div>
						<div class="box-content">
							<c:if test="${not empty message}">
								<div class="alert alert-success">
									<button type="button" class="close" data-dismiss="alert">×</button>
									${message}
								</div>
							</c:if>
							<form name="receiverEmail" action='<c:url value="/Receiver/save" />' method="post" id="form">
							  <div class="form-group">
								  <label class="col-sm-2 control-label">Code</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<input type="text" class="form-control" name="code" id="code" placeholder="Enter receiver code">
									</div>	
								  </div>
								</div>
							  <div class="form-group">
								  <label class="col-sm-2 control-label">Name</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<input type="text" class="form-control" name="name" id="name" placeholder="Enter receiver name">
									</div>	
								  </div>
								</div>
							  <div class="form-group">
								  <label class="col-sm-2 control-label">Email</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<input type="text" class="form-control" name="email" id="email" placeholder="Enter receiver email">
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