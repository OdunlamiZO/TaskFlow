$(function () {
    $("#nav_btn").on("click", () => {
        if ($(window).width() < 768) {
            if ($("#nav_search").css("display") === "none") {
                $("#nav_search").css("display", "flex");
                $("#nav_btn .line").css("background-color", "white");
            } else {
                $("#nav_search").css("display", "none");
                $("#nav_btn .line").css("background-color", "gray");
            }
        } else {
            $("#nav").toggle("hidden");
            if ($("#search").css("display") === "none") {
                $("#search").css("visibility", "hidden");
                $("#search").css("display", "flex");
                setTimeout(() => {
                    $("#search").css("visibility", "visible");
                }, 650);
            } else if ($("#search").css("display") === "flex") {
                if ($("#search").width() < 320) {
                    $("#search").css("display", "none");
                }
            }
        }
    });

    $("#nav_ext_btn").on("click", () => {
        if ($("#nav_ext").hasClass("hidden")) {
            $("#nav_ext").removeClass("hidden");
        } else {
            $("#nav_ext").addClass("hidden");
        }
    });

    $("#nav_ext .close_btn").on("click", () => {
        $("#nav_ext").addClass("hidden");
    });

    $(document).on("click", (event) => {
        if (!$(event.target).closest("#nav_ext, #nav_ext_btn").length) {
            if (!$("#nav_ext").hasClass("hidden")) {
                $("#nav_ext").addClass("hidden");
            }
        }
    });

    adjustNav_Search();
    $(window).on("resize", adjustNav_Search);
});

const adjustNav_Search = () => {
    if ($(window).width() > 768) {
        if ($("#nav_search").css("display") === "none") {
            $("#nav_search").css("display", "flex");
        }
        if ($("#nav_btn .line").css("background-color") !== "gray") {
            $("#nav_btn .line").css("background-color", "gray");
        }
        if ($("#nav").css("display") === "flex") {
            $("#nav").css("display", "block");
        }
        if ($("#search").width() < 320) {
            $("#search").css("display", "none");
        }
        if ($("#search").css("display") === "none") {
            $("#search").css("display", "flex");
            if ($("#search").width() < 320) {
                $("#search").css("display", "none");
            }
        }
    } else {
        if (!$("#search input").is(":focus")) {
            if ($("#nav_search").css("display") === "flex") {
                $("#nav_search").css("display", "none");
            }
        }
        if ($("#search").css("display") === "none") {
            $("#search").css("display", "flex");
        }
        if ($("#nav").css("display") === "none") {
            $("#nav").css("display", "flex");
        }
    }
};
