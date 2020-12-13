window.onload = function() { // avoid variables ending up in the global scope

    /*var sendButton = document.getElementById("send-button");
    sendButton.addEventListener("click", (e) => {

        var numberofquestions = document.getElementById("number-of-questions");
        var questionsContainer = document.getElementById("questions-container");
        var numberOfChildren = questionsContainer.children.length - 2  //only questions
        numberofquestions.value = numberOfChildren;
        console.log(numberofquestions.value + " question(s)");

        var date = document.querySelector('input[type="date"]');
        var arrayDate = date.value.split("-",3);
        var selectedDate = new Date(arrayDate[0], arrayDate[1]-1, arrayDate[2]);
        console.log("selected date: " + selectedDate);

        var today = new Date();
        var dd = String(today.getDate()).padStart(2, '0');
        var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
        var yyyy = today.getFullYear();
        today = new Date(yyyy, mm-1, dd);
        console.log("today : " + today);

        if(selectedDate < today){
            alert("Date should be today or later!")
        }

    });*/

    var deleteQuestionButton = document.getElementById("delete-question-button");
    deleteQuestionButton.addEventListener("click", (e) => {
        var container = document.getElementById("questions-container");
        var count = container.querySelectorAll('input[type="text"]').length;
        var toBeDeleted = document.getElementById(count);
        console.log("tobedeleted: question" + count);
        var countAux = count+1;
        //var br = document.getElementById("br"+countAux);
        //console.log(br);
        if(count===1){
            deleteQuestionButton.classList.add("masked");
            alert("You must insert at least one marketing question!");
        }
        else{
            toBeDeleted.remove();
            console.log("deleted: question" + count);
            //br.remove();
        }

    });

    var addQuestionButton = document.getElementById("add-question-button");
    addQuestionButton.addEventListener("click", (e) => {

        var deleteQuestionButton = document.getElementById("delete-question-button");
        deleteQuestionButton.classList.add("visible");
        /*var present = 0;
        for(var el of deleteQuestionButton.classList.entries()) {
           if (el === "visible"){
              present = 1;
           }
        }
        if(!present)
           deleteQuestionButton.classList.add("visible");
        */
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
