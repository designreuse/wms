<jsp:include page="/logoutbar.jsp"></jsp:include>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>User Add Page</title>
<script type="text/javascript">
	$(document).ready(function() {
		$("#form").validate();
	});
</script>
</head>
<body>
	<div id="content" class="col-sm-12 full">
		<div class="box">
			<div class="box-content">
				<h2 align="center">Create User</h2>
				<div class="row">
					<div class="col-lg-12" style="text-align: center;margin-left: 25%;margin-right: 25%">
						<c:if test="${not empty message}">
							<div class="form-group">
								<div class="controls row">
									<div class="alert alert-success col-sm-6">${message}</div>
								</div>
							</div>
						</c:if>
					</div>
					<form action='<c:url value="/User/role/save/" />' method="post"
						name="role" id="form">
						<div class="col-lg-8" style="margin-left: 30%;margin-right: 30%;margin-top: 20px">
							<div class="col-lg-4">
								<label>Role Name : </label> <input type="text" name="role"
									class="form-control required">
							</div>
							<div class="col-lg-4" style="text-align: center; margin-top: 25px">
								<input type="submit" value="Save" class="btn btn-primary">
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>