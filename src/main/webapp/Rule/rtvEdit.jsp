<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tags:page>
	<jsp:attribute name="title">Post RTV Rules</jsp:attribute>
	<jsp:attribute name="script">
<script type="text/javascript">
	$(document).ready(function() {
		$("#form").validate();
	});
</script>
</jsp:attribute>
	<jsp:body>	
	<div id="content" class="col-lg-10 col-sm-11">
		<div class="box">
		<div class="box-header" style="padding-top: 10px;padding-left: 10px">
		Post RTV Rule
		</div>
			<div class="box-content">
				<div class="row" style="margin-bottom: 20px">
					<form name="rule" action='<c:url value= "/RtvRule/update"/>'
							method="post" id="form">
						<input type="hidden" name="id" value="${rule.id}">
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<label>Rule Name :</label> <input type="text" name="name"
										class="form-control required" value="${rule.name}" />
							</div>
							<div class="col-lg-4">
								<label>Days Passed Manifest Greater than :</label> <input
										type="text" name="value" class="form-control required"
										value="${rule.value}" />
							</div>
							<div class="col-lg-4"
									style="margin-top: 25px; text-align: center;">
								<input type="submit" value="Update" class="btn btn-primary">
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</tags:page>