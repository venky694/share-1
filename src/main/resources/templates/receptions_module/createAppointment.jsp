<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style type="text/css">
tr td {
	text-align: left;
}

td input {
	width: 300px;
	height: 35px;
	font-size: 15px;
}

select {
	width: 300px;
	height: 35px;
	font-size: 15px;
}

table tr td {
	padding-top: 10px;
}
</style>

<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script>
	$(function() {
		$("#header").load("receptionHeader");
		$("#footer").load("footer");
	});
</script>
</head>
<body>

	<div id="header"></div>
	<h3 style="text-align: center;">Pre-Appointment</h3>
	<hr>
	<div style="margin: auto; width: 45%; padding-left: 100px">
		<form action="saveEmployee" method="post">
			<table>
				<tr>
					<td>ID</td>
					<td>:</td>
					<td><input type="text" name="id" required="required"></td>
				</tr>
				<tr>
					<td>name</td>
					<td>:</td>
					<td><input type="text" name="name" required="required"></td>
				</tr>
				<tr>
					<td>Mobile</td>
					<td>:</td>
					<td><input type="text" name="mobile" required="required"></td>
				</tr>
				<tr>
					<td>Gender</td>
					<td>:</td>
					<td><select name="gender">
							<option>--Select Gender--</option>
							<option value="Male">Male</option>
							<option value="Female">Female</option>
							<option value="Others">Others</option>
					</select></td>
				</tr>
				<tr>
					<td>Age</td>
					<td>:</td>
					<td><input type="text" name="salary" required="required"></td>
				</tr>
				<tr>
					<td>Problem</td>
					<td>:</td>
					<td><textarea rows="2" cols="38" name="problem"></textarea></td>
				</tr>
				<tr>
					<td>Appointment Date</td>
					<td>:</td>
					<td><input type="date" name="app_date" required="required"></td>
				</tr>
				<tr>
					<td>Doctor</td>
					<td>:</td>
					<td><select name="doctor">
							<option>--Select Gender--</option>
							<option value="Male">Male</option>
							<option value="Female">Female</option>
							<option value="Others">Others</option>
					</select></td>
				</tr>
				<tr>
					<td colspan="3" style="text-align: center; padding-top: 25px">
						<input style="width: 100px; background-color: #4CD6F1;"
						type="submit" value="Save">
					</td>
				</tr>
			</table>
		</form>
		<br>

	</div>
	<div id="footer"></div>

</body>
</html>