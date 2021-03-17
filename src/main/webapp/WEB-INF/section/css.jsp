<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<link rel="stylesheet" href="/static/css/resume-common.min.css">

<sec:authorize access="isAuthenticated()">
	<link rel="stylesheet" href="/static/css/resume-ex.min.css">
</sec:authorize>
