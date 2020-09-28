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

<c:url var="monthlyOtSummaryList" value="/getMonthlyOtSummaryList" />
<c:url var="saveMonthlyOtSummaryList" value="/saveMonthlyOtSummaryList" />

<!-- <link rel="stylesheet" type="text/css"
	href="http://rawgit.com/vitmalina/w2ui/master/dist/w2ui.min.css" /> -->
	
<link href="${pageContext.request.contextPath}/resource/w2ui/w2ui.min.css" rel="stylesheet">


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

.col-xs-1, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9, .col-xs-10, .col-xs-11, .col-xs-12 {
    float: none;
}
</style>



<script type="text/javascript">
	function goBack() {
		window.history.back();
	}

	$(document).ready(function() {
		$('#table1').DataTable({
			dom : 'Bfrtip',
			buttons : [ 'copy', 'csv', 'excel', 'print', {
				extend : 'pdfHtml5',
				orientation : 'landscape',
				pageSize : 'LEGAL'
			} ]
		});

	});
</script>

</head>

<body>

	<input type="hidden" value="${pageContext.request.contextPath}"
		id="contextPath">

	<div class="page-head">
		<h2 class="pull-left" style="color: #428bca;">Overtime Summary
			Entry Sheet for ${otMonth}, ${otYear}</h2>
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
								<div class="pull-left">Overtime Summary Entry Sheet for
									${otMonth}, ${otYear}</div>
								<div class="widget-icons pull-right">
									<a href="#" class="wminimize"><i class="fa fa-chevron-up"></i></a>
									<a href="#" class="wclose"><i class="fa fa-times"></i></a>
								</div>
								<div class="clearfix"></div>
							</div>

							<div class="widget-content table-responsive">

								<div id="grid" style="width: 100%; height: 500px;"></div>
								
								<div class="col-xs-12">
									<div class="col-xs-1">
										<label class="level-control">Overall Remarks : </label>
									</div>
									<div class="col-xs-11">
										<textarea rows="2" class="form-control" name="overallRemarks"
											id="overallRemarks"></textarea>
											<input type="hidden" id="overallRemarksVariable">
									</div>
								</div>
								<br> <div class="clearfix"></div>
								<button class="w2ui-btn" onclick="showChanged()">Submit</button>
								<button class="w2ui-btn" id="otSummaryFinalSubmitBtn">Final
									Submit</button>

							</div>
						</div>
					</div>

					<form action="${pageContext.request.contextPath}/otSummaryFinalSubmit"
						method="post" hidden="true" id="otSummaryFinalSubmitForm">
						<input type="hidden" value="${otYear}" name="otYear"> <input
							type="hidden" value="${otMonth}" name="otMonth">
					</form>

				</div>

			</div>

		</div>

	</div>
	<!-- table ends -->
	<!-- Mainbar ends -->
	<div class="clearfix"></div>

	<script type="text/javascript">
		var monthlyOtSummaryListURL = '<c:out value="${monthlyOtSummaryList}"/>';
		var saveMonthlyOtSummaryListURL = '<c:out value="${saveMonthlyOtSummaryList}"/>';

		var resultData = new Array();
		var otYear = $('#otYear').val();
		var otMonth = $('#otMonth').val();

		$(function() {

			loadTableData();
		});

		function showChanged() {
			var tableData = w2ui['grid'].getChanges();
			var overallRemarks = $('#overallRemarks').val();
			var overallRemarksVariable = $('#overallRemarksVariable').val();
			if(overallRemarksVariable != overallRemarks || tableData.length > 0){
				$.ajax({
					type : "post",
					url : saveMonthlyOtSummaryListURL,
					async : false,
					data : JSON.stringify({
						overallRemarks:overallRemarks,
						overtimeSheetList : w2ui['grid'].getChanges(),
						otYear : otYear,
						otMonth : otMonth
					}),
					contentType : "application/json",
					success : function(response) {
						var result = JSON.parse(response);
						if (result == 'success') {
							location.reload();
							w2alert("Please Wait...");
						} else {
							w2alert("Please try again after sometimes!");
						}

					},
					error : function() {
						w2alert("Server Error!!!");
					}
				});
			}			
			
		}

		function loadTableData() {

			$.ajax({
				type : "post",
				url : monthlyOtSummaryListURL,
				async : false,
				data : JSON.stringify({
					otYear : otYear,
					otMonth : otMonth
				}),
				contentType : "application/json",
				success : function(response) {
					result = JSON.parse(response);
					resultData = result;					
					$('#overallRemarks').val(result[0].overallRemarks);
					$('#overallRemarksVariable').val(result[0].overallRemarks);
					
				},
				error : function() {
				}
			});

			$('#grid').w2grid({
				name : 'grid',
				show : {
					toolbar : true,
					footer : true
				},
				multiSearch : true,
				searches : [ {
					field : 'slNo',
					caption : 'SL. No.',
					type : 'int'
				}, {
					field : 'recid',
					caption : 'Row ID ',
					type : 'int'
				}, {
					field : 'department.name',
					caption : 'Department',
					type : 'text'
				}],
				columns : [ {
					field : 'slNo',
					caption : '<center>SL. No.<center>',
					size : '80px',
					sortable : true,
					resizable : false
				}, {
					field : 'recid',
					caption : '<center>Row ID<center>',
					size : '100px',
					sortable : true,
					resizable : false
				}, {
					field : 'department.name',
					caption : '<center>Department<center>',
					size : '150px',
					sortable : true,
					resizable : true,
					editable : false
				}, {
					field : 'totalOfPervThreeMonth',
					caption : '<center>${prevThreeMonth}-${year3}<center>',
					size : '120px',
					sortable : true,
					resizable : true,
					editable : false
				}, {
					field : 'totalOfPervTwoMonth',
					caption : '<center>${prevTwoMonth}-${year2}<center>',
					size : '120px',
					sortable : true,
					resizable : true,
					editable : false
				}, {
					field : 'totalOfPervOneMonth',
					caption : '<center>${prevOneMonth}-${year1}<center>',
					size : '120px',
					sortable : true,
					resizable : true,
					editable : false
				}, {
					field : 'totalOfThisMonth',
					caption : '<center>${otMonth}-${otYear}<center>',
					size : '120px',
					sortable : true,
					resizable : true,
					editable : false
				}, {
					field : 'differLastTwoMonth',
					caption : '<center> Difference Between <br>${otMonth}-${otYear} <br>& <br>${prevOneMonth}-${year1}<center>',
					size : '120px',
					sortable : true,
					resizable : true,
					editable : false
				}, {
					field : 'remarks',
					caption : '<center>Remarks<center>',
					size : '600px',
					sortable : true,
					resizable : true,
					editable : {
						type : 'text'
					}
				} ],
				records : resultData
			});

		}

		$(document)
				.on(
						"click",
						"#otSummaryFinalSubmitBtn",
						function() {
							var tableData = w2ui['grid'].getChanges();
							if (tableData.length > 0) {
								w2alert("Your Changes Are Not Saved. Please Click Submit Button First.");
							} else {
								w2confirm(
										'After final submit you can\'t change any data forever, Do you Agree?',
										function btn(answer) {
											if (answer == 'Yes') {
												$('#otSummaryFinalSubmitForm')
														.submit();
												w2alert("Please Wait...");
											}
										});
							}
						});
	</script>

	<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script> -->
	<!-- <script type="text/javascript"
		src="http://rawgit.com/vitmalina/w2ui/master/dist/w2ui.min.js"></script> -->
	<script src="${pageContext.request.contextPath}/resource/w2ui/w2ui.min.js"></script>
</body>
</html>