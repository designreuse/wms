<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<tags:page title="ICA Rules Bulk Upload">
     <jsp:attribute name="script">
    	<script type="text/javascript">
	    	$(document).ready(function() {
	    		$("#form").validate({
		        
		            // Specify the validation rules
		            rules: {
		            	postedFile: "required"
		            },
		            
		            // Specify the validation error messages
		            messages: {
		            	postedFile: "Please select a file"
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
							<h2>ICA Rules Bulk Upload</h2>
						</div>
						<div class="box-content">
							<c:if test="${not empty message}">
								<c:choose>
									<c:when test="${success}">
										<div class="alert alert-success">
											<button type="button" class="close" data-dismiss="alert">�</button>
											${message}
										</div>
									</c:when>
									<c:otherwise>
										<div class="alert alert-danger" style="overflow:auto; height:160px">
											<button type="button" class="close" data-dismiss="alert">�</button>
											${message}
										</div>
									</c:otherwise>
								</c:choose>
							</c:if>
							<form action='<c:url value="/ICARules/saveBulkUpload" />' method="post" enctype="multipart/form-data" id="form" name="fileData">
								<div class="form-group">
								  <label class="col-sm-2 control-label">ICA Rules</label>
								  <div class="controls">
									<div class="input-group col-sm-6">
										<input type="file" class="form-control" name="postedFile" id="postedFile">
									</div>
									<label class="col-sm-offset-2">
										(Please upload a .csv file with Action either <code>Edit</code> or <code>Add_Replace</code>)
									</label>
								  </div>
								</div>
								<div class="form-actions">
									<a href='<c:url value="/ICARules/templateBulkUpload" />' class="col-sm-offset-2 btn btn-success">Download Template</a>
									<button type="submit" class="btn btn-primary">Submit</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</tags:page>