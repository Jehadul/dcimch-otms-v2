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

<!-- <script type="text/javascript"
	src="http://rawgit.com/vitmalina/w2ui/master/dist/w2ui.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="http://rawgit.com/vitmalina/w2ui/master/dist/w2ui.min.css" /> -->
	
<link href="${pageContext.request.contextPath}/resource/w2ui/w2ui.min.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/resource/w2ui/w2ui.min.js"></script>

<c:url var="monthlyOtSummaryList" value="/getMonthlyOtSummaryList" />
</head>

<body>

	<input type="hidden" value="${pageContext.request.contextPath}"
		id="contextPath">

	<div class="page-head">
		<h2 class="pull-left" style="color: #428bca;">Overtime Summary for the
			Month of ${otMonth} - ${otYear}</h2>
		<div class="clearfix"></div>
	</div>

	<input type="hidden" value="${otYear}" id="otYear">
	<input type="hidden" value="${otMonth}" id="otMonth">

	<!--   <div class="matter"> -->
	<div class="container">

		<div class="row">

			<div class="col-md-12">

				<!-- Table -->

				<div class="row">

					<div class="col-md-12">

						<div class="widget">

							<div class="widget-head">
								<div class="pull-left">Overtime Summary for the Month of ${otMonth} - ${otYear}</div>
								<div class="widget-icons pull-right">
									<a href="#" class="wminimize"><i class="fa fa-chevron-up"></i></a>
									<a href="#" class="wclose"><i class="fa fa-times"></i></a>
								</div>
								<div class="clearfix"></div>
							</div>

							<div class="widget-content">

								<div class="table-responsive padd">
									<div id="main" style="width: 100%; height: 500px;"></div>
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
		var monthlyOtSummaryListURL = '<c:out value="${monthlyOtSummaryList}"/>';
		// widget configuration
		var config = {
			grid : {
				name : 'grid',
				show : {
					footer : true,
					toolbar : true
				},
				columns : [ {
					field : 'slNo',
					caption : 'SL No.',
					size : '70px',
					sortable : true,
					searchable : 'int',
					resizable : true
				}, {
					field : 'department.name',
					caption : 'Department',
					size : '150px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'totalOfPervThreeMonth',
					caption : '<center>${prevThreeMonth}-${year3}<center>',
					size : '150px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'totalOfPervTwoMonth',
					caption : '<center>${prevTwoMonth}-${year2}<center>',
					size : '150px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'totalOfPervOneMonth',
					caption : '<center>${prevOneMonth}-${year1}<center>',
					size : '150px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'totalOfThisMonth',
					caption : '<center>${otMonth}-${otYear}<center>',
					size : '150px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'differLastTwoMonth',
					caption : '<center> Difference Between <br>${otMonth}-${otYear} <br>& <br>${prevOneMonth}-${year1}<center>',
					size : '150px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'remarks',
					caption : 'Remarks',
					size : '300px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'overallRemarks',
					caption : 'Overall Remarks',
					size : '300px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}]
			}
		};

		var resultData = new Array();
		var otYear = $('#otYear').val();
		var otMonth = $('#otMonth').val();
		
		$(function() {
			// initialization
			$().w2grid(config.grid);
			//get Data from server by taleb
			$.ajax({
				type : "post",
				url : monthlyOtSummaryListURL,
				async : false,
				data : JSON.stringify({otYear:otYear, otMonth:otMonth}),
				contentType : "application/json",
				success : function(response) {

					if (response != null || response != "") {
						result = JSON.parse(response);
						w2ui['grid'].records = result;
						
						w2ui.grid.refresh();
					}
				},
				error : function() {
					w2alert("Server Error!");
				}
			});
			
			$('#main').w2render('grid');
		});
		
	
	</script>

</body>
</html>