$(function () {
    Livewire.on("delete-initiate", () => {
        _delete = $("#delete");
        _delete.next().toggleClass("hidden");
        _delete.toggleClass("hidden");
    });

    Livewire.on("delete-cancel", () => {
        _delete = $("#delete");
        _delete.next().toggleClass("hidden");
        _delete.toggleClass("hidden");
    });
});
