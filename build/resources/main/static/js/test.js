var signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
var container = document.getElementById('container');

//signUpButton.addEventListener("click", () => {
//alert("I'm active");
//	console.log("SIGN UP CLICKED  >>>>>>>>>>>>")
//	container.classList.add("right-panel-active");
//});

signInButton.addEventListener('click', () => {
    alert("I'm active");
    console.log("SIGN IN CLICKED  >>>>>>>>>>>>")
	container.classList.remove("right-panel-active");
});

window.onclick = e => {
alert("I'm active");
    console.log("SIGN IN CLICKED  >>>>>>>>>>>>")
    console.log(e.target.innerText);
}

function fun() {
     alert("I'm active");
    container.classList.add("right-panel-active");
}