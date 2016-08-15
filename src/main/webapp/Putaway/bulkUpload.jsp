<jsp:include page="/logoutbar.jsp"></jsp:include>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
				<h2 align="center">Putaway Upload</h2>
				<div class="row" style="margin-bottom: 20px">
					<form action='<c:url value="/Putaway/action" />' method="post"
						id="form" name="fileBean" enctype="multipart/form-data">
						<div class="col-lg-12" style="margin-top: 20px">
							<!-- <div class="col-lg-4">
								<label>CRPCode : </label> <input type="text" name="code"
									class="form-control required" />
							</div> -->
							<div class="col-lg-4">
								<label>CRI Code :</label> <input type="file" name="postedFile"
									class="form-control required" />
							</div>
							<div class="col-lg-4" style="text-align: center;">
								<input type="submit" class="btn btn-primary" value="Get Putaway"
									style="margin-top: 25px" />
							</div>
							<div class="col-lg-4">
								<a href='<c:url value="/Upload/template" />'
									class="btn btn-success" style="margin-top: 25px;">Download
									Template</a>
							</div>
						</div>
					</form>
					<c:if test="${found}">
						<h1>${result.location}</h1>
						<h1>${result.status}</h1>
					</c:if>
				</div>
			</div>
		</div>
	</div>
</body>
</html>