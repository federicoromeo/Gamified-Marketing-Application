
window.onload = function() { // avoid variables ending up in the global scope

    var radioContainer = document.getElementById('radio-container');
    radioContainer.addEventListener("click", (e) => {

        var selectedRadio = radioContainer.querySelector('input[name="questionnaire"]:checked');
        var tables = document.getElementsByTagName("table");
        var labels = document.getElementsByTagName("label");

        $("[th\\:id='*{tables}']");

        //console.log(tables);
        for (var i = 0; i < tables.length; i++) {
            if(tables[i].id === selectedRadio.value){
                tables[i].classList.remove("masked");
                labels[i].classList.remove("masked");
                tables[i].classList.add("visible");
                labels[i].classList.add("visible");
            }
            else{
                tables[i].classList.remove("visible");
                labels[i].classList.remove("visible");
                tables[i].classList.add("masked");
                labels[i].classList.add("masked");
            }
        }
    });

};
