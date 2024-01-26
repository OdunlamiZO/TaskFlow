<div id="add_task_modal" class="modal hidden" wire:ignore>
    <livewire:close-button />
    <div class="header">Add Task</div>
    <form wire:submit="add">
        <div style="padding-bottom: 5px; display: flex; flex-direction: column; gap: 5px;">
            Description
            <input type="text" id="description" class="will_validate" wire:model="description">
            <div class="error hidden">
                <svg style="overflow: visible; enable-background: new 0 0 32 32" viewBox="0 0 32 32" width="16px" height="16px">
                    <g>
                        <g id="Error_1_">
                            <g id="Error">
                                <circle cx="16" cy="16" id="BG" r="16" style="fill: #e42043" />
                                <path d="M14.5,25h3v-3h-3V25z M14.5,6v13h3V6H14.5z" id="Exclamatory_x5F_Sign" style="fill: #e6e6e6" />
                            </g>
                        </g>
                    </g>
                </svg>
                <span></span>
            </div>
        </div>
        <div style="padding-bottom: 5px; display: flex; flex-direction: column; gap: 5px;">
            Project
            <div style="position: relative; width: 100%;">
                <input type="text" id="project" readonly wire:model="project">
                <div class="select hidden" data-select-id="project" data-select-for="project">
                    <span class="select_item" wire:click="$dispatch('select-click', {value: '⸺ ⸺'})">⸺ ⸺</span>
                    @foreach ($project_options as $project_option)
                    <span class="select_item" wire:click="$dispatch('select-click', {value: `{{$project_option}}`})">{{$project_option}}</span>
                    @endforeach
                </div>
                <div style="z-index: 1; position: absolute; top: 50%; right: 5px; transform: translateY(-50%);" wire:click="$dispatch('select-toggle', {id: 'project'})">
                    <i class="fa-solid fa-chevron-down" style="color: #808080;"></i>
                </div>
            </div>
        </div>
        <div style="padding-bottom: 5px; display: flex; flex-direction: column; gap: 5px;">
            Deadline
            <div style="position: relative; width: 100%;">
                <input type="text" id="deadline" readonly wire:model="deadline">
                <div style="z-index: 1; position: absolute; top: 50%; right: 5px; transform: translateY(-50%);" wire:click="$dispatch('datepicker-toggle', {id: 'deadline'})">
                    <svg enable-background="new 0 0 32 32" width="20" height="20" viewBox="0 0 32 32">
                        <g id="calendar_1_">
                            <path d="M29.334,3H25V1c0-0.553-0.447-1-1-1s-1,0.447-1,1v2h-6V1c0-0.553-0.448-1-1-1s-1,0.447-1,1v2H9V1   c0-0.553-0.448-1-1-1S7,0.447,7,1v2H2.667C1.194,3,0,4.193,0,5.666v23.667C0,30.806,1.194,32,2.667,32h26.667   C30.807,32,32,30.806,32,29.333V5.666C32,4.193,30.807,3,29.334,3z M30,29.333C30,29.701,29.701,30,29.334,30H2.667   C2.299,30,2,29.701,2,29.333V5.666C2,5.299,2.299,5,2.667,5H7v2c0,0.553,0.448,1,1,1s1-0.447,1-1V5h6v2c0,0.553,0.448,1,1,1   s1-0.447,1-1V5h6v2c0,0.553,0.447,1,1,1s1-0.447,1-1V5h4.334C29.701,5,30,5.299,30,5.666V29.333z" fill="#333332" />
                            <rect fill="#333332" height="3" width="4" x="7" y="12" />
                            <rect fill="#333332" height="3" width="4" x="7" y="17" />
                            <rect fill="#333332" height="3" width="4" x="7" y="22" />
                            <rect fill="#333332" height="3" width="4" x="14" y="22" />
                            <rect fill="#333332" height="3" width="4" x="14" y="17" />
                            <rect fill="#333332" height="3" width="4" x="14" y="12" />
                            <rect fill="#333332" height="3" width="4" x="21" y="22" />
                            <rect fill="#333332" height="3" width="4" x="21" y="17" />
                            <rect fill="#333332" height="3" width="4" x="21" y="12" />
                        </g>
                    </svg>
                </div>
            </div>
        </div>
        <div style="width: 100%; padding-top: 5px; display: flex; justify-content: flex-end;">
            <button type="submit">Add</button>
        </div>
    </form>
</div>

@push("styles")
<link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
@endpush

@push("scripts")
<script src="{{ asset('js/select.js') }}"></script>
<script src="{{ asset('js/error.js') }}"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
<script src="{{ asset('js/datepicker.js') }}"></script>
<script>
    $(function() {
        $(document).on("click", (event) => {
            if (!$(event.target).closest("#add_task_modal .select, #add_task_modal .select + div, #add_task_modal .select_item").length) {
                if (!$("#add_task_modal .select").hasClass("hidden")) {
                    $("#add_task_modal .select").addClass("hidden");
                }
            }
        });
    });
</script>
@endpush
