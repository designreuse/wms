<%@ page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page import="com.snapdeal.entity.User"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	User sessionUser = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
	pageContext.setAttribute("activeWarehouse", sessionUser
			.getActiveWarehouse().getName());
	pageContext.setAttribute("userWarehouse",
			sessionUser.getUserWarehouses());
%>
<header class="navbar">
<div class="container">
	<!-- start: Header Menu -->
	<a class="navbar-brand col-md-2 col-sm-1 col-xs-2"
		href='<c:url value="/home" />'><img src="<c:url value="/img/warehouse.png"/>" alt="Logo" height="20px" width="180px"><!-- <span>Home</span> --> </a>
	<div class="nav-no-collapse header-nav" style="margin-left: 20%">
		<ul class="nav navbar-nav">
			<!-- start: User Dropdown -->
			<li class="dropdown"><a class="btn account dropdown-toggle"
				data-toggle="dropdown" href="#"
				style="margin-left: 20%; width: 200%">
					<div class="name">
						<span class="hello">${activeWarehouse}</span>
					</div> </a>
				<ul class="dropdown-menu" style="margin-left: 20%; width: 200%">
					<c:forEach items="${userWarehouse}" var="warehouse">
						<c:if test="${warehouse.enabled}">
							<li><a
								href='<c:url value="/Warehouse/change/${warehouse.id}"/>'>${warehouse.name}</a>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</li>
			<!-- end: User Dropdown -->
		</ul>
	</div>
	<div id="search" class="col-sm-4 col-xs-8 col-lg-3">
		<form action='<c:url value="/Search/inventory"/>' method="post">
			<select name="searchType">
				<option value="barcode">Barcode</option>
				<option value="location">Location</option>
			</select> <input type="text" placeholder="Search Inventory" name="code" /> <i
				class="fa fa-search"></i>
		</form>
	</div>
	<div class="nav-no-collapse header-nav">
		<ul class="nav navbar-nav pull-right">
			<!-- start: User Dropdown -->
			<li class="dropdown"><a class="btn account dropdown-toggle"
				data-toggle="dropdown" href="#">
					<div class="avatar">
						<img src="<c:url value="/img/user.png" />" alt="User">
					</div>
					<div class="user">
						<span class="hello">Welcome!</span> <span class="name"><%=SecurityContextHolder.getContext().getAuthentication()
					.getName()%></span>
					</div> </a>
				<ul class="dropdown-menu">
					<li><a href="<c:url value="/j_spring_security_logout" />"><i
							class="fa fa-cog"></i> Logout</a>
					</li>
				</ul></li>
			<!-- end: User Dropdown -->
		</ul>
	</div>
	<!-- end: Header Menu -->
</div>
</header>