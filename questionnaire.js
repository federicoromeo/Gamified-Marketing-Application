/**
 * Questionnaire management
 */

window.onload = function() { // avoid variables ending up in the global scope

  // Events that allow switching between login / registration
  var nextButton = document.getElementById("next-button");
  var submitButton = document.getElementById("submit-button");
  var cancelButton = document.getElementById("submit-button");
  var goBackButton = document.getElementById("go-back-button");

  nextButton.addEventListener("click", (e) => {

    var marketingForm = document.getElementById("marketing-form");
    inputs = marketingForm.querySelectorAll('input');
    //form validity
    for (i = 0; i < inputs.length; ++i) {
      if(inputs[i].value.length==0){
         alert("The fields must not be empty!");
         return;
      }
    };
    marketingForm.classList.add("masked");
    var statisticalForm = document.getElementById("statistical-form");
    statisticalForm.classList.add("visible");
  });

  goBackButton.addEventListener("click", (e) => {

    var statisticalForm = document.getElementById("statistical-form");
    statisticalForm.classList.remove("visible");
    var marketingForm = document.getElementById("marketing-form");
    marketingForm.classList.remove("masked");
  });


  cancelButton.addEventListener("click", (e) => {
     //send a post.. go the home
  });

  submitButton.addEventListener("click", (e) => {
     //send a post.. go to greeting page
  });

};
