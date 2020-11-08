
window.onload = function() { // avoid variables ending up in the global scope

   var radioContainer = document.getElementById('radio-container');
   radioContainer.addEventListener("click", (e) => {
      var selectedRadio = radioContainer.querySelector("input[name=questionnaire]:checked");
      var table = document.getElementById('questionnaire-table');

      var table = document.getElementById('selected-questionnaire');
      table.innerHTML = selectedRadio.value;
   });

};
