const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => {
	console.log("SIGN UP CLICKED  >>>>>>>>>>>>")
	container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
    console.log("SIGN IN CLICKED  >>>>>>>>>>>>")
	container.classList.remove("right-panel-active");
});