$(function () {
    Livewire.on("edit-show", ({ id }) => {
        edit = $(`#${id}.edit`);
        edit.prev().toggleClass("hidden");
        edit.toggleClass("hidden");
    });

    Livewire.on("edit-hide", () => {
        edit.prev().toggleClass("hidden");
        edit.toggleClass("hidden");
        edit = null;
    });
});
