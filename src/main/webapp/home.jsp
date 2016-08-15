<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page import="com.snapdeal.entity.User"%>
<%-- <link href="<c:url value="/css/bootstrap-modal-carousel.min.css" />" rel="stylesheet"> --%>
 

<%
    User sessionUser = (User) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
    pageContext.setAttribute("activeWarehouse", sessionUser
            .getActiveWarehouse().getName());
    pageContext.setAttribute("userWarehouse",
            sessionUser.getUserWarehouses());
%>
<tags:page>
    <jsp:attribute name="title">Home Page</jsp:attribute>
    <jsp:attribute name="script">
    <jsp:include page="/Dashboard/pickupPending.jsp"></jsp:include>
    <jsp:include page="/Dashboard/putawayBreakup.jsp"></jsp:include>
    <jsp:include page="/Dashboard/totalDispatch.jsp"></jsp:include>
    <jsp:include page="/Dashboard/dailyNumbers.jsp"></jsp:include>
   
    <style>
    .modal-dialog {
            width: 600px;
             margin-left: 140px;
              margin-top: 30px;
            }
    .carousel-control.left {
            background-image: linear-gradient(to right,rgba(0,0,0,0.5) 0,rgba(0,0,0,0.0001) 0%);
            }
    .carousel-control.right{
            background-image: linear-gradient(to left,rgba(0,0,0,0.5) 0,rgba(0,0,0,0.0001) 0%);
            }
   
    </style>
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
            <div id="content" class="col-lg-10 col-sm-11">
            <input type="hidden" id="message" value="${message}" />
                <div class="box">
                    <div class="box-content">
                        <div class="row">
                            <div class="col-lg-12">   
                                <!--Code for carousel begins-->
                                    <jsp:include page="/Dashboard/carousel.jsp"></jsp:include>
                                <!--Code for carousel ends -->                           
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Button trigger modal -->
                <button type="button" class="btn btn-primary btn-sm col-md-offset-5" data-toggle="modal" data-target="#myModal">
                  View full screen
                </button>
            </div>
        </jsp:body>
</tags:page>


<jsp:include page="/Dashboard/modal.jsp"></jsp:include>

<script src="<c:url value="/js/bootstrap-modal-carousel.min.js"/>"></script>