<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="Bulk Upload">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(function() {
		$("#form").validate();
		if ($("#message").val() != "" && $("#success").val() != "") {
			noty({
				text : $("#message").val(),
				type : "success",
				layout : "topRight"
			});

			/* if($("#removed").val() != "")
			{
				/* noty({
					text : $("#removed").val(),
					type : "warning",
					layout : "topRight"
				}); 
				alert(" These suborders were duplicate and not uploaded \n"+$("#removed").val());
			} */
		} else if ($("#message").val() != "") {
			noty({
				text : $("#message").val(),
				type : "error",
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
				Bulk Upload</div>
			<div class="box-content">
			<input type="hidden" id="message" value="${message}" />
			<input type="hidden" id="success" value="${success}" />
			<input type="hidden" id="removed" value="${removed}" />
				<div class="row" style="margin-bottom: 20px; margin-top: 20px">
					<form name="fileData" action='<c:url value="/Upload/save"/>'
							method="post" enctype="multipart/form-data" id="form">
						<div class="col-lg-12">
						<c:if test="${not empty removed}">
								<div class="alert alert-danger">
									<button type="button" class="close" data-dismiss="alert">×</button>
			  							Below Records are already present :
			  						<br> ${removed}
								</div>
						</c:if>
						</div>
						<div class="col-lg-12">
							<div class="col-lg-4">
								<select class="form-control required" name="ruleId">
									<option value="">Select Rule Name</option>
									<c:forEach var="rule" items="${rules}">
										<option value="${rule.id}">${rule.name}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-lg-4">
								<input type="file" name="postedFile"
										class="form-control required" />
							</div>
							<div class="col-lg-4" style="text-align: center;">
								<input type="submit" class="btn btn-primary" value="Save" /> <a
										href='<c:url value="/Upload/template" />'
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