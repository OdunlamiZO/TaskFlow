<?php

namespace App\Livewire;

use App\Helpers\TaskFlowResource;
use Carbon\Carbon;
use Illuminate\Support\Facades\Session;
use Livewire\Attributes\On;
use Livewire\Component;

class Tasks extends Component
{
    public $user;

    public $task = [];

    public $tasks;

    public $page = 1;

    public $size = 5;

    public function mount()
    {
        $apiResponse = TaskFlowResource::request("/user/authenticated");
        if (!is_null($apiResponse)) {
            $this->user = $apiResponse["data"];
        } else {
            Session::put("current_path", "/tasks");
            $this->redirect("/login");
            return;
        }
        $this->setTasks();
    }

    #[On("update-task")]
    public function updateTask($id)
    {
        foreach ($this->tasks as $task) {
            if ($task["id"] === $id) {
                $this->task = $task;
                break;
            }
        }
        if ($this->task["deadline"] !== null) {
            $deadline = Carbon::parse($this->task["deadline"]);
            $this->task["deadline"] = $deadline->format("F d, Y");
        }
        $this->dispatch("modal-open", id: "task");
    }

    #[On("update-tasks")]
    public function setTasks()
    {
        $this->tasks = TaskFlowResource::request("/task")["data"];
    }

    #[On("next-page")]
    public function nextPage()
    {
        $this->page++;
    }

    #[On("previous-page")]
    public function previousPage()
    {
        $this->page--;
    }

    public function render()
    {
        return view('livewire.tasks');
    }
}
