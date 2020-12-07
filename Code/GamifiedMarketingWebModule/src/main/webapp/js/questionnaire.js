/**
 * Questionnaire management
 */

window.onload = function() { // avoid variables ending up in the global scope

    // differenciate name of responses to get them
    var marketingForm = document.getElementById("marketing-form");
    var responses = marketingForm.querySelectorAll('input[class="response"]');
    var labels = marketingForm.querySelectorAll('label[class="labels"]');

    //insert variable to cycle over in the servlet
    var numberofresponses = document.getElementById("number-of-responses");
    numberofresponses.value = responses.length;
    console.log(numberofresponses.value + " response(s) / answer(s)");

    //adjust questions value for the servlet
    var idQuestions = document.getElementsByClassName('id-question');
    for (let i = 0; i < responses.length; i++) {
        let myI = i + 1;
        idQuestions[i].name = "question" + myI;
        responses[i].name = "response" + myI;
        idQuestions[i].value = labels[i].textContent.replace(/\s+/g, '');
        console.log(idQuestions[i].name + " has id = " + idQuestions[i].value);
        console.log(responses[i].name + " === " + responses[i].value);
    }

    // Events that allow switching between forms
    var nextButton = document.getElementById("next-button");
    var submitButton = document.getElementById("submit-button");
    var cancelButton = document.getElementById("submit-button");
    var goBackButton = document.getElementById("go-back-button");

    nextButton.addEventListener("click", (e) => {

        //form validity
        for (i = 0; i < responses.length; i++) {
            if(responses[i].value.length===0){
                alert("The fields must not be empty!");
                return;
            }
        }
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
