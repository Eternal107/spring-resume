<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"      	uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn"     	uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form"   	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="resume" 	tagdir="/WEB-INF/tags"%>

<resume:edit-tab-header selected="info" />

<div class="panel panel-default edit-profile">
	<div class="panel-body">
		<h1 class="text-center">О себе</h1>
		<hr />
		<h4 class="data-header">Несколько слов о себе, которые предоставят Вам преимущества перед другими кандидатами </h4>
		<resume:form-display-error-if-invalid formName="infoForm"/>
		<form:form modelAttribute="profile" action="/edit/info" method="post" cssClass="form-horizontal data-form">
			<form:hidden path="uid"/>
			<resume:form-has-error path="info" />
			<div class="form-group row">
				<div class="col-sm-12">
					<form:textarea path="info" class="form-control" id="inputSummary" rows="7"
						placeholder="Пример: Наличие открытой рабочей визы в США, Женат, 2 детей, имею опыт исследовательской работы в НИИ в области физики твердого тела" />
					<resume:form-error path="info" />	
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 help-block">
					<blockquote>
						Укажите дополнительную информацию, которая действительно важна работодателю.
					</blockquote>
				</div>
			</div>
			<div class="form-group">
				<div class="row">
					<div class="col-xs-12 text-center">
						<button type="submit" class="btn btn-primary">Сохранить</button>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>