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

<c:url var="monthlyOtDltList" value="/getMonthlyOtDltList" />
<c:url var="saveMonthlyOtDltList" value="/saveMonthlyOtDltList" />

<!-- <link rel="stylesheet" type="text/css" href="http://rawgit.com/vitmalina/w2ui/master/dist/w2ui.min.css" /> -->
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
		<h2 class="pull-left" style="color: #428bca;">Overtime Details
			Entry Sheet for ${otMonth}, ${otYear}</h2>
			
		<div class="pull-right" style="color: #428bca;"><a id="updateOtDtlListBtn" href="#" class="btn btn-warning btn-sm">Update Overtime Detail</a></div>
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
								<div class="pull-left">Overtime Details Entry Sheet for
									${otMonth}, ${otYear}</div>
								<div class="widget-icons pull-right">
									<a href="#" class="wminimize"><i class="fa fa-chevron-up"></i></a>
									<a href="#" class="wclose"><i class="fa fa-times"></i></a>
								</div>
								<div class="clearfix"></div>
							</div>

							<div class="widget-content table-responsive">
							
								<div id="grid" style="width: 100%; height: 500px;"></div>
								<br>
								<button class="w2ui-btn" onclick="showChanged()">Submit</button>
								<button class="w2ui-btn" id="otDtlFinalSubmit">Final Submit</button>

							</div>
						</div>
					</div>
					
					<form action="${pageContext.request.contextPath}/otDtlFinalSubmit" method="post" hidden="true" id="otDtlFinalSubmitForm">
						<input type="hidden" value="${otYear}" name="otYear">
						<input type="hidden" value="${otMonth}" name="otMonth">
					</form>

				</div>

			</div>

		</div>

	</div>
	<!-- table ends -->
	<!-- Mainbar ends -->
	<div class="clearfix"></div>
	
	<script type="text/javascript">
	var monthlyOtDltListURL = '<c:out value="${monthlyOtDltList}"/>';
	var saveMonthlyOtDltListURL = '<c:out value="${saveMonthlyOtDltList}"/>';
	
	var resultData = new Array();
	var otYear = $('#otYear').val();
	var otMonth = $('#otMonth').val();
	

	$(function () {
		
		loadTableData();
	});

	function showChanged() {	
		var tableData = w2ui['grid'].getChanges();
		if(tableData.length > 0){
			$.ajax({
			    type : "post",
			    url : saveMonthlyOtDltListURL,
			    async : false,
			    data : JSON.stringify({overtimeSheetList:w2ui['grid'].getChanges()}),
			    contentType : "application/json",
			    success : function(response) {
			    	var result = JSON.parse(response);
			    	if(result == 'success'){
			    		location.reload();
			    		w2alert("Please Wait...");
			    	}else{
			    		w2alert("Please try again after sometimes!");
			    	}
			    	
			    },
			    error : function() {
			    	w2alert("Server Error!!!");
			    }
			});	
		}	       
	}
	
	function loadTableData(){
		
		$.ajax({
		    type : "post",
		    url : monthlyOtDltListURL,
		    async : false,
		    data : JSON.stringify({otYear:otYear, otMonth:otMonth}),
		    contentType : "application/json",
		    success : function(response) {
		    	result = JSON.parse(response);			
		    	//w2alert(result);
		    	resultData = result;
		    	//var jsonObj = $.parseJSON('[' + response + ']');
		    },
		    error : function() {
		    }
		   });
		
		$('#grid').w2grid({ 
	        name: 'grid', 
	        show: { 
	            toolbar: true,
	            footer: true
	        },
	        multiSearch: true,
	        searches: [
	            { field: 'slNo', caption: 'SL. No.', type: 'int' },
	            { field: 'recid', caption: 'Row ID ', type: 'int' },
	            { field: 'employee.name', caption: 'Employee Name', type: 'text' },
	            { field: 'employee.empId', caption: 'Employee ID.', type: 'text' },
	            { field: 'employee.department.name', caption: 'Department', type: 'text' },
	            { field: 'employee.designation.name', caption: 'Designation', type: 'text' },
	            { field: 'joinDate', caption: 'Join Date', type: 'text' },
	            { field: 'employee.bankAccNo', caption: 'Bank Account No', type: 'text' }
	        ],
	        columns: [                
	            { field: 'slNo', caption: 'SL. No.', size: '60px', sortable: true, resizable: false },
	            { field: 'recid', caption: 'Row ID', size: '70px', sortable: true, resizable: false },
	            { field: 'employee.name', caption: 'Employee', size: '200px', sortable: true, resizable: true, 
	                editable: false
	            },
	            { field: 'employee.empId', caption: 'Employee ID', size: '100px', sortable: true, resizable: true, 
	                editable: false
	            },
	            { field: 'employee.department.name', caption: 'Department', size: '120px', sortable: true, resizable: true, 
	                editable: false
	            },
	            { field: 'employee.designation.name', caption: 'Designation', size: '120px', sortable: true, resizable: true, 
	                editable: false
	            },
	            { field: 'joinDate', caption: 'Date of Joining', size: '90px', sortable: true, resizable: true,
	                editable: false 
	            },
	            { field: 'basicSalary', caption: 'Basic Salary', size: '100px', sortable: true, resizable: true,
	                editable: false
	            },
	            { field: 'handCash', caption: 'Hand Cash', size: '100px', sortable: true, resizable: true,
	                editable: { type: 'int', min: 0}
	            },
	            { field: 'otUnit', caption: 'Unit (8% of Basic)', size: '110px', sortable: true, resizable: true,
	                editable: false
	            },
	            { field: 'totalOtHours', caption: 'Total OT (Hours)', size: '100px', sortable: true, resizable: true,
	                editable: { type: 'float', min: 0.0, max: 1000.0 }
	            },
	            { field: 'otTotalDays', caption: 'Total OT (Days)', size: '100px', sortable: true, resizable: true,
	                editable: false
	            },
	            { field: 'totalPayment', caption: 'Total Payment', size: '100px', sortable: true, resizable: true,
	                editable: false
	            },
	            { field: 'employee.bankAccNo', caption: 'Bank A/C No', size: '120px', sortable: true, resizable: true, 
	                editable: false
	            },
	            { field: 'remarks', caption: 'Remarks', size: '200px', sortable: true, resizable: true, 
	                editable: { type: 'text' }
	            }
	        ],
	        records: resultData
	    }); 
		
	}
	
	$(document).on("click", "#otDtlFinalSubmit", function(){
		var tableData = w2ui['grid'].getChanges();
		if(tableData.length > 0){
			w2alert("Your Changes Are Not Saved. Please Click Submit Button First.");
		} else {
			w2confirm('After final submit you can\'t change any data forever, Do you Agree?', function btn(answer) {
			    if(answer == 'Yes'){		    	
					$('#otDtlFinalSubmitForm').submit();
					w2alert("Please Wait...");
			    } 
			});
		}
	});
	
	$(document).on("click", "#updateOtDtlListBtn", function(){
		
		var otYear = $('#otYear').val();
		var otMonth = $('#otMonth').val();
		var contextPath = $('#contextPath').val();
		w2confirm('Do you Agree?', function btn(answer) {
			if(answer == 'Yes'){		    	
				//$('#otDtlFinalSubmitForm').submit();
					window.location.href = contextPath+"/updateOtDtlList?otYear=" + otYear + "&otMonth=" + otMonth;
					w2alert("Please Wait...");
			    } 
			});
		});
	</script>
	
	<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script> -->
	<!-- <script type="text/javascript" src="http://rawgit.com/vitmalina/w2ui/master/dist/w2ui.min.js"></script> -->
	<script src="${pageContext.request.contextPath}/resource/w2ui/w2ui.min.js"></script>
</body>
</html>