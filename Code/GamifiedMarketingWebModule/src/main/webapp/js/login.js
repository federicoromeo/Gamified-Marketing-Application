
(function() { // avoid variables ending up in the global scope

    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1; //January is 0!
    var yyyy = today.getFullYear();
    if(dd<10)
        dd='0'+dd
    if(mm<10)
        mm='0'+mm
    today = yyyy+'-'+mm+'-'+dd;
    document.getElementById("date").setAttribute("max", today);


    // Events that allow switching between login / registration
    var showRegistrationButton = document.getElementById("show-registration");
    var showLoginButton = document.getElementById("show-login");

    showRegistrationButton.addEventListener("click", (e) => {
        console.log("trigger show register");
        var registrationForm = document.getElementById("registration-form");
        var loginForm = document.getElementById("login-form");
        /*console.log("registration before: ");
        console.log(registrationForm.classList);
        console.log("login before: ");
        console.log(registrationForm.classList);*/
        registrationForm.classList.add("visible");
        loginForm.classList.add("masked");
        console.log("registration after: ");
        console.log(registrationForm.classList);
        console.log("login after: ");
        console.log(registrationForm.classList);
    });

    showLoginButton.addEventListener("click", (e) => {
        console.log("trigger show login");
        var registrationForm = document.getElementById("registration-form");
        var loginForm = document.getElementById("login-form");
        /*console.log("registration before: ");
        console.log(registrationForm.classList);
        console.log("login before: ");
        console.log(registrationForm.classList);*/
        registrationForm.classList.remove("visible");
        loginForm.classList.remove("masked");
        console.log("registration after: ");
        console.log(registrationForm.classList);
        console.log("login after: ");
        console.log(registrationForm.classList);
    });

})();
