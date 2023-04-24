function signUpClick() {
document.getElementById('container').classList.add('right-panel-active');
}

function signInClick() {
document.getElementById('container').classList.remove('right-panel-active');
}

function validateForm() {
  if (document.forms["loginForm"]["email"].value == "") {
        alert("Email can not be empty");
        return false;
  }
  if (document.forms["loginForm"]["password"].value == "") {
        alert("Password can not be empty");
        return false;
  }
}