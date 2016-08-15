<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:resources>
	<jsp:attribute name="title">Login Form</jsp:attribute>
	<jsp:body>
			<div id="content" class="col-sm-12 full">
				<div class="row">
					<div class="login-box">
						<div class="header">Login to WMS</div>
						<form method="post"
						action="<c:url value='/j_spring_security_check' />">
							<fieldset class="col-sm-12">
								<c:if test="${not empty message}">
									<div class="form-group">
										<div class="controls row">
											<c:choose>
												<c:when test="${success}">
													<div class="alert alert-success col-sm-12">
														${message}</div>
												</c:when>
												<c:otherwise>
													<div class="alert alert-danger col-sm-12">${message}
													</div>
												</c:otherwise>
											</c:choose>
										</div>
									</div>
								</c:if>
								<div class="form-group">
									<div class="controls row">
										<div class="input-group col-sm-12">
											<input type="text" name="username" class="form-control"
											placeholder="Username"><span
											class="input-group-addon"><i class="fa fa-user"></i>
											</span>
										</div>
									</div>
								</div>

								<div class="form-group">
									<div class="controls row">
										<div class="input-group col-sm-12">
											<input type="password" name="password" class="form-control"
											placeholder="Password"> <span
											class="input-group-addon"><i class="fa fa-key"></i> </span>
										</div>
									</div>
								</div>
								<div class="row">

									<input type="submit" class="btn btn-lg btn-primary col-xs-12"
									value="Login" />

								</div>
							</fieldset>
						</form>

						<div class="clearfix"></div>

					</div>
				</div>
				<!--/row-->

			</div>
</jsp:body>
</tags:resources>