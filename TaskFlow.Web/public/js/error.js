$(function () {
    Livewire.on("error-show", ({ id, message }) => {
        const errorDiv = $(`#${id}`).next(".error");
        errorDiv.removeClass("hidden");
        errorDiv.find("span").text(message);
    });

    Livewire.on("error-hide", ({ id }) => {
        const errorDiv = $(`#${id}`).next(".error");
        errorDiv.addClass("hidden");
        errorDiv.find("span").text("");
    });
});
