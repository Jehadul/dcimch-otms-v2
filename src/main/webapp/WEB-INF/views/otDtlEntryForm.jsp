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
		<h2 class="pull-left" style="color: #428bca;">Overtime Details</h2>
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
						<div class="pull-left"></div>
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
								id="otDtlEntryFormId" class="form-horizontal"
								modelAttribute="overtimeDtlForm"
								action="${pageContext.request.contextPath}/generateOtDtl">

								<div class="form-group">
									<label class="col-lg-2 control-label">Year <span
										class="red">*</span>:
									</label>
									<div class="col-lg-5">
										<select id="otYear" name="otYear" class="form-control">
											<option value="">Select One</option>
											<option value="2017">2017</option>
											<option value="2018">2018</option>
											<option value="2019">2019</option>
											<option value="2020">2020</option>
											<option value="2021">2021</option>
											<option value="2022">2022</option>
											<option value="2023">2023</option>
											<option value="2024">2024</option>
											<option value="2025">2025</option>
											<option value="2026">2026</option>
											<option value="2027">2027</option>
											<option value="2028">2028</option>
											<option value="2029">2029</option>
											<option value="2030">2030</option>
										</select>
									</div>
								</div>

								<div class="form-group">
									<label class="col-lg-2 control-label">Month  <span
										class="red">*</span>: </label>
									<div class="col-lg-5">
										<select id="otMonth" name="otMonth" class="form-control">
											<option value="">Select One</option>
											<option value="January">January</option>
											<option value="February">February</option>
											<option value="March">March</option>
											<option value="April">April</option>
											<option value="May">May</option>
											<option value="June">June</option>
											<option value="July">July</option>
											<option value="August">August</option>
											<option value="September">September</option>
											<option value="October">October</option>
											<option value="November">November</option>
											<option value="December">December</option>
										</select>
									</div>
								</div>

								<div class="form-group">

									<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">										
										<button type="button"
											class="btn btn-sm btn-primary btn-success ai-save-update-btn">
											Generate</button>
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
	
	<div class="clearfix"></div>

	<script type="text/javascript">
		$(document).ready(function() {
			$('.ai-save-update-btn').click(function() {
				var flag = false;
				$(".ai-save-update-btn").prop('disabled', true);

				if ($('#otYear').val().trim().length > 0) {
					flag = false;
				} else {
					flag = true;
					$(".ai-save-update-btn").prop('disabled', false);
					w2alert("Please Select a Year.");
					return flag;
				}

				if ($('#otMonth').val().trim().length > 0) {
					flag = false;
				} else {
					flag = true;
					$(".ai-save-update-btn").prop('disabled', false);
					w2alert("Please Select a Month.");
					return flag;
				}				

				if (!flag) {
					w2confirm('Are you sure, Generate Overtime Details Sheet for this Month, If Not Exists?', function btn(answer) {
					    if(answer == 'Yes'){
					    	$(".ai-save-update-btn").prop('disabled', true);
							$('#otDtlEntryFormId').submit();
					    } else {
					    	$(".ai-save-update-btn").prop('disabled', false);
					    }
					});
					
				} else {
					$(".ai-save-update-btn").prop('disabled', false);
					return flag;
				}
			});

		});
		
	</script>

</body>
</html>