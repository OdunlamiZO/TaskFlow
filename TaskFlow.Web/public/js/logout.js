$(function () {
    Livewire.on("auth-logout", ({ url }) => {
        $.ajax({
            type: "POST",
            url: `${url}/logout`,
            xhrFields: {
                withCredentials: true,
            },
            beforeSend: function (xhr) {
                $.ajax({
                    type: "GET",
                    url: `${url}/csrf`,
                    async: false,
                    xhrFields: {
                        withCredentials: true,
                    },
                    success: function (response) {
                        xhr.setRequestHeader(
                            response.headerName,
                            response.token
                        );
                    },
                });
            },
            error: function (xhr, status, error) {
                console.error(
                    `
						status: ${status} \n
						error: ${error}
					`
                );
            },
            complete: function (xhr) {
                if (xhr.status === 200) {
                    window.location.href = "/login";
                }
            },
        });
    });
});
