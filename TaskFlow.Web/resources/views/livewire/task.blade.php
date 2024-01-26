<div id="task_modal" class="modal hidden" wire:ignore.self>
    <livewire:close-button />
    <div class="header">Task / {{ $task["description"]??"Lorem ipsum dolor" }}</div>
    <div style="padding-top: 5px;">
        <div style="padding-bottom: 5px; display: flex; flex-direction: column; gap: 5px;">
            Status:
            <div style="display: flex; align-items: center; justify-content: space-between;">
                <div style="display: flex; align-items: center; gap: 15px;">
                    @php
                    $status = $task["status"]??null;
                    if($status === "Pending") {
                    $color = "brown";
                    }else if($status === "In Progress") {
                    $color = "#0180c6a6";
                    }else if($status === "Completed") {
                    $color = "green";
                    }else {
                    $color = "white";
                    }
                    @endphp
                    <span style="width: 15px; height: 15px; border-radius: 50%; display: inline-block; background-color: {{ $color }};"></span>
                    {{ $status??"In Progress" }}
                </div>
                <div class="half_box_btn" wire:click="$dispatch('edit-show', {id: '_status'})">
                    <i class="fa-regular fa-pen-to-square" style="color: #808080;"></i>
                </div>
            </div>
            <div id="_status" class="edit hidden" style="display: flex; align-items: center; justify-content: space-between;">
                <div style="position: relative; width: 95%;">
                    <input type="text" id="status" readonly wire:model="status">
                    <div class="select hidden" data-select-id="status" data-select-for="status">
                        <span class="select_item" wire:click="$dispatch('select-click', {value: 'Pending'})">Pending</span>
                        <span class="select_item" wire:click="$dispatch('select-click', {value: 'In Progress'})">In Progress</span>
                        <span class="select_item" wire:click="$dispatch('select-click', {value: 'Completed'})">Completed</span>
                    </div>
                    <div style="z-index: 1; position: absolute; top: 50%; right: 5px; transform: translateY(-50%);" wire:click="$dispatch('select-toggle', {id: 'status'})">
                        <i class="fa-solid fa-chevron-down" style="color: #808080;"></i>
                    </div>
                </div>
                <div class="half_box_btn" wire:click="$dispatch('save-task')">
                    <i class="fa-solid fa-check" style="color: #808080;"></i>
                </div>
            </div>
        </div>
        <div style="padding-bottom: 5px; display: flex; flex-direction: column; gap: 5px;">
            Deadline:
            <div style="display: flex; align-items: center; justify-content: space-between;">
                <div style="display: flex; align-items: center; gap: 15px;">
                    <svg enable-background="new 0 0 32 32" width="15" height="15" viewBox="0 0 32 32">
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
                    {{ $task["deadline"]??"Nil" }}
                </div>
                <div class="half_box_btn" wire:click="$dispatch('edit-show', {id: '__deadline'})">
                    <i class="fa-regular fa-pen-to-square" style="color: #808080;"></i>
                </div>
            </div>
            <div id="__deadline" class="edit hidden" style="display: flex; align-items: center; justify-content: space-between;">
                <div style="position: relative; width: 95%;">
                    <input type="text" id="_deadline" readonly wire:model="_deadline">
                    <div style="z-index: 1; position: absolute; top: 50%; right: 5px; transform: translateY(-50%);" wire:click="$dispatch('datepicker-toggle', {id: '_deadline'})">
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
                <div class="half_box_btn" wire:click="$dispatch('save-task')">
                    <i class="fa-solid fa-check" style="color: #808080;"></i>
                </div>
            </div>
        </div>
        @php
        $project= $task["project"]??null;
        @endphp
        @if($project !== null)
        <div style="padding-bottom: 5px; display: flex; flex-direction: column; gap: 5px;">
            Project:
            <div style="display: flex; align-items: center; gap: 15px;">
                <img src="{{ asset('images/logo.png') }}" alt="Logo" width="15" />
                {{ $project }}
            </div>
        </div>
        @endif
        <div style="padding-top: 7.5px;">
            <div id="delete" class="box_btn" style="padding: 8px; float: right;" wire:click="$dispatch('delete-initiate')">
                <i class="fa-solid fa-trash fa-xs" style="color: #ffffff;"></i>
            </div>
            <div class="hidden" style="display: flex; justify-content: flex-end; gap: 10px;">
                <div class="box_btn" style="padding: 8px 9px;" wire:click="$dispatch('delete-cancel')">
                    <i class="fa-solid fa-xmark" style="color: #ffffff;"></i>
                </div>
                <div class="box_btn" style="padding: 8px;" wire:click="$dispatch('delete-task')">
                    <i class="fa-solid fa-check" style="color: #ffffff;"></i>
                </div>
            </div>
        </div>
    </div>
</div>

@push("scripts")
<script src="{{ asset('js/modal.js') }}"></script>
<script src="{{ asset('js/edit.js') }}"></script>
<script src="{{ asset('js/delete.js') }}"></script>
@endpush
