<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form"   	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="resume" 	tagdir="/WEB-INF/tags"%>

<div class="panel panel-info small-center-block">
	<div class="panel-heading">
		<h3 class="panel-title">
			<i class="fa fa-user-plus"></i> Укажите Ваши персональные данные
		</h3>
	</div>
	<div class="panel-body">
		<c:if test="${param.error!=null && param.error.length()>0}">
			<div  class="alert alert-danger">
				${param.error}
			</div>
		</c:if>
		<resume:form-display-error-if-invalid formName="profileForm" />
		<form:form action="/sign-up" modelAttribute="profileForm" method="post">
			<resume:form-has-error path="email"/>
			<div class="form-group ${hasError ? 'has-error has-feedback' : ''}">
				<label for="email">Email</label>
				<form:input path="email" id="email" cssClass="form-control" placeholder="Example: MyResume@gmail.com" required="required"/>
				<resume:form-error path="email" />
			</div>

			<div class="form-group ${hasError ? 'has-error has-feedback' : ''}">
				<label class="control-label" for="password">Новый пароль</label> 
				<form:password path="password" id="password" cssClass="form-control" required="required"/>
				<resume:form-error path="password" />
			</div>
			
			<resume:form-has-error path="confirmPassword"/>
			<div class="form-group ${hasError ? 'has-error has-feedback' : ''}">
				<label class="control-label" for="confirmPassword">Подтверждение пароля</label> 
				<form:password path="confirmPassword" id="confirmPassword" cssClass="form-control" required="required"/>
				<resume:form-error path="confirmPassword" />
			</div>
		    <div class="form-group">
		    	<button type="submit" class="btn btn-success center-block">Зарегистрироваться</button>
			</div>

	    	<div class="form-group">
	    		<a href="/oauth2/authorize/google" class="btn btn-primary center-block"><i class="fa fa-google"></i>&nbsp;&nbsp;Sign up with Google</a>
	    	</div>

			<div class="form-group">
				<a href="/oauth2/authorize/facebook" class="btn btn-primary center-block"><i class="fa fa-facebook"></i>&nbsp;&nbsp;Sign up with Facebook</a>
			</div>

			<div class="form-group">
				<a href="/oauth2/authorize/github" class="btn btn-primary center-block"><i class="fa fa-github"></i>&nbsp;&nbsp;Sign up with Github</a>
			</div>
			<hr/>

			<div class="text-center">
				<a href="/sign-in" >Уже есть аккаунт</a>
			</div>
			</form:form>
	</div>
</div>