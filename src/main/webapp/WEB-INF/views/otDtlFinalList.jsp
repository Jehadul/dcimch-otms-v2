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

<c:url var="monthlyOtDltList" value="/getMonthlyOtDltList" />
</head>

<body>

	<input type="hidden" value="${pageContext.request.contextPath}"
		id="contextPath">

	<div class="page-head">
		<h2 class="pull-left" style="color: #428bca;">Overtime for the
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
								<div class="pull-left">Overtime for the Month of ${otMonth} - ${otYear}</div>
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
		var monthlyOtDltListURL = '<c:out value="${monthlyOtDltList}"/>';
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
					field : 'employee.name',
					caption : 'Employee Name',
					size : '200px',
					sortable : true,
					searchable : 'int',
					resizable : true
				}, {
					field : 'employee.empId',
					caption : 'Employee ID',
					size : '100px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'employee.department.name',
					caption : 'Department',
					size : '150px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'employee.designation.name',
					caption : 'Designation',
					size : '150px',
					resizable : true,
					sortable : true,
					searchable : 'text'
				}, {
					field : 'joinDate',
					caption : 'Date of Joining',
					size : '120px',
					resizable : true,
					sortable : true,
					searchable : 'text'
				}, {
					field : 'basicSalary',
					caption : 'Basic Salary',
					size : '100px',
					resizable : true,
					sortable : true,
					searchable : 'text',
				}, {
					field : 'handCash',
					caption : 'Hand Cash',
					size : '100px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'otUnit',
					caption : 'Unit',
					size : '100px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'totalOtDays',
					caption : 'Total Days',
					size : '100px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'totalPayment',
					caption : 'Total Payment',
					size : '100px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'employee.bankAccNo',
					caption : 'Bank A/C No',
					size : '110px',
					sortable : true,
					searchable : 'text',
					resizable : true
				}, {
					field : 'remarks',
					caption : 'Remarks',
					size : '200px',
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
			$
					.ajax({
						type : "post",
						url : monthlyOtDltListURL,
						async : false,
						data : JSON.stringify({otYear:otYear, otMonth:otMonth}),
						contentType : "application/json",
						success : function(response) {

							if (response != null || response != "") {
								result = JSON.parse(response);
								w2ui['grid'].records = result;
								/* for (var i = 0; i < result.length; i++) {
									w2ui['grid'].records
											.push({
												recid : i + 1,
												slNo : i + 1,
												punchId : result[i].punchId,
												name : result[i].name,
												status : '<p style="text-align:center"><span class="status" >Present&nbsp;&nbsp;<i class="fa fa-circle" style="color:green"></i></span> </p>',
												office : result[i].office,
												designation : result[i].designation,
												date : result[i].date,
												inTime : result[i].inTime,
												lateIn : result[i].lateIn,
												earlyOut : result[i].earlyOut,
												outTime : result[i].outTime,
												totalTime : result[i].totalTime,
												device : result[i].device,
												action : '<a title="Edit" target="_blank" class="btn btn-primary custom-width" href="editPunch?id='
														+ result[i].id
														+ '">&nbsp;&nbsp;<i class="fa fa-pencil" aria-hidden="true"></i>&nbsp;&nbsp;</a>'
											});
								} */
								w2ui.grid.refresh();
							}
						},
						error : function() {
							w2alert("Server Error!");
						}
					});
			// generate data
			/* var fname = ['Vitali', 'Katsia', 'John', 'Peter', 'Sue', 'Olivia', 'Thomas', 'Sergei', 'Snehal', 'Avinash', 'Divia'];
			var lname = ['Peterson', 'Rene', 'Johnson', 'Cuban', 'Twist', 'Sidorov', 'Vasiliev', 'Yadav', 'Vaishnav']; */
			// add 25k records
			/* for (var i = 0; i < 25000; i++) {
			    w2ui['grid'].records.push({ 
			        recid : i+1,
			        personid: i+1,
			        fname: fname[Math.floor(Math.random() * fname.length)], 
			        lname: lname[Math.floor(Math.random() * lname.length)],
			        email: 'vm@gmail.com', manager: '--',
			        snumber: Math.floor(Math.random() * 8000),
			        sdate: (new Date(Math.floor(Math.random() * 20000) * 100000000)).getTime()
			    });
			} */
			//w2ui.grid.refresh();
			$('#main').w2render('grid');
		});
		
	
	</script>

</body>
</html>