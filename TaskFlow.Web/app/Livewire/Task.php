<?php

namespace App\Livewire;

use App\Helpers\TaskFlowResource;
use Illuminate\Support\Facades\Session;
use Livewire\Attributes\On;
use Livewire\Attributes\Reactive;
use Livewire\Component;

class Task extends Component
{
    #[Reactive]
    public $task;

    public $status;

    public $_deadline;

    #[On("save-task")]
    public function saveTask()
    {
        $apiResponse = TaskFlowResource::request("/task/" . $this->task["id"] . "/update", "PUT", ["json" => ["status" => $this->status, "deadline" => $this->_deadline]]);
        if (is_null($apiResponse)) {
            Session::put("current_path", "/tasks");
            $this->redirect("/login");
            return;
        }
        if ($apiResponse["status"] === "SUCCESS") {
            $this->dispatch("update-tasks");
            $this->dispatch("update-task", id: $this->task["id"]);
            $this->dispatch("edit-hide");
        }
    }

    #[On("delete-task")]
    public function deleteTask()
    {
        $apiResponse = TaskFlowResource::request("/task/" . $this->task["id"] . "/delete", str_replace("_", "", "_DELETE"));
        if (is_null($apiResponse)) {
            Session::put("current_path", "/tasks");
            $this->redirect("/login");
            return;
        }
        if ($apiResponse["status"] === "SUCCESS") {
            $this->dispatch("update-tasks");
            $this->dispatch("modal-close");
        }
    }

    public function render()
    {
        return view('livewire.task');
    }
}
