<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="resume" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html>
<head>
	<title>My Resume</title>
	<meta charset="utf-8">
	<meta http-equiv="x-ua-compatible" content="ie=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<jsp:include page="../section/css.jsp"/>
</head>
<body class="resume">
<jsp:include page="../section/navigation.jsp"/>
<section class="container">
	<div class="panel panel-danger">
		<div class="panel-heading">
			<h3 class="panel-title">
				<i class="fa fa-exclamation-circle"></i> Error
			</h3>
		</div>
		<div class="panel-body">
			<h4>Application can't process your request! Please try again later...</h4>
		</div>
	</div>
</section>
<jsp:include page="../section/js.jsp"/>
</body>
</html>