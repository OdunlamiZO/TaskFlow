$(document).ready(function () {
	let stage = 1;
	$("#confirm_button").click(function () {
		if (!$("#error").hasClass("hidden")) {
			$("#error").addClass("hidden");
		}
		const passwordConstraints = $("#password").next();
		if (!passwordConstraints.hasClass("hidden")) {
			passwordConstraints.addClass("hidden");
		}
		if (stage === 1) {
			const email = $("#email").val();
			if (match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, email)) {
				$.ajax({
					url: "/user",
					data: { query: email },
					dataType: "json",
					beforeSend: function () {
						$("#confirm_button").prop("disabled", true);
						$("#email ~ span.loading").removeClass("hidden");
					},
					success: function (response) {
						if (!response.user) {
							$("#email").prop("disabled", true);
							$("#email").parent().css("--status_color", "lightgreen");
							$("#email")
								.parent()
								.append(
									`<input type="hidden" name="email" value="${email}" />`
								);
							$("#name").parent().removeClass("hidden");
							stage++;
						} else {
							$("#email").parent().css("--status_color", "#E42043");
							showError("Email already in use.");
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
						$("#confirm_button").prop("disabled", false);
						$("#email ~ span.loading").addClass("hidden");
					},
				});
			} else {
				$("#email").parent().css("--status_color", "#E42043");
				let message;
				if (!email) {
					message = "Please enter your email address.";
				} else {
					message = "Please enter a valid email address.";
				}
				showError(message);
			}
		} else if (stage === 2) {
			const name = $("#name").val();
			if (match(/^[A-Za-z\-']+ [A-Za-z\-']+$/, name)) {
				$("#name").prop("disabled", true);
				$("#name").parent().css("--status_color", "lightgreen");
				$("#name")
					.parent()
					.append(`<input type="hidden" name="name" value="${name}" />`);
				$("#username").parent().removeClass("hidden");
				stage++;
			} else {
				$("#name").parent().css("--status_color", "#E42043");
				let message;
				if (!name) {
					message = "Please enter your full name.";
				} else {
					const parts = name.split(" ");
					if (parts.length < 2) {
						message = "Please enter your last name.";
					} else {
						message = "Requires only first & last name.";
					}
				}
				showError(message);
			}
		} else if (stage === 3) {
			const username = $("#username").val();
			if (match(/^[a-zA-Z0-9_]{3,16}$/, username)) {
				$.ajax({
					url: "/user",
					data: { query: username },
					dataType: "json",
					beforeSend: function () {
						$("#confirm_button").prop("disabled", true);
						$("#username ~ span.loading").removeClass("hidden");
					},
					success: function (response) {
						if (!response.user) {
							$("#username").prop("disabled", true);
							$("#username").parent().css("--status_color", "lightgreen");
							$("#username")
								.parent()
								.append(
									`<input type="hidden" name="username" value="${username}" />`
								);
							$("#password").parent().removeClass("hidden");
							stage++;
						} else {
							$("#username").parent().css("--status_color", "#E42043");
							showError("Username is already taken.");
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
						$("#confirm_button").prop("disabled", false);
						$("#username ~ span.loading").addClass("hidden");
					},
				});
			} else {
				$("#username").parent().css("--status_color", "#E42043");
				let message;
				if (!username) {
					message = "Please enter a username.";
				} else {
					const unwantedChars = username.match(/[^a-zA-Z0-9_]/g);
					message = `Username cannot contain '${[
						...new Set(unwantedChars),
					].join("")}', please enter a valid username.`;
				}
				showError(message);
			}
		} else if (stage === 4) {
			const password = $("#password").val();
			if (match(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/, password)) {
				$("#password").prop("disabled", true);
				$("#password").parent().css("--status_color", "lightgreen");
				$("#confirm_password").parent().removeClass("hidden");
				stage++;
			} else {
				$("#password").parent().css("--status_color", "#E42043");
				let message;
				if (!password) {
					message = "Please enter a password.";
				} else if (password.length < 8) {
					message =
						"Password should be at least 8 characters long, please enter a valid password.";
				} else {
					const violations = [];
					if (!/[a-z]/.test(password)) {
						violations.push("lowercase letter");
					}
					if (!/[A-Z]/.test(password)) {
						violations.push("uppercase letter");
					}
					if (!/\d/.test(password)) {
						violations.push("digit");
					}
					let violationString = "";
					for (let i = 0; i < violations.length; i++) {
						let x = "one ";
						if (i > 0 && i < violations.length - 1) {
							x = ", one";
						}
						if (i === violations.length - 1 && violations.length != 1) {
							x = " and one ";
						}
						violationString = violationString + x + violations[i];
					}
					const suffix =
						violations.length === 1
							? ", please enter a valid password."
							: ". Please enter a valid password.";
					message = `Password should contain at least ${violationString}${suffix}`;
				}
				showError(message);
			}
		} else if (stage === 5) {
			const password = $("#password").val();
			const confirmPassword = $("#confirm_password").val();
			if (password === confirmPassword) {
				$("#confirm_password").prop("disabled", true);
				$("#confirm_password").parent().css("--status_color", "lightgreen");
				$("#password")
					.parent()
					.append(
						`<input type="hidden" name="password" value="${password}" />`
					);
				$("#agreement_check").parent().removeClass("hidden");
				$(this).addClass("hidden");
				$("#sign_button").removeClass("hidden");
			} else {
				$("#confirm_password").parent().css("--status_color", "#E42043");
				let message;
				if (!confirmPassword) {
					message = "Please enter your password.";
				} else {
					message =
						"Password does not match, please enter a matching password.";
				}
				showError(message);
			}
		}
	});
	$("#password").on("keydown", function () {
		const constraints = $(this).next();
		if (constraints.hasClass("hidden") && $("#error").hasClass("hidden")) {
			constraints.removeClass("hidden");
		}
	});
	$("#agreement_check").on("click", function () {
		const disabled = $("#sign_button").prop("disabled");
		$("#sign_button").prop("disabled", !disabled);
	});
	$("#signup_form")
		.find('input[type="text"], input[type="email"], input[type="password"]')
		.each(function () {
			$(this).on("focus", function () {
				$(this).prev("label").removeClass("hidden");
				if ($(this).is("#email") && $(this).val() === "") {
					$(this).attr("placeholder", "john@taskflow.xxx");
				}
				if ($(this).is("#name") && $(this).val() === "") {
					$(this).attr("placeholder", "John Kayode");
				}
				if ($(this).is("#username") && $(this).val() === "") {
					$(this).attr("placeholder", "JKay_99");
				}
				if ($(this).is("#password") && $(this).val() === "") {
					$(this).attr("placeholder", "i8Binkay");
				}
				if ($(this).is("#confirm_password") && $(this).val() === "") {
					$(this).attr("placeholder", "");
				}
			});
			$(this).on("blur", function () {
				if ($(this).val() === "") {
					$(this).prev("label").addClass("hidden");
					if ($(this).is("#email")) {
						$(this).attr("placeholder", "Email");
					}
					if ($(this).is("#name")) {
						$(this).attr("placeholder", "Name");
					}
					if ($(this).is("#username")) {
						$(this).attr("placeholder", "Username");
					}
					if ($(this).is("#password")) {
						$(this).attr("placeholder", "Password");
					}
					if ($(this).is("#confirm_password")) {
						$(this).attr("placeholder", "Confirm Password");
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
