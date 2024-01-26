<div id="page">
    <livewire:header :user="$user" />
    <livewire:project :project="$project" />
    <livewire:add-project />
    <div class="content">
        @if(empty($projects))
        <div id="open_add_project_modal" class="box_btn" style="cursor: default;" wire:click="$dispatch('modal-open', {id: 'add_project'})">
            <i class="fa-solid fa-plus" style="color: #ffffff"></i>
            Add Project
        </div>
        @else
        <div style="overflow-x: auto;" wire:push="update-projects,next-page,previous-page">
            <div class="table">
                <div class="header">
                    <div class="cell">Title</div>
                    <div class="cell">Progress</div>
                    <div class="cell">Deadline</div>
                </div>
                @php
                $start = ($page - 1) * $size;
                @endphp
                @for($i = $start; $i < $start + $size; $i++)
                @if($i > count($projects) - 1)
                @break
                @endif
                <div class="row" wire:click="$dispatch('update-project', {id: {{$projects[$i]['id']}}})">
                    <div class="cell">
                        <div class="caret">
                            <i class="fa-solid fa-arrow-up-right-from-square" style="color: #808080;"></i>
                        </div>
                        {{ $projects[$i]["title"] }}
                    </div>
                    @php
                    $progress = ($projects[$i]['progress'] == intval($projects[$i]['progress'])) ? intval($projects[$i]['progress']) : number_format($projects[$i]['progress'], 1);
                    @endphp
                    <div class="cell" style="display: flex; align-items: center; justify-content: space-between;">
                        <div style="width: 85%; height: 4px; border: 1px solid gray;">
                            <div style="width: {{ $progress }}%; height: 100%; background-color: #0180c6a6;"></div>
                        </div>
                        {{ $progress }}%
                    </div>
                    <div class="cell">{{ $projects[$i]["deadline"]??"⸺" }}</div>
                </div>
                @endfor
                <div class="footer">
                    <div></div>
                    <div style="display: flex; align-items: center; gap: 15px;">
                        @if(count($projects) > $size)
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
                            @if(count($projects) > $page * $size)
                            wire:click="$dispatch('next-page')"
                            @endif
                        >
                            <i class="fa-solid fa-chevron-right" style="color: #ffffff;"></i>
                        </div>
                        @endif
                    </div>
                    <div class="box_btn" style="padding: 10.5px;" wire:click="$dispatch('modal-open', {id: 'add_project'})">
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

@push('styles')
<link rel="stylesheet" href="{{ asset('css/projects.css') }}" />
@endpush

@push("title")
TaskFlow | Projects
@endpush
