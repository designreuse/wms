<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="GatePass">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(function() {
		$("#form").validate();
		if ($("#message").val() != "" && $("#success").val() == "true") {
			noty({
				text : $("#message").val(),
				type : "success",
				layout : "topRight"
			});
		} 
	});
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-sm-11 col-lg-10">
		<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Gate Pass Panel</div>
			<div class="box-content">
				<c:if test="${not empty message and success ne true}">
					<div class="alert alert-danger">
						<button type="button" class="close" data-dismiss="alert">×</button>
						${message}
					</div>
				</c:if>
				<input type="hidden" id="message" value="${message}" /> <input
					type="hidden" id="success" value="${success}" />
				<div class="row" style="margin-bottom: 20px; margin-top: 20px">
					<form name="fileData" action='<c:url value="/Gatepass/packages"/>'
						method="post" enctype="multipart/form-data" id="form">
						<div class="col-lg-12">
							<div class="col-lg-4">
								<input type="file" name="postedFile"
									class="form-control required" />
							</div>
							<div class="col-lg-8" style="text-align: center;">
								<input type="submit" class="btn btn-primary" value="Submit" />
								<a href='<c:url value="/Gatepass/template" />'
									class="btn btn-success" style="margin-left: 20px">Download
									Template</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</tags:page>