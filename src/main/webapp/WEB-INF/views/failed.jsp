<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="padd">
	<div align="center">
			<h4 style="color: red">
			<strong>Operation Not Successful!</strong>
			</h4>
			<a href='<c:if test="${!empty message}">${message}</c:if>'><button>Back</button></a>
			</div>
</div>