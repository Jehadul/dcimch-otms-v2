<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<!-- Title and other stuffs -->
<!--   <title>Dashboard - Lexicon Merchandise</title> -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="author" content="">

<link href="<c:url value='/resource/w2ui/w2ui-1.5.rc1.css' />"
	rel="stylesheet"></link>
<script type="text/javascript"
	src="<c:url value='/resource/w2ui/w2ui-1.5.rc1.js' />"></script>
<link href="<c:url value='/resource/w2ui/w2ui-1.5.rc1.min.css' />"
	rel="stylesheet"></link>
<script type="text/javascript"
	src="<c:url value='/resource/w2ui/w2ui-1.5.rc1.min.js' />"></script>


<link
	href="https://cdn.datatables.net/buttons/1.2.2/css/buttons.dataTables.min.css"
	rel="stylesheet">
<script src="https://code.jquery.com/jquery-1.12.3.js"></script>
<script
	src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
<script
	src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.flash.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
<script
	src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/pdfmake.min.js"></script>
<script
	src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/vfs_fonts.js"></script>
<script
	src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
<script
	src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.print.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/js/additional-methods.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/js/jquery.validate.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resource/js/resizeImage.js"></script>
<style type="text/css">
<
c:url var ="findURL " value ="/checkDepartment.htm " /> <style>.add-on,
	textarea, input[type="text"], input[type="password"], input[type="datetime"],
	input[type="datetime-local"], input[type="date"], input[type="month"],
	input[type="time"], input[type="week"], input[type="number"], input[type="email"],
	input[type="url"], input[type="search"], input[type="tel"], input[type="color"],
	input[type="file"], .uneditable-input {
	border-color: #4CAF50 !important;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border-radius: 2px;
	-webkit-box-shadow: none;
	-moz-box-shadow: none;
	box-shadow: none;
}
</style>
<script>
	
</script>

</head>

<body>

	${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}


	<div class="page-head">
		<c:choose>
			<c:when test="${edit}">
				<h2 class="pull-left" style="color: #5BBC2E;">Edit Unit</h2>
			</c:when>
			<c:otherwise>
				<h2 class="pull-left" style="color: #5BBC2E;">Add New Unit</h2>
			</c:otherwise>
		</c:choose>
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
							<c:if test="${!empty message}">
								<div style="color: olive; font-size: 20px;">${message}</div>
							</c:if>

							<br />

							<!-- Form starts.  -->
							<form:form id="departmentForm" cssClass="form-horizontal"
								method="POST" modelAttribute="command" name="reg"
								action="${pageContext.request.contextPath}/saveCompany.htm"
								class="form-horizontal" role="form"
								enctype="multipart/form-data">
								<form:hidden id="id" path="id" value="${company.id}"
									class="form-control" />



								<div class="form-group">
									<label class="col-lg-2 control-label">Unit Name <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<form:input id="name" path="name" value="${company.name}"
											class="form-control" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-lg-2 control-label">Unit Address <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<textarea class="form-control" id="address" name="address">${company.address}</textarea>
										<%-- 										<form:textarea id="address" path="address" --%>
										<%-- 											value="${company.address}" class="form-control" /> --%>
									</div>
								</div>
								<div class="form-group">
									<label class="col-lg-2 control-label">Unit Email <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<form:input id="email" path="email" value="${company.email}"
											class="form-control" />
										<span class="email_valid_err" style="color: red"></span> <input
											type="hidden" value="0" id="emailAlready"> <input
											type="hidden" id="email_update" value="${company.email}" />

									</div>
								</div>
								<div class="form-group">
									<label class="col-lg-2 control-label">Phone <span
										style="color: red">*</span></label>
									<div class="col-lg-5">
										<form:input id="phone" path="phone" value="${company.phone}"
											class="form-control" />
									</div>
								</div>
								<c:choose>
									<c:when test="${edit}">
										<div class="form-group">
											<label class="col-lg-2 control-label">Unit Keyword
												<span style="color: red">*</span>
											</label>
											<div class="col-lg-5">
												<form:input id="companyKeyword" path="companyKeyword"
													readonly="true" value="${company.companyKeyword}"
													class="form-control" />
												<span class="companyKeyword_valid_err" style="color: red"></span>
												<input type="hidden" value="0" id="companyKeywordAlready">
											</div>
										</div>

									</c:when>
									<c:otherwise>

										<div class="form-group">
											<label class="col-lg-2 control-label">Unit Keyword
												<span style="color: red">*</span>
											</label>
											<div class="col-lg-5">
												<form:input id="companyKeyword" path="companyKeyword"
													value="${company.companyKeyword}" class="form-control" />
												<span class="companyKeyword_valid_err" style="color: red"></span>
												<input type="hidden" value="0" id="companyKeywordAlready">
											</div>
										</div>
									</c:otherwise>
								</c:choose>

								<div class="form-group">
									<label class="col-lg-2 control-label">Web-Site</label>
									<div class="col-lg-5">
										<form:input id="website" path="website"
											value="${company.website}" class="form-control" />
									</div>
								</div>
								<c:choose>
									<c:when test="${edit}">
										<div class="form-group">
											<label class="col-lg-2 control-label">Status <span
												style="color: red">*</span></label>
											<div class="col-lg-5 ">
												<c:choose>
													<c:when test="${company.status eq '1'}">
														<form:radiobutton path="status" value="1"
															checked="checked" />
										Active
										<form:radiobutton path="status" value="0" />
										Inactive
											
										</c:when>
													<c:otherwise>
														<form:radiobutton path="status" value="1" />
										Active
										<form:radiobutton path="status" value="0" checked="checked" />
										Inactive
											
										</c:otherwise>
												</c:choose>

											</div>
										</div>

									</c:when>
									<c:otherwise>

										<div class="form-group">
											<label class="col-lg-2 control-label">Status <span
												style="color: red">*</span></label>
											<div class="col-lg-5 ">
												<form:radiobutton path="status" value="1" checked="checked" />
												Active
												<form:radiobutton path="status" value="0" />
												Inactive
											</div>
										</div>
									</c:otherwise>
								</c:choose>



								<div class="form-group">
									<label class="col-lg-2 control-label">Unit Logo</label> <input
										type="hidden" value="" id="imageUplodable" name="companyLogo">
									<div class="col-lg-5">
										<input id="companyLogo" type="file" name="file"
											value="${company.companyLogo}"
											class="image-upload form-control" />
										<div class="images">
											<c:if test="${edit}">
												<img width="200" height="150"
													src="${pageContext.request.contextPath}/companyLogo/${company.companyKeyword}">
										</div>

										</c:if>




									</div>


								</div>






								<div class="form-group">
									<c:choose>
										<c:when test="${edit}">
											<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">
												<button type="submit" name="submit_btn" value="update"
													class="btn btn-sm btn-primary btn-success req-save-update-btn">
													Update</button>
											</div>
											<div class="col-lg-1 col-xs-4">
												<a class="btn btn-sm btn-danger"
													href="${pageContext.request.contextPath}/newDeptForm.htm">Cancel</a>
											</div>
										</c:when>
										<c:otherwise>
											<div class="col-lg-offset-2 col-lg-1 col-xs-4" id="">
												<button type="submit" name="submit_btn" value="save"
													class="btn btn-sm btn-primary req-save-update-btn">Save</button>
											</div>
											<div class="col-lg-1 col-xs-4">
												<button type="reset" class="btn btn-sm btn-danger ">Reset</button>
											</div>
										</c:otherwise>
									</c:choose>
								</div>
							</form:form>

						</div>

					</div>
					<div class="widget-foot">
						<!-- Footer goes here -->
					</div>
				</div>

				<!-- Table Start-->


				<!-- Table End-->

			</div>
		</div>
	</div>
	<!-- table ends -->
	<div class="clearfix"></div>

