<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<script src="/static/js/resume-common.min.js"></script>
<sec:authorize access="isAuthenticated()">
	<script src="/static/js/resume-ex.min.js"></script>
</sec:authorize>
<script src="https://www.google.com/recaptcha/api.js" async="async" defer="defer"></script>
<script src="/static/js/messages.jsp"></script>