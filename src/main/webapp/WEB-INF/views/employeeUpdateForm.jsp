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
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="author" content="">

<link
	href="${pageContext.request.contextPath}/resource/select2/select2.min.css"
	rel="stylesheet" type="text/css">
<script
	src="${pageContext.request.contextPath}/resource/select2/select2.min.js"></script>

<link
	href="${pageContext.request.contextPath}/resource/w2ui/w2ui-1.5.rc1.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resource/w2ui/w2ui-1.5.rc1.css"
	rel="stylesheet">
<script
	src="${pageContext.request.contextPath}/resource/w2ui/w2ui-1.5.rc1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/w2ui/w2ui-1.5.rc1.js"></script>

<style type="text/css">
.table td.fit, .table th.fit {
	white-space: nowrap;
	width: 1%;
}
</style>


</head>

<body>
	<!-- Page heading start -->
	<div class="page-head">

		<h2 class="pull-left" style="color: #428bca;">EMPLOYEE Edit FORM</h2>
		<div class="clearfix"></div>
	</div>
	<!-- Page heading ends -->

	<!-- Matter -->

	<!--   <div class="matter"> -->
	<div class="container">

		<div class="row">

			<div class="col-md-12">


				<div class="widget wgreen">

					<div class="widget-head">
						<div class="pull-left">
						<a href="${pageContext.request.contextPath}/downloadEmpSample" class="btn btn-success btn-sm"> Download Sample File</a>
						</div>
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
							<form:form cssClass="form-horizontal" method="POST"
								id="employeeForm" class="form-horizontal"
								modelAttribute="employeeForm" enctype="multipart/form-data"
								action="${pageContext.request.contextPath}/employeeGSUpload">



								<div class="form-group">
									<label class="col-lg-2 control-label">Excel File :</label>
									<div class="col-lg-5">
										<input type="file" name="excelFile" id="excelFile" accept=".xls" class="form-control">
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-lg-2 control-label">Ignore First Row (Title) ?:</label>
									<div class="col-lg-5">
										<select name="firstRowIgnore" id="firstRowIgnore" class="form-control">
										<option value = "1"> Yes </option>
										<option value = "0"> No </option>
										</select>
									</div>
								</div>

								<div class="form-group">

									<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">										
										<button type="button"
											class="btn btn-sm btn-primary req-save-update-btn">
											Upload</button>
									</div>
									<div class="col-lg-1 col-xs-4">
										<button type="reset" class="btn btn-sm btn-danger ">Reset</button>
									</div>
								</div>
							</form:form>
						</div>

					</div>
					<div class="widget-foot">
						<!-- Footer goes here -->
					</div>
				</div>

				<!-- Table -->

			</div>

		</div>

	</div>

	<!-- table ends -->

	<!-- Matter ends -->

	<!-- Mainbar ends -->
	<div class="clearfix"></div>


	<script type="text/javascript">
		$(document).ready(function() {
			$('.req-save-update-btn').click(function() {
				var flag = false;
				var x = $('#excelFile').val();
				var res = x.split(".");
				$(".req-save-update-btn").prop('disabled', true);

				if ($('#excelFile').val().trim().length > 0) {
					flag = false;
				} else {
					flag = true;
					$(".req-save-update-btn").prop('disabled', false);
					w2alert("Please Upload a File.");
					return flag;
				}	
				
				if (res[1] == 'xls') {
					flag = false;
				} else {
					flag = true;
					$(".req-save-update-btn").prop('disabled', false);
					w2alert("Please Upload (.xls) Type File.");
					return flag;
				}	

				if (!flag) {
					$(".req-save-update-btn").prop('disabled', true);
					$('#employeeForm').submit();
				} else {
					$(".req-save-update-btn").prop('disabled', false);
					return flag;
				}
			});

		});

	</script>

</body>
</html>