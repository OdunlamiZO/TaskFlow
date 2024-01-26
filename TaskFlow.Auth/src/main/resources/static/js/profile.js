$(document).ready(function () {
	$("#nav_btn").on("click", function () {
		if ($("#nav").css("display") === "none") {
			$("#nav").css("display", "flex");
		} else {
			$("#nav").css("display", "none");
		}
	});

	$(window).on("resize", function () {
		if ($(window).width() > 768) {
			if ($("#nav").css("display") === "none") {
				$("#nav").css("display", "flex");
			}
		}
	});

	$("#profile_update_form").on("submit", function (event) {
		$("#update_profile").addClass("hidden");
		$("span.loading:eq(0)").removeClass("hidden");
		let isValid = true;
		const name = $("#name").val();
		if (!match(/^[A-Za-z\-']+ [A-Za-z\-']+$/, name)) {
			isValid = false;
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
			showError("name", message);
		}
		const username = $("#username").val();
		if (match(/^[a-zA-Z0-9_]{3,16}$/, username)) {
			$.ajax({
				url: "/user",
				data: { query: username },
				dataType: "json",
				async: false,
				success: function (response) {
					if (response.user && principal != username) {
						isValid = false;
						showError("username", "Username is already taken.");
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
			});
		} else {
			isValid = false;
			let message;
			if (!username) {
				message = "Please enter a username.";
			} else {
				const unwantedChars = username.match(/[^a-zA-Z0-9_]/g);
				message = `Username cannot contain '${[...new Set(unwantedChars)].join(
					""
				)}', please enter a valid username.`;
			}
			showError("username", message);
		}
		if (!isValid) {
			event.preventDefault();
		}
	});

	let croppie;

	$("#_croppie input").on("change", function (event) {
		const input = event.target;
		if (input.files && input.files[0]) {
			const reader = new FileReader();

			reader.onload = function (e) {
				if (croppie) {
					croppie.destroy();
				}
				croppie = new Croppie($("#croppie")[0], {
					url: e.target.result,
					boundary: { width: "320", height: "200" },
					viewport: { width: "175", height: "175", type: "circle" },
					showZoomer: false,
				});
			};

			reader.readAsDataURL(input.files[0]);
		}
		$(".modal:eq(0) .header").text("Crop Photo");
		$("#_avatar").addClass("hidden");
		$("#_croppie").removeClass("hidden");
		$("#upload_photo").addClass("hidden");
		$("#delete_photo").addClass("hidden");
		$("#save_photo").removeClass("hidden");
	});

	$("#edit_photo").on("click", function () {
		$(this).addClass("hidden");
		$("#upload_photo").removeClass("hidden");
		$("#delete_photo").removeClass("hidden");
	});

	$("#upload_photo").on("click", function () {
		$("#_croppie input").click();
	});

	$("#delete_photo").on("click", function () {
		const csrfToken = $("input[name='_csrf']").val();
		$.ajax({
			type: "DELETE",
			url: "/profile/photo/delete",
			xhrFields: {
				withCredentials: true,
			},
			beforeSend: function (xhr) {
				xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
				$("span.loading:eq(1)").removeClass("hidden");
				$("#upload_photo").addClass("hidden");
				$("#delete_photo").addClass("hidden");
			},
			error: function (xhr, status, error) {
				$("#upload_photo").removeClass("hidden");
				$("#delete_photo").removeClass("hidden");
				console.error(
					`
						status: ${status} \n
						error: ${error}
					`
				);
			},
			complete: function (xhr) {
				if (xhr.status === 200) {
					$("#avatar img").attr("src", "/images/avatar_placeholder.png");
					$("#edit_photo").removeClass("hidden");
					$("#_croppie input").val(""); // reset input value
				}
				$("span.loading:eq(1)").addClass("hidden");
			},
		});
	});

	$("#save_photo").on("click", function () {
		croppie
			.result({ type: "base64", circle: false })
			.then(function (result) {
				const regex = /^data:(image\/([a-zA-Z]+));base64,(.+)$/;
				const resultParts = result.match(regex);
				const csrfToken = $("input[name='_csrf']").val();
				$.ajax({
					type: "POST",
					url: "/profile/photo/upload",
					dataType: "json",
					contentType: "application/json",
					data: JSON.stringify({
						data: resultParts[3],
						mimeType: resultParts[1],
					}),
					xhrFields: {
						withCredentials: true,
					},
					beforeSend: function (xhr) {
						xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
						$("span.loading:eq(1)").removeClass("hidden");
						$("#save_photo").addClass("hidden");
					},
					success: function (response) {
						$("#avatar img").attr(
							"src",
							`data:${response.mimeType};base64,${response.data}`
						);
					},
					error: function (xhr, status, error) {
						$("#save_photo").removeClass("hidden");
						console.error(
							`
								status: ${status} \n
								error: ${error}
							`
						);
					},
					complete: function (xhr) {
						if (xhr.status === 200) {
							$(".modal:eq(0) .header").text("Profile Photo");
							$("#_croppie").addClass("hidden");
							$("#_avatar").removeClass("hidden");
							$("#edit_photo").removeClass("hidden");
						}
						$("span.loading:eq(1)").addClass("hidden");
					},
				});
			})
			.catch(function (error) {
				console.error(`error: ${error}`);
			});
	});
});

const match = (regex, value) => regex.test(value);

const showError = (id, message) => {
	const errorDiv = $(`#${id}`).next(".error");
	errorDiv.removeClass("hidden");
	errorDiv.find("span").text(message);
};

const showPhotoUpdate = () => {
	$(".modal:eq(0)").removeClass("hidden");
	$(".overlay").removeClass("hidden");
};

const hidePhotoUpdate = () => {
	$(".modal:eq(0)").addClass("hidden");
	$(".modal:eq(0) .header").text("Profile Photo");
	$(".overlay").addClass("hidden");
	if (!$("#_croppie").hasClass("hidden")) {
		$("#_croppie").addClass("hidden");
	}
	if ($("#_avatar").hasClass("hidden")) {
		$("#_avatar").removeClass("hidden");
	}
	if (!$("#save_photo").hasClass("hidden")) {
		$("#save_photo").addClass("hidden");
	}
	if (!$("#upload_photo").hasClass("hidden")) {
		$("#upload_photo").addClass("hidden");
	}
	if (!$("#delete_photo").hasClass("hidden")) {
		$("#delete_photo").addClass("hidden");
	}
	if ($("#edit_photo").hasClass("hidden")) {
		$("#edit_photo").removeClass("hidden");
	}
};
