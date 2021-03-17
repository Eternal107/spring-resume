<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" 	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form"   	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="resume" 	tagdir="/WEB-INF/tags"%>

<div class="panel panel-info small-center-block">
	<div class="panel-heading">
		<h3 class="panel-title">
			<i class="fa fa-sign-in"></i> Вход в Ваш личный кабинет 
		</h3>
	</div>
	<div class="panel-body">
		<form:form action="/sign-in-handler" method="post">

			<c:if test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION != null}">
				<div class="alert alert-danger" role="alert">
					<button type="button" class="close" data-dismiss="alert" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
						${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message }
					<c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session" />
				</div>
			</c:if>
			<div class="form-group">
				<label for="uid">Логин</label> <input id="uid" name="uid" class="form-control" placeholder="UID или Email или Phone" required autofocus >
			</div>
			<div class="form-group">
				<label for="password">Пароль</label> <input id="password" type="password" name="password" class="form-control" placeholder="Password" required>
			</div>
			<div class="form-group">
				<label><input type="checkbox" name="remember-me" value="true"> Запомнить меня</label>
			</div>
			<hr/>
           <c:if test="${showRecaptcha}">
			    <spring:eval expression="@environment.getProperty('recaptcha.validation.site-key')" var="siteKey"/>
			    <div class="form-group">
				    <div class="g-recaptcha" data-sitekey="${siteKey}"></div>
			    </div>
		   </c:if>

			<div class="form-group">
			<button type="submit" class="btn btn-success center-block">Войти</button>
			</div>
			<div class="form-group">
				<a href="/oauth2/authorize/google" class="btn btn-primary center-block"><i class="fa fa-google"></i>&nbsp;&nbsp;Sign in with Google</a>
			</div>

			<div class="form-group">
				<a href="/oauth2/authorize/facebook" class="btn btn-primary center-block"><i class="fa fa-facebook"></i>&nbsp;&nbsp;Sign in with Facebook</a>
			</div>

			<div class="form-group">
				<a href="/oauth2/authorize/github" class="btn btn-primary center-block"><i class="fa fa-github"></i>&nbsp;&nbsp;Sign in with Github</a>
			</div>

			<hr/>
			<div class="text-center">
			<a href="/restore" >Восстановить доступ</a>
			</div>
		</form:form>
	</div>
</div>