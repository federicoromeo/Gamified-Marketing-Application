/**
 * Login management
 */

(function() { // avoid variables ending up in the global scope

  // Events that allow switching between login / registration
  var showRegistrationButton = document.getElementById("show-registration");
  var showLoginButton = document.getElementById("show-login");
  var mail = document.getElementById("email");

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

  function validateEmail(mail){

     //var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
     //checks if the mail fits the regular expression
     if(1) {
        return true;
     }
     else{
        return false;
     }
  }
   /*		Explanation:
        / .. /	All regular expressions start and end with forward slashes.
        ^	Matches the beginning of the string or line.
        \w+	Matches one or more word characters including the underscore. (There must be at least one character before the '@')
        [\.-]	\ Indicates that the next character is special and not to be interpreted literally.
              .- matches character . or -.
        ?	Matches the previous character 0 or 1 time. Here previous character is [.-].
        \w+	Matches 1 or more word characters including the underscore.
        *	Matches the previous character 0 or more times.
        ([.-]?\w+)*	Matches 0 or more occurrences of [.-]?\w+.
        \w+([.-]?\w+)*	This sub-expression is used to match the username in the email. It begins with at least one or more word characters including the underscore,
        followed by . or -  and  . or - must follow by a word character (A-Za-z0-9_).
        @	It matches only @ character.
        \w+([.-]?\w+)*	It matches the domain name with the same pattern of user name described above..
        \.\w{2,3}	It matches a . followed by two or three word characters, (.com, .it)
        +	The + sign specifies that the above sub-expression shall occur one or more times
        $	Matches the end of the string or line.
   */

  var registerButton = document.getElementById("register-button");
    var email = document.getElementById("email");
    registerButton.addEventListener("click", (e) => {
    if(!validateEmail(email))
       alert("Not valid mail format!")
  });

})();
