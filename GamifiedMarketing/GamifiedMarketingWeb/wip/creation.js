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

      }
   });

   var addQuestionButton = document.getElementById("add-question-button");
   addQuestionButton.addEventListener("click", (e) => {

      var container = document.getElementById("questions-container");
      //var label = document.createElement("LABEL")
      //var text = document.createTextNode("Insert another question:")
      //label.text = text;
      //container.appendChild(label);
      //container.appendChild(document.createElement("br"));
      // Create an <input> element, set its type and name attributes
      var input = document.createElement("input");
      input.type = "text";
      input.required = true;
      container.appendChild(input);
      // Append a line break
      container.appendChild(document.createElement("br"));

   });

};
