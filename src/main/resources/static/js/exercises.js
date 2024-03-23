

var workoutPlan = []; // Array to store selected exercises

    function createMuscleRadioButtons() {
    var muscleOptions = [
    "biceps",
    "calves",
    "chest",
    "forearms",
    "glutes",
    "hamstrings",
    "lats",
    "lower_back",
    "middle_back",
    "neck",
    "quadriceps",
    "traps",
    "triceps"]; // Define muscle options

    var radioButtonsContainer = document.getElementById("muscleRadioButtons"); // Get container to append radio buttons

    // Clear existing radio buttons if any
    radioButtonsContainer.innerHTML = '';

    // Loop through muscle options and create radio buttons
    muscleOptions.forEach(function (muscle) {
    var radioButton = document.createElement("input");
    radioButton.setAttribute("type", "radio");
    radioButton.setAttribute("name", "muscleOption");
    radioButton.setAttribute("value", muscle);
    radioButton.id = muscle; // Set id for the radio button
    var label = document.createElement("label");
    label.setAttribute("for", muscle);
    label.textContent = muscle; // Set label text

    // Append radio button and label to the container
    radioButtonsContainer.appendChild(radioButton);
    radioButtonsContainer.appendChild(label);
    radioButtonsContainer.appendChild(document.createElement("br")); // Add line break
});
}

    // Call the function to create radio buttons when the page loads
    document.addEventListener("DOMContentLoaded", function () {
    createMuscleRadioButtons();
});

    // Function to display exercises in the "Your exercises" div
    function displayWorkoutPlan() {
    var yourExercisesDiv = document.querySelector('.yourExercises');
        if (workoutPlan.length === 0) {
            yourExercisesDiv.style.display = 'none';
        } else {
            yourExercisesDiv.style.display = 'block';
        }
    yourExercisesDiv.innerHTML = '<h2>Your exercises:</h2><br>';
        var workoutPlanName = document.createElement('input');
        workoutPlanName.setAttribute('type', 'text');
        workoutPlanName.setAttribute('name', 'workoutPlanName');
        workoutPlanName.setAttribute('placeholder', 'Enter workout plan name');
        workoutPlanName.setAttribute('required', 'required');
        yourExercisesDiv.appendChild(workoutPlanName);
    workoutPlan.forEach(function (exercise) {
    var exerciseText = document.createElement('span');
    exerciseText.textContent = exercise;
    exerciseText.classList.add('exerciseText');
    exerciseText.addEventListener('click', function () {
    var index = workoutPlan.indexOf(exercise);
    if (index !== -1) {
    workoutPlan.splice(index, 1);
    displayWorkoutPlan();
}
});
    yourExercisesDiv.appendChild(exerciseText);
    yourExercisesDiv.appendChild(document.createElement('br')); // Append <br> after each exercise
});
        // Create and append the "Submit workout plan" button
        var submitButton = document.createElement('button');
        submitButton.textContent = 'Submit workout plan';
        submitButton.id = 'submitButton';
        yourExercisesDiv.appendChild(document.createElement('br'));
        yourExercisesDiv.appendChild(submitButton);
}


    // Function to return the workoutPlan array
    function returnWorkoutPlan() {
    return workoutPlan;
}



    // Function that triggers the display of the pagination buttons
    function triggerButton(page) {
        // Select the exerciseNames div
        var exerciseNamesDiv = document.getElementById('exerciseNames');

// Create the buttons
        var previousButton = document.createElement('button');
        previousButton.textContent = 'Previous exercises';
        previousButton.id = 'previousPage';

        var nextButton = document.createElement('button');
        nextButton.textContent = 'Next exercises';
        nextButton.id = 'nextPage';

// Append the buttons to the exerciseNames div
        exerciseNamesDiv.appendChild(previousButton);
        exerciseNamesDiv.appendChild(nextButton);

    if (page > 0) {
    document.getElementById('previousPage').style.display = 'block';
} else {
    document.getElementById('previousPage').style.display = 'none';
}

    // Check if total number of exercises fetched is less than 10, if so, hide the "Next exercises" button
    var totalExercises = $('#exerciseNames').children().length; // Count number of exercise elements
    if (totalExercises < 10) {
    document.getElementById('nextPage').style.display = 'none';
} else {
    document.getElementById('nextPage').style.display = 'block';
}
}

const urlParams = new URLSearchParams(window.location.search);
const workoutPlanId = parseInt(urlParams.get('workoutPlanId'));

