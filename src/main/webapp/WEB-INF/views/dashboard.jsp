<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>	
<a href="${pageContext.request.contextPath}/users">Users</a>
<a href="${pageContext.request.contextPath}/j_spring_security_logout">Logout</a>
este es el dashboard . jsp

<h3>Ingresar Usuario</h3>
<form action="ingresarUsuario" method="POST">
<input type="text" value="" name="user" />
	<input type="text" name="password" />
	<select class="form-control" id="profile" name="profile" >
                      <option value="">- Seleccionar Perfil -</option>
                      <c:forEach var="profile" items="${profiles}" >
	    				<option value="${profile.idProfile}" 
	    				<c:if test="${profile.idProfile == user.getProfile().getIdProfile()}">selected="selected"</c:if> 
	    				>${profile.description}</option>
					</c:forEach>											                        
       </select>
	<br/><br/>
	<input type="submit" value="ingresar" />
	<br/>haste el listado man ;)
</form>