<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="/framework-resource.jsp"></jsp:include>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test Page</title>
</head>
<body>
	<div id="content" class="col-sm-12 full">
		<div class="box">
			<div class="box-content">
				<h2 align="center">Existing SubCategories</h2>
				<ol>
					<c:forEach var="subcat" items="${issueCategory}">
						<li>${subcat.description}</li>
					</c:forEach>
				</ol>
			</div>
		</div>
	</div>
</body>
</html>