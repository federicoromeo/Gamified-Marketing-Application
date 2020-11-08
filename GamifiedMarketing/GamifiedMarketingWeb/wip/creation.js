window.onload = function() { // avoid variables ending up in the global scope

   var sendButton = document.getElementById("send-button");
   sendButton.addEventListener("click", (e) => {

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
         return;
      }
      else{
         //////////
      }
   });

   var deleteQuestionButton = document.getElementById("delete-question-button");
   deleteQuestionButton.addEventListener("click", (e) => {
      var container = document.getElementById("questions-container");
      var count = container.querySelectorAll("input").length;
      var toBeDeleted = document.getElementById(count);
      var countAux = count+1;
      //var br = document.getElementById("br"+countAux);
      //console.log(br);
      if(count===1){
         deleteQuestionButton.classList.add("masked");
         alert("You must insert al least one marketing question");
      }
      else{
         toBeDeleted.remove();
         console.log("deleted: " + count);
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
      var count = container.querySelectorAll("input").length;
      var countAux = count+1;
      console.log("added: " + countAux);

      var input = document.createElement("input");
      input.type = "text";
      input.placeholder = "Your question:"
      input.name = "question"+countAux;
      input.required = true;
      input.id = countAux;
      container.appendChild(input);
      console.log(input);
      // Append a line break
      var br = document.createElement("br");
      br.id = "br"+countAux;
      container.appendChild(br);

   });

};
