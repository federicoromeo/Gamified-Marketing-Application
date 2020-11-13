/**
 * Login management
 */

(function() { // avoid variables ending up in the global scope

  // Events that allow switching between login / registration
  var showRegistrationButton = document.getElementById("show-registration");
  var showLoginButton = document.getElementById("show-login");

  showRegistrationButton.addEventListener("click", (e) => {
    var registrationForm = document.getElementById("registration-form");
    registrationForm.classList.add("visible");
    var loginForm = document.getElementById("login-form");
    loginForm.classList.add("masked");
  });

  showLoginButton.addEventListener("click", (e) => {
    var registrationForm = document.getElementById("registration-form");
    registrationForm.classList.remove("visible");
    var loginForm = document.getElementById("login-form");
    loginForm.classList.remove("masked");
  });

  // LOGIN SUBMIT
  document.getElementById("login-button").addEventListener('click', (e) => {
    var form = e.target.closest("form");
    if (form.checkValidity()) {
      makeCall("POST", 'CheckLogin', e.target.closest("form"),
        function(req) {
          if (req.readyState == XMLHttpRequest.DONE) {
            var message = req.responseText;

            var user = JSON.parse(message);

            if(user === "Account not existing"){
               document.getElementById("errormessage-login").textContent = user;
               return;
            }

            switch (req.status) {
              case 200:
            	sessionStorage.setItem('user', user.username);
              if(user.role === "client"){
                window.location.href = "HomeClient.html";
              }else{
                window.location.href = "HomeEmployee.html";
              }
                break;
              case 400: // bad request
                document.getElementById("errormessage-login").textContent = message;
                break;
              case 401: // unauthorized
                  document.getElementById("errormessage-login").textContent = message;
                  break;
              case 500: // server error
            	document.getElementById("errormessage-login").textContent = message;
                break;
            }
          }
        }
      );
    } else {
    	 form.reportValidity();
    }
  });

  //var registerButton = document.getElementById("register-button");

})();
