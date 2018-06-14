var ans = "0"; // current answer
var pressed = "0"; // keeps track of everything that has been pressed
var resultsScreen = $("#results");
var needsNumber = true; // keeps track of whether or not an operation was selected previously
var allButtons = $("button");
var decimalUsed = false; // makes sure that the user uses the decimal only once

$(document).ready(function(){
    allButtons.on("click", function(){
        var content = $(this).html();
        // special cases
        switch (content) {
            case "=":
                computeAnswer();
                break;
            case "AC":
                pressed = "0";
                break;
            case "CE":
                removeLastPressed();
                break;
            case "Ans":
                if (lastPressedOperation()) {
                    pressed += ans;
                }
                if (pressed == "0") { // if refreshed by pressing AC
                    pressed = ans;
                }
                break;
            default:
                var val = $(this).attr("value");
                console.log("You pressed " + val);
                if (val == null) { // if its an operation
                    if (!lastPressedOperation()) { // if allowed to press operation again
                        pressed += content;
                    }
                } else {
                    if (!(val == "." && decimalUsed)) {
                        if (pressed == "0") {
                            pressed = val;
                        } else {
                            pressed += val;
                        }
                        if (val ==".") {
                            decimalUsed = true;
                        }
                    }
                }
                console.log(pressed);
                break;
        }
        refreshDisplay();
    });
});

// post: displays the most current equation or answer
function refreshDisplay(){
    resultsScreen.html(pressed);
    console.log("Your current equation is " + pressed);
}

// post: calculates the answer;
// displays "syntax error" if there is one and alerts the user
function computeAnswer() {
    var val = 0;
    if (lastPressedOperation()) {
        pressed = "Syntax error!";
        refreshDisplay();
        alert("Syntax Error!");
    } else {
        pressed = pressed.replace("x", "*");
        pressed = eval(pressed) + "";
        ans = pressed;
        refreshDisplay();
    }
}

// post: returns true if the last pressed button was an operation; false otherwise
function lastPressedOperation() {
    var lastPressed = pressed.charAt(pressed.length - 1);
    return lastPressed == "x" || lastPressed == "+" || lastPressed == "-" || lastPressed == "/" || lastPressed == "%";
}

// post: removes the last pressed button from the screen and memory
function removeLastPressed() {
    if (pressed.length > 0) {
        pressed = pressed.substring(0, pressed.length - 1); // forget the last pressed button
        if (pressed == "") {
            pressed = "0";
        }
    }
};