</body>


<script>
	$(document).ready(function() {
		$(".image-upload").ImageResize({
			maxWidth : 200,
			onImageResized : function(imageData) {
				$(".images").html($("<img/>", {
					src : imageData
				}));
				//console.log(imageData)
				$("#imageUplodable").val(imageData);
			}
		});
	});
	$(function() {
		$('#email')
				.on(
						"change",
						function() {
							if ($('#email_update').val() != $('#email').val()) {
								jQuery
										.ajax({
											type : "POST",
											url : "${pageContext.request.contextPath}/checkUnicKEmail",
											dataType : 'json',
											data : {
												email : $('#email').val()
											},
											success : function(res) {
												if (res.companyInfo != null) {
													$('#emailAlready').val(1);
													$(".email_valid_err")
															.text(
																	"Email Address Already in Use.");
												} else {
													$(".email_valid_err").text(
															"");
													$('#emailAlready').val(0);
												}
											}
										});
							} else {
								$(".email_valid_err").text("");
								$('#emailAlready').val(0);
							}

						});
		$('#companyKeyword')
				.on(
						"change",
						function() {
							jQuery
									.ajax({
										type : "POST",
										url : "${pageContext.request.contextPath}/checkUnicKcompanyKeyword",
										dataType : 'json',
										data : {
											companyKeyword : $(
													'#companyKeyword').val()
										},
										success : function(res) {
											if (res.companyInfo != null) {
												$('#companyKeywordAlready')
														.val(1);
												$(".companyKeyword_valid_err")
														.text(
																"Company Keyword Already in Use.");
											} else {
												$(".companyKeyword_valid_err")
														.text("");
												$('#companyKeywordAlready')
														.val(0);
											}
										}
									});
						});

		$('.req-save-update-btn')
				.on(
						"click",
						function(e) {

							if ($('#emailAlready').val() == 1) {
								e.preventDefault();
								$(".email_valid_err").text(
										"Email Address Already in Use.");
							} else {
								$(".email_valid_err").text("");
							}

							if ($('#companyKeywordAlready').val() == 1) {
								e.preventDefault();
								$(".companyKeyword_valid_err")
										.text(
												"Keyword  Already in Use Please try another one.");
							} else {
								$(".companyKeyword_valid_err").text("");
							}
						});
		// Initialize form validation on the registration form.
		// It has the name attribute "registration"
		$("form[name='reg']").validate({
			// Specify validation rules
			rules : {
				// The key name on the left side is the name attribute
				// of an input field. Validation rules are defined
				// on the right side
				name : "required",
				address : "required",
				email : {
					required : true,
					email : true
				},
				phone : "required",
				status : "required",
				companyKeyword : "required",

			},

			// Make sure the form is submitted to the destination defined
			// in the "action" attribute of the form when valid
			submitHandler : function(form) {
				form.submit();
			}
		});
	});
</script>
</html>