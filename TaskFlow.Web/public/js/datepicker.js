$(function () {
    Livewire.on("datepicker-toggle", ({ id }) => {
        dateInput = $(`#${id}`);
        dateInput.datepicker({
            minDate: "today",
            dateFormat: "dd/mm/yy",
            onSelect: function () {
                document.getElementById(id).dispatchEvent(new Event("input"));
            },
            showOn: null,
        });
        if (isDatePickerHidden) {
            dateInput.datepicker("show");
            isDatePickerHidden = false;
        } else {
            dateInput.datepicker("hide");
            isDatePickerHidden = true;
        }
    });
});
