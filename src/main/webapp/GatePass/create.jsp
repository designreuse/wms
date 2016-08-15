<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page title="Warehouse">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#form").validate();
						$("form")
								.submit(
										function(e) {
											$("#form").validate();
											if ($("#status_name").hasClass(
													"duplicate")) {
												e.preventDefault();
												noty({
													text : "Duplicate Status. Please correct and proceed.",
													type : "error",
													layout : "topRight"
												});
											}
										});
						$("#status_name")
								.on(
										"change",
										function() {
											var status_name = $(this).val();
											var request = $
													.ajax({
														type : "POST",
														url : "/Wms/Gatepass/checkStatus",
														data : {
															name : status_name
														}
													});
											request
													.done(function(msg) {
														if (msg == "failure") {
															$("#status_name")
																	.addClass(
																			"duplicate");
															noty({
																text : "Status already exists. Please Select a different status.",
																type : "error",
																layout : "topRight"
															});
														} else {
															$("#status_name")
																	.removeClass(
																			"duplicate");
														}
													});
											request
													.fail(function(jqXHR,
															textStatus) {
														noty({
															text : "Failed to check Status.",
															type : "error",
															layout : "topRight"
														});
													});
										});
					});
</script>
</jsp:attribute>
	<jsp:body>
	<div id="content" class="col-lg-10 col-sm-11">
		<div class="box">
			<div class="box-header" style="padding-top: 10px; padding-left: 10px">
				Gate Pass Status</div>
			<div class="box-content">
				<div class="row">
					<form name="status" action='<c:url value="/Gatepass/save" />'
						method="post" id="form">
						<input type="hidden" name="id" value="${status.id}" />
						<div class="col-lg-12"
							style="margin-top: 20px; margin-bottom: 20px">
							<div class="col-lg-4">
								<label>Status : </label> <input type="text" name="status"
									class="form-control required" value="${status.status}"
									id="status_name">
							</div>
							<div class="col-lg-8"
								style="text-align: center; margin-top: 20px">
								<input type="submit" value="Save" class="btn btn-primary">
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</tags:page>