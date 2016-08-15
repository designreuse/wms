<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<tags:page title="Test">
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(function() {
		if ($("#message").val() != "") {
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
				Awb Testing Desk</div>
			<div class="box-content">
				<input type="hidden" id="message" value="${message}" />
				<div class="row" style="margin-bottom: 20px; margin-top: 20px">
					<form action='<c:url value="/Test/getAwb" />' method="post"
							id="form">
						<div class="col-lg-12">
							<div class="col-lg-4">
								<label>Pincode : </label> <input type="text" name="pincode"
										class="form-control" id="barcode" />
							</div>
							<div class="col-lg-4" style="text-align: center;">
								<input type="submit" class="btn btn-primary" value="Get Awb"
										style="margin-top: 25px" id="putawayButton" />
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</jsp:body>
</tags:page>