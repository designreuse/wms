<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<tags:page title="Edit Reciever">
     <jsp:attribute name="script">
    	<script type="text/javascript">
		    $(document).ready(function() {
		    	
		    	// From validation
		        $("#form").validate({
		        
		            // Specify the validation rules
		            rules: {
		            	email: {
		            		required: true,
		            		email: true
		            	}
		            },
		            
		         	// Specify the validation error messages
		            messages: {
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
							<h2>Search Receiver</h2>
						</div>
						<div class="box-content">
							<c:if test="${not empty message}">
								<div class="alert alert-danger">
									<button type="button" class="close" data-dismiss="alert">×</button>
									${message}
								</div>
							</c:if>
							<form name="receiverEmailSearch" action='<c:url value="/Receiver/info" />' method="get" id="formSearch">
								<div class="form-group">
								  <label class="col-sm-2 control-label">Code </label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<input type="text" class="form-control" name="code" id="code" placeholder="Enter receiver code">
									</div>	
								  </div>
								</div>
								<div class="form-actions">
									<button id="submit" type="submit" class="col-sm-offset-2 btn btn-primary">Search</button>
								</div>
							</form>
						</div>
					</div>
					<c:if test="${not empty receiverEmail}">
						<div class="box">
							<div class="box-header">
								<h2>Edit Receiver</h2>
							</div>
							<div class="box-content">
								<c:if test="${not empty messageUpdate}">
									<div class="alert alert-success">
										<button type="button" class="close" data-dismiss="alert">×</button>
										${messageUpdate}
									</div>
								</c:if>
								<form name="receiverEmail" action='<c:url value="/Receiver/update" />' method="post" id="form">
								  <div class="form-group hidden">
									  <label class="col-sm-2 control-label">Id</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<input type="text" class="form-control" name="id" id="id" value="${receiverEmail.id}" readonly>
										</div>	
									  </div>
									</div>
								  <div class="form-group">
									  <label class="col-sm-2 control-label">Code</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<input type="text" class="form-control" name="code" id="code" value="${receiverEmail.code}" readonly>
										</div>	
									  </div>
									</div>
								  <div class="form-group">
									  <label class="col-sm-2 control-label">Name</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<input type="text" class="form-control" name="name" id="name" value="${receiverEmail.name}" placeholder="Enter receiver name">
										</div>	
									  </div>
									</div>
								  <div class="form-group">
									  <label class="col-sm-2 control-label">Email</label>
									  <div class="controls">
										<div class="input-group col-sm-6">
											<input type="text" class="form-control" name="email" id="email" value="${receiverEmail.email}" placeholder="Enter receiver email">
										</div>	
									  </div>
									</div>
									<div class="form-actions">
										<button id="submit" type="submit" class="col-sm-offset-2 btn btn-success">Submit</button>
									</div>
								</form>
							</div>
						</div>
					</c:if>
				</div>
			</div>
		</div>
	</jsp:body>
</tags:page>