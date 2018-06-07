var userChoice = "";
var userChoice = prompt("Choose rock, paper, or scissors");
var playagain = "Y";
var computerChoice = Math.random();

if (computerChoice < 0.34) {
    computerChoice = "rock";
} else if (computerChoice <= 0.67) {
    computerChoice = "paper";
} else {
    computerChoice = "scissors";
}

choice1 = userChoice;
choice2 = computerChoice;

while (playagain == "Y") {
    function compare(choice1, choice2) {

        if (choice1 == choice2) {
            return ("It's a tie!");
        }
        if (choice1 == "rock") {
            if (choice2 == "scissors") {
                return ("You win!", choice2);

            } else {
                return ("computer wins!");
            }
        }
        if (choice1 == "paper") {
            if (choice2 == "rock") {
                return ("you win!");
            } else {
                return ("computer wins!");
            }
        }
        if (choice1 == "scissors") {
            if (choice2 == "rock") {
                return ("computer wins!");
            } else {
                return ("you win!");
            }
        }
    }

    document.write(compare(choice1, choice2));
    document.write("<br>");
    playagain = prompt("Do you want to play again, Y or N");
    userChoice = prompt("Choose rock, paper, or scissors");
}