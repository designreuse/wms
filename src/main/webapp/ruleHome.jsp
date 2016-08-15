<jsp:include page="/logoutbar.jsp"></jsp:include>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title>Home Page</title>
</head>
<body>
	<div id="content" class="col-sm-12 full">
		<div class="box">
			<div class="box-content">
				<h2 align="center">Rule Home Page</h2>
				<center>
					<div class="row">
						<div class="col-lg-12" style="margin-top: 20px">
							<div class="col-lg-4">
								<a class="btn btn-primary" href="<c:url value="/Rule/create" />">Create
									New Rule</a>
							</div>
							<div class="col-lg-4">
								<a class="btn btn-primary" href="/Wms/Rule/view">Update Rules</a>
							</div>
							<div class="col-lg-4">
								<a class="btn btn-primary" href="<c:url value="/Rule/bulkCreate" />">Create
									New Bulk Upload Rule</a>
							</div>
						</div>
					</div>
				</center>
			</div>
		</div>
	</div>
</body>
</html>