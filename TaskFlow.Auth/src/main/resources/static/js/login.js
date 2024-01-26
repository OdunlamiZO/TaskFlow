$(document).ready(function () {
	$("#next_button").click(function () {
		if (!$("#error").hasClass("hidden")) {
			$("#error").addClass("hidden");
		}
		const username = $("#username").val();
		$.ajax({
			url: "/user",
			data: { query: username },
			dataType: "json",
			beforeSend: function () {
				$("#next_button").prop("disabled", true);
				$("#username ~ span.loading").removeClass("hidden");
			},
			success: function (response) {
				if (response.user) {
					$("#username").prop("disabled", true);
					$("#username").parent().css("--status_color", "lightgreen");
					$("#username")
						.parent()
						.append(
							`<input type="hidden" name="username" value="${username}" />`
						);
					$("#password").parent().removeClass("hidden");
					$("#next_button").addClass("hidden");
					$("#login_button").removeClass("hidden");
				} else {
					$("#username").parent().css("--status_color", "#E42043");
					showError(`No match found for ${username}.`);
				}
			},
			error: function (xhr, status, error) {
				console.error(
					`
						status: ${status} \n
						error: ${error}
					`
				);
			},
			complete: function () {
				$("#next_button").prop("disabled", false);
				$("#username ~ span.loading").addClass("hidden");
			},
		});
	});

	$("#login_form")
		.find('input[type="text"], input[type="password"]')
		.each(function () {
			$(this).on("focus", function () {
				$(this).prev("label").removeClass("hidden");
				if ($(this).val() === "") {
					$(this).attr("placeholder", "");
				}
			});
			$(this).on("blur", function () {
				if ($(this).val() === "") {
					$(this).prev("label").addClass("hidden");
					if ($(this).is("#username")) {
						$(this).attr("placeholder", "Username");
					}
					if ($(this).is("#password")) {
						$(this).attr("placeholder", "Password");
					}
				}
			});
		});
});

const match = (regex, value) => regex.test(value);

const showError = (message) => {
	$("#error_message").html(message);
	$("#error").removeClass("hidden");
};
