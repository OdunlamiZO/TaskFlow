<?php

namespace App\Livewire;

use App\Helpers\TaskFlowResource;
use Illuminate\Support\Facades\Session;
use Livewire\Component;

class AddTask extends Component
{

    public $project;

    public $project_options;

    public $deadline;

    public $description;

    public function mount()
    {
        $this->project_options = TaskFlowResource::request("/project", "GET", ["query" => ["field" => "title"]])["data"];
    }

    public function add()
    {
        $apiResponse = TaskFlowResource::request(
            "/task/add",
            "POST",
            [
                "json" => [
                    "project" => $this->project === "⸺ ⸺" || $this->project === "" ? null : $this->project,
                    "deadline" => $this->deadline,
                    "description" => $this->description
                ]
            ]
        );
        if (is_null($apiResponse)) {
            // save form data and display on login
            Session::put("current_path", "/tasks");
            $this->redirect("/login");
            return;
        }
        if ($apiResponse["status"] === "SUCCESS") {
            $this->project = null;
            $this->deadline = null;
            $this->description = null;
            $this->dispatch("modal-clear");
            $this->dispatch("update-tasks");
            $this->dispatch("modal-close");
        } else {
            foreach ($apiResponse["data"]["details"] as $id => $message) {
                $this->dispatch("error-show", id: $id, message: $message);
            }
        }
    }

    public function render()
    {
        return view('livewire.add-task');
    }
}
