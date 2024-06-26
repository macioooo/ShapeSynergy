$(document).ready(function () {
    // Event listener for the edit buttons
    $('.editWorkoutPlanBtn').click(function () {
        // Get the workout plan ID from the button's data attribute
        var workoutPlanId = $(this).data('workout-plan-id');

        // Redirect to the desired URL with parameters
        window.location.href = '/user/exercises?workoutPlanId=' + workoutPlanId;
    });


    // Event listener for the delete buttons
    $('.deleteWorkoutPlanBtn').click(function () {Swal.fire({
        title: "Are you sure you want to delete this plan?",
        showDenyButton: true,
        showCancelButton: false,
        confirmButtonText: "Delete",
        denyButtonText: `Don't delete`
    }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
            // Get the workout plan ID from the button's data attribute
            var workoutPlanId = $(this).data('workout-plan-id');

            // Store a reference to the button element
            var button = $(this);

            // Send a POST request to delete the workout plan
            $.ajax({
                type: 'POST',
                url: '/user/exercises/deleteWorkoutPlan/' + workoutPlanId,
                headers: {
                    'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
                },
                dataType: 'text',
                success: function (response) {
                    Swal.fire({
                        title: "Good job!",
                        text: response, // Use response directly
                        icon: "success"
                    });
                    button.closest('.container').remove(); // Remove the deleted workout plan from the view
                },
                error: function (response) {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: response,
                    });
                }
            });
        } else if (result.isDenied) {
            Swal.fire("Your plan is safe!");
        }
    });

    });
});
