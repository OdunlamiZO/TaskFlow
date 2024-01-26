$(function () {
    Livewire.on("modal-open", ({ id }) => {
        modal = $(`#${id}_modal`);
        modal.removeClass("hidden");
        $("#page .overlay").removeClass("hidden");
    });

    Livewire.on("modal-clear", () => {
        $(`#${modal.attr("id")} input`).val("");
    });

    Livewire.on("modal-close", () => {
        modal.addClass("hidden");
        modal = null;
        $("#page .overlay").addClass("hidden");
    });
});
