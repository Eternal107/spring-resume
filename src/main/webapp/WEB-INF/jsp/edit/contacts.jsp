<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form"   	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="resume"	tagdir="/WEB-INF/tags"%>

<resume:edit-tab-header selected="contacts" />

<div class="panel panel-default">
	<div class="panel-body">
		<h4 class="data-header">Дополнительные контакты: skype, ссылки на социальные профили, и т.д. </h4>
		<resume:form-display-error-if-invalid formName="contactsForm" />
		<form:form modelAttribute="contactsForm" action="/edit/contacts" cssClass="form-horizontal data-form">
			<div class="row">
				<div class="col-sm-7">
					<resume:form-has-error path="skype" />
					<div class="form-group ${hasError ? 'has-error' : ''}">
						<label for="inputSkype" class="col-sm-4 control-label">Skype</label>
						<div class="col-sm-8">
							<form:input path="skype" class="form-control" id="inputSkype" placeholder="Example: ivan-ivanovich" />
							<resume:form-error path="skype" />
						</div>
					</div>
				</div>
				<div class="col-sm-5 help-block">
					<blockquote>
						Если у Вас нет skype-имени, значит пришло время его создать
					</blockquote>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-7">
					<resume:form-has-error path="telegram" />
					<div class="form-group ${hasError ? 'has-error' : ''}">
						<label for="inputTelegram" class="col-sm-4 control-label">Telegram</label>
						<div class="col-sm-8">
							<form:input path="telegram" class="form-control" id="inputTelegram" placeholder="Example: ivan-ivanovich" />
							<resume:form-error path="telegram" />
						</div>
					</div>
					<resume:form-has-error path="facebook" />
					<div class="form-group ${hasError ? 'has-error' : ''}">
						<label for="inputFacebook" class="col-sm-4 control-label">Facebook</label>
						<div class="col-sm-8">
							<form:input path="facebook" class="form-control" id="inputFacebook" placeholder="Example: https://facebook.com/ivan-ivanovich" />
							<resume:form-error path="facebook" />
						</div>
					</div>
					<resume:form-has-error path="linkedin" />
					<div class="form-group ${hasError ? 'has-error' : ''}">
						<label for="inputLinkedin" class="col-sm-4 control-label">Linkedin</label>
						<div class="col-sm-8">
							<form:input path="linkedin" class="form-control" id="inputLinkedin" placeholder="Example: https://linkedin.com/ivan-ivanovich" />
							<resume:form-error path="linkedin" />
						</div>
					</div>
					<resume:form-has-error path="github" />
					<div class="form-group ${hasError ? 'has-error' : ''}">
						<label for="inputGithub" class="col-sm-4 control-label">Github</label>
						<div class="col-sm-8">
							<form:input path="github" class="form-control" id="inputGithub" placeholder="Example: https://github.com/ivan-ivanovich" />
							<resume:form-error path="github" />
						</div>
					</div>
					<resume:form-has-error path="stackoverflow" />
					<div class="form-group ${hasError ? 'has-error' : ''}">
						<label for="inputStackoverflow" class="col-sm-4 control-label">Stackoverflow</label>
						<div class="col-sm-8">
							<form:input path="stackoverflow" class="form-control" id="inputStackoverflow" placeholder="Example: https://stackoverflow.com/ivan-ivanovich" />
							<resume:form-error path="stackoverflow" />
						</div>
					</div>
				</div>
				<div class="col-sm-5 help-block">
					<blockquote>
						В настройках современных социальных сетях есть возможность создания именованных ссылок на профиль, вместо id12345678, поэтому имеет смысл
						воспользоваться данными возможностями
					</blockquote>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-7">
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-9">
							<button type="submit" class="btn btn-primary">Сохранить</button>
						</div>
					</div>
				</div>
				<div class="col-sm-5"></div>
			</div>
		</form:form>
	</div>
</div>