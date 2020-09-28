<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<!-- Title and other stuffs -->
<!-- <title>Dashboard - Lexicon Merchandise</title> -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="author" content="">


<style type="text/css">
td img {
	display: block;
	margin-left: auto;
	margin-right: auto;
}

.centered {
	width: 50px;
	margin: 0px, auto, 0px, auto;
}
</style>

<style>
.form-control {
    display: block;
    width: 100%;
    height: 32px;
    padding: 6px 12px;
    font-size: 13px;
    line-height: 1.42857143;
    color: #555;
    background-color: #fff;
    background-image: none;
    border: 1px solid #4CAF50;
    border-radius: 2px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,0.075);
    box-shadow: inset 0 1px 1px rgba(0,0,0,0.075);
    -webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;
    -o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
}
</style>

<link href="${pageContext.request.contextPath}/resource/w2ui/w2ui.min.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/resource/w2ui/w2ui.min.js"></script>
</head>

<body>

	<input type="hidden" value="${pageContext.request.contextPath}"
		id="contextPath">

	<div class="page-head">
		<h2 class="pull-left" style="color: #428bca;">Dashboard</h2>
		<div class="clearfix"></div>
	</div>

	

	<!--   <div class="matter"> -->
	<div class="container">

		<div class="row">

			<div class="col-md-12">

				<!-- Table -->

				<div class="row">

					<div class="col-md-12">

						<div class="widget">

							<div class="widget-head">
								<div class="pull-left">WELCOME</div>
								<div class="widget-icons pull-right">
									<a href="#" class="wminimize"><i class="fa fa-chevron-up"></i></a>
									<a href="#" class="wclose"><i class="fa fa-times"></i></a>
								</div>
								<div class="clearfix"></div>
							</div>

							<div class="widget-content">

								<div class="padd">
								<br />
								<!-- Form starts.  -->
								<form:form cssClass="form-horizontal" method="GET"
									id="companySetForm" class="form-horizontal"									
									action="${pageContext.request.contextPath}/index">
									<div class="form-group">
										<label class="col-lg-2 control-label">Select Unit<span
											class="red">*</span>:
										</label>
										<div class="col-lg-5">
											<select name="companyId" id="companyId" class="form-control">
												<c:if test="${!empty companyList}">
													<c:forEach items="${companyList}" var="company">
														<option value="${company.id}"> ${company.name}</option>
													</c:forEach>													
												</c:if>
											</select>
										</div>
									</div>
									
									<div class="form-group">
											<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">												
												<button type="button"
													class="btn btn-sm btn-primary ai-save-update-btn">
													Select</button>
											</div>											
									</div>
								</form:form>
								</div>
							</div>
						</div>


					</div>

				</div>

			</div>

		</div>

	</div>


	<!-- Mainbar ends -->
	<div class="clearfix"></div>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$('.ai-save-update-btn').click(function() {
				var flag = false;
				
				if ($('#companyId').val().trim().length > 0) {
					flag = false;
				} else {
					flag = true;
					w2alert("Please Select a Company.");
					return flag;
				}
				
				if (!flag) {
					$('#companySetForm').submit();
				} else {					
					return flag;
				}
			});

		});
		
	</script>
</body>
</html>
