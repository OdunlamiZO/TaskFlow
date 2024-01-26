$(function () {
    Livewire.on("select-toggle", ({ id }) => {
        select = $(`[data-select-id="${id}"]`);
        select.toggleClass("hidden");
    });

    Livewire.on("select-click", ({ value }) => {
        $(`#${select.attr("data-select-for")}`).val(value);
        document
            .getElementById(select.attr("data-select-for"))
            .dispatchEvent(new Event("input"));
        select.addClass("hidden");
    });
});
