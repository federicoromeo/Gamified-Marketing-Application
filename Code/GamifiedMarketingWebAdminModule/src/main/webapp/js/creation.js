window.onload = function() { // avoid variables ending up in the global scope

    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1; //January is 0!
    var yyyy = today.getFullYear();
    if(dd<10)
        dd='0'+dd
    if(mm<10)
        mm='0'+mm
    today = yyyy+'-'+mm+'-'+dd;
    document.getElementById("date").setAttribute("min", today);


    var sendButton = document.getElementById("send-button");
    sendButton.addEventListener("click", (e) => {

        var numberofquestions = document.getElementById("number-of-questions");
        var questionsContainer = document.getElementById("questions-container");
        var numberOfChildren = questionsContainer.querySelectorAll('input[type="text"]').length//questionsContainer.children.length - 2  //only questions
        numberofquestions.value = numberOfChildren;
        console.log(numberofquestions.value + " question(s)");
        console.log(numberOfChildren + " question(s)");

    });

    var deleteQuestionButton = document.getElementById("delete-question-button");
    deleteQuestionButton.addEventListener("click", (e) => {
        var container = document.getElementById("questions-container");
        var count = container.querySelectorAll('input[type="text"]').length;
        var toBeDeleted = document.getElementById(count);
        console.log("tobedeleted: question" + count);
        var countAux = count+1;
        if(count===1){
            deleteQuestionButton.classList.add("masked");
            alert("You must insert at least one marketing question!");
        }
        else{
            toBeDeleted.remove();
            console.log("deleted: question" + count);
        }
    });

    var addQuestionButton = document.getElementById("add-question-button");
    addQuestionButton.addEventListener("click", (e) => {

        var deleteQuestionButton = document.getElementById("delete-question-button");
        deleteQuestionButton.classList.add("visible");
        var container = document.getElementById("questions-container");
        var count = container.querySelectorAll('input[type="text"]').length;
        var countAux = count+1;
        console.log("added: question" + countAux);

        var input = document.createElement("input");
        input.type = "text";
        input.placeholder = "Your question:"
        input.name = "question"+countAux;
        input.required = true;
        input.id = countAux;
        container.appendChild(input);
        console.log(input);
    });

};
