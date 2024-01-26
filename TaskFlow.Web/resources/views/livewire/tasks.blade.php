<div id="page">
    <livewire:header :user="$user" />
    <livewire:task :task="$task" />
    <livewire:add-task />
    <div class="content">
        @if(empty($tasks))
        <div id="open_add_task_modal" class="box_btn" style="cursor: default;" wire:click="$dispatch('modal-open', {id: 'add_task'})">
            <i class="fa-solid fa-plus" style="color: #ffffff"></i>
            Add Task
        </div>
        @else
        <div style="overflow-x: auto;" wire:push="update-tasks,next-page,previous-page">
            <div class="table">
                <div class="header">
                    <div class="cell">Description</div>
                    <div class="cell">Status</div>
                    <div class="cell">Project</div>
                    <div class="cell">Deadline</div>
                </div>
                @php
                $start = ($page - 1) * $size;
                @endphp
                @for($i = $start; $i < $start + $size; $i++)
                @if($i > count($tasks) - 1)
                @break
                @endif
                <div class="row" wire:click="$dispatch('update-task', {id: {{$tasks[$i]['id']}}})">
                    <div class="cell">
                        <div class="caret">
                            <i class="fa-solid fa-arrow-up-right-from-square" style="color: #808080;"></i>
                        </div>
                        {{ $tasks[$i]["description"] }}
                    </div>
                    <div class="cell">{{ $tasks[$i]["status"] }}</div>
                    <div class="cell">{{ $tasks[$i]["project"]??"⸺" }}</div>
                    <div class="cell">{{ $tasks[$i]["deadline"]??"⸺" }}</div>
                </div>
                @endfor
                <div class="footer">
                    <div></div>
                    <div style="display: flex; align-items: center; gap: 15px;">
                        @if(count($tasks) > $size)
                        <div
                            class="box_btn"
                            style="padding: 10.5px 12.5px;"
                            @if($page !== 1)
                            wire:click="$dispatch('previous-page')"
                            @endif
                        >
                            <i class="fa-solid fa-chevron-left" style="color: #ffffff;"></i>
                        </div>
                        <div>{{ $page }}</div>
                        <div
                            class="box_btn"
                            style="padding: 10.5px 12.5px;"
                            @if(count($tasks) > $page * $size)
                            wire:click="$dispatch('next-page')"
                            @endif
                        >
                            <i class="fa-solid fa-chevron-right" style="color: #ffffff;"></i>
                        </div>
                        @endif
                    </div>
                    <div class="box_btn" style="padding: 10.5px;" wire:click="$dispatch('modal-open', {id: 'add_task'})">
                        <i class="fa-solid fa-plus" style="color: #ffffff"></i>
                    </div>
                </div>
            </div>
        </div>
        @endif
    </div>
    <div class="overlay hidden" wire:ignore></div>
    <livewire:footer />
</div>

@push("styles")
<link rel="stylesheet" href="{{ asset('css/tasks.css') }}" />
@endpush

@push("title")
TaskFlow | Tasks
@endpush
