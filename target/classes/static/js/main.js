function enableElems() {
        var elemsCollection = document.getElementsByClassName('numb'),
            i = elemsCollection.length;

        while(i--) {
            elemsCollection[i].disabled = false;
            }
    }

    function disableElems() {
        var elemsCollection = document.getElementsByClassName('numb'),
            i = elemsCollection.length;

        while(i--) {
            elemsCollection[i].disabled = true;
            }
    }

    function appendResult(val) {
        if (document.getElementById('result').value.length >= 4) {
            clearResult();
            }

		document.getElementById('result').value += val;
    }

    function clearResult() {
        document.getElementById('result').value="";
    }

    function checkResult() {
        var result = document.getElementById('result').value;
        if (result.length >= 4) {
            disableElems();
        }
    }