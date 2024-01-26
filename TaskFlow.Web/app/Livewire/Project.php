<?php

namespace App\Livewire;

use App\Helpers\TaskFlowResource;
use Illuminate\Support\Facades\Session;
use Livewire\Attributes\On;
use Livewire\Attributes\Reactive;
use Livewire\Component;

class Project extends Component
{
    #[Reactive]
    public $project;

    public $_deadline;

    #[On("save-project")]
    public function saveProject() {
        $apiResponse = TaskFlowResource::request("/project/" . $this->project["id"] . "/update", "PUT", ["query" => ["deadline" => $this->_deadline]]);
        if (is_null($apiResponse)) {
            Session::put("current_path", "/projects");
            $this->redirect("/login");
            return;
        }
        if ($apiResponse["status"] === "SUCCESS") {
            $this->dispatch("update-projects");
            $this->dispatch("update-project", id: $this->project["id"]);
            $this->dispatch("edit-hide");
        }
    }

    #[On("delete-project")]
    public function deleteProject()
    {
        $apiResponse = TaskFlowResource::request("/project/" . $this->project["id"] . "/delete", str_replace("_", "", "_DELETE"));
        if (is_null($apiResponse)) {
            Session::put("current_path", "/projects");
            $this->redirect("/login");
            return;
        }
        if ($apiResponse["status"] === "SUCCESS") {
            $this->dispatch("update-projects");
            $this->dispatch("modal-close");
        }
    }

    public function render()
    {
        return view('livewire.project');
    }
}