function fetchExercisesAndAddToWorkoutPlan() {
    $.ajax({
        url: '/exercises/editWorkoutPlan?workoutPlanId=' + workoutPlanId, // Include the workoutPlanId in the URL
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            // Check if data is not empty and is an object containing the 'exerciseList' property
            if (data && Array.isArray(data.exerciseList)) {
                data.exerciseList.forEach(function (exercise) {
                    if (!workoutPlan.includes(exercise)) {
                        workoutPlan.push(exercise);
                        console.log('Added exercise to workout plan:', exercise);
                    } else {
                        console.log('Exercise already exists in workout plan:', exercise);
                    }
                });
                displayWorkoutPlan();
            } else {
                console.log('No valid exercise list found in the response');
            }
        },
        error: function (xhr, status, error) {
            console.error('Failed to fetch exercises:', error);
        }
    });
}


    $(document).ready(function () {
    // Function to fetch exercise names based on search query, muscle, and offset
        fetchExercisesAndAddToWorkoutPlan();
    function searchExerciseNames(exercise, muscle, offset) {
        $.ajax({
            url: '/exercises/search',
            type: 'GET',
            dataType: 'html',
            data: {exercise: exercise, muscle: muscle, offset: offset},
            success: function (data) {
                $('#exerciseNames').empty();
                var exercises = data.split('\n');
                exercises.forEach(function (exercise, index) {
                    var trimmedExercise = exercise.trim(); // Trim any leading or trailing spaces
                    if (trimmedExercise) { // Check if the exercise name is not empty
                        var buttonId = 'exercise' + index;
                        if (!workoutPlan.includes(trimmedExercise)) {
                            var button = '<button id="' + buttonId + '" class="exerciseNotClicked">' + trimmedExercise + '</button>';
                        } else {
                            var button = '<button id="' + buttonId + '" class="exerciseClicked">' + trimmedExercise + '</button>';
                        }
                        var div = '<div>' + button + '</div>'; // Wrap each button in a div
                        $('#exerciseNames').append(div); // Append button within div
                        $('#' + buttonId).click(function () {
                            var exerciseIndex = workoutPlan.indexOf(trimmedExercise);
                            if (exerciseIndex === -1) {
                                // Exercise not in workout plan, add it
                                workoutPlan.push(trimmedExercise);
                                $(this).removeClass('exerciseNotClicked').addClass('exerciseClicked');
                                console.log('Added exercise to workout plan:', trimmedExercise);
                            } else {
                                // Exercise already in workout plan, remove it
                                workoutPlan.splice(exerciseIndex, 1);
                                $(this).removeClass('exerciseClicked').addClass('exerciseNotClicked');
                                console.log('Removed exercise from workout plan:', trimmedExercise);
                            }
                            displayWorkoutPlan(); // Update the display of exercises in "Your exercises" div

                            console.log('Current workout plan:', workoutPlan);
                        });
                    }
                });
                triggerButton(offset); // Call the function to display the pagination buttons
                // Update data-offset attribute for the next page button
                $('#nextPage').data('offset', offset);
                $('#previousPage').data('offset', offset);
            },
            error: function (xhr, status, error) {
                console.error('Failed to fetch exercise names:', error);
                $('#exerciseNames').html('Failed to fetch exercise names');
            }
        });
    }

    // Event listener for search button click
    $('#searchButton').click(function () {
    var query = $('#searchInput').val();
    var muscle = $('input[name="muscleOption"]:checked').val(); // Get the selected muscle
    var offset = 0; // Set offset to 0 when initiating a new search
    searchExerciseNames(query, muscle, offset); // Pass both query, muscle, and offset to the search function
});

    // Event listener for clicking on the previous page button
        $(document).on('click', '#previousPage', function () {
    var query = $('#searchInput').val();
    var muscle = $('input[name="muscleOption"]:checked').val(); // Get the selected muscle
    var currentOffset = parseInt($('#previousPage').data('offset')); // Get the previous offset value
    var offset = currentOffset - 10; // Check if currentOffset is NaN, if so, set it to 0
    searchExerciseNames(query, muscle, offset);
});

    // Event listener for clicking on the next page button
    $(document).on('click', '#nextPage', function () {
    var query = $('#searchInput').val();
    var muscle = $('input[name="muscleOption"]:checked').val(); // Get the selected muscle
    var currentOffset = parseInt($('#nextPage').data('offset')); // Get the current offset value
    var offset = isNaN(currentOffset) ? 0 : currentOffset + 10; // Check if currentOffset is NaN, if so, set it to 0
    searchExerciseNames(query, muscle, offset);
});


        // Event listener for clicking on the submit button
        $(document).on('click', '#submitButton', function () {
            var workoutPlanName = $('input[name="workoutPlanName"]').val(); // Get the workout plan name from the input field
            if (!workoutPlanName) {
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: "Please enter a workout plan name!",
                });
                return;
            }
            var requestBody = {
                workoutPlanName: workoutPlanName,
                exerciseList: workoutPlan,
                id_workout_plan: workoutPlanId
            };
            $.ajax({
                url: 'exercises/getarray',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(requestBody), // Send the requestBody object
                dataType: 'text', // Expect text/plain response
                success: function (response) {
                    Swal.fire({
                        title: "Good job!",
                        text: response, // Use response directly
                        icon: "success"
                    });
                },
                error: function (xhr) {
                    var errorMessage = xhr.responseText;
                    $('#error').text(errorMessage);
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: errorMessage,
                    });
                }
            });
        });

    });